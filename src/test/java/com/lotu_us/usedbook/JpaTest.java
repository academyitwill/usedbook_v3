package com.lotu_us.usedbook;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.domain.entity.Order;
import com.lotu_us.usedbook.domain.entity.OrderItem;
import com.lotu_us.usedbook.repository.ItemRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.repository.OrderItemRepository;
import com.lotu_us.usedbook.repository.OrderRepository;
import com.lotu_us.usedbook.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class JpaTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;

    private Member member;
    private Item item;
    private PrincipalDetails principalDetails;
    private List<MultipartFile> fileList = new ArrayList<>();

    @BeforeEach
    void before(){
        member = memberRepository.save(new Member("123@123.com", "123"));
        item = itemRepository.save(new Item(member, "제목1"));
        principalDetails = new PrincipalDetails(member);

//        ItemDTO.Write itemDTO = ItemDTO.Write.builder()
//                .title("제목").category(Category.CARTOON).price(1000).stock(10).content("내용입니당").build();
//        fileList.add(new MockMultipartFile("file1", "file1.jpg", MediaType.IMAGE_JPEG_VALUE, "hello".getBytes(StandardCharsets.UTF_8)));
//        Long writeId = itemService.write(member, itemDTO, fileList);
        System.out.println("===============================================================================================================================");
    }

    /**
     * Order - Item 사이가 OrderItem.
     * Order는 여러 Item을를 가질 수 있다.
     * Item은 여러 Order에 속할 수 있다.
     *
     * Item리스트를 받아서 OrderItem을 저장한 후
     * Order에 OrderItem(일종의 Item의 리스트)를 추가한다.
     *
     * Order를 save하면 OrderItem에도 자동으로 데이터가 들어가야한다.
     */
    @Test
    @DisplayName("데이터 추가 - Order의 List<OrderItem>에 fetch = FetchType.LAZY, cascade = CascadeType.REMOVE")
    void test(){
        OrderItem orderItem = OrderItem.builder().item(item).build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder().member(member).orderItems(orderItemList).build();

        orderRepository.save(order);
        //orderItem 저장안됨
    }

    @Test
    @DisplayName("데이터 추가 - Order의 List<OrderItem>에 fetch = FetchType.LAZY, cascade = CascadeType.ALL")
    void test2(){
        OrderItem orderItem = OrderItem.builder().item(item).build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder().member(member).orderItems(orderItemList).build();

        orderRepository.save(order);
        //orderitem에 itemId는 저장되었지만 orderId는 null로 저장됨
    }

    @Test
    @DisplayName("데이터 추가 - Order의 List<OrderItem>에 cascade = CascadeType.ALL")
    void test3(){
        OrderItem orderItem = OrderItem.builder().item(item).build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder().member(member).orderItems(orderItemList).build();

        orderRepository.save(order);
        //orderitem에 itemId는 저장되었지만 orderId는 null로 저장됨
        //fetch는 아무 상관없음
    }

    @Test
    @DisplayName("데이터 추가 - Order의 List<OrderItem>에 fetch = FetchType.LAZY, cascade = CascadeType.REMOVE")
    void test4(){
        OrderItem orderItem = OrderItem.builder().item(item).build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder().member(member).orderItems(orderItemList).build();
        for (OrderItem orderOrderItem : order.getOrderItems()) {
            orderOrderItem.setOrder(order);
        }

        orderRepository.save(order);
        //orderItem 저장안됨
    }

    @Test
    @DisplayName("데이터 추가 - Order의 List<OrderItem>에 fetch = FetchType.LAZY, cascade = CascadeType.ALL")
    void test5(){
        OrderItem orderItem = OrderItem.builder().item(item).build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder().member(member).orderItems(orderItemList).build();
        for (OrderItem orderOrderItem : order.getOrderItems()) {
            orderOrderItem.setOrder(order);
        }

        orderRepository.save(order);
        //OrderItem의 orderId에도 저장됨. 정상저장!!!!!!!!!
    }


    @Test
    @DisplayName("데이터 삭제")
    void test6(){
        OrderItem orderItem = OrderItem.builder().item(item).build();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.builder().member(member).orderItems(orderItemList).build();
        for (OrderItem orderOrderItem : order.getOrderItems()) {
            orderOrderItem.setOrder(order);
        }

        Order save = orderRepository.save(order);
        //OrderItem의 orderId에도 저장됨. 정상저장!!!!!!!!!

        orderRepository.deleteById(save.getId());
        //둘다 삭제됨
    }
}
