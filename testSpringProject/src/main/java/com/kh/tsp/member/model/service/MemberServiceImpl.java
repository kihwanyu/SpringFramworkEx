package com.kh.tsp.member.model.service;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.kh.tsp.member.model.dao.MemberDao;
import com.kh.tsp.member.model.vo.Member;


@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberDao md;
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	@Override
	public Member loginMember(Member m) throws LoginException {
		
		return md.loginCheck(m);
	}
	/*트랜젝션 범위 지정
	 * 불안정하기 때매 XML 권장
	 * */
	@Override
	/*@Transactional(propagation=Propagation.REQUIRED, rollbackFor={Exception.class})*/
	public void insertMember(Member m) {
		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		
		메소드 내로 트렉젝션 범위를 지정하겠다.
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			sqlSession.getConnection().setAutoCommit(false);
			int result = md.insertMember(m, sqlSession);
			int result2 = md.insertBoard(m, sqlSession);
			
			transactionManager.commit(status);
		} catch (SQLException e) {
			transactionManager.rollback(status);
		}*/
		
		int result = md.insertMember(m, sqlSession);
		int result2 = md.insertBoard(m, sqlSession);
		
	}

}
