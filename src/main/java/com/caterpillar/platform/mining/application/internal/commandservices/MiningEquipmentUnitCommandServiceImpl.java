package com.caterpillar.platform.mining.application.internal.commandservices;

import com.caterpillar.platform.mining.domain.exceptions.DuplicateMiningEquipmentUnitException;
import com.caterpillar.platform.mining.domain.model.aggregates.MiningEquipmentUnit;
import com.caterpillar.platform.mining.domain.model.commands.CreateMiningEquipmentUnitCommand;
import com.caterpillar.platform.mining.domain.repositories.MiningEquipmentUnitRepository;
import com.caterpillar.platform.mining.domain.services.MiningEquipmentUnitCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles equipment registration commands and applies the mining business rules.
 */
@Service
public class MiningEquipmentUnitCommandServiceImpl implements MiningEquipmentUnitCommandService {

    private final MiningEquipmentUnitRepository equipmentUnitRepository;

    public MiningEquipmentUnitCommandServiceImpl(MiningEquipmentUnitRepository equipmentUnitRepository) {
        this.equipmentUnitRepository = equipmentUnitRepository;
    }

    @Override
    @Transactional
    public MiningEquipmentUnit handle(CreateMiningEquipmentUnitCommand command) {
        MiningEquipmentUnit equipmentUnit = new MiningEquipmentUnit(command);
        boolean duplicate = equipmentUnitRepository.existsByAssignedMineSiteAndSerialNumberFingerprint(
                equipmentUnit.getAssignedMineSite(), equipmentUnit.getSerialNumberFingerprint());
        if (duplicate) {
            throw new DuplicateMiningEquipmentUnitException();
        }
        return equipmentUnitRepository.save(equipmentUnit);
    }
}
