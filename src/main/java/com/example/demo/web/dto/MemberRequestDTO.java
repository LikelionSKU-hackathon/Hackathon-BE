package com.example.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class MemberRequestDTO {
    @Getter
    @Setter
    public static class JoinDTO {
        @NotBlank
        String username;
        @NotNull
        String email;
        @Size(min = 5, max = 12)
        String password;
        @Size(min = 5, max = 12)
        String confirmPassword;
        @NotNull
        String age_group;

        List<Long> memberKeyword;

        private  MultipartFile profileImage;
    }

    @Getter
    @Setter
    public static class LoginDTO{
        @NotBlank
        String email;

        @Size(min =5, max=12)
        String password;

    }

    @Getter
    @Setter
    public static class setKeywordDTO{
        List<Long> keywordIdList;
    }


}
