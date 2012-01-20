import com.rabbitmq.client.ConnectionFactory

println """
***********************************************************
*  1. Create Direct Exchange named 'my.exchange'
*  2. Create Queue named 'my.queue"
*  3. Create Binding 'my.exchange' to 'my.queue' with
*     RoutingKey 'my.routing.key'
*  4. Publish message on 'my.exchange' with 'my.routing.key'
*  5. Get message (synchronous) from 'my.queue' & auto ack
*
*  Connection set to 'amqp://localhost:5672'
*
***********************************************************

"""

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')


def connectionFactory = new ConnectionFactory()
println "Create [ConnectionFactory]"
connectionFactory.host = "localhost"
connectionFactory.port = 5672
//connectionFactory.virtualHost = '/'
//connectionFactory.username = 'guest'
//connectionFactory.password = 'guest'

println "Create [Connection] <-- [ConnectionFactory]"
def connection = connectionFactory.newConnection()
println "Create [Channel] <-- [Connection]"
def channel = connection.createChannel()

// Exchange, Type

println "\n\n*** 1. EXCHANGE\n"
println "[Exchange] <-- [Channel]"
def exchange = "my.exchange"
println "Declaring Direct Exchange '$exchange'"
def declareOk = channel.exchangeDeclare "$exchange", "direct"
println "Declared Exchange returned $declareOk"
//println "Channel Type '${channel.class.name}'"

// Queue, Durable, Exclusive, AutoDelete, Arguments
println "\n\n*** 2. QUEUE\n"
println "[Queue] <-- [Channel]"
def queueName = "my.queue"
println "Declaring Queue '$queueName' non-durable, non-exclusive, autodelete and no arguments"
def declareOk1 = channel.queueDeclare queueName, false, false, true, null
println "Declare Queue returned '$declareOk1'"


// Queue, Exchange, Routing Key
println "\n\n*** 3. BIND\n"
def routingKey = "my.routing.key"
println "[Queue]+[RoutingKey]+[Exchange] <-- [Channel]"
println "Binding Queue '$queueName' to Exchange '$exchange' with RoutingKey '$routingKey'"
def bindOk = channel.queueBind queueName, exchange, routingKey
println "Queue Binding returned '$bindOk'"

println "\n\n*** 4. PUBLISH\n"
println "[Message]+[RoutingKey] --> [Exchange]"
def messageBody = "Hello, world!";
println "Publishing message '$messageBody' on Exchange '$exchange' RoutingKey '$routingKey' no properties"
channel.basicPublish exchange, "$routingKey", null, messageBody.bytes
println "Publish does not return results"

////Receiving a message:
//println "\n\n*** 5. RECEIVE\n"
//println "[Message] <-- [Queue] <-- [Channel]"
//println "Receiving message (synchronized get) from Queue '$queueName' and acknowledging receipt"
//def getResponse = channel.basicGet queueName, true
//def basicProperties = getResponse.props
//println "Properties '$basicProperties'"
//println "Message Properties \n\t[\n\t\tApp Id '${basicProperties.appId}'"
//println "\t\tClass Id '${basicProperties.classId}'"
//println "\t\tClass Name '${basicProperties.className}'"
//println "\t\tCluster Id '${basicProperties.clusterId}'"
//println "\t\tContent Encoding '${basicProperties.contentEncoding}'"
//println "\t\tContent Type '${basicProperties.contentType}'"
//println "\t\tCorrelation Id '${basicProperties.correlationId}'"
//println "\t\tDelivery Mode '${basicProperties.deliveryMode}'"
//println "\t\tExpiration '${basicProperties.expiration}'"
//println "\t\tHeaders '${basicProperties.headers}'"
//println "\t\tMessage Id '${basicProperties.messageId}'"
//println "\t\tPriority '${basicProperties.priority}'"
//println "\t\tReply To '${basicProperties.replyTo}'"
//println "\t\tTimestamp '${basicProperties.timestamp}'"
//println "\t\tType '${basicProperties.type}'"
//println "\t\tUser Id '${basicProperties.userId}'\n\t]"
//
//def envelope = getResponse.envelope
//println "Envelope '$envelope'"
//println "Message Envelope \n\t[\n\t\tDelivery Tag '${envelope.deliveryTag}'"
//println "\t\tis Redelivered? '${envelope.redeliver}'"
//println "\t\tExchange '${envelope.exchange}'"
//println "\t\tRouting Key '${envelope.routingKey}'\n\t]"
//println "Body '${new String(getResponse.body)}'"


channel.close()
connection.close()
