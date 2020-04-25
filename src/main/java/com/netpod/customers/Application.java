package com.netpod.customers;

import com.netpod.customers.domain.Location;
import com.netpod.customers.service.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private Invitation invitation;

    @Value("${location.latitude}")
    private String latitude;

    @Value("${location.longitude}")
    private String longitude;

    @Value("${location.range}")
    private String range;

    @Override
    public void run(String... args) throws Exception {
        Location baseLocation = Location.builder().latitude(latitude).longitude(longitude).build();

        System.out.println("Nearest customers");
        invitation.inviteCustomers(baseLocation, new BigDecimal(range)).stream().forEach(System.out::println);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
