import com.rabbitmq.client.ConnectionFactory

println """
***********************************************************
*  Create Queue by name (required)
*  Send string message to a direct queue
*
*  Gets message from queue (defaults to false)
*  No Explicit Exchange or RoutingKey
*
*  Connection set to 'amqp://localhost:5672'
*
*  -- AMQP Defaults --
*     Exchange = Default AMQP Exchange
*     RoutingKey = queue name
***********************************************************

"""

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def reader = System.in.newReader()

// create new version of readLine that accepts a prompt
reader.metaClass.readLine = { String prompt -> print prompt; return readLine() }

final uri = "amqp://localhost"
def queueName = reader.readLine "Queue Name: "
def message = reader.readLine "Message: "
def remove = reader.readLine "Get Message Too? "
def trueOptions = ['1','t','true','y','yes']
remove = remove.trim()toLowerCase()

println "Create [ConnectionFactory]"
def connectionFactory = new ConnectionFactory()
connectionFactory.uri = uri

println "Create [Connection] <-- [ConnectionFactory]"
def connection = connectionFactory.newConnection()
println "Create [Channel] <-- [Connection]"
def channel = connection.createChannel()
println "Channel '$channel' created"

// Queue, Durable, Exclusive, AutoDelete, Arguments
println "Declaring Queue '$queueName' non-Durable, non-Exclusive, no-AutoDelete, no-Arguments via Channel '$channel'"
def declareOk = channel.queueDeclare queueName, false, false, false, null
println "Declare Queue returned '$declareOk'"

println "\n\n*** PUBLISH\n"
println "Publishing '$message' on Queue '$queueName' from channel '$channel'"
channel.basicPublish '', queueName, null, message.bytes
println "Publish does not return results"

if (trueOptions.contains(remove)) {
    println "\n\n*** GET\n"
    println "[Message] <-- [Queue] <-- [Channel]"
    println "Receiving message (synchronized get) from Queue '$queueName' and acknowledging receipt"
    def getResponse = channel.basicGet queueName, true
    def basicProperties = getResponse.props
    println "Properties '$basicProperties'"
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

    def envelope = getResponse.envelope
    println "Envelope '$envelope'"
    println "Message Envelope \n\t[\n\t\tDelivery Tag '${envelope.deliveryTag}'"
    println "\t\tis Redelivered? '${envelope.redeliver}'"
    println "\t\tExchange '${envelope.exchange}'"
    println "\t\tRouting Key '${envelope.routingKey}'\n\t]"
    println "Body '${new String(getResponse.body)}'"
}

channel.close()
connection.close()

