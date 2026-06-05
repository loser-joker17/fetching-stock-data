package com.example.client.apiClient;

import com.example.client.dto.CandleApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CandleApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.symbol}")
    private String symbol;

    @Value("${api.timeframe}")
    private String timeframe;

    @Value("${api.start-date}")
    private String startDate;

    @Value("${api.end-date}")
    private String endDate;

    public CandleApiResponse fetchCandles() {
        String url = baseUrl + "/api/v1/candles" + "?symbol=" + symbol + "&timeframe=" + timeframe + "&startDate=" + startDate + "&endDate=" + endDate + "&page=0" + "&size=5";

        System.out.println("Calling URL : " + url);
        return restTemplate.getForObject(url, CandleApiResponse.class);
    }
}
