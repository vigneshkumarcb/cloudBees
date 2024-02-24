package com.train.services.ticketing.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.services.ticketing.model.User;
import com.train.services.ticketing.utils.SizeLimitedLinkedHashSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrainSeatsConfiguration {
    public int sectionSize = 3;

    @Bean
    public SizeLimitedLinkedHashSet<User> trainSizeLimitedLinkedHashSet() {
        return new SizeLimitedLinkedHashSet<>(4, sectionSize);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
