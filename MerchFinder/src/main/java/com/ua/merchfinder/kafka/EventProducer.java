/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.merchfinder.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author diogo
 */
public class EventProducer 
{
    /**
     * Declaration of the the singleton instance for this class.
     * The application will only have one producer
     */
    private static EventProducer single_instance = null; 
    
    /**
     * It's a persistent set of properties
     */
    private Properties properties;
    
    /**
     * Kafka producer
     */
    private KafkaProducer kafkaProducer;
    
    /**
     * constructor
     */
    public EventProducer ()
    {      
        this.properties = new Properties();
        this.properties.put("bootstrap.servers", "172.17.0.2:9092");
        this.properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.kafkaProducer = new KafkaProducer(this.properties);
    }
    
    /**
     * Send event to the kafka cluster
     * @param recordTopic
     * @param recordPartition
     * @param recordVaue 
     */
    public void storeEvent(String recordTopic, int recordPartition, String recordVaue){
        
        try{
            // recordTopic = "merchUpdateTopic"
            this.kafkaProducer.send(new ProducerRecord(recordTopic, Integer.toString(recordPartition), recordVaue));

        }catch (Exception e){
            e.printStackTrace();
            this.kafkaProducer.close();
        }
    }
    
    /**
     * static method to create and return an instance of this class
     * @return EventProducer
     */
    public static EventProducer getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new EventProducer(); 
  
        return single_instance; 
    } 
}
