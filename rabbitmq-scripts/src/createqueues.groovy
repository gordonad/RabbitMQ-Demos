import com.rabbitmq.client.*

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def factory = new ConnectionFactory()
factory.setHost("localhost")

//  factory.setPort(56001), setHostname, setUsername, setPassword, setVirtualHost
conn = factory.newConnection()
channel = conn.createChannel()

// Exchange, Type
result = channel.exchangeDeclare("myex", "direct")
println "Declared Exchange with Result ${result}"
println "Channel Type: $channel.getClass().getName()"

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
channel.queueDeclare("myq", false, false, true, null)

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
channel.queueBind("myq", "myex", "ABC")
messageBodyBytes = "Hello, world!".getBytes();
channel.basicPublish("myex", "ABC", null, messageBodyBytes)


//Receiving a message:
rcvMsg = channel.basicGet("myq", true)
println new String(rcvMsg.getBody())
println "Properties Received: $rcvMsg.getProps()"

return "done"
