package dea.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Column, DataFrame, Row, SQLContext}
import org.apache.spark.sql.functions._


/**
  * This Object holds utility functions relevant for data processing in Spark
  */
object Util {

  /**
    * Converts camelCase to snake_case
    *
    * @author Jason Reid
    * @param name
    * @return
    */
  def snakify(name: String) = {name.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase}


  /**
    * Extracts a variable list of keys from a Column of type map and applies a function which takes the key and
    * the extracted column and returns a column
    *
    * @author Jason Reid
    * @param map
    * @param f
    * @param keys
    * @return
    */
  def extractKeysAndMap(map:Column, f: (String, Column) => Column, keys:String*): Seq[Column] = {
    keys.map(key => f(key, map.getItem(key)))
  }


  /**
    * Extracts a variable list of keys from a Column of type map
    *
    * @author Jason Reid
    * @param map
    * @param keys
    * @return
    */
  def extractKeys(map:Column, keys:String*): Seq[Column] = {
    extractKeysAndMap(map, (key, col) => col, keys: _*)
  }

  /**
    * Extracts a variable list of keys from a Column of type map
    *
    * @author Jason Reid
    * @param map
    * @param keys
    * @return
    */
  def extractKeysAndAlias(map:Column, keys:String*): Seq[Column] = {
    extractKeysAndMap(map, (key, col) => col.alias(key), keys: _*)
  }


  /**
    * Extracts a variable list of keys from a Column of type map  and returns the column aliased with the snakify
    * version of the map key
    *
    * @param map
    * @param keys
    * @return
    */
  def extractKeysAndSnakify(map:Column, keys:String*): Seq[Column] = {
    extractKeysAndMap(map, (key, col) => col.alias(snakify(key)), keys: _*)
  }

  /**
    * This method takes in a Dateframe and an input schema and re-orders columns of Dataframe to match passed in schema
    *
    * @param df DataFrame to be aligned
    * @param schema Target column Order
    * @return
    *
    * @deprecated Use the implicit DataFrameUtils.alignToSchema instead
    */
  def alignToSchema(df: DataFrame, schema: StructType): DataFrame = {
    df.alignToSchema(schema)
  }

  /**
    * This takes in a reference to Hive table and a dataframe.
    * It extracts schema of table and re-orders columns of dataframe to match the schema.
    *
    * @param df DataFrame to be reshaped
    * @param tableName Hive Table Name
    * @param sqlContext
    * @return
    *
    * @deprecated Use the implicit DataFrameUtils.alignToTableSchema instead
    */
  def alignToTableSchema(df: DataFrame, tableName: String)(implicit sqlContext: SQLContext): DataFrame = {
    df.alignToTableSchema(tableName)
  }

  /**
    * Creates a Dataframe from the schema definition of Target Hive Table
    *
    * @param rdd
    * @param tableName
    * @param sqlContext
    * @return
    *
    * @deprecated Use the implicit RDDUtils.createDataFrameFromTableSchema instead
    */
  def createDataFrameFromTableSchema(rdd: RDD[Row], tableName: String)(implicit sqlContext: SQLContext): DataFrame = {
    rdd.createDataFrameFromTableSchema(tableName)
  }



  implicit class DataFrameUtils(val df: DataFrame) {

    /**
      * This method takes in a Dateframe and an input schema and re-orders columns of Dataframe to match passed in schema
      *
      * @param schema Target column Order
      * @return
      */
    def alignToSchema(schema: StructType): DataFrame = {
      df.select(schema.fieldNames.map { df(_) }:_*)
    }

    /**
      * This takes in a reference to Hive table and a dataframe.
      * It extracts schema of table and re-orders columns of dataframe to match the schema.
      *
      * @param tableName Hive Table Name
      * @param sqlContext
      * @return
      */
    def alignToTableSchema(tableName: String)(implicit sqlContext: SQLContext): DataFrame = {
      val schema = sqlContext.table(tableName).schema
      df.alignToSchema(schema)
    }

    /**
      * Trims all StringTypes in a Dataframe without explicitly selecting with the trim function
      * @return
      */
    def trimAllStrings()= {
      val trimmedCols = df.dtypes.map {
        case(key, dtype) => dtype match {
          case "StringType" => trim(df(key)) as key
          case _  => df(key) as key }
      }
      df.select(trimmedCols:_*)
    }

  }

  implicit class RDDUtils(val rdd: RDD[Row]) {

    /**
      * Creates a Dataframe from the schema definition of Target Hive Table
      *
      * @param tableName
      * @param sqlContext
      * @return
      */
    def createDataFrameFromTableSchema(tableName: String)(implicit sqlContext: SQLContext): DataFrame = {
      val schema = sqlContext.table(tableName).schema
      sqlContext.createDataFrame(rdd, schema)
    }

  }

}
