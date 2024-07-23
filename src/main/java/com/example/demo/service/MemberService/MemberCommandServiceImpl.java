package com.example.demo.service.MemberService;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.demo.Provider.JwtProvider;

import com.example.demo.apiPayload.code.status.ErrorStatus;
import com.example.demo.apiPayload.exception.handler.KeywordHandler;
import com.example.demo.aws.s3.AmazonS3Manager;
import com.example.demo.converter.MemberConverter;
import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;
import com.example.demo.domain.enums.SocialType;
import com.example.demo.domain.mapping.MemberKeyword;
import com.example.demo.repository.KeywordRepository;
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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//쓰기
@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AmazonS3Manager s3Manager;

    @Override
    public Member joinMember(MemberRequestDTO .JoinDTO request){
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String profileImageUrl = s3Manager.uploadFile(request.getProfileImage());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member newMember = MemberConverter.toMember(request, encodedPassword, profileImageUrl);
        return memberRepository.save(newMember);
    }

    @Override
    public Member SocialJoinMember(MemberRequestDTO.SocialJoinDTO request, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id " + memberId));
        String profileImageUrl = s3Manager.uploadFile(request.getProfileImage());
        MemberConverter.toSocialMember(request, member, profileImageUrl);
        return memberRepository.save(member);
    }

    @Override
    public MemberResponseDTO.LoginResultDTO login(MemberRequestDTO.LoginDTO request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        JwtToken jwtToken = jwtProvider.generateToken(member);

        return MemberConverter.toLoginResultDTO(member, jwtToken);
    }

    @Override
    public Member setKeyword(MemberRequestDTO.setKeywordDTO request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        List<Keyword> keywords = keywordRepository.findAllById(request.getKeywordIdList());
        if (keywords.size() != request.getKeywordIdList().size()) {
            throw new IllegalArgumentException("One or more Keyword IDs are invalid.");
        }

        List<MemberKeyword> memberKeywords = keywords.stream()
                .map(keyword -> MemberKeyword.builder()
                        .member(member)
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());

        member.getMemberKeywordList().clear();
        member.getMemberKeywordList().addAll(memberKeywords);

        memberRepository.save(member);

        return member;
    }
}

