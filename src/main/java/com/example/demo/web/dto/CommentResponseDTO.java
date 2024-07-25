package com.example.demo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentResponseDTO {

    private String diaryTitle;
    private String diaryContent;
    private Long id;
    private String content;
    private String memberUsername;
}
