import org.apache.spark.sql.SparkSession



object Main extends App {
  val symbol = "BTCUSDT"
  val interval = "1M"


  // Initialize Spark session
  implicit val spark: SparkSession = SparkSession.builder()
    .appName("Spark Data Processing")
    .master("local[*]") // Use local mode for testing, or remove for cluster mode
    .getOrCreate()

  private val candlestickData = BinanceAPI.fetchCandlestickData(symbol, interval)
  DBUtil.writeDFToTable(candlestickData, "btc", "monthly_candlestick_data")

  spark.close()
}
