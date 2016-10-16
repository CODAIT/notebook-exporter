# DEA Spark Core #

This project houses reusable boilerplate code and functions (utility/udfs/udafs) that are used by downstream spark applications that subscribe to this library.

Following features are available out-of-the-box:

1. "sc" as Spark Context is created and set as class variable
2. "sqlContext" as Hive Context is created and set as class variable
3. Spark & SQL context configs are loaded automatically if specified in application's properties file with Prefix "Spark.Conf." or "SQLContext.Conf."
4. Parsing of arguments of format 'a=b c=d e=f ...' into Map{(a->b),(c->d),(e->f)}. Derived classes have argsMap ready to be used in their application.
5. If a properties file with fully qualified className.properties is placed in ~/src/properties/ then its automatically loaded and available via properties class member
7. Utility functions available in dea.util and UDFs are available under dea.spark.udf
8. Optional parameter to disable sqlContext instantiation (helps in local debugging as connection to Hive Metastore is skipped)
