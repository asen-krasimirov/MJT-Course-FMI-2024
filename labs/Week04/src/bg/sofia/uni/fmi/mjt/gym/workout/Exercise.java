package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.Objects;

public record Exercise(String name, int sets, int repetitions) implements Comparable<Exercise> {

    @Override
    public int compareTo(Exercise other) {
        if (other.name() == null) {
            return 1;
        }

        return this.name().compareTo(other.name());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Exercise exercise)) {
            return false;
        }

        return name().equals(exercise.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name());
    }
}
