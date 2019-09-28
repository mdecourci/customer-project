package com.netpod.customers.calculator;

import com.netpod.customers.domain.Location;
import org.assertj.core.data.Percentage;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GreatCircleDistanceCalculatorTest {
    public static final BigDecimal EARTH_RADIUS = BigDecimal.valueOf(6378.80).multiply(BigDecimal.valueOf(1000));
    public static final BigDecimal PI = BigDecimal.valueOf(Math.PI);
    public static final BigDecimal CIRCUMFERENCE = BigDecimal.valueOf(Math.PI).multiply(EARTH_RADIUS);

    private GreatCircleDistanceCalculator greatCircleDistanceCalculator = new GreatCircleDistanceCalculator();

    @Test
    public void givenSameLocationWhenCalculateThenNoDistance() {
        Location from = Location.builder().longitude("50").latitude("10").build();
        Location to = Location.builder().longitude("50").latitude("10").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(BigDecimal.ZERO, Percentage.withPercentage(1));
    }

    @Test
    public void givenReferenceLocationWhenCalculateThenDistance() {
        Location from = Location.builder().longitude("50").latitude("10").build();
        Location to = Location.builder().longitude("60").latitude("20").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(new BigDecimal("1545000"), Percentage.withPercentage(1));
    }

    @Test
    public void givenNorthAndSouthPostLocationWhenCalculateThenDistanceCircumference() {
        Location from = Location.builder().longitude("0").latitude("90").build();
        Location to = Location.builder().longitude("0").latitude("-90").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(CIRCUMFERENCE, Percentage.withPercentage(1));
    }

    @Test
    public void givenNorthAndEquatorPostLocationWhenCalculateThenDistanceRadius() {
        Location from = Location.builder().longitude("0").latitude("0").build();
        Location to = Location.builder().longitude("0").latitude("90").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(CIRCUMFERENCE.divide(BigDecimal.valueOf(2)), Percentage.withPercentage(1));
    }

    @Test
    public void givenSouthAndEquatorPostLocationWhenCalculateThenDistanceRadius() {
        Location from = Location.builder().longitude("0").latitude("0").build();
        Location to = Location.builder().longitude("0").latitude("-90").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(CIRCUMFERENCE.divide(BigDecimal.valueOf(2)), Percentage.withPercentage(1));
    }

    @Test
    public void givenEastAndWestPostLocationWhenCalculateThenDistanceCircumference() {
        Location from = Location.builder().longitude("0").latitude("0").build();
        Location to = Location.builder().longitude("180").latitude("0").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(CIRCUMFERENCE, Percentage.withPercentage(1));
    }

    @Test
    public void givenMeridianAndWestPostLocationWhenCalculateThenDistanceRedius() {
        Location from = Location.builder().longitude("0").latitude("0").build();
        Location to = Location.builder().longitude("90").latitude("0").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(CIRCUMFERENCE.divide(BigDecimal.valueOf(2)), Percentage.withPercentage(1));
    }

    @Test
    public void givenMeridianAndEastPostLocationWhenCalculateThenDistanceRedius() {
        Location from = Location.builder().longitude("0").latitude("0").build();
        Location to = Location.builder().longitude("-90").latitude("-90").build();

        BigDecimal distance = greatCircleDistanceCalculator.from(from, to);
        assertThat(distance).isCloseTo(CIRCUMFERENCE.divide(BigDecimal.valueOf(2)), Percentage.withPercentage(1));
    }
}