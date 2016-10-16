package dea.spark

import org.apache.spark.sql.SQLContext

/**
  * This Trait instantiates SqlContext for the downstream application without the Vault Key
  */
trait SparkApp extends SparkAppBase {

  private def _createSqlContext:Boolean = !properties.contains("sqlContext") || !properties("sqlContext").equalsIgnoreCase("disable")

  implicit lazy val sqlContext: SQLContext = spark.sqlContext

  properties.keys.foreach((x:String)=>{
    if(x.startsWith("SQLContext.Conf.")) {
      spark.conf.set(x.replace("SQLContext.Conf.",""),properties(x))
      println("Loaded SQL Conf: "+x.replace("SQLContext.Conf.","")+" = "+properties(x))
    }
  })

}
