package com.example.pharmacy.pharmacy.cache;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.pharmacy.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRedisTemplateService {

	private static final String CACHE_KEY = "PHARMACY";

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	private HashOperations<String, String, String> hashOperations;

	@PostConstruct
	public void init() {
		this.hashOperations = redisTemplate.opsForHash();
	}

	public void save(PharmacyDto pharmacyDto) {
		if(Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.getId())) {
			log.error("Required Values must not be null");
			return;
		}

		try {
			hashOperations.put(CACHE_KEY,
				pharmacyDto.getId().toString(),
				serializePharmacyDto(pharmacyDto));
			log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.getId());
		} catch (Exception e) {
			log.error("[PharmacyRedisTemplateService save fail] {}", e.getMessage());
		}
	}



	private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(pharmacyDto);
	}

	private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {
		return objectMapper.readValue(value, PharmacyDto.class);
	}


}
