package com.kafka.dao;

import java.util.List;

import com.kafka.domain.Eai;

public interface MemberDAO {

	// 현재시간체크
	public String getTime();

	List<Eai> eaiListAll();
	
	int eaiColumnUpdate(Eai result);
		
}
