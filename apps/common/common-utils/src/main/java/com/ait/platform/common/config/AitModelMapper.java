package com.ait.platform.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AitModelMapper {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
