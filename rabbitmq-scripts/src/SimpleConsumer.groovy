import com.rabbitmq.client.*

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def reader = System.in.newReader()

// create new version of readLine that accepts a prompt
reader.metaClass.readLine = { String prompt -> print prompt ; return readLine() }

def uri = "amqp://localhost"
def queueName = reader.readLine "Queue Name: "

def connectionFactory = new ConnectionFactory()
connectionFactory.setUri uri
def connection = connectionFactory.newConnection()

def channel = connection.createChannel()

channel.queueDeclare queueName, false, false, false, null

def consumer = new QueueingConsumer(channel)
channel.basicConsume queueName, consumer

while (true) {
    delivery = consumer.nextDelivery()
    println "Consuming Message from $queueName: $delivery.getBody()"
    channel.basicAck delivery.getEnvelope().getDeliveryTag(), false
    println "Acknowledged $delivery.getEnvelope().getDeliveryTag()"
}
