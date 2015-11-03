package com.savoirtech.embassy.dc.json2xml;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.impl.BreakpointSupport;
import org.apache.camel.impl.DefaultDebugger;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.Breakpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Read queue.
 * Transform json body into XML file.
 */
public class RequestRouteBuilder extends RouteBuilder {

    

    @Override
    public void configure() throws Exception {

        DefaultDebugger dd = new DefaultDebugger();
        Breakpoint breakpoint = new BreakpointSupport() {
            @Override
            public void beforeProcess(Exchange exchange, Processor processor, ProcessorDefinition<?> definition) {
                super.beforeProcess(exchange, processor, definition);
                // Set break point in editor here while in Debug mode.
                log.info("Before process: Exchange: " + exchange.getIn().getBody().toString());
            }
        };
        dd.addSingleStepBreakpoint(breakpoint);
        getContext().setDebugger(dd);


        from("jmsConsumer:queue:request").id("json2xml").
        log("convertingJMS2XML").
        unmarshal().xmljson().
        to("file://../requestFiles?fileName=translated.xml").
        log("conversionComplete");
    }  

}
