package com.example;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        final StreamExecutionEnvironment env = 
            StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Person> flintstones = env.fromData(
            new Person("Fred", 40),
            new Person("Wilma", 38),
            new Person("Pebbles", 5),
            new Person("Barney", 39),
            new Person("Betty", 38),
            new Person("Bamm-Bamm", 6)
        );

        DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age >= 18;
            }
        });

        adults.print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
