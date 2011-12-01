import com.rabbitmq.client.*

// @Grab(group='com.rabbitmq', module='amqp-client', version='1.7.2')
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

channel.basicPublish 'myExchange', 'myRoutingKey', null, "Hello, world!".bytes

channel.close()
conn.close()
