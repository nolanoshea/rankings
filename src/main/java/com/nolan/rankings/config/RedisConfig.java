package com.nolan.rankings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.nolan.rankings.model.FPlusTeam;

@Configuration
public class RedisConfig {
    
    @Bean
    public ReactiveRedisTemplate<String, FPlusTeam> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<FPlusTeam> valueSerializer = new Jackson2JsonRedisSerializer<>(FPlusTeam.class);
        
        RedisSerializationContext.RedisSerializationContextBuilder<String, FPlusTeam> builder =
            RedisSerializationContext.newSerializationContext(keySerializer);
        
        RedisSerializationContext<String, FPlusTeam> context = builder
            .value(valueSerializer)
            .build();
        
        return new ReactiveRedisTemplate<>(factory, context);
    }
}