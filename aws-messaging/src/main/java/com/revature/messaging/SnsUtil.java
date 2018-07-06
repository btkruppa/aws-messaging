package com.revature.messaging;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

//@Component
public class SnsUtil {
	private BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("MESSAGING_ACCESS_KEY"), System.getenv("MESSAGING_SECRET_ACCESS_KEY"));
	private AmazonSNS snsClient = AmazonSNSClient
										.builder()
										.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
										.withRegion(System.getenv("MESSAGING_REGION"))
										.build();
	
	public SnsUtil() {
		System.out.println(System.getenv("MESSAGING_ACCESS_KEY"));
		System.out.println(System.getenv("MESSAGING_SECRET_ACCESS_KEY"));
	}

	/**
	 * Publishes a message to the sns topic, really we should probably have a properties file to link
	 * topics to their arn and have the topic be passed in as a parameter
	 * @param message
	 */
	public void publish(String message) {
		System.out.println(System.getenv("MESSAGING_TOPIC_ARN"));
		PublishRequest pubRequest = new PublishRequest(System.getenv("MESSAGING_TOPIC_ARN"), message);
		PublishResult pubResult = snsClient.publish(pubRequest);
		System.out.println("MessageId - " + pubResult.getMessageId());
	}	
}
