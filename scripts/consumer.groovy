import com.rabbitmq.client.*

@Grab(group='com.rabbitmq', module='amqp-client', version='2.7.0')
params = new ConnectionParameters(
    username: 'guest',
    password: 'guest',
    virtualHost: '/',
    requestedHeartbeat: 0
)
factory = new ConnectionFactory(params)
conn = factory.newConnection('lab.ndpar.com', 5672)
channel = conn.createChannel()

exchangeName = 'myExchange'; queueName = 'myQueue'

channel.exchangeDeclare exchangeName, 'direct'
channel.queueDeclare queueName
channel.queueBind queueName, exchangeName, 'myRoutingKey'

def consumer = new QueueingConsumer(channel)
channel.basicConsume queueName, false, consumer

while (true) {
    delivery = consumer.nextDelivery()
    println "Received message: ${new String(delivery.body)}"
    channel.basicAck delivery.envelope.deliveryTag, false
}
channel.close()
conn.close()

return 1
