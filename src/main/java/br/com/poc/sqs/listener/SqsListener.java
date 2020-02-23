package br.com.poc.sqs.listener;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsListener {
	
	@PostConstruct
	public void init() {
		
		System.out.println("SqsListener...");
		
	}

	@JmsListener(destination = "${sqs.bucket.name}")
	public void read(String payload) throws IOException {
		
		System.out.println(payload);

	}
}
