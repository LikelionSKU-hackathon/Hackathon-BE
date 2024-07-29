package com.example.demo.web.dto;

import com.example.demo.validation.annotation.ExistKeywords;
import jakarta.validation.constraints.*;
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
        @Email(message = "이메일 형식이 잘못되었습니다.")
        String email;

        @Size(min = 5, max = 12)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{5,12}$", message = "비밀번호는 숫자와 문자를 포함해야 합니다.")
        String password;

        @NotNull
        String age_group;
        @NotNull
        String role;

        private  MultipartFile profileImage;
    }

    @Getter
    @Setter
    public static class SocialJoinDTO{
        @NotBlank
        String username;
        @NotNull
        String age_group;

        private MultipartFile profileImage;
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
        @ExistKeywords
        List<Long> keywordIdList;
    }








}
