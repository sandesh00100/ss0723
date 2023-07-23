package org.personal.models;

import lombok.Builder;
import lombok.Value;
import org.personal.enums.Tool;

import java.time.LocalDate;

@Value
@Builder
public class CheckOut {
    private Tool tool;
    private int rentalDayCount;
    private int discountPercentage;
    private LocalDate checkOutDate;
}
