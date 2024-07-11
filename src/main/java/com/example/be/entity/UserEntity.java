package com.example.be.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name= "user") //자카르타
// DB의 어떤 테이블과 mapping 시킬 건지
// 만약 클래스 이름이 UserEntity가 아니라 User면 Table Entity 필요 없음
@Table(name = "user")
// 밑에 두 줄 chatgpt
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @Column(name="user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_email", nullable = true)
    private String userEmail;
    @Column(name = "user_login_type")
    private String userLoginType;
    @Column(name = "user_role")
    private String userRole;
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    public UserEntity(String userId, String userName,
                      String userEmail, String userLoginType,
                      String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userLoginType = userLoginType;
        this.userRole = userRole;
    }
    public UserEntity(String userId, String userName,
                      String userLoginType,
                      String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userLoginType = userLoginType;
        this.userRole = userRole;
        this.userEmail = "";
    }

}
