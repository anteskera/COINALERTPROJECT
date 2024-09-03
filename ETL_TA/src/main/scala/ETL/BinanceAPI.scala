package ETL

import DB.CandlestickData
import org.apache.spark.sql.functions.desc
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import scala.io.Source.fromURL

object BinanceAPI {
  val baseAPIUrl = "https://api.binance.com/api/v3"

  def fetchAllCandleStickData(symbol: String, interval: String, openTime: Long = 1262304000000L)(implicit spark: SparkSession): DataFrame = {
    var allData: DataFrame = spark.emptyDataFrame
    var keepFetching = true
    var apiUrl = s"$baseAPIUrl/klines?symbol=$symbol&interval=$interval&startTime=$openTime&limit=1000"


    while (keepFetching) {
      val df = fetchCandlestickData(apiUrl)
      val rowCount = df.count()

      if (rowCount == 1000) {
        allData = if (allData.isEmpty) df else allData.union(df)

        // Update `apiUrl` with logic to fetch the next set of data based on the last closeTime
        val lastCloseTime = df.orderBy(desc("closeTime")).select("closeTime").first().getLong(0)
        apiUrl = s"$baseAPIUrl/klines?symbol=$symbol&interval=$interval&startTime=$lastCloseTime&limit=1000"
      } else {
        keepFetching = false
        allData = if (allData.isEmpty) df else allData.union(df)
      }
    }

    allData
  }

  def fetchCandlestickData(apiUrl: String)(implicit spark: SparkSession): DataFrame = {
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
          open.toDouble,
          high.toDouble,
          low.toDouble,
          close.toDouble,
          volume.toDouble,
          closeTime.toLong,
          baseAssetVolume.toDouble,
          numberOfTrades.toLong,
          takerBuyVolume.toDouble,
          takerBuyBaseAssetVolume.toDouble,
          ignore
        )
    }

    spark.createDataFrame(data).drop("ignore")
  }
}
