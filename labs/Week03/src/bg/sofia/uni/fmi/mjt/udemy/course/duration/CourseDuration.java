package bg.sofia.uni.fmi.mjt.udemy.course.duration;

public record CourseDuration(int hours, int minutes) {

    public CourseDuration {
        // interval could be not inclusive [0, 24] (hours) and [0, 60] (minutes)
        if (hours < 0 || hours > 24) throw new IllegalArgumentException("Illegal hours value");
        if (minutes < 0 || minutes > 60) throw new IllegalArgumentException("Illegal minutes value!");
    }

//    TODO: finish
//    public static CourseDuration of(Resource[] content) {
//        return ;
//    }
}