package com.example.camelmicroserviceNy.routes.a;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrenTimeBean getCurrenTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingProcessingComponent;

    Logger log = LoggerFactory.getLogger(MyFirstTimerRouter.class);

    @Override
    public void configure() throws Exception {

        //timer
        //transformation
        //log

        log.info("Configuring timer route");

        from("timer:first-timer") // queue
                .log("${body}")
                .transform().constant("My constant message")
                .log("${body}")
               // .transform().constant("time now is " + LocalDateTime.now())
                .bean(getCurrenTimeBean)
                .log("${body}")
                .bean(loggingProcessingComponent)
                .log("${body}")
                .process(new SimplLoggingProcessor())
                .to("log:first-timer?showBody=true&level=ERROR"); // database

    }

    private class SimplLoggingProcessor implements org.apache.camel.Processor {

        private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
        @Override
        public void process(Exchange exchange) throws Exception {
            logger.info("SimplLoggingProcessor {}", exchange.getMessage().getBody());
        }
    }
}

@Component
class GetCurrenTimeBean{
    public String getCurrentTime(){
        return "time now is " + LocalDateTime.now();
    }

}

@Component
class SimpleLoggingProcessingComponent{

    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    public void process(String msg){

        logger.info("SimpleLoggingProcessingComponent {}", msg);

    }


}