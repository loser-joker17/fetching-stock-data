package com.example.defineEdge.constants;

public final class MessageConstants {
    private MessageConstants() {}
    public static final String INVALID_TIMEFRAME = "Unsupported timeframe. Supported values: 1m, 5m, 15m, 30m, 1h, 1d";
    public static final String INVALID_DATE_RANGE = "startDate cannot be greater than endDate";
    public static final String SYMBOL_NOT_FOUND = "No candle data found for the given symbol";
}