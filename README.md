# SQS
## Introduction
SQS is the Simple Queue Service provided by AWS. With SQS we can push messages on the queue and it will store those messages for a specified amount of time or until deleted. It does not follow a pub sub model but rather a polling model where messages have to explicitly be requested. (You could set up cloud watch logs to trigger a lambda or notify a server to begin the polling process, or you can manually invoke the polls, or you could set up a server to automatically poll at certain time intervals). 

The SQS messaging queues we create would be apart of AWS serverless services. We do not have to manage the server that the Queue is hosted on and we do not pay for the duration in which it is not being used. We only pay based on the messages we are sending.

## AWS Console Demo
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

### Working with messgaes
- You should now see your queue in the table. 

- Select you queue and then at the top select Send Message from the Queue Action dropdown.

- Put anything you want in the message body and then send the message. 

- It should refresh the queue and show 1 message available. You can send a few more if you want.

- You can select View/Delete Messages from the dropdown as well to try things out.

- You can also purge the queue from the dropdown which would remove all messages.

## Updating Queue Permissions
If you do not already have an SNS Topic, I would skip this section for now and come back to it later.
There are 2 ways to control permissions for SQS Queues. If a group or role is being used to access the queue and has a policy that give it permissions it will have no problem. However the Queue itself also has the ability to give additional permissions.

### Give An SNS Topic Permissions
- Select the queue and then at the bottom of the page select the permissions tab.
- Select add permission.
- Leave Effect on Allow.
- Select Everybody for principal. - This will give everyone, everywhere access to this queue unless we add a condition.
- Select Send Message from the actions dropdown.
- Now click on Add Conditions.
- Qualifier do ForAllValues (I'm not sure on the difference between Qualifiers exactly, will have to look more into it)
- Condition chose ArnEquals
- Key chose SourceArn
- Value paste in the ARN from the SNS Topic.
- Select Add Condition.
- Select Add Permission

## Message States and Dead Letter Queues
When we poll a message from our application we can use the default specified for Visibility Timeout, or we can override and specify a different duration. Either way what this time represents is basically how long it should take for this message to be processed. When a message is received by a server it will go to the state of inflight for the duration of the Visibility Timeout. During this time no other server will read that message when polling the queue. During that time maybe the server that polled it crashed or slowed down too much or couldn't understand the message. If the server does not delete the message before the time runs out it will go back to available and another server can attempt to process the message. 

How can we stop a message from going between available and inflight over and over if the server keeps failing to process it? For this we can specify a Queue to use as a Dead Letter Queue. You would specify it in the configuration and can set the number of times a message can go from available to inflight before it gets sent to the Dead Letter Queue specified. This would allow us to not keep wasting server time failing over and over but also not lose the message and process this message in a different way from another queue. Worst case maybe someone has to manually look at the messages in this queue to see what went wrong. 

# SNS
## Introduction
SNS is the Simple Notification Service provided by AWS. With SNS we can send messages to topics and the topic will push that message out to all of its subscribers. We can have a variety of subscribers, such as: AWS Lambdas, SQS queues, HTTP endpoints, email, etc. SNS will not store messages so if one of the subscribers is down at the time a message is published it will just be lost. However you will often see SNS in conjunction with SQS where a messaged published by an SNS topic could be broadcasted to multiple SQS Queues.

## AWS Console Demo
### Creating The Topic
The process of creating a topic is very easy. Select create topic and give it a topic name and display name. Once you do this you should see your topic with it's arn in a table. If not you can select topics from the side nav of SNS and it should show the table.

### Subscribing To The Topic
- Select your topic from the table and then select Subscribe to Topic from the dropdown.
- Do not change the Topic ARN.
- Change Protocol to Amazon SQS
- Copy and past the SQS arn into the Endpoint box.
- Now you can select publish to topic and give it a message. - Don't change the message format to JSON unless you have a reason why. You can leave it as RAW and insert JSON text into the message still.
- At this point you would notice that it does not actually publish the message to the SQS QUEUE - go back to the section on updating SQS Queue permissions.


# Practical Use Cases
## Scenario 1 
Lets say we updated a Trainer from our application Caliber. Caliber could save it in it's database and then push the updated user to an SNS topic. The topic could then push that message into an SQS queue for BAM, Assign Force, and Track Force. This way each app could process the messages from their own queues at their own rate. 

## Scenario 2
Or another use case might be an application where a purchase is made. It would update it's database and send a message to some topic saying that the purchase was made. Then the topic could send that message to a queue that ends up being used to send out confirmation emails and another queue that notifies the actual vendor that was selling the item on the website.

In this scenario what if something was off with the message and it caused the application attempting to notify the vendor to crash over and over. We would want a dead letter queue in place that the message would go to. At this point you would maybe want to set up cloud watch logs to alert the team saying that a message entered the dead letter queue and then someone can go into the message and process it manually or fix the problem that was causing the application to not be able to notify the vendor. 


# Java Demo
## Environment Variables Needed For This Demo
MESSAGING_ACCESS_KEY - this is an IAM user's access key id
MESSAGING_SECRET_ACCESS_KEY - this is the IAM user's secret access key, you must get this at the time the user is created
MESSAGING_TOPIC_ARN - the arn for the topic
MESSAGING_QUEUE_URL - not the ARN for the messaging queue but the url

note that after updating environment variables you will have to restart eclipse if it is open for it see see the new values.

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!IMPORTANT NOTICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  
PLEASE NEVER HARD CODE ACCESS KEYS INTO YOUR CODE OR PUT THEM IN A FILE THAT GETS PUSHED TO YOUR ONLINE REPO!!!!!!!!!!!
ALSO PLEASE NEVER CREATE KEYS THAT HAVE FULL ADMIN ACCESS TO EVERY SERVICE THAT AWS PROVIDES!!!!!!!!!!!!

## Viewing and Deleting Messages From Java
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

## Running The Demo
Be sure you have all of the environment variables on your computer.
Clone this repo and import the maven project.

There is a SnsUtil that has a method for publishing a message to our SNS topic.

There is a SqsUtil that has a method for receiving a message from our QUEUE, and also a method for deleting a message from the QUEUE.

You can use the launcher and just comment out or uncomment the sections based on what you want to do.

I recommend first showing just the sns part of publishing a message. Then I would show reading a message, the visibility timeout is set to 15 seconds so you can read it and then immediately go to SQS from the AWS Console and show the message is now in flight. Wait a little bit and it will go back to available. Then you can make the assumption that when you read it it process successfully and then call the method to delete it.

