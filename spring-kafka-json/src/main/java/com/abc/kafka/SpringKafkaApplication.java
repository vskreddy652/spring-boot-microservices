package com.abc.kafka;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.abc.kafka.consumer.Receiver;
import com.abc.kafka.producer.Sender;
import com.abc.model.Car;

@SpringBootApplication
public class SpringKafkaApplication implements CommandLineRunner {

	@Autowired
	Sender sndr;
	
	@Autowired
	Receiver rcvr;
	
  public static void main(String[] args) {
    SpringApplication.run(SpringKafkaApplication.class, args);
  }
  
	@Override
	public void run(String... arg0) throws Exception {
	    Car car = new Car("Passat", "Volkswagen", "ABC-123");
	    sndr.send(car);

	    rcvr.getLatch().await(10000, TimeUnit.MILLISECONDS);
	}
}
