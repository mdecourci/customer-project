package com.netpod.customers.repository;

import com.netpod.customers.converters.CustomerConverter;
import com.netpod.customers.datasource.CustomerDataSource;
import com.netpod.customers.domain.Customer;
import com.netpod.customers.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class CustomerRepository {
    private final CustomerConverter customerConverter;
    private final CustomerDataSource customerDataSource;

    @Autowired
    public CustomerRepository(CustomerDataSource customerDataSource, CustomerConverter customerConverter) {
        this.customerDataSource = customerDataSource;
        this.customerConverter = customerConverter;
    }

    public Set<Customer> findAll() {
        log.info("Find all customers");
        try {
            if (customerDataSource.exists()) {
                return customerDataSource.read().stream().map(line -> customerConverter.convert(line)).collect(Collectors.toSet());
            } else {
                return Collections.emptySet();
            }
        } catch (Exception e) {
            log.error("Error finding customers", e);
            throw new DataNotFoundException(e);
        }
    }
}
