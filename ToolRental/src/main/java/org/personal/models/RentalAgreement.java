package org.personal.models;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Value
@Builder
public class RentalAgreement {
    private CheckOut checkOut;
    private LocalDate dueDate;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public BigDecimal getPreDiscountCharge(){
        return preDiscountCharge.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getDiscountAmount(){
        return discountAmount.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getFinalCharge(){
        return finalCharge.setScale(2, RoundingMode.HALF_UP);
    }
}
