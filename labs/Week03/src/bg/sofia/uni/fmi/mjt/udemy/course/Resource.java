package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {
    private String name;
    private ResourceDuration duration;
    private int completePercentage = 0;

    public Resource(String name, ResourceDuration duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public boolean isCompleted() {
        return completePercentage == 100;
    }

    @Override
    public int getCompletionPercentage() {
        return completePercentage;
    }

    public String getName() {
        return name;
    }

    public ResourceDuration getDuration() {
        return duration;
    }

    public void complete() {
        completePercentage = 100;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Course)) {
            return false;
        }

        Course course = (Course) obj;

        return this.getName().equals(course.getName());
    }
}
