package bg.sofia.uni.fmi.mjt.itinerary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.itinerary.utils.FixedConstants.PRICE_PER_KM;
import static bg.sofia.uni.fmi.mjt.itinerary.utils.FixedConstants.TO_METERS_CONST;
import static bg.sofia.uni.fmi.mjt.itinerary.utils.DistanceCalculator.calculateManhattanDistance;

public class CityNode implements Comparable<CityNode> {

    private City city = null;
    private CityNode parent = null;
    private Journey journeyTaken = null;
    private final List<Edge> neighbors;

    public BigDecimal f = new BigDecimal("0");
    public BigDecimal g = new BigDecimal("0");

    CityNode(City city) {
        this.city = city;
        this.neighbors = new ArrayList<>();
    }

    public CityNode getParent() {
        return parent;
    }

    public void setParent(CityNode parent) {
        this.parent = parent;
    }

    public Journey getJourneyTaken() {
        return journeyTaken;
    }

    public void setJourneyTaken(Journey journey) {
        this.journeyTaken = journey;
    }

    public void setUpNode() {
        this.parent = null;
        this.journeyTaken = null;
    }

    public List<Edge> getNeighbors() {
        return neighbors;
    }

    @Override
    public int compareTo(CityNode n) {
        return this.f.compareTo(n.f);
    }

    public void addBranch(Journey journey, CityNode cityNode) {
        BigDecimal weight = journey.taxedPrice()
            .add(journey.getDistance()
                .multiply(PRICE_PER_KM)
                .divide(TO_METERS_CONST)
            );
        Edge newEdge = new Edge(weight, cityNode, journey);
        neighbors.add(newEdge);
    }

    public BigDecimal calculateHeuristic(CityNode target) {
        return calculateManhattanDistance(city.location(), target.city.location())
            .multiply(PRICE_PER_KM)
            .divide(TO_METERS_CONST);
    }

    public static class Edge {
        public BigDecimal weight;
        public CityNode node;
        public Journey journey;

        Edge(BigDecimal weight, CityNode node, Journey journey) {
            this.weight = weight;
            this.node = node;
            this.journey = journey;
        }
    }

}
