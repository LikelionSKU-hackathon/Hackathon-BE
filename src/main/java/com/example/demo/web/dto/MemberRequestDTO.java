package com.example.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;


public class MemberRequestDTO {
    @Getter
    public static class JoinDTO{
        @NotBlank
        String name;
        @Size(min =5, max=12)
        String userId;
        @NotNull
        String email;
        @Size(min =5, max=12)
        String password;
        @NotNull
        Integer age;


    }

}
