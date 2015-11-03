package com.savoirtech.embassy.dc.jms2db;

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
import org.apache.camel.component.jdbc.JdbcEndpoint;
import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.lang.Override;
import java.util.concurrent.TimeUnit;

public class MsgAckRouteBuilderTest extends CamelTestSupport {

    protected EmbeddedDatabase db;

    private String query = "select * from customer order by ID";

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

    @Before
    @Override
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.DERBY).addScript("sql/init.sql").build();
        
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        db.shutdown();
    }

    @Test
    public void testJMS2DB() {

        try {

            getMockEndpoint("mock:jmsConsumer:queue:jms2db").expectedBodiesReceived(query);

            template.sendBody("jmsConsumer:queue:jms2db", query);

            assertMockEndpointsSatisfied(1000, TimeUnit.MILLISECONDS);

        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry = super.createRegistry();
        jndiRegistry.bind("derbyDB", db);
        return jndiRegistry;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        CamelContext c = context();
        c.setTracing(true);

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        c.addComponent("jmsConsumer", jmsComponentAutoAcknowledge(connectionFactory));

        JdbcEndpoint jdbc = new JdbcEndpoint();
        jdbc.setCamelContext(c);
        jdbc.setDataSource(db);
        try {
            c.addEndpoint("derbyDB", jdbc);   
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new RouteBuilder() {
            public void configure() {
                from("jmsConsumer:queue:jms2db").to("derbyDB");
            }
        } ;
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
