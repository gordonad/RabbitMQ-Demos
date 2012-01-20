import com.rabbitmq.client.ConnectionFactory

@Grab(group = 'com.rabbitmq', module = 'amqp-client', version = '2.7.0')

def reader = System.in.newReader()

// create new version of readLine that accepts a prompt
reader.metaClass.readLine = { String prompt -> print prompt; return readLine() }

def String uri = "amqp://localhost"
def String queueName = reader.readLine "Queue Name: "

def connectionFactory = new ConnectionFactory()
connectionFactory.uri = uri

def connection = connectionFactory.newConnection()
def channel = connection.createChannel()

println "Deleting Queue '$queueName'"
//try {
def deleteOk = channel.queueDelete queueName
println "Returned '$deleteOk'"
//} catch (IOException e) {
//    println "Error: '${e.localizedMessage}'"
//}

channel.close()
connection.close()
