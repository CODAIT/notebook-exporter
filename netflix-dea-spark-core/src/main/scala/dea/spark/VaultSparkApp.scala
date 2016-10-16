package dea.spark

/**
  * This Trait sets the Vault Key for the downstream application
  */
trait VaultSparkApp extends SparkApp {
  println("Loading property for Vault Access")
  sqlContext.setConf("XXXXXXXXXXXXXXXXX","YYYYYYYYYYYYYYYYYYYYYYYYYYYY")
}
