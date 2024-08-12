import org.apache.spark.sql.{DataFrame, SparkSession}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import scala.io.Source.fromURL

object BinanceAPI {
  def fetchCandlestickData(symbol: String, interval: String)(implicit spark: SparkSession): DataFrame = {

    val apiUrl = s"https://api.binance.com/api/v3/klines?symbol=$symbol&interval=$interval"

    implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
    val response = fromURL(apiUrl)

    val responseString = response.mkString
    val json = parse(responseString)
    val data = json.extract[List[List[Any]]].map {
      case List(openTime: BigInt, open: String, high: String, low: String, close: String, volume: String,
      closeTime: BigInt, baseAssetVolume: String, numberOfTrades: BigInt,
      takerBuyVolume: String, takerBuyBaseAssetVolume: String, ignore: String) =>
        CandlestickData(
          openTime.toLong,
          open,
          high,
          low,
          close,
          volume,
          closeTime.toLong,
          baseAssetVolume,
          numberOfTrades.toLong,
          takerBuyVolume,
          takerBuyBaseAssetVolume,
          ignore
        )
    }

    spark.createDataFrame(data).drop("ignore")
  }
}
