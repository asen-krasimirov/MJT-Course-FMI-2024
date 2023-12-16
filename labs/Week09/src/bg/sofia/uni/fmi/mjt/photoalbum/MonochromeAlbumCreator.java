package bg.sofia.uni.fmi.mjt.photoalbum;

public interface MonochromeAlbumCreator {

    /**
     * Iterates over all files from @sourceDirectory and picks up image ones - those with extensions jpeg, jpg, and png.
     * Starts a new thread for each image and loads it into a shared data structure.
     * Starts @imageProcessorsCount threads that process the images from the mentioned above shared data structure,
     * and save them into the provided @outputDirectory. In case the @outputDirectory does not exist, it is created.
     *
     * @param sourceDirectory directory from where the image files are taken. The directory should exist,
     *                        throw the appropriate exception if there are issues with loading the files.
     * @param outputDirectory the directory where the output b&w images are stored, if it does not exist, it is created.
     */
    void processImages(String sourceDirectory, String outputDirectory);

}
