package br.com.alura.ecommerce;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());
        for (var i = 0; i < 100; i++){
            var key = UUID.randomUUID().toString();
            var value = key + ",654684,1234";
            var email = "Thanl you for your order! Welcome! We are processing your order!";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, value);
            var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);
            producer.send(record, getCallback()).get();
            producer.send(emailRecord, getCallback()).get();
        }
    }

    private static Callback getCallback() {
        return (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("SUCCESS SEND " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestemp " + data.timestamp());
        };
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }


}
