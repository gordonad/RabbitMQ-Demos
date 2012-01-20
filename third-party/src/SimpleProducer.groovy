import com.rabbitmq.client.ConnectionFactory


println """
***********************************************************
*  Create Exchange by name
*  Create queue by name (required)
*  Send string message to a direct queue
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
reader.metaClass.readLine = { String prompt -> print prompt; return readLine() }

final String uri = "amqp://localhost"
def String exchange = reader.readLine "Exchange: "
def String queueName = reader.readLine "Queue Name: "
def String message = reader.readLine "Message: "

def connectionFactory = new ConnectionFactory()
connectionFactory.uri = uri

println "Create [Connection] <-- [ConnectionFactory]"
def connection = connectionFactory.newConnection()
println "Create [Channel] <-- [Connection]"
def channel = connection.createChannel()
println "Channel '$channel' created"

println "No Exchange specified configuring [channel] -> [queue]"
// Queue, Durable, Exclusive, AutoDelete, Arguments
println "Declaring Queue '$queueName' non-Durable, non-Exclusive, no-AutoDelete, no-Arguments for Channel '$channel'"
def declareOk = channel.queueDeclare queueName, false, false, false, null
println "Declare Queue returned '$declareOk'"

if (exchange) {
    println "Exchange is good $exchange"
    println "[Exchange] <-- [Channel]"
    println "Declaring Direct Exchange '$exchange'"
    def declareOk1 = channel.exchangeDeclare exchange, "direct"
    println "Declared Exchange returned $declareOk1"
}

println "Publishing '$message' on Exchange '$exchange' for Queue '$queueName' from channel '$channel'"
channel.basicPublish exchange, queueName, null, message.bytes
println "Publish does not return results"

channel.close()
connection.close()

