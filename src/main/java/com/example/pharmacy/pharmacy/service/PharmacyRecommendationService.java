package com.example.pharmacy.pharmacy.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import com.example.pharmacy.api.service.KakaoAddressSearchService;
import com.example.pharmacy.direction.entity.Direction;
import com.example.pharmacy.direction.service.DirectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyRecommendationService {

	private final KakaoAddressSearchService kakaoAddressSearchService;
	private final DirectionService directionService;

	public void recommendPharmacyList(String address) {

		KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

		if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
			log.error("PharmacyRecommendationService recommendPharmacyList input address: {}", address);
			return;
		}

		DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

		List<Direction> directionList = directionService.buildDirectionList(documentDto);

		directionService.saveAll(directionList);
	}
}
