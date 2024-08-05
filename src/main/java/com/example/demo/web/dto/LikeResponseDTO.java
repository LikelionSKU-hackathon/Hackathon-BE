package com.example.demo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikeResponseDTO {
    private Long id;
    private Long diaryId;
    private Long memberId;
    private boolean iLiked;
}
