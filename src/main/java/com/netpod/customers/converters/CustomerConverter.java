package com.netpod.customers.converters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netpod.customers.domain.Customer;
import com.netpod.customers.domain.Location;
import com.netpod.customers.exception.CustomerConverionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerConverter<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Customer convert(String line) {
        if (line == null || line.length() == 0) {
            throw new IllegalArgumentException();
        }

        try {
            MappedCustomer mappedCustomer =  objectMapper.readValue(line, MappedCustomer.class);
            return Customer.builder()
                    .name(mappedCustomer.getName())
                    .userId(mappedCustomer.getUserId())
                    .location(Location.builder()
                            .longitude(mappedCustomer.getLongitude())
                            .latitude(mappedCustomer.getLatitude())
                            .build())
                    .build();

        } catch (JsonMappingException | JsonParseException e) {
            throw new CustomerConverionException("Error converting JSON", e);
        } catch (IOException e) {
            throw new CustomerConverionException("Error reading JSON", e);
        }
    }
}
