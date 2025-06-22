package com.example;

import java.io.File;
import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer {
    private final static String QUEUE_NAME = "exampleTestQueue";
    private final static String LOREM_FILENAME = "lorem.txt";

    public Producer() {}

    public static void main(String[] args) throws Exception {
        File file = new File("src/resources/" + LOREM_FILENAME);
        Scanner scanner = new Scanner(file);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            while (scanner.hasNext()) {
                String message = scanner.next();
                if (message.isEmpty()) {
                    continue; // Skip empty lines
                }
                // Publish the message to the queue
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
                Thread.sleep(10); // Delay 10ms between messages
            }
            scanner.close();
            System.out.println("Messages sent to the queue from " + LOREM_FILENAME);
        }
    }
}
