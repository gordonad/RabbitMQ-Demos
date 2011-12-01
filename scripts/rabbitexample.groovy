import com.rabbitmq.client.*
@Grab(group='com.rabbitmq', module='amqp-client', version='2.7.0')

factory = new com.rabbitmq.client.ConnectionFactory()
factory.setHost("localhost")

//  factory.setPort(56001), setHostname, setUsername, setPassword, setVirtualHost
conn = factory.newConnection()
channel = conn.createChannel()
channel.exchangeDeclare("myex", "direct")
channel.queueDeclare("myq", false, false , true, null) 
channel.queueBind("myq", "myex", "ABC")
messageBodyBytes = "Hello, world!".getBytes();
channel.basicPublish("myex", "ABC", null, messageBodyBytes)
//Receiving a message:
rcvMsg = channel.basicGet("myq", true)
println new String(rcvMsg.getBody())
println rcvMsg.getProps()

return "done"