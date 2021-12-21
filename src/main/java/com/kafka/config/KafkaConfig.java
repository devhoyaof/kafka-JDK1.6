package com.kafka.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {
	


	/*
	 * public KafkaProducer<String, String> producerSetting() { Properties props =
	 * new Properties(); props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	 * kafkaProp.getProperty("kafka.server"));
	 * props.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG,
	 * kafkaProp.getProperty("kafka.max_block_ms_config"));
	 * props.setProperty(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,
	 * kafkaProp.getProperty("kafka.max_request_size_config"));
	 * props.setProperty(ProducerConfig.ACKS_CONFIG,
	 * kafkaProp.getProperty("kafka.acks_config"));
	 * props.setProperty(ProducerConfig.RETRIES_CONFIG,
	 * kafkaProp.getProperty("kafka.retries_config"));
	 * props.setProperty(ProducerConfig.LINGER_MS_CONFIG,
	 * kafkaProp.getProperty("kafka.linger_ms_config"));
	 * props.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG,
	 * kafkaProp.getProperty("kafka.buffer_memory_config"));
	 * props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	 * StringSerializer.class.getName());
	 * props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	 * StringSerializer.class.getName());
	 * 
	 * 
	 * // Producer 梓端 持失 KafkaProducer<String, String> producer = new
	 * KafkaProducer<String, String>(props); return producer;
	 * 
	 * }
	 */
	
	public KafkaProducer<String, String> producerSetting() {
		Properties props = new Properties();
		props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "nex_grid");
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9090,localhost:9091,localhost:9092");
		props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty(ProducerConfig.RETRIES_CONFIG, "0");
		props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
		props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "0");
		props.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
		// Producer 梓端 持失
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		return producer;
	}

}
