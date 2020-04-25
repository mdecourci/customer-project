package com.netpod.customers.repository;

import com.netpod.customers.converters.CustomerConverter;
import com.netpod.customers.datasource.CustomerDataSource;
import com.netpod.customers.domain.Customer;
import com.netpod.customers.domain.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerRepositoryTest {

    @Mock
    private CustomerConverter customerConverter;

    @Mock
    private CustomerDataSource customerDataSource;

    @InjectMocks
    private CustomerRepository customerRepository;

    @Test
    public void givenDataSourceWhenFindCustomersThenCustomers() {
        final String line = "ABC";
        Customer customer = Customer.builder().userId(999).name("Christina").location(Location.builder().latitude("52.986375").build()).build();
        List<String> lines = Arrays.asList(line);

        when(customerDataSource.exists()).thenReturn(true);
        when(customerDataSource.read()).thenReturn(lines);
        when(customerConverter.convert(line)).thenReturn(customer);

        Set<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(1);

        Customer foundCustomer = customers.iterator().next();

        assertThat(foundCustomer.getUserId()).isEqualTo(999);
        assertThat(foundCustomer.getName()).isEqualTo("Christina");
        assertThat(foundCustomer.getLocation().getLatitude()).isEqualTo("52.986375");
    }

    @Test
    public void givenNoDataFileWhenFindAllThenNoCustomers() {
        when(customerDataSource.exists()).thenReturn(false);
        Set<Customer> customers =  customerRepository.findAll();

        assertThat(customers).isEmpty();

        verify(customerDataSource, never()).read();
        verifyZeroInteractions(customerConverter);
    }

    @Test
    public void givenNoDataWhenFindAllThenNoCustomers() {
        when(customerDataSource.exists()).thenReturn(true);
        when(customerDataSource.read()).thenReturn(Collections.emptyList());

        Set<Customer> customers =  customerRepository.findAll();

        assertThat(customers).isEmpty();

        verify(customerDataSource).exists();
        verifyZeroInteractions(customerConverter);
    }

}