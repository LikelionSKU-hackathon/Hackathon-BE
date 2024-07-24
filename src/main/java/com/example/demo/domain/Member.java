package com.example.demo.domain;

import com.example.demo.domain.common.BaseEntity;
import com.example.demo.domain.enums.Role;
import com.example.demo.domain.enums.SocialType;
import com.example.demo.domain.mapping.MemberKeyword;
import com.fasterxml.jackson.databind.introspect.MemberKey;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 50)
    private String ageGroup;

    @Column(nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = true, length =255)
    private String profileImage;

    @OneToMany(mappedBy ="member", cascade= CascadeType.ALL)
    private  List<MemberKeyword> memberKeywordList = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade=CascadeType.ALL)
    private List<Diary> diaryList = new ArrayList<>();

}


