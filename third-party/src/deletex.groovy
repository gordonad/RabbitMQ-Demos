import com.rabbitmq.client.ConnectionFactory

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def reader = System.in.newReader()

// create new version of readLine that accepts a prompt
reader.metaClass.readLine = { String prompt -> print prompt; return readLine() }

def String uri = "amqp://localhost"
def String exchange = reader.readLine "Exchange: "

println "Create [ConnectionFactory]"
def connectionFactory = new ConnectionFactory()
connectionFactory.uri = uri

def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

println "Deleting Exchange '$exchange'"
def deleteOk = channel.exchangeDelete exchange
println "Returned '$deleteOk'"

channel.close()
connection.close()

