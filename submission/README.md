# Stock Market Data Aggregation Service

## Tech Stack

* Java 17
* Spring Boot
* Apache Cassandra
* Maven

---

## Project Structure

Under the defineEdge there is submission folder.

```text
submission/
├── server/
├── client/
├── schema.cql
└── README.md
```

---

## Cassandra Setup

Start Cassandra locally.

Default Port:

```text
9042
```

Create schema:

```bash
cqlsh -f schema.cql
```

---

## Data Ingestion

CSV data is automatically loaded at startup through `CsvImporter`.

File:

```text
src/main/resources/stock_data.csv
```

---

## Start Server

```bash
cd server

mvn spring-boot:run
```

Server starts on:

```text
http://localhost:8080
```

---

## API Endpoint

```http
GET /api/v1/candles
```

### Example Request ( No Pagination )

```http
http://localhost:8080/api/v1/candles?symbol=RELIANCE&timeframe=15m&startDate=2026-01-01T09:15:00&endDate=2026-01-01T15:30:00
```

### Pagination Example

```http
http://localhost:8080/api/v1/candles?symbol=RELIANCE&timeframe=15m&startDate=2026-01-01T09:15:00&endDate=2026-01-01T15:30:00&page=1&size=5
```

---

## Sample Response

```json
{
  "symbol": "RELIANCE",
  "timeframe": "15m",
  "candles": [
    {
      "datetime": "2026-01-01T10:30:00",
      "open": 1582.9,
      "high": 1583.4,
      "low": 1578.5,
      "close": 1579.0,
      "volume": 134552
    },
    {
      "datetime": "2026-01-01T10:45:00",
      "open": 1578.9,
      "high": 1583.1,
      "low": 1578.7,
      "close": 1581.0,
      "volume": 128041
    }
  ],
  "count": 5,
  "page": 1,
  "size": 5,
  "totalRecords": 25
}
```

---

## Supported Timeframes

* 1m
* 5m
* 15m
* 30m
* 1h
* 1d

---

## Start Client

```bash
cd client

mvn spring-boot:run
```

Client reads configuration running on port 8081: and default value of page=0 and size=5

```yaml
api:
  base-url: http://localhost:8080
  symbol: RELIANCE
  timeframe: 15m
  start-date: 2026-01-01T09:15:00
  end-date: 2026-01-01T15:30:00
```

---

## Sample Client Output

```text
=== Fetched Candle Data ===

Symbol: RELIANCE | Timeframe: 15m | Total Candles: 5

2026-01-01T09:15 | O:1577.5 | H:1589.6 | L:1575.2 | C:1588.1 | V:527749
2026-01-01T09:30 | O:1588.5 | H:1592.5 | L:1585.2 | C:1586 | V:433822
2026-01-01T09:45 | O:1585.2 | H:1586.9 | L:1582.9 | C:1583.1 | V:204159
2026-01-01T10:00 | O:1583.5 | H:1587.5 | L:1582.2 | C:1582.2 | V:209213
2026-01-01T10:15 | O:1582.5 | H:1582.9 | L:1579.5 | C:1582.9 | V:134286

===========================
```

---

## Error Handling

Implemented:

* Missing parameters
* Invalid timeframe
* Invalid date range
* Symbol not found

Appropriate HTTP status codes returned.

Examples:

* 400 Bad Request
* 404 Not Found

---

## Design Decisions

* Cassandra partition key:
  `(symbol, candle_date)`

* Clustering column:
  `datetime`

This allows efficient time-series queries.

* Aggregation performed in the service layer.
* Separate client application consumes the REST API.
* Pagination support added for large result sets.
* Global exception handling provides meaningful error responses.

---

