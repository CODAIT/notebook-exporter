# Notebook Tools

## Notebook Exporter as an Apache Spark Application

Enables exporting an Apache Zeppelin or Jupyter Notebook as an application that can be directly submitted to a Apache Spark cluster.

### Exporting a Zeppelin Notebook

```
java -jar ./target/scala-2.11/notebook-exporter.jar \
     --export src/main/resources/notebooks/zeppelin/sample-bank.json \
     --to target/sample-zeppelin.jar
$SPARK_HOME/bin/spark-submit --class NotebookApplication target/sample-zeppelin.jar
```

### Exporting a Jupyter Notebook

```
java -jar ./target/scala-2.11/notebook-exporter.jar \
     --type=jupyter \
     --export src/main/resources/notebooks/jupyter/sample-bank.ipynb \
     --to target/sample-jupyter.jar
$SPARK_HOME/bin/spark-submit --class NotebookApplication target/sample-jupyter.jar
```

### Exporting multiple notebooks

The `notebook-exporter` tool also supports processing multiple notebooks. In this scenario, notebooks
will be processed independently in the order they are provided.

To provide multiple notebooks, utilize multiple --export parameters:

```
java -jar ./target/scala-2.11/notebook-exporter.jar \
     --type=jupyter \
     --export src/main/resources/notebooks/jupyter/sample1.ipynb \
     --export src/main/resources/notebooks/jupyter/sample2.ipynb \
     --to target/sample-jupyter.jar
$SPARK_HOME/bin/spark-submit --class NotebookApplication target/sample-jupyter.jar
```


# Development information

### Building the project

The Notebook Tools is an Scala application built with SBT

```
sbt clean compile assembly
```

## Other information

This tool was developed as part of the Operationalization efforts of the [Spark Technology Center Advisory Council](http://www.spark.tc/advisory-council/) and in close colaboration with [Rohan Sharma](https://github.com/rohancs) representing [Netflix](http://netflix.com)