package com.amm.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	String kafkaTopic = "stock_topic";
	
	public void send(String message) {
	    kafkaTemplate.send(kafkaTopic, message);
	}
}
