package com.netpod.customers.converters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class MappedCustomer {
    private String latitude;
    @JsonProperty("user_id")
    private Integer userId;
    private String name;
    private String longitude;
}
