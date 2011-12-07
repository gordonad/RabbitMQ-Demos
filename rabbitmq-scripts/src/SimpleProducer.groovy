import com.rabbitmq.client.ConnectionFactory

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def reader = System.in.newReader()

// create new version of readLine that accepts a prompt
reader.metaClass.readLine = { String prompt -> print prompt; return readLine() }

def String uri = "amqp://localhost"
def String message = reader.readLine "Message: "
def String exchange = reader.readLine "Exchange: "
def String queueName = reader.readLine "Queue Name: "

def connectionFactory = new ConnectionFactory()
connectionFactory.setUri uri

def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

if (!exchange) {
    // Queue, Durable, Exclusive, AutoDelete, Arguments
    println "Declaring Queue $queueName non-Durable, non-Exclusive, no-AutoDelete, no-Arguments"
    channel.queueDeclare(queueName, false, false, false, null)
}

println "Publishing Message $message on Exchange $exchange and Queue $queueName"
channel.basicPublish exchange, queueName, null, message.getBytes()
channel.close()
connection.close()

