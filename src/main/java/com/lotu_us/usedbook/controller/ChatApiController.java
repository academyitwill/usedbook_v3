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
import org.springframework.messaging.handler.annotation.DestinationVariable;
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


    @MessageMapping("/message/{nickname}") //StompConfig에 적힌 setApplicationDestinationPrefixes 제외해서 작성한다.
    public void message(@RequestBody ChatDTO.Send chatDTO, @DestinationVariable String nickname) throws Exception{
        //@AuthenticationPrincipal PrincipalDetails principalDetails 은 Rest, Mvc 메서드에서 동작한다. MessageMapping에서는 동작하지 않는다.
        //@PathVariable도 마찬가지이다. @DestinationVariable을 사용하자.
        Member receiver = memberRepository.findByNickname(nickname).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        Member sender = memberRepository.findByNickname(chatDTO.getSenderNickname()).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );

        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .message(chatDTO.getMessage()).build();
//        Chat save = chatRepository.save(chat);

        ChatDTO.Receive receive = ChatDTO.entityToDTO(chat);

//        System.out.println("receiver : " + receiver);
//        System.out.println("sender : " + sender);
        template.convertAndSend("/api/chat/receive/message/"+nickname, receive);
        // StompConfig에 적힌 enableSimpleBroker 도 포함해서 작성해야한다.
    }
}
