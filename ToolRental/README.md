# ToolRental App
## Building App
1. Run `./gradlew build` from the ToolRental directory
2. Runnable fat jar should be created in the `build/libs` directory

## Running App
Running with no arguments will give you the usage 
```
java -jar ToolRental-1.0-0.jar
usage: ToolRentalService
 -c,--checkoutDate <arg>         Date in the MM/DD/YY format. (eg.
                                 07/20/23)
 -d,--discountPercentage <arg>   Percentage discounted. Any number from
                                 0-100 (eg. 20 = 20% discount)
 -r,--rentalDayCount <arg>       Number of days for rental. Any number
                                 from 1-100
 -t,--toolCode <arg>             Unique identifier for a tool (eg. 'CHNS'
                                 or 'LADW')
```
Here's an example of us running with arguments
```
java -jar ToolRental-1.0-0.jar -t "LADW" -c "07/02/20" -r 3 -d 10
Rental Agreement
Tool code: LADW
Tool type: Ladder
Tool brand: Werner
Rental days: 3
Checkout date: 07/02/20
Due date: 09/02/20
Rental day charge: $1.99
Charge days: 3
Pre discount charge: 5.97
Discount percent: 10%
Discount amount: $0.60
Final charge: $5.37
```