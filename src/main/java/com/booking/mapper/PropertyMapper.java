package com.booking.mapper;


import com.booking.dto.PropertyDto;
import com.booking.entities.Property;

public class PropertyMapper {

    public static PropertyDto toDto(Property property) {
        if (property == null) return null;
        return new PropertyDto(
                property.getId(),
                property.getName(),
                property.getCity(),
                property.getCountry()
        );
    }
}

