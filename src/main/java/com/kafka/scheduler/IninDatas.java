package com.kafka.scheduler;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kafka.service.KafkaService;





@Service("anotherObject")
public class IninDatas {

	@Autowired
	KafkaService kafkaService;

	public void loadIpAddress() {
		try {
			String callUUID = UUID.randomUUID().toString();
			kafkaService.getEaiList(callUUID);
		} catch (Exception ex) {
		}
	}
}
