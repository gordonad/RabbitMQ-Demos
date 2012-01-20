import com.rabbitmq.client.*

println """
***********************************************************
*  Enter queue name (required)
*  Create QueueingConsumer
*  Consume string message from queue
*
*  No Explicit RoutingKey
*
*  Connection set to 'amqp://localhost:5672'
*
*  -- AMQP Defaults --
*     RoutingKey = queue name
***********************************************************

"""
@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def reader = System.in.newReader()

// create new version of readLine that accepts a prompt
reader.metaClass.readLine = { String prompt -> print prompt ; return readLine() }

final uri = "amqp://localhost"
def queueName = reader.readLine "Queue Name: "

def connectionFactory = new ConnectionFactory()
connectionFactory.uri = uri
def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

println "Create Queueing Consumer for channel '$channel'"
def consumer = new QueueingConsumer(channel)
println "Created Consumer '$consumer' on channel '$channel'"

println "Begin consuming messages from queue '$queueName' with consumer '$consumer'"
def result = channel.basicConsume queueName, consumer
println "Basic consume returned '$result'"


while (true) {
    println "Checking for Messages... "
    def delivery = consumer.nextDelivery 1000
    if (!delivery) {
        println "No more messages! Done."
        break
    }
    println "$delivery"
    
    def basicProperties = delivery.properties
    println "Properties'${basicProperties}'"
    println "Message Properties \n\t[\n\t\tApp Id '${basicProperties.appId}'"
    println "\t\tClass Id '${basicProperties.classId}'"
    println "\t\tClass Name '${basicProperties.className}'"
    println "\t\tCluster Id '${basicProperties.clusterId}'"
    println "\t\tContent Encoding '${basicProperties.contentEncoding}'"
    println "\t\tContent Type '${basicProperties.contentType}'"
    println "\t\tCorrelation Id '${basicProperties.correlationId}'"
    println "\t\tDelivery Mode '${basicProperties.deliveryMode}'"
    println "\t\tExpiration '${basicProperties.expiration}'"
    println "\t\tHeaders '${basicProperties.headers}'"
    println "\t\tMessage Id '${basicProperties.messageId}'"
    println "\t\tPriority '${basicProperties.priority}'"
    println "\t\tReply To '${basicProperties.replyTo}'"
    println "\t\tTimestamp '${basicProperties.timestamp}'"
    println "\t\tType '${basicProperties.type}'"
    println "\t\tUser Id '${basicProperties.userId}'\n\t]"

    def deliveryTag = delivery.envelope.deliveryTag
    println "Message Envelope \n\t[\n\t\tDelivery Tag '$deliveryTag'"
    println "\t\tis Redelivered? '${delivery.envelope.redeliver}'"
    println "\t\tExchange '${delivery.envelope.exchange}'"
    println "\t\tRouting Key '${delivery.envelope.routingKey}'\n\t]"
    println "Body '${new String(delivery.body)}'"


    println "Acknowledging for delivery tag '$deliveryTag'"
    channel.basicAck deliveryTag, false
    println "Acknowledged delivery tag '$deliveryTag'"
}

channel.close()
connection.close()
