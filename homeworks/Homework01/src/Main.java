import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.Location;
import bg.sofia.uni.fmi.mjt.itinerary.RideRight;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.math.BigDecimal;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.BUS;
import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.PLANE;
import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.TRAIN;

public class Main {

    static void testOne(City varna, City kardzhali, RideRight rideRight)
        throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test One -----------");

        for (var journey : rideRight.findCheapestPath(varna, kardzhali, true)) {
            System.out.println(journey + "\n");
        }
    }

    static void testTwo(City sofia, City varna, RideRight rideRight)
        throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test Two -----------");

        for (var journey : rideRight.findCheapestPath(sofia, varna, true)) {
            System.out.println(journey + "\n");
        }
    }

    static void testThree() throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test Three -----------");

        City sofia = new City("Sofia", new Location(0, 2000));
        City burgas = new City("Burgas", new Location(0, 6000));

        List<Journey> schedule = List.of(
            new Journey(BUS, sofia, burgas, new BigDecimal("20")),
            new Journey(TRAIN, sofia, burgas, new BigDecimal("20")),
            new Journey(PLANE, sofia, burgas, new BigDecimal("20"))
        );

        RideRight rideRight = new RideRight(schedule);

        for (var journey : rideRight.findCheapestPath(sofia, burgas, true)) {
            System.out.println(journey + "\n");
        }
    }

    static void testFour() throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test Four -----------");

        City sofia = new City("Sofia", new Location(0, 2000));
        City burgas = new City("Burgas", new Location(0, 6000));
        City tarnovo = new City("Tarnovo", new Location(10000000, 1000000));

        List<Journey> schedule = List.of(
            new Journey(BUS, sofia, burgas, new BigDecimal("1000")),
            new Journey(TRAIN, sofia, tarnovo, new BigDecimal("1")),
            new Journey(TRAIN, tarnovo, burgas, new BigDecimal("1"))
        );

        RideRight rideRight = new RideRight(schedule);

        for (var journey : rideRight.findCheapestPath(sofia, burgas, true)) {
            System.out.println(journey + "\n");
        }
    }

    static void testFive() throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test Five -----------");

        City sofia = new City("Sofia", new Location(0, 2000));
        City plovdiv = new City("BPlovdiv", new Location(1000, 0));
        City burgas = new City("Burgas", new Location(0, 6000));
        City ruse = new City("ARuse", new Location(-1000, 0));

        List<Journey> schedule = List.of(
            new Journey(BUS, ruse, burgas, new BigDecimal("20")),
            new Journey(BUS, sofia, ruse, new BigDecimal("20")),
            new Journey(BUS, sofia, plovdiv, new BigDecimal("20")),
            new Journey(BUS, plovdiv, burgas, new BigDecimal("20"))
        );

        RideRight rideRight = new RideRight(schedule);

        for (var journey : rideRight.findCheapestPath(sofia, burgas, true)) {
            System.out.println(journey + "\n");
        }
    }

    static void testSix() throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test Six -----------");

        City sofia = new City("Sofia", new Location(0, 2000));
        City burgas = new City("Burgas", new Location(0, 6000));
        City ruse = new City("ARuse", new Location(-1000, 0));

        List<Journey> schedule = List.of(
            new Journey(BUS, ruse, burgas, new BigDecimal("20"))
        );

        RideRight rideRight = new RideRight(schedule);
        try {
            for (var journey : rideRight.findCheapestPath(sofia, burgas, true)) {
                System.out.println(journey + "\n");
            }
        } catch (CityNotKnownException e) {
            System.out.println("Threw a CityNotKnowException => correct");
        }
    }
    static void testSeven() throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test Seven -----------");

        City sofia = new City("Sofia", new Location(0, 2000));
        City burgas = new City("Burgas", new Location(0, 6000));
        City ruse = new City("ARuse", new Location(-1000, 0));

        List<Journey> schedule = List.of(
            new Journey(BUS, ruse, burgas, new BigDecimal("20")),
            new Journey(BUS, ruse, sofia, new BigDecimal("20"))
        );

        RideRight rideRight = new RideRight(schedule);
        try {
            for (var journey : rideRight.findCheapestPath(sofia, burgas, true)) {
                System.out.println(journey + "\n");
            }
        } catch (NoPathToDestinationException e) {
            System.out.println("Threw a NoPathToDestinationException => correct");
        }
    }

    static void test8(City varna, City kardzhali, RideRight rideRight)
        throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test 8 -----------");
        try {
            rideRight.findCheapestPath(varna, kardzhali, false);
        } catch (NoPathToDestinationException e) {
            System.out.println("Threw a NoPathToDestinationException => correct");
        }
    }

    static void test9(City varna, City burgas, RideRight rideRight)
        throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test 9 -----------");

        for (Journey journey : rideRight.findCheapestPath(varna, burgas, false)) {
            System.out.println(journey + "\n");
        }
    }

    static void test10() throws CityNotKnownException, NoPathToDestinationException {
        System.out.println("----------- Test 10 -----------");

        City sofia = new City("Sofia", new Location(0, 2000));
        City plovdiv = new City("Plovdiv", new Location(0, 6000));
        City burgas = new City("Burgas", new Location(0, 4000));

        List<Journey> schedule = List.of(
            new Journey(TRAIN, sofia, burgas, new BigDecimal("20")),
            new Journey(TRAIN, burgas, plovdiv, new BigDecimal("20")),
            new Journey(TRAIN, sofia, plovdiv, new BigDecimal("40"))
        );

        RideRight rideRight = new RideRight(schedule);

        for (Journey journey : rideRight.findCheapestPath(sofia, plovdiv, true)) {
            System.out.println(journey + "\n");
        }
    }

    public static void main(String[] args) throws CityNotKnownException, NoPathToDestinationException {
        City sofia = new City("Sofia", new Location(0, 2000));
        City plovdiv = new City("Plovdiv", new Location(4000, 1000));
        City varna = new City("Varna", new Location(9000, 3000));
        City burgas = new City("Burgas", new Location(9000, 1000));
        City ruse = new City("Ruse", new Location(7000, 4000));
        City blagoevgrad = new City("Blagoevgrad", new Location(0, 1000));
        City kardzhali = new City("Kardzhali", new Location(3000, 0));
        City tarnovo = new City("Tarnovo", new Location(5000, 3000));

        List<Journey> schedule = List.of(
            new Journey(BUS, sofia, blagoevgrad, new BigDecimal("20")),
            new Journey(BUS, blagoevgrad, sofia, new BigDecimal("20")),
            new Journey(BUS, sofia, plovdiv, new BigDecimal("90")),
            new Journey(BUS, plovdiv, sofia, new BigDecimal("90")),
            new Journey(BUS, plovdiv, kardzhali, new BigDecimal("50")),
            new Journey(BUS, kardzhali, plovdiv, new BigDecimal("50")),
            new Journey(BUS, plovdiv, burgas, new BigDecimal("90")),
            new Journey(BUS, burgas, plovdiv, new BigDecimal("90")),
            new Journey(BUS, burgas, varna, new BigDecimal("60")),
            new Journey(BUS, varna, burgas, new BigDecimal("60")),
            new Journey(BUS, sofia, tarnovo, new BigDecimal("150")),
            new Journey(BUS, tarnovo, sofia, new BigDecimal("150")),
            new Journey(BUS, plovdiv, tarnovo, new BigDecimal("40")),
            new Journey(BUS, tarnovo, plovdiv, new BigDecimal("40")),
            new Journey(BUS, tarnovo, ruse, new BigDecimal("70")),
            new Journey(BUS, ruse, tarnovo, new BigDecimal("70")),
            new Journey(BUS, varna, ruse, new BigDecimal("70")),
            new Journey(BUS, ruse, varna, new BigDecimal("70")),
            new Journey(PLANE, varna, burgas, new BigDecimal("200")),
            new Journey(PLANE, burgas, varna, new BigDecimal("200")),
            new Journey(PLANE, burgas, sofia, new BigDecimal("150")),
            new Journey(PLANE, sofia, burgas, new BigDecimal("250")),
            new Journey(PLANE, varna, sofia, new BigDecimal("290")),
            new Journey(PLANE, sofia, varna, new BigDecimal("300"))
        );

        RideRight rideRight = new RideRight(schedule);

        testOne(varna, kardzhali, rideRight);
        testTwo(sofia, varna, rideRight);

        testThree();
        testFour();
        testFive();

//        exceptions
        testSix();
        testSeven();
        test8(varna, kardzhali, rideRight);

        test9(varna, burgas, rideRight);

        test10();
    }
}