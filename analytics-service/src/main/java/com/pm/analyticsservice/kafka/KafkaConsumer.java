package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LogManager.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event){

        try{
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            // perform analytics related business logic - calling db, service call etc
            log.info("analytics service consumer received event {}", patientEvent.toString());
        } catch (InvalidProtocolBufferException e){
            log.info("Event consumption Failed : {} ", e.getMessage());
        }
    }
}
