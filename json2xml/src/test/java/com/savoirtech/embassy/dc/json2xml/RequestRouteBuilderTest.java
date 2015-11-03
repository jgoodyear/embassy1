package com.savoirtech.embassy.dc.json2xml;

import com.savoirtech.embassy.dc.json2xml.RequestRouteBuilder;

import javax.jms.ConnectionFactory;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.ActiveMQConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.Override;
import java.util.concurrent.TimeUnit;

public class RequestRouteBuilderTest extends CamelTestSupport {

    @Override
    public String isMockEndpoints() { return "*"; }

    @BeforeClass
    public static void setUpClass() throws Exception {
        BrokerService brokerSvc = new BrokerService();
        brokerSvc.setBrokerName("TestBroker");
        brokerSvc.setPersistent(false);
        brokerSvc.addConnector("tcp://localhost:61616");
        brokerSvc.start();
    }

    private final static String jsonBody = "{ \"business\": \"test\" }";

    @Test
    public void testRequest() {

        try {

            getMockEndpoint("mock:jmsConsumer:queue:request").expectedBodiesReceived("{ \"business\": \"test\" }");

            template.sendBody("jmsConsumer:queue:request", jsonBody);

            assertMockEndpointsSatisfied(1000, TimeUnit.MILLISECONDS);

        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry = super.createRegistry();
        return jndiRegistry;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        CamelContext c = context();
        c.setTracing(true);

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        c.addComponent("jmsConsumer", jmsComponentAutoAcknowledge(connectionFactory));

        return new RequestRouteBuilder();
    }

    @Override
    public boolean isUseDebugger() { return true; }

    @Override
    protected void debugBefore(Exchange exchange, Processor processor, 
                               ProcessorDefinition<?> definition, String id, String shortName) {

        // this method is invoked before we are about to enter the given processor
        // from your Java editor you can just add a break point in the code below
        log.info("Before " + definition + " with body " + exchange.getIn().getBody());
    }
}

