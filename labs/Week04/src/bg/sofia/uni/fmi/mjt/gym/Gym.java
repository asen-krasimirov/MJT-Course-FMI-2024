package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;

public class Gym implements GymAPI {

    private int capacity;
    private final Address address;
    private final TreeSet<GymMember> members;

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
        SortedSet<GymMember> collectionToReturn = Collections.unmodifiableSortedSet(members);

        Arrays.sort(collectionToReturn.toArray());

        return collectionToReturn;
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> collectionToReturn = Collections.unmodifiableSortedSet(members);

        GymProximityComparator comparator = new GymProximityComparator(address);

        Arrays.sort(collectionToReturn.toArray(), comparator);

        return collectionToReturn;
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
        capacity++;
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
        capacity += members.size();
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

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Value of exerciseName shouldn't be null or empty!");
        }

        Map<DayOfWeek, List<String>> reportToReturn = HashMap.newHashMap(DayOfWeek.values().length);

        for (GymMember member : members) {
            Set<Map.Entry<DayOfWeek, Workout>> entries = member.getTrainingProgram().entrySet();

            for (Map.Entry<DayOfWeek, Workout> entry : entries) {

                for (Exercise exercise : entry.getValue().exercises()) {

                    if (exercise.name().compareTo(exerciseName) == 0) {
                        reportToReturn.get(entry.getKey()).add(member.getName());
                    }

                }

            }
        }

        return Collections.unmodifiableMap(reportToReturn);
    }

}
