package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.constants.Constants;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("Values of utilityType or billable shouldn't be null!");
        }

        double utilityCost = switch (utilityType) {
            case WATER -> billable.getWaterConsumption();
            case ELECTRICITY -> billable.getElectricityConsumption();
            case NATURAL_GAS -> billable.getNaturalGasConsumption();
        };

        return Constants.roundUpNumber(utilityCost);
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("Value of billable shouldn't be null!");
        }

        double totalCost = billable.getWaterConsumption() + billable.getElectricityConsumption() +
            billable.getNaturalGasConsumption();

        return Constants.roundUpNumber(totalCost);
    }

    private <T extends Billable> void fillCostDifferences(Map<UtilityType, Double> costDifferences, T firstBillable, T secondBillable) {
        if (costDifferences == null) {
            throw new IllegalArgumentException("Value of costDifferences shouldn't be null!");
        }

        costDifferences.put(UtilityType.WATER,
            Constants.roundUpNumber(
                Math.abs(
                    firstBillable.getWaterConsumption() - secondBillable.getWaterConsumption()
                )
            )
        );

        costDifferences.put(UtilityType.ELECTRICITY,
            Constants.roundUpNumber(
                Math.abs(
                    firstBillable.getElectricityConsumption() - secondBillable.getElectricityConsumption()
                )
            )
        );

        costDifferences.put(UtilityType.NATURAL_GAS,
            Constants.roundUpNumber(
                Math.abs(
                    firstBillable.getNaturalGasConsumption() - secondBillable.getNaturalGasConsumption()
                )
            )
        );
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("Values of firstBillable and secondBillable shouldn't be null!");
        }

        Map<UtilityType, Double> costDifferences = new HashMap<>();

        fillCostDifferences(costDifferences, firstBillable, secondBillable);

        return Collections.unmodifiableMap(costDifferences);
    }

}
