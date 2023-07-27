package org.demo.models;

import lombok.Builder;
import lombok.Value;
import org.demo.enums.Tool;

import java.time.LocalDate;

@Value
@Builder
public class CheckOut {
    private Tool tool;
    private int rentalDayCount;
    private int discountPercentage;
    private LocalDate checkOutDate;
}
