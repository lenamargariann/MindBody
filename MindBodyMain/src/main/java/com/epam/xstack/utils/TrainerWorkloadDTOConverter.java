package com.epam.xstack.utils;

import com.epam.xstack.model.dto.TrainerWorkloadDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

@RequiredArgsConstructor
public class TrainerWorkloadDTOConverter implements MessageConverter {
    private final ObjectMapper objectMapper;

    @Nonnull
    @Override
    public Message toMessage(@Nonnull Object object, @Nonnull Session session) throws JMSException, MessageConversionException {
        if (!(object instanceof TrainerWorkloadDTO dto)) {
            throw new MessageConversionException("Object is not of type TrainerWorkloadDTO");
        }
        String payload;
        try {
            payload = objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new MessageConversionException("Error converting TrainerWorkloadDTO to JSON string", e);
        }

        TextMessage message = session.createTextMessage(payload);
        message.setStringProperty("_trainerWorkloadRequestType", "trainerWorkloadRequest");
        return message;
    }

    @Nonnull
    @Override
    public Object fromMessage(@Nonnull Message message) throws JMSException, MessageConversionException {
        if (!(message instanceof TextMessage textMessage)) {
            throw new MessageConversionException("Message is not of type TextMessage");
        }

        String payload = textMessage.getText();
        try {
            return objectMapper.readValue(payload, TrainerWorkloadDTO.class);
        } catch (Exception e) {
            throw new MessageConversionException("Error converting JSON string to TrainerWorkloadDTO", e);
        }
    }


}
