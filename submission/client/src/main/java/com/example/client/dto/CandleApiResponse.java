package com.example.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleApiResponse {

    private String symbol;
    private String timeframe;
    private List<CandleResponseDto> candles;
    private Integer count;
}