package com.netpod.customers.service;


import com.netpod.customers.calculator.Calculator;
import com.netpod.customers.domain.Customer;
import com.netpod.customers.domain.Location;
import com.netpod.customers.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvitationTest {

    public static final BigDecimal TARGET_LOCATION_IN_KM = new BigDecimal("100000");

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Calculator calculator;

    @InjectMocks
    private Invitation invitation;

    private Location withInRangeLocation;
    private Location outOfRangeLocation;

    private Customer withInRangeCustomer;
    private Customer outOfRangeCustomer;
    private BigDecimal withInRangeDistance;
    private BigDecimal outOfRangeDistance;

    @Before
    public void setUp() throws Exception {
        withInRangeLocation = Location.builder().latitude("52.986375").build();
        outOfRangeLocation = Location.builder().latitude("10.986375").build();

        withInRangeCustomer = Customer.builder().userId(100).location(withInRangeLocation).build();
        outOfRangeCustomer = Customer.builder().userId(60).location(outOfRangeLocation).build();

        withInRangeDistance =  TARGET_LOCATION_IN_KM.divide(new BigDecimal("2"));
        outOfRangeDistance =  TARGET_LOCATION_IN_KM.multiply(new BigDecimal("2"));
        
    }

    @Test
    public void givenLocationAndNearestCustomersWhenInvitedThenFindNearest() {

        Location toLocation = Location.builder().build();

        Set<Customer> foundCustomers = Set.of(withInRangeCustomer, outOfRangeCustomer);

        when(customerRepository.findAll()).thenReturn(foundCustomers);
        when(calculator.from(withInRangeLocation, toLocation)).thenReturn(withInRangeDistance);
        when(calculator.from(outOfRangeLocation, toLocation)).thenReturn(outOfRangeDistance);

        List<Customer> customers = invitation.inviteCustomers(toLocation, TARGET_LOCATION_IN_KM);

        assertThat(customers).hasSize(1);
        assertThat(customers.get(0)).isEqualTo(withInRangeCustomer);
    }

    @Test
    public void givenLocationAndNearestCustomersWhenInvitedThenCustomerInSortOrderOfUserIdAscending() {
        Location toLocation = Location.builder().build();

        Location withInRangeLocationForSortedCustomer = Location.builder().latitude("30.986375").build();
        Customer withInRangeSortedCustomer = Customer.builder()
                .userId(10).location(withInRangeLocationForSortedCustomer).build();

        Set<Customer> foundCustomers = Set.of(withInRangeCustomer, withInRangeSortedCustomer, outOfRangeCustomer);

        BigDecimal withInRangeSortedCustomerDistance =
                TARGET_LOCATION_IN_KM.divide(new BigDecimal("3"), 6, RoundingMode.HALF_UP);

        when(customerRepository.findAll()).thenReturn(foundCustomers);

        when(calculator.from(withInRangeLocation, toLocation)).thenReturn(withInRangeDistance);
        when(calculator.from(outOfRangeLocation, toLocation)).thenReturn(outOfRangeDistance);
        when(calculator.from(withInRangeLocationForSortedCustomer, toLocation))
                .thenReturn(withInRangeSortedCustomerDistance);

        List<Customer> customers = invitation.inviteCustomers(toLocation, TARGET_LOCATION_IN_KM);

        assertThat(customers).hasSize(2);
        assertThat(customers.get(0).getUserId()).isEqualTo(10);
        assertThat(customers.get(1).getUserId()).isEqualTo(100);
    }

    @Test
    public void givenNoLocationAndCustomersWhenInvitedThenNoCustomers() {

        Set<Customer> foundCustomers = Set.of(withInRangeCustomer, outOfRangeCustomer);

        when(customerRepository.findAll()).thenReturn(foundCustomers);

        List<Customer> customers = invitation.inviteCustomers(null, TARGET_LOCATION_IN_KM);

        assertThat(customers).isEmpty();
        verifyZeroInteractions(calculator);
    }

    @Test
    public void givenLocationAndZeroDistanceFromLocationAndCustomersWhenInvitedThenAllCustomers() {

        Location toLocation = Location.builder().build();

        Set<Customer> foundCustomers = Set.of(withInRangeCustomer, outOfRangeCustomer);

        when(customerRepository.findAll()).thenReturn(foundCustomers);
        when(calculator.from(withInRangeLocation, toLocation)).thenReturn(withInRangeDistance);
        when(calculator.from(outOfRangeLocation, toLocation)).thenReturn(outOfRangeDistance);

        List<Customer> customers = invitation.inviteCustomers(toLocation, BigDecimal.ZERO);

        assertThat(customers).hasSize(2);
        assertThat(customers).contains(withInRangeCustomer, outOfRangeCustomer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenLocationAndNegativeDistanceWhenInvitedThenException() {
        invitation.inviteCustomers(Location.builder().build(), TARGET_LOCATION_IN_KM.negate());
    }
}