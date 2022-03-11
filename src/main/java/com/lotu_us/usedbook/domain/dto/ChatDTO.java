package com.lotu_us.usedbook.domain.dto;

import com.lotu_us.usedbook.domain.entity.Chat;
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
        private String senderNickname;
        private String message;
        private String sendTime;
    }

    public static ChatDTO.Receive entityToDTO(Chat chat){
        Receive receive = new Receive();
        receive.senderNickname = chat.getSender().getNickname();
        receive.message = chat.getMessage();
        receive.sendTime = chat.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return receive;
    }
}
