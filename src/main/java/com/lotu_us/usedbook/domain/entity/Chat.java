package com.lotu_us.usedbook.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_member_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_member_id")
    private Member receiver;
//    둘다 member_id로 설정하면  Repeated column in mapping for entity 에러 발생
//    joincolumn은 어노테이션이 붙은 필드의 엔티티를 추적해서 엔티티의 PK를 join한다.
//    PK의 칼럼명 membr_id와 꼭 맞아야하는 것이 아니다. -> joincolumn의 name은 외래키 컬럼명을 만들어주는 설정이다.

    private String message;

    private LocalDateTime createTime = LocalDateTime.now();

    @Builder
    public Chat(Member sender, Member receiver, String message, ChatRoom chatRoom) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
