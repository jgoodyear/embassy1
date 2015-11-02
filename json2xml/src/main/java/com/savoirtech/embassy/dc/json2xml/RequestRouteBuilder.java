package com.savoirtech.embassy.dc.json2xml;

import org.apache.camel.builder.RouteBuilder;

/**
 * Read queue.
 * Transform json body into XML file.
 */
public class RequestRouteBuilder extends RouteBuilder {

    

    @Override
    public void configure() throws Exception {

        from("jmsConsumer:queue:request").id("json2xml").
        log("convert jms payload to xml file").
        to("file://requestFiles?fileName=translated.txt");
    }  

}
