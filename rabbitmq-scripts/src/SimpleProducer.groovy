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
println "Channel '$channel' created"

if (!exchange) {
    println "No Exchange specified configuring [channel] -> [queue]"
    // Queue, Durable, Exclusive, AutoDelete, Arguments
    println "Declaring Queue '$queueName' non-Durable, non-Exclusive, no-AutoDelete, no-Arguments for Channel '$channel'"
    def declareOk = channel.queueDeclare(queueName, false, false, false, null)
    println "Declare Queue returned '$declareOk'"
}

println "Publishing '$message' on Exchange '$exchange' for Queue '$queueName' from channel '$channel'"
channel.basicPublish exchange, queueName, null, message.getBytes()
println "Publish does not return results"

channel.close()
connection.close()

