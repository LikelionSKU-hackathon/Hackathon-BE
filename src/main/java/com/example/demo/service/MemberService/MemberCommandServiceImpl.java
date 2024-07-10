package com.example.demo.service.MemberService;

import com.example.demo.Provider.JwtProvider;

import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.web.dto.JwtToken;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//쓰기
@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @Override
    public Member joinMember(MemberRequestDTO .JoinDTO request){
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호와 확인 비밀번호 일치 확인
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Member 생성
        Member newMember = MemberConverter.toMember(request, encodedPassword);

        return memberRepository.save(newMember);
    }

    @Override
    public MemberResponseDTO.LoginResultDTO login(MemberRequestDTO.LoginDTO request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        JwtToken jwtToken = jwtProvider.generateToken(member);

        return MemberConverter.toLoginResultDTO(member, jwtToken);
    }
}
