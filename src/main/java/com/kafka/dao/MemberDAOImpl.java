package com.kafka.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kafka.domain.Eai;

@Repository
public class MemberDAOImpl implements MemberDAO {
	
	private static final String NAMESPACE = "com.kafka.mapper.MemberMapper";
	private final SqlSession sqlSession;
	
	@Inject
	public MemberDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	
	@Override
	public List<Eai> eaiListAll() {
		return sqlSession.selectList(NAMESPACE + ".kafkaEaiSdw");
	}


	@Override
	public String getTime() {
		return sqlSession.selectOne(NAMESPACE + ".getTime");
	}
	
	@Override
	public int eaiColumnUpdate(Eai result) {
		System.err.println(result);
		return sqlSession.update(NAMESPACE + ".eaiColumnUpdate", result);
	}
}
