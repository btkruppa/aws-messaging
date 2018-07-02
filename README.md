# SQS
## Introduction
SQS is the Simple Queue Service provided by AWS. With SQS we can push messages on the queue and it will store those messages for a specified amount of time or until deleted. It does not follow a pub sub model but rather a polling model where messages have to explicitly be requested. (You could set up cloud watch logs to trigger a lambda or notify a server to begin the polling process, or you can manually invoke the polls, or you could set up a server to automatically poll at certain time intervals). 

## Demo
In this demo we will look at using the ui to create a queue, send messages to the queue, view messages, and delete messages. 

After doing that from the UI we will look at sending, receiving, and deleting messages using the AWS SDK in Java. However the AWS SDK does have the functionaity to much more such as creating, configuring, and deting queues altogether. 

### Setting Up The Queue
- Select the Simple Queue Service from the Services dropdown in the AWS console.

- If you have no queues it should have an option for get started now, if you do you can select create new queue.

- Give the queue a name, I will call mine: demo-queue.

- You can leave it on Standard Queue, it talks about the differences between Standard and FIFO in that menu. 

- If you wish you can select Configure Queue at the bottom
  - However you shouldn't have to change any of the options from their defaults, unless you want to.

- Then select Create Queue at the bottom.

### Sending Messages From The UI
- You should now see your queue in the table. 

- Select you queue and then at the top select Send Message from the Queue Action dropdown.

- Put anything you want in the message body and then send the message. 

- It should refresh the queue and show 1 message available. You can send a few more if you want.

# SNS
SNS is the Simple Notification Service provided by AWS. With SNS we can send messages to topics and the topic will push that message out to all of its subscribers. SNS will not store messages so if one of the subscribers is down at the time a message is published it will just be lost. Messages can be published to 