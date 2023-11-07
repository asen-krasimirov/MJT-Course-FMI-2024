package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Member implements Comparable<Member>, GymMember {
    private Address address;
    private String name;
    private int age;
    private String personalIdNumber;
    private Gender gender;
    private HashMap<DayOfWeek, Workout> trainingProgramme;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgramme = HashMap.newHashMap(DayOfWeek.values().length);
    }

    @Override
    public int compareTo(Member other) {
        if (other.getPersonalIdNumber() == null) {
            return 1;
        }

        return this.getPersonalIdNumber().compareTo(other.getPersonalIdNumber());
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
        return Map.copyOf(trainingProgramme);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Values of day and workout should not be null!");
        }

        trainingProgramme.put(day, workout);
    }
}
