/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ait.platform.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.util.AitConfigConstants;

/**
 * Cache support configuration. Use REDIS to persist cache information
 * 
 * @author AllianzIT
 *
 */
@Configuration
@EnableCaching
@Component
public class AitCacheConfigBase extends CachingConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(AitCacheConfigBase.class);

	private final StringRedisSerializer serializer = new StringRedisSerializer();

	@Bean
	public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		AitLogger.debug(logger, "Definiendo administrador de cache");
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		// TODO mandar a variable de configuracion
		cacheManager.setDefaultExpiration(3600);// timepo en segundos (1 hora)
		cacheManager.setUsePrefix(true);
		cacheManager.setCachePrefix(new RedisCachePrefix() {

			@Override
			public byte[] prefix(String cacheName) {
				return serializer.serialize(AitConfigConstants.profile + ":" + cacheName);
			}
		});
		AitLogger.trace(logger, "Chache manager definido correctamente para el perfil {}", AitConfigConstants.profile);
		return cacheManager;
	}

	@Bean
	@Primary
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory rcf) {
		AitLogger.debug(logger, "Serializer overriding ");

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(rcf);

		GenericJackson2JsonRedisSerializer valSerializer = new GenericJackson2JsonRedisSerializer();

		template.setValueSerializer(valSerializer);
		template.setKeySerializer(new RedisSerializer<Object>() {

			@Override
			public byte[] serialize(Object t) throws SerializationException {
				return (t == null ? null : (":" + t.toString()).getBytes());
			}

			@Override
			public Object deserialize(byte[] bytes) throws SerializationException {
				return (bytes == null ? null : new String(bytes));
			}
		});
		template.setHashValueSerializer(valSerializer);
		return template;
	}
}