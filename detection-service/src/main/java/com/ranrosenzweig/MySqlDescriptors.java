package com.ranrosenzweig;

import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;

public class MySqlDescriptors {

  public static final TableDescriptor SENSORS_OUT_JDBC =
          TableDescriptor.forConnector("jdbc")
                  .schema(
                          Schema.newBuilder()
                                  .column("sensor_id", DataTypes.STRING())
                                  .column("temperature", DataTypes.INT())
                                  .column("status", DataTypes.STRING())
                                  .column("event_time", DataTypes.BIGINT())
                                  .column("ptime", DataTypes.TIMESTAMP())
                                  //.primaryKey("c_id")
                                  .build())
                  .option("url", "jdbc:mysql://localhost:" + "3306" + "/example_db")
                  .option("table-name", "sensors")
                  .option("username", "inserter")
                  .option("password", "password")
                  .build();
  public static final TableDescriptor CEP_DATA_JDBC =
          TableDescriptor.forConnector("jdbc")
                  .schema(
                          Schema.newBuilder()
                                  .column("sensor_id", DataTypes.STRING())
                                  .column("event_time", DataTypes.TIMESTAMP(3))
                                  .column("non_errors", DataTypes.BIGINT())
                                  .column("history", DataTypes.STRING())
                                  .column("min_temperature", DataTypes.INT())
                                  .column("avg_temperature", DataTypes.INT())
                                  .column("max_temperature", DataTypes.INT())
                                  .column("elapsed", DataTypes.BIGINT())
                                  //.watermark("ptime", "ptime - INTERVAL '1' SECONDS")
                                  //.primaryKey("sensor_id")
                                  .build())
                  .option("url", "jdbc:mysql://localhost:" + "3306" + "/example_db")
                  .option("table-name", "cep_data")
                  .option("username", "inserter")
                  .option("password", "password")
                  .build();

  private MySqlDescriptors() {}
}