package com.kafka.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.config.KafkaConfig;
import com.kafka.dao.MemberDAO;
import com.kafka.domain.Eai;
import com.kafka.service.KafkaService;
import com.kafka.utils.DateUtil;

@Service
@PropertySource("classpath:/kafka.properties")
public class KafkaServiceImpl implements KafkaService{
	
	private static final Logger log = LoggerFactory.getLogger(KafkaServiceImpl.class);
	
	@Autowired
	Environment env;
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private KafkaConfig kafkaProducerConfig;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	
	@Override
	public ResponseEntity<Object> producer(String message, String callUUID) throws JsonProcessingException {
		String topic = "from_gdcb_gdcbdatacreated_message"; 
		String setPayload = "";
		
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("ID", UUID.randomUUID().toString()); 
		map.put("DESTINATION", topic); 
		map.put("DATE", DateUtil.getDateStr(DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss")));
		map.put("X-App-Name", "sdcb_app"); 
		map.put("X-Global-Transation-ID", callUUID); 
		JSONObject headersData = new JSONObject(map);
		
		setPayload = mapper.writeValueAsString(message);
		
		JSONObject payload = new JSONObject();
		payload.put("headers", headersData);
		payload.put("payload", setPayload);
				
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, payload.toString());
		
		try {
			System.err.println(kafkaProducerConfig);
			kafkaProducerConfig.producerSetting().send(record, new KafkaCallback());
			log.info(" kafka Message Info => {} ", record);
			return new ResponseEntity<Object>(payload.toString(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(payload.toString(), HttpStatus.BAD_REQUEST);
		}
		
	}
	

	@Override
	public void getEaiList(String callUUID) throws Exception {
		String topic = env.getProperty("kafka.topic_name");
		String xAppName = env.getProperty("kafka.x_app_name");
		String setPayload = "";
		
		List<Eai> result = memberDAO.eaiListAll();
		
		for(Eai data : result) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", UUID.randomUUID().toString());
			map.put("DESTINATION", topic);
			map.put("DATE", DateUtil.getDateStr(DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss")));
			map.put("X-App-Name", xAppName);
			map.put("X-Global-Transation-ID", callUUID);
			JSONObject headersData = new JSONObject(map);

			setPayload = mapper.writeValueAsString(data);

			JSONObject payload = new JSONObject();
			payload.put("headers", headersData);
			payload.put("payload", setPayload);

			ProducerRecord<String, String> record = new ProducerRecord<String, String>(
					topic, payload.toString()
			);

			if(result.size() > 0) {
				try {
					kafkaProducerConfig.producerSetting().send(record, new KafkaCallback()).get();
					log.info(" kafka Message Info => {} ", record);
					memberDAO.eaiColumnUpdate(data);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					kafkaProducerConfig.producerSetting().flush();
					kafkaProducerConfig.producerSetting().close();
				}
			} else {
				log.info("kafka Message not data");
				kafkaProducerConfig.producerSetting().close();
			}
		}
		
	}
	

	/*
	 * @Override public ResponseEntity<Object> producer(String message, String
	 * callUUID) {
	 * 
	 * String topic = "from_gdcb_gdcbdatacreated_message"; String setPayload = "";
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("ID",
	 * UUID.randomUUID().toString()); map.put("DESTINATION", topic); map.put("DATE",
	 * DateUtil.getDateStr(DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss")));
	 * map.put("X-App-Name", "sdcb_app"); map.put("X-Global-Transation-ID",
	 * callUUID); JSONObject headersData = new JSONObject(map);
	 * 
	 * try { setPayload = mapper.writeValueAsString(message);
	 * 
	 * JSONObject payload = new JSONObject(); payload.put("headers", headersData);
	 * payload.put("payload", setPayload);
	 * 
	 * ProducerRecord<String, String> record = new ProducerRecord<String,
	 * String>(topic, payload.toString());
	 * 
	 * try { kafkaProducerConfig.producerSetting().send(record, new
	 * KafkaCallback()).get(); log.info(" kafka Message Info => {} ", record);
	 * return new ResponseEntity<Object>(payload, HttpStatus.OK); } catch (Exception
	 * e) { e.printStackTrace(); return new ResponseEntity<Object>(payload,
	 * HttpStatus.BAD_REQUEST); } finally {
	 * kafkaProducerConfig.producerSetting().flush();
	 * kafkaProducerConfig.producerSetting().close(); }
	 * 
	 * 
	 * } catch (JsonProcessingException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return new ResponseEntity<Object>(message, HttpStatus.OK); }
	 */
	/**
	 * KafkaCallBack method create
	 */
	class KafkaCallback implements Callback {
		final Logger logger = LoggerFactory.getLogger(KafkaCallback.class);
		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			if(e == null) {
				logger.info("================================================================== Success ===================================================================");
				logger.info("SuccessFully recieved the datils as : \n "+
							"Topic: {} " + recordMetadata.topic() + "\n" +
							"Partition: {} " +  recordMetadata.partition() + "\n" +
							"Offset: {} " + recordMetadata.offset() + "\n" +
							"Timestamp: {} " + recordMetadata.timestamp());
				logger.info("==============================================================================================================================================");
			} else {
				logger.info("=================================================================== fail =====================================================================");
				logger.error("Can't producer,getting error{} " , e.getMessage(), e);
				logger.info("==============================================================================================================================================");
			}
		}
	}


}
