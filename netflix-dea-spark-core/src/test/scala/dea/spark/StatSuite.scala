package dea.spark

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
// import dea.spark.udf.stats.{ApproxSetUDAF, _}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

@RunWith(classOf[JUnitRunner])
class StatSuite extends FunSuite with SparkContextSuite with BeforeAndAfter {
  private var df: DataFrame = _
  private val schemaString = "a b"
  private val schema =
    StructType(
      schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))

  before {
    // setup a dataframe
    val testData = sc.parallelize(List("a","a","a","a","b","c","d","d","e","f","f").map (v=> Row("1", v)))
    df = sqlContext.createDataFrame(testData, schema)
    df.registerTempTable("test")
  }


//  test("implementation has no crazy bugs") {
//    // not an accuracy test, but testing to see that implementation is not doing anything crazy
//    import sqlContext.implicits._
//    val approxSet = new ApproxSetUDAF
//    val approxMerge = new ApproxMergeUDAF
//    sqlContext.udf.register("approxSet",approxSet)
//    sqlContext.udf.register("approxMerge",approxMerge)
//    sqlContext.udf.register("cardinality",Cardinality.cardinality)
//    val approxCountQuery =
//      """
//        | select a, cardinality(approxMerge(b)) from
//        |  (select a, approxSet(b) as b from test group by a) sub
//        | group by a
//      """.stripMargin
//    val trueCountQuery =
//      """
//        | select a, count(distinct b) from test group by a
//      """.stripMargin
//    val approxCount = sqlContext.sql(approxCountQuery).collect()
//    val trueCount = sqlContext.sql(trueCountQuery).collect()
//    assert(approxCount(0).get(1).asInstanceOf[Long] == trueCount(0).get(1).asInstanceOf[Long])
//
//  }

}
