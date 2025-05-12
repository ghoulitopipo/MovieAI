package fr.univtln.laure.service;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;

@Converter(autoApply = true)
public class DateConverter implements AttributeConverter<LocalDate, Long> {
    @Override
    public Long convertToDatabaseColumn(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public LocalDate convertToEntityAttribute(Long epochMilli) {
        if (epochMilli == null) {
            return null;
        }
        return Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}