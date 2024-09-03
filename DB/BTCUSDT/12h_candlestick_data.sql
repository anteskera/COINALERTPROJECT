CREATE TABLE "BTCUSDT"."12h_candlestick_data" (
	"openTime" int8 NOT NULL,
	"open" double8 NOT NULL,
	high double8 NOT NULL,
	low double8 NOT NULL,
	"close" double8 NOT NULL,
	volume double8 NOT NULL,
	"closeTime" int8 NOT NULL,
	"baseAssetVolume" double8 NOT NULL,
	"numberOfTrades" int8 NOT NULL,
	"takerBuyVolume" double8 NOT NULL,
	"takerBuyBaseAssetVolume" double8 NOT NULL,
	CONSTRAINT "12h_candlestick_data_pk" PRIMARY KEY ("openTime")
);
