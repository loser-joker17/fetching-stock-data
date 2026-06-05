package com.example.client;

import com.example.client.apiClient.CandleApiClient;
import com.example.client.dto.CandleApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ClientApplication implements CommandLineRunner {

	private final CandleApiClient client;

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@Override
	public void run(String... args) {

		CandleApiResponse response = client.fetchCandles();

		System.out.println();
		System.out.println("=== Fetched Candle Data ===");

		System.out.printf("Symbol: %s | Timeframe: %s | Total Candles: %d%n", response.getSymbol(), response.getTimeframe(), response.getCount());

		response.getCandles().forEach(candle -> System.out.printf("%s | O:%s | H:%s | L:%s | C:%s | V:%d%n",
						candle.getDatetime(),
						candle.getOpen(),
						candle.getHigh(),
						candle.getLow(),
						candle.getClose(),
						candle.getVolume()
				)
		);

		System.out.println("===========================");
	}
}
