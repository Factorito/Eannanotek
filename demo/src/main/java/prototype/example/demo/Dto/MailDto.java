package prototype.example.demo.Dto;

import lombok.Data;

@Data
public class MailDto {
    private String address; // 상대방 주소
    private String title; // 메일 제목
    private String message; // 메일 메시지
}