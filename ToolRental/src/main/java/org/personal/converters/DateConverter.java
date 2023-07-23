package org.personal.converters;

import com.beust.jcommander.IStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter implements IStringConverter<LocalDate>{
    @Override
    public LocalDate convert(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("DD/MM/YY");
        return LocalDate.parse(value, formatter);
    }
}
