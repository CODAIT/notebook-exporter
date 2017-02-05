# Notebook Tools

## Building the project

The Notebook Tools is an Scala application built with SBT

```
sbt clean compile assembly
```

## Notebook Exporter as an Apache Spark Application

Enables exporting an Apache Zeppelin or Jupyter Notebook as an application that can be directly submitted to a Apache Spark cluster.

Exporting a Zeppelin Notebook

```
java -jar ./target/scala-2.11/notebook-exporter.jar \
     --export src/main/resources/notebooks/zeppelin/sample-bank.json \
     --to target/sample-zeppelin.jar
$SPARK_HOME/bin/spark-submit --class NotebookApplication target/sample-zeppelin.jar
 
```

Exporting a Jupyter Notebook

```
java -jar ./target/scala-2.11/notebook-exporter.jar \
     --type=jupyter \
     --export src/main/resources/notebooks/jupyter/sample-bank.ipynb \
     --to target/sample-jupyter.jar
$SPARK_HOME/bin/spark-submit --class NotebookApplication target/sample-jupyter.jar
 
```