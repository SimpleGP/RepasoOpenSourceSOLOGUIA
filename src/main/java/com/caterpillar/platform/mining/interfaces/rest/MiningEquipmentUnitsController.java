package com.caterpillar.platform.mining.interfaces.rest;

import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.platform.mining.domain.services.MiningEquipmentUnitCommandService;
import com.caterpillar.platform.mining.interfaces.rest.resources.CreateMiningEquipmentUnitResource;
import com.caterpillar.platform.mining.interfaces.rest.resources.MiningEquipmentUnitResource;
import com.caterpillar.platform.mining.interfaces.rest.transform.CreateMiningEquipmentUnitCommandFromResourceAssembler;
import com.caterpillar.platform.mining.interfaces.rest.transform.MiningEquipmentUnitResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the equipment unit registration operation.
 */
@RestController
@RequestMapping(value = "/api/v1/equipment-units", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Mining Equipment Units", description = "Operations for registering mining equipment units")
public class MiningEquipmentUnitsController {

    private final MiningEquipmentUnitCommandService commandService;

    public MiningEquipmentUnitsController(MiningEquipmentUnitCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Registers a mining equipment unit.
     *
     * @param resource validated registration data
     * @return the created resource with generated identifiers
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a mining equipment unit", description = "Creates and persists a mining equipment unit.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipment unit created"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "409", description = "Serial number already registered at the mine site")
    })
    public ResponseEntity<MiningEquipmentUnitResource> createEquipmentUnit(
            @Valid @RequestBody CreateMiningEquipmentUnitResource resource) {
        var command = CreateMiningEquipmentUnitCommandFromResourceAssembler.toCommandFromResource(resource);
        MiningEquipmentUnit equipmentUnit = commandService.handle(command);
        var response = MiningEquipmentUnitResourceFromEntityAssembler.toResourceFromEntity(equipmentUnit);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
