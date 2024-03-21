package com.example.demo.common.config;

import com.example.demo.common.service.MessageSender;
import com.example.demo.common.util.DemoMessageSender;
import com.example.demo.common.util.DemoRandomProviderImpl;
import com.example.demo.common.util.RandomProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("demo")
@Configuration
public class DemoConfiguration {

    @Bean
    @Primary
    public RandomProvider randomProvider() {
        return new DemoRandomProviderImpl();
    }

    @Bean
    @Primary
    public MessageSender<String> messageSender() {
        return new DemoMessageSender();
    }
}
