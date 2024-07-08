package com.example.demo.service.MemberService;

import com.example.demo.domain.Member;
import com.example.demo.web.dto.MemberRequestDTO;

public interface MemberCommandService {
    Member joinMember(MemberRequestDTO.JoinDTO request);
    //Member Login (MemberRequestDTO.)
}
