package com.example.demo.service.MemberService;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.demo.Provider.JwtProvider;

import com.example.demo.apiPayload.code.status.ErrorStatus;
import com.example.demo.apiPayload.exception.handler.KeywordHandler;
import com.example.demo.apiPayload.exception.handler.MemberHandler;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
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
    //일반 회원가입
    @Override
    public Member joinMember(MemberRequestDTO .JoinDTO request){
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXIST);
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new MemberHandler(ErrorStatus.PASSWORD_MISMATCH);
        }
        String profileImageUrl = s3Manager.uploadFile(request.getProfileImage());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member newMember = MemberConverter.toMember(request, encodedPassword, profileImageUrl);
        return memberRepository.save(newMember);
    }
    //소셜 회원가입
    @Override
    public Member SocialJoinMember(MemberRequestDTO.SocialJoinDTO request, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String profileImageUrl = s3Manager.uploadFile(request.getProfileImage());
        MemberConverter.toSocialMember(request, member, profileImageUrl);
        return memberRepository.save(member);
    }
    //로그인
    @Override
    public MemberResponseDTO.LoginResultDTO login(MemberRequestDTO.LoginDTO request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_CREDENTIALS));

        JwtToken jwtToken = jwtProvider.generateToken(member);

        return MemberConverter.toLoginResultDTO(member, jwtToken);
    }
    //키워드 설정
    @Override
    public Member setKeyword(MemberRequestDTO.setKeywordDTO request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        List<Keyword> keywords = keywordRepository.findAllById(request.getKeywordIdList());
        if (keywords.size() != request.getKeywordIdList().size()) {
            throw new KeywordHandler(ErrorStatus.INVALID_KEYWORD_IDS);
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

