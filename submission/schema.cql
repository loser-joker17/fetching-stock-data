CREATE KEYSPACE IF NOT EXISTS stock_keyspace
WITH replication = {
    'class' : 'SimpleStrategy',
    'replication_factor' : 1
};

USE stock_keyspace;

CREATE TABLE IF NOT EXISTS stock_candles (
    symbol text,
    candle_date date,
    datetime timestamp,
    open decimal,
    high decimal,
    low decimal,
    close decimal,
    volume bigint,
    PRIMARY KEY ((symbol, candle_date), datetime)
)
WITH CLUSTERING ORDER BY (datetime ASC);