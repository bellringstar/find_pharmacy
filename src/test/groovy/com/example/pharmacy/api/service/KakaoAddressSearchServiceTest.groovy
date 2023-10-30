package com.example.pharmacy.api.service

import com.example.pharmacy.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    def "address 파라미터 값이 null이면, requestAddressSearch 메서드는 null을 리턴한다."() {
        given:
        String address = null;

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "주소값이 valid하면, requestAddressSearch 메소드는 정장적으로 document를 반환한다."() {
        given:
        def address = "서울 성북구 종암로 10길"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        result.documentList.get(0).addressName != null
    }
}
