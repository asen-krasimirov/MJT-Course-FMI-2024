package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {

    final List<Journey> schedule;
    final HashMap<City, CityNode> cityNodes;

    public RideRight(List<Journey> schedule) {
        this.schedule = List.copyOf(schedule);
        this.cityNodes = new HashMap<>();

        for (var journey : schedule) {
            if (!cityNodes.containsKey(journey.from())) {
                cityNodes.put(journey.from(), new CityNode(journey.from()));
            }

            if (!cityNodes.containsKey(journey.to())) {
                cityNodes.put(journey.to(), new CityNode(journey.to()));
            }

            cityNodes.get(journey.from()).addBranch(journey, cityNodes.get(journey.to()));
        }
    }

    private void setUpNodes() {
        cityNodes.forEach(
            (City city, CityNode cityNode) -> cityNode.setUpNode()
        );
    }

    private void iterateThroughNeighbours(PriorityQueue<CityNode> closedList, PriorityQueue<CityNode> openList,
                                          CityNode targetNode, CityNode n) {
        for (CityNode.Edge edge : n.getNeighbors()) {
            CityNode m = edge.node;
            BigDecimal totalWeight = n.g.add(edge.weight);

            if (!openList.contains(m) && !closedList.contains(m)) {
                m.setParent(n);
                m.setJourneyTaken(edge.journey);

                m.g = totalWeight;
                m.f = m.g.add(m.calculateHeuristic(targetNode));
                openList.add(m);
            } else {
                if (totalWeight.compareTo(m.g) < 0 || (totalWeight.compareTo(m.g) == 0 &&
                    edge.journey.from().name().compareTo(m.getJourneyTaken().from().name()) < 0)) {
                    m.setParent(n);
                    m.setJourneyTaken(edge.journey);

                    m.g = totalWeight;
                    m.f = m.g.add(m.calculateHeuristic(targetNode));

                    if (closedList.contains(m)) {
                        closedList.remove(m);
                        openList.add(m);
                    }
                }
            }
        }
    }

    private CityNode runAStartAlgorithm(PriorityQueue<CityNode> closedList, PriorityQueue<CityNode> openList,
                                        CityNode targetNode) {
        while (!openList.isEmpty()) {
            CityNode n = openList.peek();

            if (n == targetNode) {
                return n;
            }

            iterateThroughNeighbours(closedList, openList, targetNode, n);

            openList.remove(n);
            closedList.add(n);
        }

        return null;
    }

    private CityNode findCheapestPathWithTransfers(City start, City destination) {
        setUpNodes();

        PriorityQueue<CityNode> closedList = new PriorityQueue<>();
        PriorityQueue<CityNode> openList = new PriorityQueue<>();

        CityNode startNode = cityNodes.get(start);
        CityNode targetNode = cityNodes.get(destination);

        startNode.f = startNode.g.add(startNode.calculateHeuristic(targetNode));
        openList.add(startNode);

        return runAStartAlgorithm(closedList, openList, targetNode);
    }

    private SequencedCollection<Journey> getCheapestPathWithTransfersData(City start, City destination)
        throws NoPathToDestinationException {
        CityNode currentNode = findCheapestPathWithTransfers(start, destination);

        if (currentNode == null) {
            throw new NoPathToDestinationException("Path does not exist to destination!");
        }

        List<Journey> journeysTaken = new ArrayList<>();

        while (currentNode.getParent() != null) {
            journeysTaken.add(currentNode.getJourneyTaken());
            currentNode = currentNode.getParent();
        }

        Collections.reverse(journeysTaken);

        return journeysTaken;
    }

    private SequencedCollection<Journey> findCheapestPathWithoutTransfers(City start, City destination) {
        for (Journey journey : schedule) {
            if (journey.from().equals(start) && journey.to().equals(destination)) {
                return new ArrayList<>(List.of(journey));
            }
        }

        return null;
    }

    private SequencedCollection<Journey> getCheapestPathWithoutTransfersData(City start, City destination)
        throws NoPathToDestinationException {
        SequencedCollection<Journey> result = findCheapestPathWithoutTransfers(start, destination);
        if (result != null) {
            return result;
        }

        throw new NoPathToDestinationException("Path does not exist to destination!");
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        if (!cityNodes.containsKey(start)) {
            throw new CityNotKnownException("Start city not found!");
        }

        if (!cityNodes.containsKey(destination)) {
            throw new CityNotKnownException("Destination city not found!");

        }

        if (!allowTransfer) {
            return getCheapestPathWithoutTransfersData(start, destination);
        } else {
            return getCheapestPathWithTransfersData(start, destination);
        }
    }

}
