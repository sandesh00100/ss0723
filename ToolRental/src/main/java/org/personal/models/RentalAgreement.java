package org.personal.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.personal.ToolRentalService;
import org.personal.enums.Tool;

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

    @Override
    public String toString() {
        String newLine = System.getProperty("line.separator");
        Tool tool = checkOut.getTool();
        return "Tool code: " + tool.name() + newLine +
                "Tool type: " + tool.getType().getName() + newLine +
                "Tool brand: " + tool.getBrand() + newLine +
                "Rental days: " + checkOut.getRentalDayCount() + newLine +
                "Checkout date: " + checkOut.getCheckOutDate().format(ToolRentalService.dateFormatter) + newLine +
                "Due date: " + dueDate.format(ToolRentalService.dateFormatter) + newLine +
                "Rental day charge: $" + tool.getType().getDailyCharge() + newLine +
                "Charge days: " + chargeDays + newLine +
                "Pre discount charge: " + getPreDiscountCharge() + newLine +
                "Discount percent: " + checkOut.getDiscountPercentage() + "%" + newLine +
                "Discount amount: $" + getDiscountAmount() + newLine +
                "Final charge: $" + getFinalCharge() + newLine;
    }
}
