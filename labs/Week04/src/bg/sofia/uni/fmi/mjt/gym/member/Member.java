package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class Member implements GymMember, Comparable<GymMember> {

    private final Address address;
    private final String name;
    private final int age;
    private final String personalIdNumber;
    private final Gender gender;
    private final Map<DayOfWeek, Workout> trainingProgramme;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgramme = new LinkedHashMap<>();
    }

    @Override
    public int compareTo(GymMember other) {
        if (other.getPersonalIdNumber() == null) {
            return 1;
        }

        return this.getName().compareTo(other.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GymMember gymMember)) {
            return false;
        }

        return getPersonalIdNumber().equals(gymMember.getPersonalIdNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonalIdNumber());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Collections.unmodifiableMap(trainingProgramme);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Values of day and workout should not be null!");
        }

        trainingProgramme.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Value of exerciseName shouldn't be null or empty!");
        }

        Set<Entry<DayOfWeek, Workout>> entries = trainingProgramme.entrySet();
        ArrayList<DayOfWeek> validEntries = new ArrayList<>();

        for (Entry<DayOfWeek, Workout> entry : entries) {
            if (entry.getValue().exercises().getLast().name().compareTo(exerciseName) == 0) {
                validEntries.add(entry.getKey());
            }
        }

        return validEntries;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null || exercise == null) {
            throw new IllegalArgumentException("Value of day and exercise shouldn't be null!");
        }

        Workout workout = trainingProgramme.get(day);

        if (workout == null) {
            throw new DayOffException(day + " is considered a day off!");
        }

        workout.exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null || exercises == null) {
            throw new IllegalArgumentException("Value of day and exercise shouldn't be null!");
        } else if (exercises.isEmpty()) {
            throw new IllegalArgumentException("Exercises list shouldn't be empty!");
        }

        Workout workout = trainingProgramme.get(day);

        if (workout == null) {
            throw new DayOffException(day + " is considered a day off!");
        }

        workout.exercises().addAll(exercises);
    }

}
