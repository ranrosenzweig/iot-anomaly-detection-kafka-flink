package com.ranrosenzweig;

import com.ranrosenzweig.KafkaDescriptors;
import com.ranrosenzweig.MySqlDescriptors;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 */
public class SensorsSink {

  static {
    // make console printing prettier
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  public static void main(String[] args) throws Exception {
    // setup environment
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(4);
    env.enableCheckpointing(200);
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    // create tables to connect to external systems

    // Read from Kafka topic
    tableEnv.createTable("SensorsKafka", KafkaDescriptors.SENSORS_DESCRIPTOR);
    // JDBC one time scans and instant lookups
    tableEnv.createTable("SensorsJDBC", MySqlDescriptors.SENSORS_OUT_JDBC);

    // read the Sensors kafka topic log
//    tableEnv.executeSql("SELECT *,CAST(ptime AS timestamp(3)) FROM SensorsKafka order by event_time desc limit 5").print();

    // read the Sensors kafka topic log and update Sensors table
    tableEnv.executeSql("INSERT INTO SensorsJDBC " +
           " SELECT sensor_id,temperature,status, event_time,CAST(ptime AS timestamp(3)) as ptime FROM SensorsKafka").print();
  }
}
