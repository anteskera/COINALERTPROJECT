import org.apache.spark.sql.{DataFrame, SparkSession}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.io.Source.fromURL
case class CandlestickData(openTime: Long,
                           open: String,
                           high: String,
                           low: String,
                           close: String,
                           volume: String,
                           closeTime: Long,
                           baseAssetVolume: String,
                           numberOfTrades: Long,
                           takerBuyVolume: String,
                           takerBuyBaseAssetVolume: String,
                           ignore: String)
object BinanceAPI {
  def fetchCandlestickData(symbol: String, interval: String): DataFrame = {
    val spark = SparkSession.builder()
      .appName("BinanceCandlestickFetcher")
      .master("local[*]")
      .getOrCreate()

    val apiUrl = s"https://api.binance.com/api/v3/klines?symbol=$symbol&interval=$interval"
    println(apiUrl)
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

    spark.createDataFrame(data)
  }
}

object Main extends App {
  val symbol = "BTCUSDT"
  val interval = "1M"

  val candlestickData = BinanceAPI.fetchCandlestickData(symbol, interval)
}
