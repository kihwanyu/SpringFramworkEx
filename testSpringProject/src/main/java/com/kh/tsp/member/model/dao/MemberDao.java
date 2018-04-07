package com.kh.tsp.member.model.dao;

import javax.security.auth.login.LoginException;

import org.mybatis.spring.SqlSessionTemplate;

import com.kh.tsp.member.model.vo.Member;

public interface MemberDao {

	Member loginCheck(Member m) throws LoginException;

	int insertMember(Member m, SqlSessionTemplate sqlSession);

	int insertBoard(Member m, SqlSessionTemplate sqlSession);

}
