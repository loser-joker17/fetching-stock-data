package com.example.defineEdge.service;

import com.example.defineEdge.dto.CandleApiResponse;

public interface CandleService {
    public CandleApiResponse getCandles(String symbol, String timeframe, String startDate, String endDate,int page , int size);
}
