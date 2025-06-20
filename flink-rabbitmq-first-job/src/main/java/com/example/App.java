package com.example;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSink;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final RMQConnectionConfig connectionConfig = new RMQConnectionConfig.Builder()
            .setHost("localhost")
            .setPort(5672)
            .setVirtualHost("/")
            .setUserName("guest")
            .setPassword("guest")
            .build();

        final DataStream<String> stream = env
            .addSource(new RMQSource<>(
                connectionConfig,
                "source",
                true, // use correlation ids
                new SimpleStringSchema()
            ))
            .setParallelism(1).name("RabbitMQ Source");

        final DataStream<String> proccesedStream = stream.map(new MapFunction<String, String>() {

            @Override
            public String map(String value) throws Exception {
                return "Processed: " + value;
            }
        }).name("Process Map");

        proccesedStream.addSink(new RMQSink<String>(connectionConfig, "sink", new SimpleStringSchema())).name("RabbitMQ Sink");

        try {
            env.execute("Flink RabbitMQ Example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
