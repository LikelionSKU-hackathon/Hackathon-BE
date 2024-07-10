package com.example.demo.service.MemberService;


import com.example.demo.domain.Member;
import com.example.demo.web.dto.MemberRequestDTO;
import com.example.demo.web.dto.MemberResponseDTO;

public interface MemberCommandService {
    Member joinMember(MemberRequestDTO.JoinDTO request);
    MemberResponseDTO.LoginResultDTO login (MemberRequestDTO.LoginDTO request);


}
