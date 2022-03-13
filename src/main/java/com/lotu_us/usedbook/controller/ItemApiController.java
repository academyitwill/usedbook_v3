package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.service.ItemService;
import com.lotu_us.usedbook.util.aop.ReturnBindingResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemApiController {
    private final ItemService itemService;

    /**
     * 상품 저장
     */
    @PostMapping("")
    @ReturnBindingResultError
    public ResponseEntity write(
            @Validated @RequestPart(value = "jsonData") ItemDTO.Write itemDTO, BindingResult bindingResult,
            @RequestPart(value = "fileList") List<MultipartFile> fileList,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        Long itemId = itemService.write(principalDetails.getMember(), itemDTO, fileList);
        return ResponseEntity.status(HttpStatus.OK).body(itemId);
    }

    /**
     * 상품 상세보기
     */
    @GetMapping("/{itemId}")
    public ResponseEntity detail(@PathVariable Long itemId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        ItemDTO.Response detail = itemService.detail(itemId, principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(detail);
    }

    /**
     * 상품 수정
     * multipart는 한번에 여러 리소스를 처리한다. put으로 여러 리소스를 처리하려면 각각 별도의 put요청을 보내야한다. 따라서 Post를 사용하여 한번에 처리.
     * 추가 or 삭제된 파일이 있을수도 없을수도 있으니 required = false
     */
    @PostMapping("/{itemId}")
    public ResponseEntity edit(
            @PathVariable Long itemId,
            @Validated @RequestPart(value = "jsonData") ItemDTO.Write itemDTO, BindingResult bindingResult,
            @RequestPart(value = "plusFileList", required = false) List<MultipartFile> plusFileList,
            @RequestPart(value = "removeFileList", required = false) List<String> removeFileList,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        itemService.edit(itemId, principalDetails.getMember(), itemDTO, plusFileList, removeFileList);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity remove(
            @PathVariable Long itemId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        itemService.remove(itemId, principalDetails.getMember());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 상품 리스트보기
     * size : 한 페이지 당 조회할 개수 (default 20)
     * page : 요청할 페이지 번호
     * sort : 정렬방법 표기. 필드명, 정렬기준 (createTime, desc)
     */
    @GetMapping(value = {"/list", "/list/{category}"})
    public ResponseEntity list(
            @PathVariable(required = false) String category,
            @RequestParam(required = false) String search,
            @PageableDefault(size=10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Map map = itemService.list(category, search, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    /**
     * 대시보드 내가 업로드한 상품 리스트보기
     * size : 한 페이지 당 조회할 개수 (default 20)
     * page : 요청할 페이지 번호
     * sort : 정렬방법 표기. 필드명, 정렬기준 (createTime, desc)
     */
    @GetMapping("/dashboard")
    public ResponseEntity mylist(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault(size=10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ){
        String nickname = principalDetails.getMember().getNickname();
        Map map = itemService.list(null, "2,"+nickname, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    /**
     * 인덱스페이지
     */
    @GetMapping("/index")
    public ResponseEntity index(@RequestParam(defaultValue = "10") int count){
        Map<String, List> index = itemService.index(count);
        return ResponseEntity.status(HttpStatus.OK).body(index);
    }

}
