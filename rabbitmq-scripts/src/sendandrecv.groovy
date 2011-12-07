import com.rabbitmq.client.*

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def connectionFactory = new ConnectionFactory()
connectionFactory.setHost("localhost")

//  factory.setPort(56001), setHostname, setUsername, setPassword, setVirtualHost
def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

// Exchange, Type

println "\n\n*** 1. EXCHANGE\n"
println "Declaring Direct Exchange 'myex'"
def declareOk = channel.exchangeDeclare "myex", "direct"
println "Declared Exchange returned $declareOk"
//println "Channel Type: $channel.getClass().getName()"

/**
 * Declare a queue
 * @see com.rabbitmq.client.AMQP.Queue.Declare
 * @see com.rabbitmq.client.AMQP.Queue.DeclareOk
 * @param queue the name of the queue
 * @param durable true if we are declaring a durable queue (the queue will survive a server restart)
 * @param exclusive true if we are declaring an exclusive queue (restricted to this connection)
 * @param autoDelete true if we are declaring an autodelete queue (server will delete it when no longer in use)
 * @param arguments other properties (construction arguments) for the queue
 * @return a declaration-confirm method to indicate the queue was successfully declared
 * @throws java.io.IOException if an error is encountered
 */
// Queue, Durable, Exclusive, AutoDelete, Arguments
println "\n\n*** 2. QUEUE\n"
println "Declaring Queue 'myq' non-durable, non-exclusive, autodelete and no arguments"
def declareOk1 = channel.queueDeclare("myq", false, false, true, null)
println "Declare Queue returned '$declareOk1'"

/**
 * Bind a queue to an exchange, with no extra arguments.
 * @see com.rabbitmq.client.AMQP.Queue.Bind
 * @see com.rabbitmq.client.AMQP.Queue.BindOk
 * @param queue the name of the queue
 * @param exchange the name of the exchange
 * @param routingKey the routine key to use for the binding
 * @return a binding-confirm method if the binding was successfully created
 * @throws java.io.IOException if an error is encountered
 */

// Queue, Exchange, Routing Key
println "\n\n*** 3. BIND\n"
println "Binding Queue 'myq' to Exchange 'myex' with routing key 'ABC'"
def bindOk = channel.queueBind "myq", "myex", "ABC"
println "Queue Binding returned '$bindOk'"

println "\n\n*** 4. PUBLISH\n"
def messageBody = "Hello, world!";
println "Publishing message '$messageBody' on Exchange 'myex' routing key 'ABC' no properties"
channel.basicPublish "myex", "ABC", null, messageBody.getBytes()
println "Publish does not return results"

//Receiving a message:
println "\n\n*** 5. RECEIVE\n"
println "Receiving message from Queue 'myq' and acknowledging receipt"
def getResponse = channel.basicGet "myq", true
def basicProperties = getResponse.getProps()
println "Properties'${basicProperties}'"
println "Message Properties \n\t[\n\t\tApp Id '${basicProperties.getAppId()}'"
println "\t\tClass Id '${basicProperties.getClassId()}'"
println "\t\tClass Name '${basicProperties.getClassName()}'"
println "\t\tCluster Id '${basicProperties.getClusterId()}'"
println "\t\tContent Encoding '${basicProperties.getContentEncoding()}'"
println "\t\tContent Type '${basicProperties.getContentType()}'"
println "\t\tCorrelation Id '${basicProperties.getCorrelationId()}'"
println "\t\tDelivery Mode '${basicProperties.getDeliveryMode()}'"
println "\t\tExpiration '${basicProperties.getExpiration()}'"
println "\t\tHeaders '${basicProperties.getHeaders()}'"
println "\t\tMessage Id '${basicProperties.getMessageId()}'"
println "\t\tPriority '${basicProperties.getPriority()}'"
println "\t\tReply To '${basicProperties.getReplyTo()}'"
println "\t\tTimestamp '${basicProperties.getTimestamp()}'"
println "\t\tType '${basicProperties.getType()}'"
println "\t\tUser Id '${basicProperties.getUserId()}'\n\t]"

println "Message Envelope \n\t[\n\t\tDelivery Tag '${getResponse.getEnvelope().getDeliveryTag()}'"
println "\t\tis Redelivered? '${getResponse.getEnvelope().isRedeliver()}'"
println "\t\tExchange '${getResponse.getEnvelope().getExchange()}'"
println "\t\tRouting Key '${getResponse.getEnvelope().getRoutingKey()}'\n\t]"
println "Body '${new String(getResponse.getBody())}'"


channel.close()
connection.close()
