package com.movie.domain;

import java.time.Year;
import jakarta.persistence.Converter;
import jakarta.persistence.AttributeConverter;

@Converter(autoApply = true)
public class YearAttributeConverter implements AttributeConverter<Year, Short> {
    @Override
    public Short convertToDatabaseColumn(Year year) {
        return (year != null) ? (short) year.getValue() : null;
    }

    @Override
    public Year convertToEntityAttribute(Short dbData) {
        return (dbData != null) ? Year.of(dbData) : null;
    }
}
