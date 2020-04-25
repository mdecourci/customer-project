package com.netpod.customers.domain;

import lombok.Builder;
import lombok.Value;

@Builder(builderClassName = "Builder")
@Value
public class Customer {
    private Integer userId;
    private String name;
    private Location location;
}
