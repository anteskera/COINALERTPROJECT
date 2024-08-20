import org.apache.spark.sql.SparkSession



object Main extends App {
  private val symbols = Array("BTCUSDT")

  //Intervals available:
  // 1s, 1m,3m,5m,15m,30m, 1h,2h,4h,6h,8h, 12h, 1d,3d, 1w, 1M
  private val intervals = Array("1M", "1w", "3d", "1d", "12h", "4h")


  // Initialize Spark session
  implicit val spark: SparkSession = SparkSession.builder()
    .appName("Spark Data Processing")
    .master("local[*]") // Use local mode for testing, or remove for cluster mode
    .getOrCreate()

  for(symbol <- symbols){
    for (interval <- intervals) {
      val candlestickData = BinanceAPI.fetchAllCandleStickData(symbol, interval)
      DBUtil.writeDFToTable(candlestickData, symbol, s"${interval}_candlestick_data")
    }
  }

  spark.close()
}
