package dea.spark

import Util._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Column, DataFrame, Row, SQLContext}

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, BeforeAndAfter}
import org.scalatest.junit.JUnitRunner

case class TrimAllStringsTest (v1: String, v2: Int, v3:Double)
/**
  * Created by jasonr on 3/28/16.
  */
@RunWith(classOf[JUnitRunner])
class UtilSuite extends FunSuite with SparkContextSuite with BeforeAndAfter {
  import sqlContext.implicits._

  private var df: DataFrame = _
  private val schema = StructType(Seq(StructField("mapCol", MapType(StringType,IntegerType), true)))

  before {
    // setup a dataframe
    val m = Map("camelCase1" -> 1, "snake_case_2" -> 2)
    val rdd = sc.parallelize(Seq(Row(m)), 1)
    df = sqlContext.createDataFrame(rdd, schema)
  }

  test("extractKeys") {
    import sqlContext.implicits._
    val extract = df.select(extractKeys($"mapCol", "camelCase1", "snake_case_2", "col3"):_*)
    extract.show()
    val row = extract.first()
    assert(row.length == 3)
    assert(row.get(0) == 1)
    assert(row.get(1) == 2)
    assert(row.get(2) == null)
  }

  test("extractKeysAndAlias") {
    import sqlContext.implicits._
    val extract = df.select(extractKeysAndAlias($"mapCol", "camelCase1", "snake_case_2"):_*)
    extract.show()
    val row = extract.first()
    assert(row.length == 2)
    assert(row.get(0) == 1)
    assert(row.get(1) == 2)
    assert(row.schema.fieldNames sameElements  Array("camelCase1", "snake_case_2"))
  }

  test("extractKeysAndSnakify") {
    import sqlContext.implicits._
    val extract = df.select(extractKeysAndSnakify($"mapCol", "camelCase1", "snake_case_2"):_*)
    extract.show()
    val row = extract.first()
    assert(row.length == 2)
    assert(row.get(0) == 1)
    assert(row.get(1) == 2)
    assert(row.schema.fieldNames sameElements  Array("camel_case1", "snake_case_2"))
  }

  test("extractKeysAndMap") {
    import sqlContext.implicits._
    val extract = df.select(extractKeysAndMap($"mapCol", (key: String, col: Column) => col.alias(key.toUpperCase()), "camelCase1", "snake_case_2"):_*)
    extract.show()
    val row = extract.first()
    assert(row.length == 2)
    assert(row.get(0) == 1)
    assert(row.get(1) == 2)
    assert(row.schema.fieldNames sameElements  Array("CAMELCASE1", "SNAKE_CASE_2"))
  }

  test("alignToSchema") {
    val sourceSchema = StructType(Seq(StructField("field3", StringType, true), StructField("field2", StringType, true), StructField("field1", StringType, true)))
    val targetSchema = StructType(Seq(StructField("field1", StringType, true), StructField("field2", StringType, true)))
    val rdd = sc.parallelize(Seq(Row("value3","value2", "value1")), 1)
    val df = sqlContext.createDataFrame(rdd, sourceSchema)
    val targetDf = df.alignToSchema(targetSchema)
    targetDf.printSchema()
    targetDf.show()
    assert(targetDf.schema == targetSchema)
  }

  test("alignToTableSchema") {
    val sourceSchema = StructType(Seq(StructField("field3", StringType, true), StructField("field2", StringType, true), StructField("field1", StringType, true)))
    val targetSchema = StructType(Seq(StructField("field1", StringType, true), StructField("field2", StringType, true)))
    val rdd = sc.parallelize(Seq(Row("value3","value2", "value1")), 1)

    // setup the target table dataframe and register it as a temp table
    val targetTable = sqlContext.createDataFrame(rdd, targetSchema)
    targetTable.createOrReplaceTempView("target")

    val df = sqlContext.createDataFrame(rdd, sourceSchema)
    val targetDf = df.alignToTableSchema("target")(sqlContext)
    targetDf.printSchema()
    targetDf.show()
    assert(targetDf.schema == targetSchema)
  }

  test("createDataFrameFromTableSchema") {
    val targetSchema = StructType(Seq(StructField("field1", StringType, true), StructField("field2", StringType, true)))
    val rdd = sc.parallelize(Seq(Row("value1","value2")), 1)

    // setup the target table dataframe and register it as a temp table
    val targetTable = sqlContext.createDataFrame(rdd, targetSchema)
    targetTable.createOrReplaceTempView("target")

    val targetDf = rdd.createDataFrameFromTableSchema("target")(sqlContext)
    targetDf.printSchema()
    targetDf.show()
    assert(targetDf.schema == targetSchema)
  }


  test("trimAllStrings") {
    import sqlContext.implicits._
    val test1 = TrimAllStringsTest(" value1 ", 2, 2.5)
    val test2 = TrimAllStringsTest("  value2", 2, 2.5)
    val df = sc.parallelize(List(test1, test2)).toDF
    val trimmedDf = df.trimAllStrings()
    assert(trimmedDf.collect.map { _(0).asInstanceOf[String]}.sorted.mkString(",")  == "value1,value2")  // String is trimmed
    assert(trimmedDf.collect.map { _(1).asInstanceOf[Int]}.sum == 4) // Ints are untouched
    assert(trimmedDf.collect.map { _(2).asInstanceOf[Double]}.sum == 5.0) // Doubles are untouched
  }



}
