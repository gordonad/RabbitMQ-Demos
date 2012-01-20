import com.rabbitmq.client.ConnectionFactory

println """
***********************************************************
*  Create Queue by name (required)
*  Create Topic Exchange by name
*  Bind Exchange to Queue
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


def connectionFactory = new ConnectionFactory()
println "Create [ConnectionFactory]"
connectionFactory.host = "localhost"
connectionFactory.port = 5672
def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

// Exchange, Type
println "\n\n*** 1. EXCHANGE\n"
def exchange = "logger.exchange"
println "Declaring Direct Exchange '$exchange'"
def declareOk = channel.exchangeDeclare "$exchange", "topic"
println "Declared Exchange returned $declareOk"

// Queue, Durable, Exclusive, AutoDelete, Arguments
println "\n\n*** 2. QUEUES\n"
def queues = ["logger.error", "logger.info", "logger.all"]
queues.each { queueName ->
    println "Declaring Queue '$queueName' (autodelete)"
    declareOk = channel.queueDeclare queueName, false, false, true, null
    println "Returned '$declareOk'"
}

println "\n\n*** 3. BINDINGS\n"
def bindings = ["*.error": "logger.error", "*.info": "logger.info", "#": "logger.all"]
bindings.each { key, queue ->

    println "Binding [$exchange] --($key)--> [$queue]"
    bindOk = channel.queueBind queue, exchange, key
    println "Queue Binding returned '$bindOk'"
}

println "\n\n*** 4. PUBLISHING\n"
def messages = ["Charlie Babbot": "logger.error", "Wapner in 15": "logger.info", "We're counting cards": "#"]

messages.each { message, key ->
    println "Publishing message '$message' on Exchange '$exchange' RoutingKey '$key' no properties"
    channel.basicPublish exchange, "$key", null, message.bytes
}


channel.close()
connection.close()
