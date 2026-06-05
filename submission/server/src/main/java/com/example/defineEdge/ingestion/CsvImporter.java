package com.example.defineEdge.ingestion;

import com.example.defineEdge.entity.CandleEntity;
import com.example.defineEdge.repository.CandleRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvImporter implements CommandLineRunner {

    private final CandleRepository repository;

    @Override
    public void run(String... args) throws Exception {

        if(repository.count() > 0){
            return;
        }
        List<CandleEntity> candles = new ArrayList<>();

        try(CSVReader reader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/stock_data.csv")))) {
            reader.readNext();
            String[] row;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while((row = reader.readNext()) != null){

                CandleEntity candle = new CandleEntity();

                candle.setSymbol(row[0]);

                LocalDateTime dateTime = LocalDateTime.parse(row[1], formatter);
                candle.setDatetime(dateTime);
                candle.setCandleDate(dateTime.toLocalDate());

                candle.setOpen(new BigDecimal(row[2]));
                candle.setHigh(new BigDecimal(row[3]));
                candle.setLow(new BigDecimal(row[4]));
                candle.setClose(new BigDecimal(row[5]));
                candle.setVolume(Long.parseLong(row[6]));

                candles.add(candle);
            }
        }

        repository.saveAll(candles);
        log.info("Imported records : {}", candles.size());
    }
}
