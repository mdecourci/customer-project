package com.netpod.customers.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {

    @Test
    public void givenCustomerAndLocationFieldsWhenBuildThenCustomerBuilt() {

        Customer customer = Customer.builder()
                .userId(12)
                .name("Christina McArdle")
                .location(Location.builder()
                        .latitude("52.986375")
                        .longitude("-6.043701")
                        .build())
                .build();

        assertThat(customer.getLocation().getLatitude()).isEqualTo("52.986375");
        assertThat(customer.getUserId()).isEqualTo(12);
        assertThat(customer.getName()).isEqualTo("Christina McArdle");
        assertThat(customer.getLocation().getLongitude()).isEqualTo("-6.043701");
    }
}