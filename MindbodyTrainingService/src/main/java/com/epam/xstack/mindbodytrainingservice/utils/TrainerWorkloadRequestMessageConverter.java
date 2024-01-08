package com.epam.xstack.mindbodytrainingservice.utils;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Collections;

public class TrainerWorkloadRequestMessageConverter extends MappingJackson2MessageConverter {

    public TrainerWorkloadRequestMessageConverter(ObjectMapper mapper) {
        setTypeIdPropertyName("_trainerWorkloadRequestType");
        setTypeIdMappings(Collections.singletonMap("trainerWorkloadRequest", TrainerWorkloadRequest.class));
        setObjectMapper(mapper);
        setTargetType(MessageType.TEXT);
    }

}

