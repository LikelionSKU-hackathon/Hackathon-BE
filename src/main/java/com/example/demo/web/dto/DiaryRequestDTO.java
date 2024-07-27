package com.example.demo.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryRequestDTO {
    private String title;
    private String content;
    private boolean isPublic;
    private Long memberId;
    private Long moodId;
}
