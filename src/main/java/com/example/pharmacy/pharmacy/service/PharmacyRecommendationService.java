package com.example.pharmacy.pharmacy.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import com.example.pharmacy.api.service.KakaoAddressSearchService;
import com.example.pharmacy.direction.dto.OutputDto;
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

	private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
	private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

	public List<OutputDto> recommendPharmacyList(String address) {

		KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

		if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
			log.error("PharmacyRecommendationService recommendPharmacyList input address: {}", address);
			return Collections.emptyList();
		}

		DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

		List<Direction> directionList = directionService.buildDirectionList(documentDto);

		return directionService.saveAll(directionList)
			.stream()
			.map(this::convertToOutputDto)
			.collect(Collectors.toList());
	}

	private OutputDto convertToOutputDto(Direction direction) {

		String params = String.join(",", direction.getTargetPharmacyName(),
			String.valueOf(direction.getTargetLatitude()),
			String.valueOf(direction.getTargetLongitude())
			);
		String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
			.toUriString();

		return OutputDto.builder()
			.pharmacyName(direction.getTargetPharmacyName())
			.pharmacyAddress(direction.getTargetAddress())
			.roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
			.directionUrl(result)
			.distance(String.format("%.2f km", direction.getDistance()))
			.build();
	}
}
