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

def exchange = "logger.exchange"

def connectionFactory = new ConnectionFactory()
connectionFactory.host = "localhost"
def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

def messages = ["Charlie Babbot": "logger.*", "Wapner in 15": "", "We're counting cards": "logger.error"]

messages.each { msg, key ->
    println "Publishing message '$msg' on Exchange '$exchange' RoutingKey '$key' no properties"
    channel.basicPublish exchange, "$key", null, msg.bytes
}


channel.close()
connection.close()
