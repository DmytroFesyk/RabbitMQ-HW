package com.example.rabbitmqhw.config;

import cm.example.rabbitmqhw.config.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = Config.class)
public class ProducerConfig{}
