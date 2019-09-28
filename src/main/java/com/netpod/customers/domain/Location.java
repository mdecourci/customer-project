package com.netpod.customers.domain;

import lombok.Builder;
import lombok.Value;

@Builder(builderClassName = "Builder")
@Value
public class Location {
    private String latitude;
    private String longitude;
}
