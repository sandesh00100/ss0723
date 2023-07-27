import org.junit.jupiter.api.Test;
import org.demo.ToolRentalService;
import org.demo.enums.Tool;
import org.demo.exceptions.CheckoutValidationException;
import org.demo.models.CheckOut;
import org.demo.models.RentalAgreement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToolRentalUtilityTest {
   ToolRentalService toolRentalService = new ToolRentalService();

   @Test
    public void rentalDaysUnderOne(){
        assertThrows(CheckoutValidationException.class, () -> {
            CheckOut checkout = CheckOut.builder()
                    .tool(Tool.JAKR)
                    .checkOutDate(LocalDate.parse("2015-09-03"))
                    .rentalDayCount(0)
                    .discountPercentage(101)
                    .build();
            toolRentalService.calculateRentalAgreement(checkout);
        });
    }

    // Test 1
   @Test
    public void discountOverHundred(){
       assertThrows(CheckoutValidationException.class, () -> {
           CheckOut checkout = CheckOut.builder()
                   .tool(Tool.JAKR)
                   .checkOutDate(LocalDate.parse("2015-09-03"))
                   .rentalDayCount(5)
                   .discountPercentage(101)
                   .build();
           toolRentalService.calculateRentalAgreement(checkout);
       });
   }

   // Test 2
    @Test
    public void discountFreeIndependenceDay() throws CheckoutValidationException {
        CheckOut checkout = CheckOut.builder()
                .tool(Tool.LADW)
                .checkOutDate(LocalDate.parse("2020-07-02"))
                .rentalDayCount(3)
                .discountPercentage(10)
                .build();
        RentalAgreement rentalAgreement = toolRentalService.calculateRentalAgreement(checkout);

        assertEquals(2, rentalAgreement.getChargeDays());

        assertEquals(BigDecimal.valueOf(3.98), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(3.58), rentalAgreement.getFinalCharge());
    }

    // Test 3
    @Test
    public void discountChargeIndependenceDay() throws CheckoutValidationException {
        CheckOut checkout = CheckOut.builder()
                .tool(Tool.CHNS)
                .checkOutDate(LocalDate.parse("2015-07-02"))
                .rentalDayCount(5)
                .discountPercentage(25)
                .build();
        RentalAgreement rentalAgreement = toolRentalService.calculateRentalAgreement(checkout);

        assertEquals(3, rentalAgreement.getChargeDays());

        assertEquals(BigDecimal.valueOf(4.47), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(3.35), rentalAgreement.getFinalCharge());
    }


    // Test 4
    @Test
    public void discountChargeFreeLaborDay() throws CheckoutValidationException {
        CheckOut checkout = CheckOut.builder()
                .tool(Tool.JAKD)
                .checkOutDate(LocalDate.parse("2015-09-03"))
                .rentalDayCount(6)
                .discountPercentage(0)
                .build();
        RentalAgreement rentalAgreement = toolRentalService.calculateRentalAgreement(checkout);

        assertEquals(3, rentalAgreement.getChargeDays());

        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getFinalCharge());
    }

    // Test 4
    @Test
    public void discountFreeIndependenceDayLong() throws CheckoutValidationException {
        CheckOut checkout = CheckOut.builder()
                .tool(Tool.JAKR)
                .checkOutDate(LocalDate.parse("2015-07-02"))
                .rentalDayCount(9)
                .discountPercentage(0)
                .build();
        RentalAgreement rentalAgreement = toolRentalService.calculateRentalAgreement(checkout);

        assertEquals(6, rentalAgreement.getChargeDays());

        assertEquals(BigDecimal.valueOf(17.94), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(17.94), rentalAgreement.getFinalCharge());
    }

    // Test 6
    @Test
    public void discountFreeIndependenceDayShort() throws CheckoutValidationException {
        CheckOut checkout = CheckOut.builder()
                .tool(Tool.JAKR)
                .checkOutDate(LocalDate.parse("2020-07-02"))
                .rentalDayCount(4)
                .discountPercentage(50)
                .build();
        RentalAgreement rentalAgreement = toolRentalService.calculateRentalAgreement(checkout);

        assertEquals(1, rentalAgreement.getChargeDays());

        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), rentalAgreement.getFinalCharge());
    }

}
