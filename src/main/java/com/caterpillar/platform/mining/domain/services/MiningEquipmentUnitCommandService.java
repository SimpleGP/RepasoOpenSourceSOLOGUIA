package com.caterpillar.platform.mining.domain.services;

import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.platform.mining.domain.model.commands.CreateMiningEquipmentUnitCommand;

/**
 * Defines the command operation available for mining equipment units.
 */
public interface MiningEquipmentUnitCommandService {

    /**
     * Registers an equipment unit after checking business uniqueness rules.
     *
     * @param command creation data
     * @return the persisted aggregate root
     */
    MiningEquipmentUnit handle(CreateMiningEquipmentUnitCommand command);
}
