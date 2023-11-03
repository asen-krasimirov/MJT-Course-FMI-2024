package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {

    public CourseDuration {
        // interval could be not inclusive [0, 23] (hours) and [0, 59] (minutes)
        if (hours < 0 || hours >= 24) throw new IllegalArgumentException("Illegal hours value");
        if (minutes < 0 || minutes >= 60) throw new IllegalArgumentException("Illegal minutes value!");
    }

    public static CourseDuration of(Resource[] content) {
        int hours = 0;
        int minutes = 0;

        for (Resource resource: content) {
            minutes += resource.getDuration().minutes();
            if (minutes >= 60) {
                hours++;
                minutes -= 60;
            }
        }

        return new CourseDuration(hours, minutes);
    }

    public static int compare(CourseDuration d1, CourseDuration d2) {
        int res = Integer.compare(d1.hours, d2.hours);

        if (res == 0) {
            return Integer.compare(d1.minutes, d2.minutes);
        }

        return res;
    }
}