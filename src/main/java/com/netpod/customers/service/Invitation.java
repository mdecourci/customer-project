package com.netpod.customers.service;

import com.netpod.customers.calculator.Calculator;
import com.netpod.customers.domain.Customer;
import com.netpod.customers.domain.Location;
import com.netpod.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class Invitation {

    private final CustomerRepository customerRepository;
    @Qualifier("GreatCircleDistanceCalculator")
    private final Calculator calculator;

    public List<Customer> inviteCustomers(Location toLocation, final BigDecimal maxRangeKiloMetres) {
        log.info("Invite customers that are within {}km of location {} ", maxRangeKiloMetres, toLocation);
        if (maxRangeKiloMetres.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Range from location must be positive");
        }

        List<Customer> foundCustomers = customerRepository.findAll().stream()
                .filter(customer -> checkInRange(customer, toLocation, maxRangeKiloMetres))
                .sorted(Comparator.comparing(Customer::getUserId))
                .collect(Collectors.toList());

        return foundCustomers;
    }

    private Boolean checkInRange(Customer fromCustomer, Location toLocation, BigDecimal maxRangeKiloMetres) {
        if (toLocation != null) {
            BigDecimal customerDistance = calculator.from(fromCustomer.getLocation(), toLocation) ;

            if (maxRangeKiloMetres.equals(BigDecimal.ZERO) ||
                    maxRangeKiloMetres.compareTo(customerDistance) > 0) {
                return true;
            }
        }
        return false;
    }
}
