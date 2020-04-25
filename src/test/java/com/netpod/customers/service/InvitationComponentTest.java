package com.netpod.customers.service;

import com.netpod.customers.domain.Customer;
import com.netpod.customers.domain.Location;
import com.netpod.customers.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class InvitationComponentTest {

    @Value("${location.latitude}")
    private String latitude;

    @Value("${location.longitude}")
    private String longitude;

    @Value("${location.range}")
    private long range;

    @Value("${customer.datafile}")
    private String custerDatafileName;

    @Autowired
    private Invitation invitation;

    @Autowired
    private CustomerRepository customerRepository;

    private int totalNumberOfCustomers;
    private final Integer outOfRangeCustomerWithUserId = 30;
    private final Integer nearInRangeCustomerWithUserId = 23;
    private final Integer nextInRangeCustomerInNextSortOrderUserId = 25;

    @Before
    public void setUp() throws Exception {
        URL url = ClassLoader.getSystemClassLoader().getResource(custerDatafileName);
        totalNumberOfCustomers = (int) Files.lines(Paths.get(url.toURI())).count();
    }

    @Test
    public void givenCustomerDataFileWhenFindInRepositoryThenCustomers() {

        Set<Customer> customers =  customerRepository.findAll();
        assertThat(customers).hasSize(totalNumberOfCustomers);

        assertThat(customers).extracting(Customer::getUserId)
                .containsExactlyInAnyOrder(
                        outOfRangeCustomerWithUserId,
                        nearInRangeCustomerWithUserId,
                        nextInRangeCustomerInNextSortOrderUserId);
    }

    @Test
    public void givenCustomerDataFileAndVenueAndRangeWhenInvitationThenNearCustomers() {

        Location venueLocation = Location.builder().latitude(latitude).longitude(longitude).build();
        List<Customer> customers =  invitation.inviteCustomers(venueLocation, new BigDecimal(range));

        assertThat(customers).hasSize(2);

        Customer nearInRangeCustomer = customers.get(0);
        Customer nextInRangeCustomerInNextSortOrder = customers.get(1);

        assertThat(nearInRangeCustomer).hasFieldOrPropertyWithValue("userId", nearInRangeCustomerWithUserId);
        assertThat(nextInRangeCustomerInNextSortOrder).hasFieldOrPropertyWithValue("userId", nextInRangeCustomerInNextSortOrderUserId);
    }
}