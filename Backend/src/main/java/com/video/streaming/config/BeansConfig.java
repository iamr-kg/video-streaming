package com.video.streaming.config;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class BeansConfig {

    @Bean
    @Scope("prototype")
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

   // @Scheduled(fixedRate = 60000L)
    public void print(){
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time.toString() +"mytime");
    }

}
