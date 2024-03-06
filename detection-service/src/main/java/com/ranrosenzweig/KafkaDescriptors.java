package com.ranrosenzweig;

import org.apache.flink.streaming.connectors.kafka.table.KafkaConnectorOptions;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;

public class KafkaDescriptors {
  static String KAFKA_HOST = "localhost:29092";
  public static TableDescriptor SENSORS_DESCRIPTOR =
          TableDescriptor.forConnector("kafka")
                  .schema(
                          Schema.newBuilder()
                                  .column("sensor_id", DataTypes.STRING())
                                  .column("temperature", DataTypes.INT())
                                  .column("status", DataTypes.STRING())
                                  .column("event_time", DataTypes.BIGINT())
                                  .columnByExpression("ptime","PROCTIME()")
                                  .build())
                  .option(KafkaConnectorOptions.VALUE_FORMAT, "json")
                  .option("topic", "sensors")
                  .option(KafkaConnectorOptions.SCAN_STARTUP_MODE, KafkaConnectorOptions.ScanStartupMode.EARLIEST_OFFSET)
                  .option(KafkaConnectorOptions.PROPS_BOOTSTRAP_SERVERS, KAFKA_HOST)
                  .build();

  private KafkaDescriptors() {}
}
