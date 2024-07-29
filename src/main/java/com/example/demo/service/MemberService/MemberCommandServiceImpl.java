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
        } // email을 기준으로 멤버를 찾은 후 멤버가 존재하는지 확인 후 에러 반환

        String profileImageUrl = s3Manager.uploadFile(request.getProfileImage()); //s3Manager를 통해 file 업로드
        String encodedPassword = passwordEncoder.encode(request.getPassword()); //passwordEncoder를 통해 비밀번호 암호화
        Member newMember = MemberConverter.toMember(request, encodedPassword, profileImageUrl); // 새로운 멤버 생성
        return memberRepository.save(newMember); // 저장
    }
    //소셜 회원가입
    @Override
    public Member SocialJoinMember(MemberRequestDTO.SocialJoinDTO request, Long memberId){
        Member member = memberRepository.findById(memberId) //memberId를 통해 조회한 후 없으면 member없음을 반환
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String profileImageUrl = s3Manager.uploadFile(request.getProfileImage()); //s3Manager를 통해 file 업로드
        MemberConverter.toSocialMember(request, member, profileImageUrl); //social Member 업데이트
        return memberRepository.save(member); //저장
    }
    //로그인
    @Override
    public MemberResponseDTO.LoginResultDTO login(MemberRequestDTO.LoginDTO request) {
        Member member = memberRepository.findByEmail(request.getEmail()) //email을 통해 member를 조회
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword())) //비밀번호가 일치하는지 조회
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_CREDENTIALS));// 에러 발생

        JwtToken jwtToken = jwtProvider.generateToken(member); // 로그인 성공하면 accessToken, refreshToken 생성

        return MemberConverter.toLoginResultDTO(member, jwtToken);
    }
    //키워드 설정
    @Override
    public Member setKeyword(MemberRequestDTO.setKeywordDTO request, Long memberId) {
        Member member = memberRepository.findById(memberId) //멤버 조회 후 에러 반환
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Keyword> keywords = keywordRepository.findAllById(request.getKeywordIdList()); // 사용자가 선택한 키워드를  keyword에서 조회

        List<MemberKeyword> memberKeywords = keywords.stream() //해당 키워드들을 memberKeyword로 저장
                .map(keyword -> MemberKeyword.builder()
                        .member(member)
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());

        member.getMemberKeywordList().clear();// 이전에 선택한 내용을 삭제
        member.getMemberKeywordList().addAll(memberKeywords); // 새로받은 memberKeyword들을 저장

        memberRepository.save(member); // 저장

        return member;
    }
}

