package com.netpod.customers.calculator;

import com.netpod.customers.domain.Location;

import java.math.BigDecimal;

public interface Calculator {
    BigDecimal from(Location fromLocation, Location toLocation);
}
