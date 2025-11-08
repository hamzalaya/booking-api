package com.booking.api;


import com.booking.dto.BlockDto;
import com.booking.services.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.booking.holders.ApiPaths.BLOCKS;

@RestController
@RequestMapping(BLOCKS)
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    // Create a new block
    @PostMapping
    public ResponseEntity<BlockDto> createBlock(@RequestBody BlockDto blockDto) {
        BlockDto createdBlock = blockService.createBlock(blockDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlock);

    }

    // Get block by ID
    @GetMapping("/{id}")
    public ResponseEntity<BlockDto> getBlock(@PathVariable Long id) {

        BlockDto blockDto = blockService.findById(id);
        return ResponseEntity.ok(blockDto);

    }

    // Update a block
    @PutMapping("/{id}")
    public ResponseEntity<BlockDto> updateBlock(@PathVariable Long id, @RequestBody BlockDto blockDto) {
        BlockDto updatedBlock = blockService.updateBlock(id, blockDto);
        return ResponseEntity.ok(updatedBlock);
    }

    // Cancel a block
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BlockDto> cancelBlock(@PathVariable Long id) {
        BlockDto canceledBlock = blockService.cancelBlock(id);
        return ResponseEntity.ok(canceledBlock);
    }

    // Delete a block
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        blockService.deleteBlock(id);
        return ResponseEntity.noContent().build();
    }
}
