package com.chariotsolutions.rabbitmq.rabbit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.fail;

@ContextConfiguration("RabbitTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AnotherRabbitTest {
    private static final String NULL_VAL = "NULL - d'oh";

    private static final Logger logger = LoggerFactory
            .getLogger(AnotherRabbitTest.class);


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void setup() {
        logger.debug("****************************************");
        String[] beans = applicationContext.getBeanDefinitionNames();
        for (String o : beans) {
            String[] aliases = applicationContext.getAliases(o);
            logger.debug(
                    "*** Bean ***\n\t   ID = {}\n\t TYPE = {}\n\tALIAS = {}",
                    new String[]{
                            o,
                            applicationContext.getBean(o).getClass().getName(),
                            (aliases != null && aliases.length > 0) ? Arrays
                                    .asList(aliases).toString() : ""});
        }

        logger.debug("*** Number of Beans = {} ***",
                applicationContext.getBeanDefinitionCount());
        logger.debug("****************************************");
    }

    @Test
    public void sendTestMessage() {
        try {
            String sendMsg = "Hello Wabbit";
            logger.debug("Sending message through Gateway {}", sendMsg);
//            String recvMsg = rabbitGateway.send(sendMsg);
            rabbitTemplate.convertAndSend("rabbitdemo.default.key",
                    "This is my test");

//            Object recvMsg = amqpTemplate
//                    .receiveAndConvert("rabbitdemo.request.queue");
//            Object recvMsg2 = amqpTemplate
//                    .receiveAndConvert("rabbitdemo.request.queue");
//            log("Received From Request Queue", (recvMsg == null ? new String(
//                    NULL_VAL) : recvMsg));
//            log("Received From Response Queue", (recvMsg2 == null ? new String(
//                    NULL_VAL) : recvMsg2));

//            assertNotNull("Received Message MUST exist", recvMsg);
//            log("Received Message", recvMsg);
//            assertEquals("Send and Receive values MUST match", sendMsg, recvMsg);
        } catch (Exception e) {
            logger.error("Rabbit Exception {}", e.getMessage(), e);
            fail("Rabbit Exception");
        }
    }

    private void log(String message, Object object) {
        logger.debug("{} {}", message, (object == null ? new String(NULL_VAL)
                : object.toString()));
        logger.debug("[{} T({})]", object.getClass().getName());
    }
}
