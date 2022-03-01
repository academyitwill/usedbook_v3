package com.lotu_us.usedbook.util;

import com.lotu_us.usedbook.domain.dto.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class CustomMailSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String form;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Mail {
        private String address;
        private String title;
        private String message;
    }

    private void mimeMailSend(Mail mailDTO){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress(form));  //naver, daum, nate일 경우 넣어주어야함
            message.setSubject(mailDTO.getTitle());
            message.setText(mailDTO.getMessage(), "UTF-8", "html");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDTO.getAddress()));
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendFindPasswordMail(MemberDTO.findPassword memberDTO, String tempPassword) {
        String htmlMessage =
        "<div>" +
            memberDTO.getNickname()+"님의 임시 비밀번호는 <span style='font-weight:bold; color:blue;'>"+tempPassword+"</span> 입니다." +
        "</div>";

        Mail mail = new Mail();
        mail.setAddress(memberDTO.getEmail());
        mail.setTitle("[책방] 임시 비밀번호 안내 메일입니다.");
        mail.setMessage(htmlMessage);

        this.mimeMailSend(mail);
    }
}
