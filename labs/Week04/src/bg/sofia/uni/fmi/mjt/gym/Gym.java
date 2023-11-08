package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;

public class Gym implements GymAPI {

    private final int capacity;
    private final Address address;
    private final SortedSet<GymMember> members;

    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        this.members = new TreeSet<>();
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> collectionToReturn = new TreeSet<>(new GymProximityComparator(address));

        collectionToReturn.addAll(members);

        return Collections.unmodifiableSortedSet(collectionToReturn);
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Value of member shouldn't be null!");
        }

        if (members.size() + 1 > capacity) {
            throw new GymCapacityExceededException("Gym capacity is at full!");
        }

        members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Value of member shouldn't be null or empty!");
        }

        if (this.members.size() + members.size() > capacity) {
            throw new GymCapacityExceededException("Gym capacity is at full!");
        }

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Value of member shouldn't be null!");
        }

        return members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException("Value of day shouldn't be null!");
        }
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Value of exerciseName shouldn't be null or empty!");
        }

        for (GymMember member : members) {
            for (Exercise exercise : member.getTrainingProgram().get(day).exercises()) {
                if (exercise.name().compareTo(exerciseName) == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private Map<DayOfWeek, List<String>> instantiateDataForAllDays() {
        Map<DayOfWeek, List<String>> dataForAllDays = HashMap.newHashMap(DayOfWeek.values().length);

        for (DayOfWeek day : DayOfWeek.values()) {
            dataForAllDays.put(day, new ArrayList<>());
        }

        return dataForAllDays;
    }

    private Map<DayOfWeek, List<String>> filterDataForAllDays(Map<DayOfWeek, List<String>> dataForAllDays) {
        Map<DayOfWeek, List<String>> reportToReturn = new LinkedHashMap<>();

        for (Map.Entry<DayOfWeek, List<String>> entry : dataForAllDays.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                reportToReturn.put(entry.getKey(), entry.getValue());
            }
        }

        return reportToReturn;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Value of exerciseName shouldn't be null or empty!");
        }

        Map<DayOfWeek, List<String>> dataForAllDays = instantiateDataForAllDays();

        for (GymMember member : members) {
            Set<Map.Entry<DayOfWeek, Workout>> entries = member.getTrainingProgram().entrySet();

            for (Map.Entry<DayOfWeek, Workout> entry : entries) {

                for (Exercise exercise : entry.getValue().exercises()) {

                    if (exercise.name().compareTo(exerciseName) == 0) {
                        dataForAllDays.get(entry.getKey()).add(member.getName());
                        break;
                    }

                }

            }
        }

        Map<DayOfWeek, List<String>> reportToReturn = filterDataForAllDays(dataForAllDays);

        return Collections.unmodifiableMap(reportToReturn);
    }

}
