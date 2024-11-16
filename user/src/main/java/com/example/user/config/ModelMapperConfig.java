package com.example.user.config;

import com.example.user.dto.UserDto;
import com.example.user.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(UserDto.class, UserEntity.class)
                .addMappings(mapper -> mapper.skip(UserEntity::setRoles));

        return modelMapper;
    }
}
