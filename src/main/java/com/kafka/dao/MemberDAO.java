package com.kafka.dao;

import java.util.List;

import com.kafka.domain.Eai;

public interface MemberDAO {

	// ����ð�üũ
	public String getTime();

	List<Eai> eaiListAll();
	
	int eaiColumnUpdate(Eai result);
		
}
