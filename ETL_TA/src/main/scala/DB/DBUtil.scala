package DB

import com.typesafe.config._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

case class CandlestickData(openTime: Long,
                           open: Double,
                           high: Double,
                           low: Double,
                           close: Double,
                           volume: Double,
                           closeTime: Long,
                           baseAssetVolume: Double,
                           numberOfTrades: Long,
                           takerBuyVolume: Double,
                           takerBuyBaseAssetVolume: Double,
                           ignore: String)
object DBUtil {
  private val config = ConfigFactory.load()

  private val dbHost: String = config.getString("db.db_host")
  private val dbName: String = config.getString("db.db_name")
  private val dbUser: String = config.getString("db.db_user")
  private val dbPassword: String = config.getString("db.db_password")

  def writeDFToTable(df: DataFrame, schema: String, table: String,
                     saveMode: SaveMode = SaveMode.Overwrite): Unit = {
    val fullTableName = s"\"$schema\".\"$table\""
    val jdbcURL = s"jdbc:postgresql://$dbHost/$dbName"

    df.write
      .format("jdbc")
      .option("url", jdbcURL)
      .option("dbtable", fullTableName)
      .option("user", dbUser)
      .option("password", dbPassword)
      .mode(saveMode) // Append, Overwrite, Ignore, ErrorIfExists
      .save()

    println(s"DataFrame written to table $fullTableName")
  }

  def readDFFromTable(schema: String, table: String)(implicit spark: SparkSession): DataFrame = {
    // JDBC connection URL
    val jdbcURL = s"jdbc:postgresql://$dbHost/$dbName"

    // Full table name with schema
    val fullTableName = s"\"$schema\".\"$table\""

    // Read data from the table
    val df = spark.read
      .format("jdbc")
      .option("url", jdbcURL)
      .option("dbtable", fullTableName)
      .option("user", dbUser)
      .option("password", dbPassword)
      .load()

    // Return the DataFrame
    df.orderBy("openTime")
  }
}
