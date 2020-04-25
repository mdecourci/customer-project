package com.netpod.customers.converters;

import com.netpod.customers.domain.Customer;
import com.netpod.customers.domain.Location;
import com.netpod.customers.exception.CustomerConverionException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CustomerConverterTest {
    private CustomerConverter customerConverter = new CustomerConverter();

    @Test
    public void givenValidCustomerJsonWhenConvertThenValidCustomer() {
       Customer customer =  customerConverter.convert("{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}");

        assertThat(customer.getUserId()).isEqualTo(12);
        assertThat(customer.getName()).isEqualTo("Christina McArdle");
        assertThat(customer.getLocation()).isEqualTo(Location.builder()
                .latitude("52.986375")
                .longitude("-6.043701")
                .build());
    }

    @Test(expected = CustomerConverionException.class)
    public void givenInValidCustomerJsonWhenConvertThenException() {
        customerConverter.convert("{\"happy\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"simon\": \"-6.043701\"}");
    }

    @Test
    public void givenEmptyJsonWhenConvertThenEmptyCustomer() {
        assertThat(customerConverter.convert("{}")).isEqualTo(Customer.builder().location(Location.builder().build()).build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyStringWhenConvertThenException() {
        customerConverter.convert("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullStringWhenConvertThenException() {
        customerConverter.convert(null);
    }
}