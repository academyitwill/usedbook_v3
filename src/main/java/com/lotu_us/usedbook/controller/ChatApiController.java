package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ChatDTO;
import com.lotu_us.usedbook.domain.entity.Chat;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.ChatRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class ChatApiController {
    private final SimpMessagingTemplate template;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;


    @PostMapping("/login/{email}/{nickname}")
    public ResponseEntity login(@PathVariable String email, @PathVariable String nickname, HttpSession session){
        Member loginMember = new Member(email, nickname);

        session.setAttribute("loginMember", loginMember);
        System.out.println("세션 저장");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @MessageMapping("/message") //StompConfig에 적힌 setApplicationDestinationPrefixes 제외해서 작성한다.
    public void message(
            @RequestBody ChatDTO.Send chatDTO,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) throws Exception{

        Member receiver = principalDetails.getMember();

        Member sender = memberRepository.findByNickname(chatDTO.getSenderNickname()).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .message(chatDTO.getMessage()).build();
//        Chat save = chatRepository.save(chat);

        ChatDTO.Receive receive = ChatDTO.entityToDTO(chat);

        template.convertAndSend("/api/chat/receive/message", receive);
        // StompConfig에 적힌 enableSimpleBroker 도 포함해서 작성해야한다.
    }
}
