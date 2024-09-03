package TechnicalAnalysis

import DB.DBUtil
import UTIL.UtilFunctions
import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {

    // Initialize Spark session
    implicit val spark: SparkSession = SparkSession.builder()
      .appName("Technical Analysis")
      .master("local[*]") // Use local mode for testing, or remove for cluster mode
      .getOrCreate()

    var weekly_df = DBUtil.readDFFromTable("BTCUSDT","1w_candlestick_data")


  }
}