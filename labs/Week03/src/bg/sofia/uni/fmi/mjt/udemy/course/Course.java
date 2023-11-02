package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;

import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {
    private String name;
    private String description;
    private double price;
    private Resource[] content;
    private Category category;
    private int completePercentage = 0;
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

    public boolean isCompleted() {
        return completePercentage == 100;
    }

    public int getCompletionPercentage() {
        return completePercentage;
    }

    public void purchase() {
        purchased = true;
    }

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

        throw new ResourceNotFoundException("Resource not found!");
    }

}
