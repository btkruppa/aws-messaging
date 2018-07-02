package com.revature;

import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.revature.messaging.SnsUtil;
import com.revature.messaging.SqsUtil;

public class Launcher {
	public static void main(String[] args) {
//		SnsUtil snsUtil = new SnsUtil();
//		snsUtil.publish("testing");
		
		
		SqsUtil sqsUtil = new SqsUtil();
		ReceiveMessageResult message = sqsUtil.poll();
		sqsUtil.deleteMessage(message.getMessages().get(0).getReceiptHandle());
		
	}
}
