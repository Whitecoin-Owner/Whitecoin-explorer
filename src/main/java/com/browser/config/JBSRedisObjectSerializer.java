package com.browser.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * redis键策略
 * @author mayakui
 *
 */
public class JBSRedisObjectSerializer implements RedisSerializer<Object> {
	
	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();
	static final byte[] EMPTY_ARRAY = new byte[0];

	@Override
	public byte[] serialize(Object value) throws SerializationException {
		if(value == null){
			return EMPTY_ARRAY;
		}
		try{
			return serializer.convert(value);
		}
		catch(Exception e){
			throw new SerializationException("Cannot serialize", e);
		}
	}

	@Override
	public Object deserialize(byte[] keyBytes) throws SerializationException {
		if(isEmpty(keyBytes)){
			return null;
		}
		try{
			return deserializer.convert(keyBytes);
		}
		catch(Exception e){
			throw new SerializationException("Cannot deserialize", e);
		}
	}

	private boolean isEmpty(byte[] data) {
	    return (data == null || data.length == 0);
	}
}
