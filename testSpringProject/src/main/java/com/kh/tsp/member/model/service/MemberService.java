package com.kh.tsp.member.model.service;

import javax.security.auth.login.LoginException;

import com.kh.tsp.member.model.vo.Member;

public interface MemberService {

	Member loginMember(Member m) throws LoginException;

	void insertMember(Member m);

}
