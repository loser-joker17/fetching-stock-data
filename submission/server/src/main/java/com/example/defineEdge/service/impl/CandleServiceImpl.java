package com.example.defineEdge.service.impl;

import com.example.defineEdge.constants.MessageConstants;
import com.example.defineEdge.dto.CandleApiResponse;
import com.example.defineEdge.dto.CandleResponseDto;
import com.example.defineEdge.entity.CandleEntity;
import com.example.defineEdge.exception.invalidExceptions.InvalidDateRangeException;
import com.example.defineEdge.exception.invalidExceptions.InvalidTimeframeException;
import com.example.defineEdge.exception.invalidExceptions.SymbolNotFoundException;
import com.example.defineEdge.repository.CandleRepository;
import com.example.defineEdge.service.CandleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleServiceImpl implements CandleService {

    private final CandleRepository repository;

    @Override
    public CandleApiResponse getCandles(String symbol, String timeframe, String startDate, String endDate, int page, int size) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        log.info("Fetching candles for symbol={}, timeframe={}, startDate={}, endDate={}", symbol, timeframe, startDate, endDate);

        if (start.isAfter(end)) {
            throw new InvalidDateRangeException(MessageConstants.INVALID_DATE_RANGE);
        }

        List<CandleEntity> candles = repository.findBySymbolAndDatetimeBetween(symbol, start, end);

        if (candles.isEmpty()) {
            throw new SymbolNotFoundException(MessageConstants.SYMBOL_NOT_FOUND);
        }

        int bucketMinutes = getBucketMinutes(timeframe);

        CandleApiResponse aggregatedResponse = aggregateCandles(candles, bucketMinutes, symbol, timeframe);

        List<CandleResponseDto> allCandles = aggregatedResponse.getCandles();

        int startIndex = page * size;

        CandleApiResponse response = new CandleApiResponse();

        response.setSymbol(symbol);
        response.setTimeframe(timeframe);
        response.setPage(page);
        response.setSize(size);
        response.setTotalRecords(allCandles.size());

        if (startIndex >= allCandles.size()) {

            response.setCandles(Collections.emptyList());
            response.setCount(0);

            return response;
        }

        int endIndex = Math.min(startIndex + size, allCandles.size());

        List<CandleResponseDto> paginatedCandles = allCandles.subList(startIndex, endIndex);

        response.setCandles(paginatedCandles);
        response.setCount(paginatedCandles.size());

        return response;
    }

    private int getBucketMinutes(String timeframe) {
        return switch (timeframe) {
            case "1m" -> 1;
            case "5m" -> 5;
            case "15m" -> 15;
            case "30m" -> 30;
            case "1h" -> 60;
            case "1d" -> 1440;

            default -> throw new InvalidTimeframeException(MessageConstants.INVALID_TIMEFRAME);
        };
    }

    private CandleApiResponse aggregateCandles(List<CandleEntity> candles, int bucketMinutes, String symbol, String timeframe) {

        Map<LocalDateTime, List<CandleEntity>> grouped = candles.stream().collect(Collectors.groupingBy(candle -> getBucket(candle.getDatetime(), bucketMinutes)));
        List<CandleResponseDto> result = new ArrayList<>();

        for (var entry : grouped.entrySet()) {
            List<CandleEntity> bucket = entry.getValue();

            bucket.sort(Comparator.comparing(CandleEntity::getDatetime));

            CandleEntity first = bucket.get(0);
            CandleEntity last = bucket.get(bucket.size() - 1);

            BigDecimal high = bucket.stream()
                    .map(CandleEntity::getHigh)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            BigDecimal low = bucket.stream()
                    .map(CandleEntity::getLow)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            long volume = bucket.stream()
                    .mapToLong(CandleEntity::getVolume)
                    .sum();

            result.add(new CandleResponseDto(entry.getKey(), first.getOpen(), high, low, last.getClose(), volume));
        }

        result.sort(Comparator.comparing(CandleResponseDto::getDatetime));
        return new CandleApiResponse(symbol, timeframe, result, result.size());
    }

    private LocalDateTime getBucket(LocalDateTime dt, int minutes) {
        if (minutes == 1440) {
            return dt.toLocalDate().atStartOfDay();
        }
        int bucketMinute = (dt.getMinute() / minutes) * minutes;
        return dt.withMinute(bucketMinute).withSecond(0).withNano(0);
    }
}