package com.example.demo.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentRequestDTO {
    private String content;
    private Long diaryId;
    private Long memberId;

}
