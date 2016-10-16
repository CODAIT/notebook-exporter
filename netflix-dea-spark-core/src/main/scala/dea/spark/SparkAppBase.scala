package dea.spark

import org.apache.spark.sql.SparkSession

import dea.util.Source
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Features:
  * This trait extends the Logging Spark Trait to offer handle onto logger object to log to Hadoop Log Files.
  * It also provides boilerplate code to instantiate Spark Context & Sql Context for the application.
  *
  * It provides argument parsing as a service to applications if passed in "a=b" format.
  * The arguments are available in argsMap Class member
  * The unparsed args are available at args class member
  *
  * Any spark/sql context configs that you may want to set prior to instantiation can be supplied using a
  * properties file which will be auto-loaded
  *
  * The Trait exposes a virutal method run() which needs to be implemented by the application extending this trait
  *
  * @author Rohan Sharma
  */
trait SparkAppBase {

  private var baseClassInitialized=false

  private var _args:Array[String] = null
  private lazy val _argsMap:Map[String,String] = parseArgs

  private lazy val className = this.getClass.getCanonicalName.replace("$","")
  protected lazy val properties=scala.collection.JavaConversions.propertiesAsScalaMap(Source.getProperties("/properties/"+className+".properties"))

  //Bootstrapping Spark Context
  protected lazy val _conf = new SparkConf().setAppName(this.getClass.getSimpleName.split("\\$").last)
  properties.keys.foreach((x:String)=>{
    if(x.startsWith("SparkContext.Conf.")) {
      _conf.set(x.replace("SparkContext.Conf.",""),properties(x))
      println("Property Loaded: "+x.replace("SparkContext.Conf.","")+" = "+properties(x))
    }
    else{
      println("Property Parsed: "+x.replace("SparkContext.Conf.","")+" = "+properties(x))
    }
  })

  implicit lazy val spark: SparkSession = SparkSession.builder().config(_conf).enableHiveSupport().getOrCreate()
  implicit lazy val sc: SparkContext = spark.sparkContext

  def args:Array[String] = if(baseClassInitialized) _args else NotInitializedException
  def argsMap:Map[String,String] = if(baseClassInitialized) _argsMap else NotInitializedException

  def NotInitializedException = throw new Exception("Base class not initialized yet ! Command Line arguments cannot be accessed in constructors as main method has not been loaded yet.")

  private def parseArgs:Map[String,String] = {
    var result: Map[String,String] = Map()
    if(_args == null) return result

    for (arg <- _args) {
      val token = arg.split("=")
      val value = if(token.length == 1) "" else token(1)
      result += (token(0)->value)
    }
    result
  }

  //Main Method
  final def main(args:Array[String]): Unit = {
    //Loading Class State
    this._args = args
    baseClassInitialized = true

    //Call Business Logic
    run()

    //Cleanup
    sc.stop
  }

  //Every Pipeline should implement this
  def run() : Unit

}
