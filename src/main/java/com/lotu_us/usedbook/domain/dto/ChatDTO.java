package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Chat;
import com.lotu_us.usedbook.domain.entity.Member;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

public class ChatDTO {

    @Getter
    @ToString
    public static class Send{
        private String senderNickname;
        private String message;
    }

    @Getter
    @ToString
    public static class Receive{
        private Long roomId;
        private String receiverNickname;
        private String senderNickname;
        private String message;
        private String sendTime;
    }

    public static ChatDTO.Receive entityToDTO(Chat chat){
        Receive receive = new Receive();
        receive.roomId = chat.getChatRoom().getId();
        receive.receiverNickname = chat.getReceiver().getNickname();
        receive.senderNickname = chat.getSender().getNickname();
        receive.message = chat.getMessage();
        receive.sendTime = chat.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return receive;
    }

    @Getter
    @ToString
    public static class List{
        private Long roomId;
        private String otherNickname;
        private String recentMessage;
        private String recentTime;
    }

    public static ChatDTO.List entityToDTOList(Chat chat, Member me) {
        ChatDTO.List list = new ChatDTO.List();
        list.roomId = chat.getChatRoom().getId();
        if(chat.getSender().getNickname() == me.getNickname()){
            list.otherNickname = chat.getReceiver().getNickname();
        }else{
            list.otherNickname = chat.getSender().getNickname();
        }
        list.recentMessage = chat.getMessage();
        list.recentTime = chat.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return list;
    }


}
