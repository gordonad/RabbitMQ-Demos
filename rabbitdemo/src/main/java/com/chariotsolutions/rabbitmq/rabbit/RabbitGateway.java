package com.chariotsolutions.rabbitmq.rabbit;

/**
 * @author gDickens
 *         <p/>
 *         String expected automatically marshalled into payload
 */
public interface RabbitGateway {
//    public String send(String payload);
    public void send(String payload);
}
