package com.example.defineEdge.controller;

import com.example.defineEdge.dto.CandleApiResponse;
import com.example.defineEdge.dto.CandleResponseDto;
import com.example.defineEdge.service.CandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candles")
@RequiredArgsConstructor
public class CandleController {

    private final CandleService candleService;

    @GetMapping
    public ResponseEntity<CandleApiResponse> getCandles(@RequestParam String symbol, @RequestParam String timeframe, @RequestParam String startDate, @RequestParam String endDate,
                                                        @RequestParam int page, @RequestParam int size) {

        return ResponseEntity.ok(candleService.getCandles(symbol, timeframe, startDate, endDate, page, size));
    }
}
