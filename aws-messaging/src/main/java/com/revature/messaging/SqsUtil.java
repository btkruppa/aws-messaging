package com.revature.messaging;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class SqsUtil {
	private BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("MESSAGING_ACCESS_KEY"), System.getenv("MESSAGING_SECRET_ACCESS_KEY"));
	private AmazonSQS sqsClient = AmazonSQSClient
										.builder()
										.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
										.build();
	private final String QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/492203503159/demo-queue";
	
	/**
	 * Would be better if we passed in a parameter to determine the queue to poll from
	 * @return 
	 */
	public ReceiveMessageResult poll() {
		ReceiveMessageRequest messageRequest = new ReceiveMessageRequest(QUEUE_URL);
		messageRequest.setVisibilityTimeout(15);
		ReceiveMessageResult message = sqsClient.receiveMessage(messageRequest);
		System.out.println(message.getMessages());
		return message;
	}
	
	public void deleteMessage(String receiptHandle) {
		DeleteMessageRequest deleteRequest = new DeleteMessageRequest(QUEUE_URL, receiptHandle);
		sqsClient.deleteMessage(deleteRequest);	
	}

}
