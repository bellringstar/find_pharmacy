package com.example.pharmacy.pharmacy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.pharmacy.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.pharmacy.entity.Pharmacy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacySearchService {

	private final PharmacyRepositoryService pharmacyRepositoryService;
	private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

	public List<PharmacyDto> searchPharmacyDtoList() {

		//redis
		List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
		if (!pharmacyDtoList.isEmpty()) return pharmacyDtoList;

		return pharmacyRepositoryService.findAll()
			.stream()
			.map(this::convertToPharmacyDto)
			.collect(Collectors.toList());
	}


	private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {
		return PharmacyDto.builder()
			.id(pharmacy.getId())
			.pharmacyAddress(pharmacy.getPharmacyAddress())
			.pharmacyName(pharmacy.getPharmacyName())
			.latitude(pharmacy.getLatitude())
			.longitude(pharmacy.getLongitude())
			.build();
	}
}
