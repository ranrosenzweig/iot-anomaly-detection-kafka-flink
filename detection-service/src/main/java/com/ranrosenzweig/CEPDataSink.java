package com.ranrosenzweig;


import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 */
public class CEPDataSink {

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
    tableEnv.createTable("CEPDataJDBC", MySqlDescriptors.CEP_DATA_JDBC);



    tableEnv.executeSql(
            "INSERT INTO CEPDataJDBC " +
                    "SELECT * " +
                    "FROM SensorsKafka " +
                    "MATCH_RECOGNIZE ( " +
                        "PARTITION BY sensor_id " +
                        "ORDER BY ptime " +
                        "MEASURES " +
                            "TO_TIMESTAMP(FROM_UNIXTIME(A.event_time / 1000)) AS event_time, " +
                            "COUNT(*) - 2 AS non_errors, " +
                            "CONCAT_WS('-', FIRST(B.status,0), FIRST(B.status,1), FIRST(B.status,2) , FIRST(B.status,3), FIRST(B.status,4)) as history, " +
                            "MIN (temperature) as min_temperature, " +
                            "ROUND(AVG(temperature),2) as avg_temperature, " +
                            "MAX(temperature) as max_temperature, " +
                            "CAST(ROUND((C.event_time - A.event_time) / 1000) AS BIGINT) AS elapsed " +
                        "AFTER MATCH SKIP PAST LAST ROW " +
                        "PATTERN (A B{2,5} C)  WITHIN INTERVAL '1' MINUTE " +
                        "DEFINE " +
                            "A AS status = 'ERROR'," +
                            "B AS status <> 'ERROR'," +
                            "C AS status = 'ERROR'" +
                    ") AS T"
    );
  }
}
