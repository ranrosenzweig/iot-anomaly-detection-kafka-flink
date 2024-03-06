package org.example;

import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;
import org.json.JSONObject;

public class Main {
  public static void main(String[] args) {

    String bootstrapServers = "kafka:9092";

    // create Producer properties
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // create the producer
    KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

    for (int j = 0; j < 10000; j++){
      for (int i = 0; i < 100; i++) {
        Random rand = new Random();
        if (rand.nextDouble() < 0.5)
          continue;
        String msg = generateDataPoint(i);
        //String encoded_message = URLEncoder.encode(msg, "UTF-8");
        // create a producer record
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("sensors", msg);
        //send data - asynchronous
        producer.send(record);
        Utils.sleep(10);

      }
      // flash and close producer
      producer.close();
    }
    }

  public static String generateDataPoint(int id){
    Random rand = new Random();
    String status;
    String [] status_arr = {"WARNING", "ERROR"};
    long temperature = Math.round(10 + rand.nextDouble() * 170);
    if (temperature >160)
      status = "ERROR";
    else if (temperature > 140 || (rand.nextInt(100 + 1 - 1) + 1) > 80) {
      int select = rand.nextInt(status_arr.length);
      status = status_arr[select];
    }
    else
      status = "OK";

    JSONObject jo = new JSONObject();
    jo.put("sensor_id", "sensor-" + (String.format("%02d", id)));
    jo.put("temperature",temperature);
    jo.put("status", status);
    jo.put("event_time", System.currentTimeMillis());

    return jo.toString();
  }
}