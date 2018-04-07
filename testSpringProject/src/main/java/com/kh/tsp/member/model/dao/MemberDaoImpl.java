package com.kh.tsp.member.model.dao;


import javax.security.auth.login.LoginException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kh.tsp.member.model.vo.Member;
@Repository
public class MemberDaoImpl implements MemberDao{
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Override
	public Member loginCheck(Member m) throws LoginException {
		System.out.println("dao userId : " + m.getUserId());
		System.out.println("dao userPw : " + m.getUserPwd());
		System.out.println("sqlSession : " + sqlSession);
		
		/*Member member = sqlSession.selectOne("Member.loginCheck",m);
		
		if(member == null){
			throw new LoginException("로그인 실패!");
		}*/
		
		Member member = null;
		
		String cryptPwd = sqlSession.selectOne("Member.selectPwd",m.getUserId());

		System.out.println("암호화된 비밀번호 : " + cryptPwd);
		
		if (!passwordEncoder.matches(m.getUserPwd(), cryptPwd)) {
			throw new LoginException("로그인 실패!");
		} else {
			member = sqlSession.selectOne("Member.loginCheck",m);
		}
		
		return member;
	}
	@Override
	public int insertMember(Member m, SqlSessionTemplate sqlSession) {
		// TODO Auto-generated method stub
		return sqlSession.insert("Member.insert",m);
	}
	@Override
	public int insertBoard(Member m, SqlSessionTemplate sqlSession) {
		// TODO Auto-generated method stub
		return sqlSession.insert("Member.insertBoard");
	}
	
}
