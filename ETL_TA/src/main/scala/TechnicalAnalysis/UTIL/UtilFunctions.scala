package TechnicalAnalysis.UTIL

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

object UtilFunctions {

  def SMA(dataFrame: DataFrame, range: Int)(implicit spark: SparkSession): DataFrame = {
    val windowSpec = Window
      .orderBy("closeTime")
      .rowsBetween(-range + 1, Window.currentRow)

    val dfWithSMA = dataFrame.withColumn(s"${range}_SMA", avg(col("close")).over(windowSpec))

    dfWithSMA
  }
  def EMA(dataFrame: DataFrame, range: Int)(implicit spark: SparkSession): DataFrame = {
    // 1. Define the multiplier for weighting the EMA
    val k = 2.0 / (range + 1)

    // 2. Define the window spec for past values
    val windowSpec = Window
      .orderBy("closeTime")

    // 3. Calculate the EMA
    val emaColumn: Column = {
      val close = col("close")
      val emaInit = first(close).over(windowSpec)  // Initial EMA (first close value)
      val emaCalc = (close * k) + (lag("close", range).over(windowSpec) * (1 - k))

      when(lag("close", range).over(windowSpec).isNull, emaInit)
        .otherwise(emaCalc)
    }

    // 4. Add the EMA column to the DataFrame
    val dfWithEMA = dataFrame.withColumn(s"${range}_EMA", emaColumn)

    dfWithEMA
  }

  def trendCalculationUsingSMA (dataframe: DataFrame, shorter_range: Int, longer_range: Int)(implicit spark: SparkSession): DataFrame =
  {

    var dataframe2 = SMA(dataframe, shorter_range)
    dataframe2 = SMA(dataframe, longer_range)

    val trendColumn: Column = {
      val trend = col("trend")

      trend
    }

    dataframe2
  }
}
