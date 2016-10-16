package dea.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import org.scalatest.{BeforeAndAfterAll, Suite}

trait SparkContextSuite extends BeforeAndAfterAll {  this: Suite =>

  private val master = "local"
  private val appName = "test"

  lazy val conf = new SparkConf()
  lazy val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
  lazy val sc: SparkContext = spark.sparkContext
  lazy val sqlContext = spark.sqlContext

  override def beforeAll() {
    System.clearProperty("spark.driver.port")
    System.clearProperty("spark.hostPort")

    conf.setAppName(appName)
        .setMaster(master)
        .set("spark.default.parallelism", "1")

    super.beforeAll() // To be stackable, must call super.beforeAll
  }

  override def afterAll() {
    try super.afterAll() // To be stackable, must call super.afterAll
    finally if (sc != null) {
      sc.stop()
    }
  }

}
