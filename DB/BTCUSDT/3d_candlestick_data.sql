CREATE TABLE "BTCUSDT"."3d_candlestick_data" (
	"openTime" int8 NOT NULL,
	"open" varchar(255) NOT NULL,
	high varchar(255) NOT NULL,
	low varchar(255) NOT NULL,
	"close" varchar(255) NOT NULL,
	volume varchar(255) NOT NULL,
	"closeTime" int8 NOT NULL,
	"baseAssetVolume" varchar(255) NOT NULL,
	"numberOfTrades" int8 NOT NULL,
	"takerBuyVolume" varchar(255) NOT NULL,
	"takerBuyBaseAssetVolume" varchar(255) NOT NULL,
	CONSTRAINT "3d_candlestick_data_pk" PRIMARY KEY ("openTime")
);
