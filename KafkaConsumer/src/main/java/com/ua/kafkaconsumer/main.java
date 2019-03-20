/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ua.kafkaconsumer;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is a simple kafka consumer that connects to the merchUpdateTopic and
 * retrieves the information stored by the kafka producer
 * 
 * @author diogo
 */
public class main 
{
    /**
     * consumer instance
     */
    private static Consumer consumer;
    
    /**
     * scanner
     */
    private static Scanner sc;
    
    /**
     * tries 100 times to retrieve information from the kafka cluster
     */
    private static int nTries = 100;
    
    public static void main(String args[])
    {      
        consumer = new Consumer("merchUpdateTopic");
        
        sc = new Scanner(System.in);
        
        boolean exit = false;
        
        while(!exit)
        {
            for (int i = 0; i < nTries; i++) 
            {
                ArrayList<String> records = consumer.consumeRecordsFromKafka();
                
                // print all the retrieved events
                for (int j = 0; j < records.size(); j++) 
                {
                    System.out.println(records.get(j));
                }
            }
            
            System.out.println("Enter \"Y\" to try receiving kafka messages again. or <ENTER> to exit this kafka consumer.");
            String input = sc.nextLine();
            
            if (input.toLowerCase().equals("y"))
                continue;
            else
                exit = true;
        }
    }
}
