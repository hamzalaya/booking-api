package com.booking.mapper;


import com.booking.dto.BlockDto;
import com.booking.entities.Block;
import com.booking.entities.Property;

public class BlockMapper {

    // Convert DTO to Entity
    public static Block toEntity(BlockDto dto, Property property) {
        if (dto == null) return null;
        Block block = new Block();
        block.setProperty(property);
        block.setStartDate(dto.getStartDate());
        block.setEndDate(dto.getEndDate());
        block.setStatus(dto.getStatus() != null ? dto.getStatus() : block.getStatus());

        return block;
    }

    // Convert Entity to DTO
    public static BlockDto toDto(Block block) {
        if (block == null) return null;
        BlockDto dto = new BlockDto();
        dto.setId(block.getId());
        dto.setPropertyId(block.getProperty() != null ? block.getProperty().getId() : null);
        dto.setStartDate(block.getStartDate());
        dto.setEndDate(block.getEndDate());
        dto.setStatus(block.getStatus());

        return dto;
    }
}
