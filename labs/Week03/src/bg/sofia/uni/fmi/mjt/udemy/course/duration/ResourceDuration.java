package bg.sofia.uni.fmi.mjt.udemy.course.duration;

public record ResourceDuration(int minutes) {

    public ResourceDuration {
        if (minutes < 0 || minutes > 60) throw new IllegalArgumentException();
    }
}
