package com.example.demo.service.MemberService;

import com.example.demo.domain.Keyword;
import com.example.demo.domain.Member;

import java.util.List;

public interface MemberQueryService {
    Member getMypage (Long MemberId);

    List<Keyword> getKeyword(String age_group);
}
