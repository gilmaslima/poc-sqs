package br.com.poc.sqs.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@EnableJms
@Configuration
public class SqsConfig {

	private SQSConnectionFactory connectionFactory;

	@Value("${aws.region}")
	private String region; 
	
	@Value("${queue.url}")
	private String endpointUrl;
	
	@PostConstruct
	@Primary
	public void init() {
		connectionFactory = createSQSConnectionFactory();
	}

	private SQSConnectionFactory createSQSConnectionFactory() {
		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(endpointUrl, region);
		final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
		return new SQSConnectionFactory(new ProviderConfiguration(), sqs);
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	@Bean
	public JmsTemplate defaultJmsTemplate() {
		return new JmsTemplate(connectionFactory);
	}

	@Bean
	public ClientConfiguration clientConfiguration() {
		final ClientConfiguration clientConfiguration = new ClientConfiguration();

		return clientConfiguration;
	}

	@Bean
	public AmazonSQS amazonSQS() {
		return AmazonSQSClientBuilder.standard().withClientConfiguration(clientConfiguration()).build();
	}

}
