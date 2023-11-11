package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    private final int buildableArea;
    private int takenArea;
    private final Map<String, E> buildables;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        this.takenArea = 0;
        this.buildables = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Value of address shouldn't be null or blank!");
        }

        if (buildable == null) {
            throw new IllegalArgumentException("Value of buildable shouldn't be null!");
        }

        if (buildables.containsKey(address)) {
            throw new BuildableAlreadyExistsException("Address is already occupied on the plot!");
        }

        if (buildable.getArea() > getRemainingBuildableArea()) {
            throw new InsufficientPlotAreaException("The required area exceeds the remaining plot area!");
        }

        buildables.put(address, buildable);
        takenArea += buildable.getArea();
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Value of buildables shouldn't be null or empty!");
        }

        int totalAreaNeeded = 0;

        Set<Entry<String, E>> entries = buildables.entrySet();

        for (Entry<String, E> entry : entries) {
            if (buildables.containsKey(entry.getKey())) {
                throw new BuildableAlreadyExistsException("Address is already occupied on the plot!");
            }

            totalAreaNeeded += entry.getValue().getArea();
        }

        if (totalAreaNeeded > getRemainingBuildableArea()) {
            throw new InsufficientPlotAreaException("The required area exceeds the remaining plot area!");
        }

        this.buildables.putAll(buildables);
        takenArea += totalAreaNeeded;
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Value of address shouldn't be null or blank!");
        }

        if (!buildables.containsKey(address)) {
            throw new BuildableNotFoundException("Address not registered on plot!");
        }

        takenArea -= buildables.get(address).getArea();
        buildables.remove(address);
    }

    @Override
    public void demolishAll() {
        takenArea = 0;
        buildables.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Collections.unmodifiableMap(buildables);
    }

    @Override
    public int getRemainingBuildableArea() {
        return buildableArea - takenArea;
    }
}
