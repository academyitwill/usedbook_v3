package com.lotu_us.usedbook.service;

import com.lotu_us.usedbook.auth.PrincipalDetails;
import com.lotu_us.usedbook.domain.dto.ChatDTO;
import com.lotu_us.usedbook.domain.entity.Chat;
import com.lotu_us.usedbook.domain.entity.ChatRoom;
import com.lotu_us.usedbook.domain.entity.Member;
import com.lotu_us.usedbook.repository.ChatRepository;
import com.lotu_us.usedbook.repository.ChatRoomRepository;
import com.lotu_us.usedbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅 내용 저장
     */
    public ChatDTO.Receive save(Long roomId, ChatDTO.Send chatDTO, String nickname) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        String dNickname = URLDecoder.decode(nickname, StandardCharsets.UTF_8);
        String dSenderNick = URLDecoder.decode(chatDTO.getSenderNickname(), StandardCharsets.UTF_8);
        Member receiver = memberRepository.findByNickname(dNickname).orElse(null);
        Member sender = memberRepository.findByNickname(dSenderNick).orElse(null);

        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .message(chatDTO.getMessage())
                .chatRoom(chatRoom).build();
        Chat save = chatRepository.save(chat);

        ChatDTO.Receive receive = ChatDTO.entityToDTO(chat);
        return receive;
    }

    /**
     * 채팅방 만들기
     */
    public Long createRoom(PrincipalDetails principalDetails, String nickname) {
        Member member = principalDetails.getMember();
        String dNickname = URLDecoder.decode(nickname, StandardCharsets.UTF_8);
        Member otherMember = memberRepository.findByNickname(dNickname).orElse(null);
        Chat chat = chatRepository.findByReceiverAndSender(member.getId(), otherMember.getId()).orElse(null);

        if(chat == null){
            ChatRoom chatRoom = new ChatRoom();
            ChatRoom save = chatRoomRepository.save(chatRoom);
            return save.getId();
        }else{
            return chat.getChatRoom().getId();
        }
    }


    /**
     * 채팅 목록 반환
     * @param principalDetails
     * @return
     */
    public List<ChatDTO.List> list(PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        List<Chat> charList = chatRepository.findByReceiverMemberId(member.getId());
        List<ChatDTO.List> list = charList.stream()
                .map(chat -> ChatDTO.entityToDTOList(chat, member))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 과거 대화내용 반환
     * @param roomId
     * @return
     */
    public List<ChatDTO.Receive> getHistory(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        List<Chat> chatList = chatRoom.getChatList();
        List<ChatDTO.Receive> collect = chatList.stream()
                .map(chat -> ChatDTO.entityToDTO(chat))
                .collect(Collectors.toList());
        return collect;
    }
}
