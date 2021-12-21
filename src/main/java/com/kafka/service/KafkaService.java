package com.kafka.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaService {

	ResponseEntity<Object> producer(String message, String callUUID) throws JsonProcessingException;
	
	void getEaiList(String callUUID) throws Exception;
}
