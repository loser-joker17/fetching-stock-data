package com.example.defineEdge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandleApiResponse {

    public CandleApiResponse(String symbol, String timeframe, List<CandleResponseDto> candles, int count) {
        this.symbol = symbol;
        this.timeframe = timeframe;
        this.candles = candles;
        this.count = count;
    }
    private String symbol;
    private String timeframe;
    private List<CandleResponseDto> candles;
    private int count;

    private int page;
    private int size;
    private long totalRecords;
}
