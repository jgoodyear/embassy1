package com.savoirtech.embassy.dc.json2xml;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Read queue.
 * Transform json body into XML file.
 */
public class RequestRouteBuilder extends RouteBuilder {

    

    @Override
    public void configure() throws Exception {

        from("jmsConsumer:queue:request").id("json2xml").
        log("convert jms payload to xml file").
        unmarshal().xmljson().
        to("file://requestFiles?fileName=translated.xml");
    }  

}
