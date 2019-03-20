/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.kafkaconsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * @author diogo
 */
public class Consumer {
    
    /**
     * Kafka consumer instance
     */
    private KafkaConsumer kafkaConsumer;
    
    /**
     * properties
     */
    private Properties properties;
    
    /**
     * constructor
     * @param topic 
     */
    public Consumer(String topic) {
        
        properties = new Properties();
        properties.put("bootstrap.servers", "172.17.0.2:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test-group");

        this.kafkaConsumer = new KafkaConsumer<>(properties);
        
        kafkaConsumer.subscribe(Arrays.asList(topic));
    }
    
    /**
     * obtain the events stored in the kafka cluster
     * @return ArrayList of the events retrieved
     */
    public ArrayList<String> consumeRecordsFromKafka()
    {
        ArrayList<String> recordsList = new ArrayList<>();
        
        ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
        for (ConsumerRecord record : records)
        {
            recordsList.add(String.valueOf(record.value()));
        }
        
        return recordsList;
    }    
}
