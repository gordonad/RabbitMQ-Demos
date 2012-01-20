import com.rabbitmq.client.ConnectionFactory

println """
***********************************************************
*  Create Queue by name (required)
*  Create Fanout Exchange by name
*  Bind Exchange to Queue
*  Send string message to a direct queue
*
*  Gets message from queue (defaults to false)
*
*  Connection set to 'amqp://localhost:5672'
*
*  RoutingKey can be any non-null value, it is ignored
***********************************************************

"""

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')


def connectionFactory = new ConnectionFactory()
println "Create [ConnectionFactory]"
connectionFactory.host = "localhost"
def connection = connectionFactory.newConnection()
def channel = connection.createChannel()
def routingKey = ""

// Exchange, Type
println "\n\n*** 1. EXCHANGE\n"
def exchange = "pricing.exchange"
println "Declaring Direct Exchange '$exchange'"
def declareOk = channel.exchangeDeclare "$exchange", "fanout", false, false, null
println "Declared Exchange returned $declareOk"

// Queue, Durable, Exclusive, AutoDelete, Arguments
println "\n\n*** 2. QUEUES\n"
def queues = ["billing.alerts", "sales.alerts", "marketing.alerts"]
queues.each { queueName ->
    println "Declaring Queue '$queueName' (durable)"
    declareOk = channel.queueDeclare queueName, true, false, false, null
    println "Returned '$declareOk'"
}

println "\n\n*** 3. BINDINGS\n"
def bindings = ["billing.alerts", "sales.alerts", "marketing.alerts"]
bindings.each { queue ->

    println "Binding [$exchange] ----> [$queue]"
    bindOk = channel.queueBind queue, exchange, routingKey
//    bindOk = channel.queueBind queue, exchange, "${exchange}.${bindings.indexOf(queue)}"
    println "Queue Binding returned '$bindOk'"
}

println "\n\n*** 4. PUBLISHING\n"
def messages = ["Kumquats|+0.05", "Garbanzo Beans|-0.03", "Rudabaga|+0.01"]

messages.each { message ->
    println "Publishing message '$message' on Exchange '$exchange'"
    channel.basicPublish exchange, routingKey, null, message.bytes
//    channel.basicPublish exchange, "toast", null, message.bytes
}


channel.close()
connection.close()
