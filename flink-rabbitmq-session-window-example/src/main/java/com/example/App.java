package com.example;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
//import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSink;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;

public class App {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final RMQConnectionConfig connectionConfig = new RMQConnectionConfig.Builder()
            .setHost("localhost")
            .setPort(5672)
            .setVirtualHost("/")
            .setUserName("guest")
            .setPassword("guest")
            .build();

        final DataStream<String> input = env
            .addSource(new RMQSource<>(
                connectionConfig,
                "exampleTestQueue",
                true, // use correlation ids
                new SimpleStringSchema()
            ))
            .assignTimestampsAndWatermarks(
                WatermarkStrategy.<String>forMonotonousTimestamps()
                .withTimestampAssigner((element, recordTimestamp) -> {
                    return System.currentTimeMillis(); // 1 second idle timeout
                }))
            .setParallelism(1).name("RabbitMQ Source");


        // Simple Tumbling window with 30ms duration where the messages are being published every 10ms
        // This will combine messages in the window and send them to the RabbitMQ sink
        DataStream<String> windowSource = input
            .windowAll(TumblingEventTimeWindows.of(Time.milliseconds(30)))
            .reduce(new ReduceFunction<String>() {
                public String reduce(String value1, String value2) throws Exception {
                    return value1 + " " + value2; // Combine messages
                }
                
            });

        

        windowSource.addSink(new RMQSink<String>(connectionConfig, "sink", new SimpleStringSchema())).name("RabbitMQ Sink");

        try {
            env.execute("Flink RabbitMQ Example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
