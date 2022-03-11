package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.ItemFile;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.enums.Category;
import com.lotu_us.usedbook.repository.ItemFileRepository;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemFileRepository itemFileRepository;
    private final LikeItemService likeItemService;

    @Value("${custom.path.upload-images}")
    private String imgUploadPath;


    /**
     * 상품 저장
     * @param member
     * @param itemDTO
     * @param fileList
     * 스프링 시큐리티로 회원만 접근 가능함
     */
    public Long write(Member member, ItemDTO.Write itemDTO, List<MultipartFile> fileList) {
        Item item = Item.builder()
                .seller(member)
                .title(itemDTO.getTitle())
                .category(itemDTO.getCategory())
                .price(itemDTO.getPrice())
                .stock(itemDTO.getStock())
                .content(itemDTO.getContent()).build();

        itemRepository.save(item);
        fileSave(item, fileList);
        return item.getId();
    }

    /**
     * 상품 상세보기
     * @param itemId
     * @param principalDetails
     * @exception : 존재하지 않는 게시글 아이디이면 ErrorCode.ID_NOT_FOUND
     * @return : ItemDTO.Response
     */
    public ItemDTO.Response detail(Long itemId, PrincipalDetails principalDetails) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        ItemDTO.Response response = ItemDTO.entityToDtoResponse(item);

        //인증객체가 비어있지 않다면 로그인한 상태.
        if(principalDetails != null){
            //현재 로그인한 멤버가 게시글에 좋아요를 눌렀다면 표시해주어야함.

            //현재 로그인한 멤버가 게시글의 작성자이면 수정, 삭제버튼을 보여주어야함.
            String sellerEmail = item.getSeller().getEmail();
            String loginMemberEmail = principalDetails.getMember().getEmail();
            if(sellerEmail.equals(loginMemberEmail)){
                response.memberIsSellerTrue();
            }

            //현재 로그인한 멤버가 게시글을 관심상품으로 등록했다면
            boolean liked = likeItemService.isLiked(principalDetails.getMember().getId(), itemId);
            response.setLikeStatus(liked);
        }

        //조회수 증가
        item.addViewCount(item.getViewCount());

        return response;
    }

    /**
     * 상품 수정
     * @param itemId
     * @param itemDTO
     * @param plusFileList
     * @param removeFileList
     * @exception : 존재하지 않는 게시글 아이디이면 ErrorCode.ID_NOT_FOUND
     * @exception : 게시글 작성자와 현재 회원이 일치하지 않다면 ErrorCode.EDIT_ACCESS_DENIED
     *
     */
    public void edit(Long itemId, Member member, ItemDTO.Write itemDTO, List<MultipartFile> plusFileList, List<String> removeFileList) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        String sellerEmail = item.getSeller().getEmail();
        String loginMemberEmail = member.getEmail();
        if(!sellerEmail.equals(loginMemberEmail)){
            throw new CustomException(ErrorCode.EDIT_ACCESS_DENIED);
        }

        item.changeContents(itemDTO);

        //추가된 파일이 있다면
        if(plusFileList != null){
            fileSave(item, plusFileList);
        }

        //삭제된 파일이 있다면
        if(removeFileList != null){
            fileRemove(item, removeFileList);
        }
    }

    /**
     * 상품 삭제
     * @param itemId
     * @exception : 존재하지 않는 게시글 아이디이면 ErrorCode.ID_NOT_FOUND
     * @exception : 게시글 작성자와 현재 회원이 일치하지 않다면 ErrorCode.DELETE_ACCESS_DENIED
     */
    public void remove(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        String sellerEmail = item.getSeller().getEmail();
        String loginMemberEmail = member.getEmail();
        if(!sellerEmail.equals(loginMemberEmail)){
            throw new CustomException(ErrorCode.DELETE_ACCESS_DENIED);
        }

        itemRepository.delete(item);
    }

    /**
     * 리스트보기
     * @return
     */
    public Map list(String category, String search, Pageable pageable) {
//        기존 JpaRepository의 기능을 사용했을 때
//        Page<ItemDTO.ListResponse> map = itemRepository.findAll(pageable)
//                .map(item -> ItemDTO.entityToDtoListResponse(item));
//        반환값은 하단 주석에서 확인

        Page<ItemDTO.ListResponse> allWithSearch = itemRepository.findAllWithSearch(category, search, pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("pagination", allWithSearch);

        if(StringUtils.isBlank(category)){
            category = "통합검색";
        }else{
            category = Category.valueOf(category.toUpperCase()).getValue();
        }
        map.put("category", category);

        return map;
    }




    /**
     * 파일 저장
     * @param item
     * @param fileList
     */
    private void fileSave(Item item, List<MultipartFile> fileList) {
        for (MultipartFile multipartFile : fileList) {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + multipartFile.getOriginalFilename();

            Path savePath = Paths.get(imgUploadPath + File.separator + filename).toAbsolutePath();

            try {
                multipartFile.transferTo(savePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //MultipartFile.transferTo()는 요청 시점의 임시 파일을 로컬 파일 시스템에 영구적으로 복사하는 역할을 수행한다.
            // 단 한번만 실행되며 두번째 실행부터는 성공을 보장할 수 없다.
            //Embedded Tomcat을 컨테이너로 사용할 경우 DiskFileItem.write()가 실제 역할을 수행한다.
            // I/O 사용을 최소화하기 위해 파일 이동을 시도하며, 이동이 불가능할 경우 파일 복사를 진행한다.

            ItemFile itemFile = ItemFile.builder()
                    .item(item)
                    .fileName(filename)
                    .filePath(imgUploadPath).build();

            item.getFiles().add(itemFile);
            itemFileRepository.save(itemFile);
        }
    }


    /**
     * 파일 삭제
     * @param item
     * @param removeFileList
     */
    private void fileRemove(Item item, List<String> removeFileList) {
        for (String removeFileName : removeFileList) {
            String fileName = URLDecoder.decode(removeFileName, StandardCharsets.UTF_8);
            itemFileRepository.deleteByFileName(fileName);
        }

        item.getFiles().removeIf(itemFile -> removeFileList.contains(itemFile.getFileName()));
    }


    /**
     * 인덱스페이지
     * @return
     */
    public Map<String, List> index(int count) {
        Map<String, List> map = new HashMap<>();

        for (Category category : Category.values()) {
            List<Item> categoryList = itemRepository.findAllByCategory(category, PageRequest.of(0, count, Sort.by("createTime").descending()));
            List<ItemDTO.ResponseIndex> indexList = categoryList.stream()
                    .map(item -> ItemDTO.entityToDTOIndex(item))
                    .collect(Collectors.toList());
            map.put(category.toString().toLowerCase(), indexList);
        }
        return map;
    }
}



/*
{
    "content": [
        {
            "id": 6,
            "title": "567",
            "category": "소설",
            "createTime": "2022-03-02 16:53",
            "saleStatus": "판매중",
            "viewCount": 0
        }   ........
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "offset": 0,
        "pageSize": 3,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": false,          //마지막 페이지인지
    "totalPages": 2,        //만들수있는 페이지 수
    "totalElements": 4,     //DB의 전체 데이터 개수
    "size": 3,              //한 페이지당 나타내는 데이터 개수
    "number": 0,            //현재 페이지 번호
    "sort": {               //정렬정보
        "empty": false,
        "unsorted": false,
        "sorted": true
    },
    "numberOfElements": 3,  //요청 페이지에서 조회된 데이터 개수 (마지막 페이지일 경우)
    "first": true,          //첫번째 페이지인지
    "empty": false
}
*/