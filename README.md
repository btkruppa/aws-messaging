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

- You can select View/Delete Messages from the dropdown as well to try things out.

### Viewing and Deleting Messages From Java
- To do this properly we will first need to create and IAM user.

- Open up the IAM service, prefferably in another tab. 

- Creating a user
  - Select Users from the side nav on the left.
  - Select Add user now.
  - I will give mine a username of java-demo
  - Access type you only need to select Programmatic access.
  - Select Next: Permissions
  - Select Create group
  - I will name my group java-demo-group, multiple users can be attached to groups.
  - In the table below, search for and select both AmazonSNSFullAccess and AmazonSQSFullAccess. - note that in an actual production application you wouldn't want to give full access to all of sns and sqs but rather you would create a more restrictive policy that only allows for certain operations on certain sns topics and sqs queues.
  - Select Create group from the bottom.
  - Select Next: Review
  - Select Create-user
  - Note the access keys, AND NEVER HARD CODE THESE OR PUT THEM IN A FILE THAT WILL BE PUSHED TO AN ONLINE REPO!!!!!
  - We will store these in environment variables.
  - Add an environment variable called MESSAGING_ACCESS_KEY with the access key id, and add an environment variable called MESSAGING_SECRET_ACCESS_KEY
  - Then you can close the page showing your keys - note if you did this wrong you cannot view these keys again, you will have to create a new user which isn't that big a deal.
  - We will aso need to add an environment variable called MESSAGING_QUEUE_URL which has the url specified in the details when you select your queue in SQS.
  - If you had eclipse open before you added the environment variables you will have to restart it for the code to recognize them.
  - You should now be able to run the launcher to perform the operation you would like to do and it should work for SQS.

# SNS
SNS is the Simple Notification Service provided by AWS. With SNS we can send messages to topics and the topic will push that message out to all of its subscribers. SNS will not store messages so if one of the subscribers is down at the time a message is published it will just be lost. Messages can be published to 