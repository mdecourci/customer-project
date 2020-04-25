package com.netpod.customers.calculator;

import com.netpod.customers.domain.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
public class GreatCircleDistanceCalculator implements Calculator {

    public static final BigDecimal EARTH_RADIUS = BigDecimal.valueOf(6378.80).multiply(BigDecimal.valueOf(1000));
    public static final BigDecimal RADIANS_UNIT =
            BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(180,0), 6, RoundingMode.HALF_UP);

    @Override
    public BigDecimal from(Location fromLocation, Location toLocation) {
        // Equation gcd = 6,378.8 * arccos[sin(lat1) *  sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)] kilometers
        BigDecimal latitude1 = new BigDecimal(fromLocation.getLatitude()).multiply(RADIANS_UNIT);
        BigDecimal latitude2 = new BigDecimal(toLocation.getLatitude()).multiply(RADIANS_UNIT);

        BigDecimal longitude1 = new BigDecimal(fromLocation.getLongitude()).multiply(RADIANS_UNIT).abs();
        BigDecimal longitude2 = new BigDecimal(toLocation.getLongitude()).multiply(RADIANS_UNIT).abs();
        BigDecimal longitudeDelta = longitude2.subtract(longitude1);

        BigDecimal sinMultiple = BigDecimal.valueOf(Math.sin(latitude1.doubleValue())).multiply(BigDecimal.valueOf(Math.sin(latitude2.doubleValue())));
        BigDecimal cosineMultiple = BigDecimal.valueOf(Math.cos(latitude1.doubleValue()))
                .multiply(BigDecimal.valueOf(Math.cos(latitude2.doubleValue())))
                .multiply(BigDecimal.valueOf(Math.cos(longitudeDelta.doubleValue())));

        BigDecimal gcd = EARTH_RADIUS.multiply(BigDecimal.valueOf(
                Math.acos(sinMultiple.add(cosineMultiple).doubleValue())));

        return gcd;
    }
}
