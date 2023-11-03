package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;

import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {
    private String name;
    private String description;
    private double price;
    private Resource[] content;
    private Category category;
    private boolean purchased = false;
    private CourseDuration totalTime;


    public Course(String name, String description, double price, Resource[] content, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.content = content;
        this.category = category;
        this.totalTime = CourseDuration.of(content);
    }

    @Override
    public boolean isCompleted() {
        return getCompletionPercentage() == 100.0;
    }

    @Override
    public int getCompletionPercentage() {
        int completed = 0;

        for (Resource resource: content) {
            if (resource.isCompleted()) {
                completed++;
            }
        }

        return (content.length / completed) * 100;
    }

    @Override
    public void purchase() {
        purchased = true;
    }

    @Override
    public boolean isPurchased() {
        return purchased;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Resource[] getContent() {
        return content;
    }

    public CourseDuration getTotalTime() {
        return totalTime;
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws IllegalArgumentException if resourceToComplete is null.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        if (resourceToComplete == null) {
            throw new IllegalArgumentException("Value of resourceToComplete should not be null!");
        }

        for (Resource resource: content) {
            if (resource.equals(resourceToComplete)) {
                resource.complete();
                return;
            }
        }

        throw new ResourceNotFoundException();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Resource)) {
            return false;
        }

        Resource resource = (Resource) obj;

        return this.getName().equals(resource.getName());
    }

}
