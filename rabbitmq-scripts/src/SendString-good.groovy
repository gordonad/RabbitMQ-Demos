import com.rabbitmq.client.ConnectionFactory

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')


def reader = System.in.newReader()

// create new version of readLine that accepts a prompt to remove duplication from the loop
reader.metaClass.readLine = { String prompt -> print prompt ; return readLine() }

def host = reader.readLine "HostName: "
def exchange = reader.readLine "Exchange: "
def exchangeType = reader.readLine "ExchangeType: "
def routingKey = reader.readLine "Routing Key: "
def message = reader.readLine     "Message: "

def connectionFactory = new ConnectionFactory()
connectionFactory.setHost host
def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

channel.exchangeDeclare exchange, exchangeType
channel.basicPublish exchange, routingKey, null, message.getBytes()
println "Sent Message $message to $exchange"
channel.close()
connection.close()

