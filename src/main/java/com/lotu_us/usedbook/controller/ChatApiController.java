package com.lotu_us.usedbook.controller;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ChatDTO;
import com.lotu_us.usedbook.domain.entity.Chat;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.ChatRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import com.lotu_us.usedbook.service.ChatService;
import com.lotu_us.usedbook.util.exception.CustomException;
import com.lotu_us.usedbook.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatApiController {
    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    /**
     * 채팅입력 및 결과반환
     * @AuthenticationPrincipal PrincipalDetails principalDetails 은 Rest, Mvc 메서드에서 동작한다. MessageMapping에서는 동작하지 않는다.
     * @PathVariable도 마찬가지이다. @DestinationVariable을 사용하자.
     */
    @MessageMapping("/room/{roomId}/{nickname}")  //StompConfig에 적힌 setApplicationDestinationPrefixes 제외해서 작성한다.
    public void message(@RequestBody ChatDTO.Send chatDTO, @DestinationVariable Long roomId, @DestinationVariable String nickname) throws Exception{
        ChatDTO.Receive receive = chatService.save(roomId, chatDTO, nickname);
        template.convertAndSend("/api/chat/receive/"+receive.getReceiverNickname(), receive);
        template.convertAndSend("/api/chat/receive/room/"+roomId+"/"+nickname, receive);
        // StompConfig에 적힌 enableSimpleBroker 도 포함해서 작성해야한다.
    }


    /**
     * 채팅방 만들기
     */
    @PostMapping("/api/chat/{nickname}")
    public ResponseEntity createRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable String nickname
    ){
        Long roomId = chatService.createRoom(principalDetails, nickname);
        return ResponseEntity.status(HttpStatus.OK).body(roomId);
    }


    /**
     * 채팅방 리스트 반환
     */
    @GetMapping("/api/chat/room/list")
    public ResponseEntity list(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<ChatDTO.List> list = chatService.list(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    /**
     * 과거 대화내용 가져오기
     */
    @GetMapping("/api/chat/room/{roomId}")
    public ResponseEntity history(@PathVariable Long roomId){
        List<ChatDTO.Receive> history = chatService.getHistory(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

}
