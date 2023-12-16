package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.image.Image;
import bg.sofia.uni.fmi.mjt.image.ImageLoader;
import bg.sofia.uni.fmi.mjt.image.ImageProcessor;
import static java.nio.file.Files.createDirectories;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    private final int imageProcessorsCount;
    private final Queue<Image> images;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
        this.images = new LinkedList<>();
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path sourcePath = Path.of(sourceDirectory);
        Path outputPath = Path.of(outputDirectory);
        createOutputPath(outputPath);

        AtomicBoolean areAllImagesLoaded = new AtomicBoolean(false);
        AtomicInteger loadedCount = new AtomicInteger(0);
        AtomicInteger imagesCount = new AtomicInteger(-1);

        for (int i = 0; i < imageProcessorsCount; ++i) {
            Thread.ofPlatform().start(new ImageProcessor(images, outputDirectory, areAllImagesLoaded));
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath)) {
            for (Path imagePath : stream) {
                if (!isExtensionValid(imagePath.toString())) {
                    continue;
                }
                startImageLoaderThread(imagePath, imagesCount, loadedCount, areAllImagesLoaded);
                imagesCount.incrementAndGet();
            }

            imagesCount.incrementAndGet();
        } catch (DirectoryIteratorException e) {
            throw new UncheckedIOException("Problem occurred while working with the directory: " + sourcePath,
                e.getCause());
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred while working with the directory: " + sourcePath, e);
        }
    }

    private void createOutputPath(Path outputPath) {
        if (Files.notExists(outputPath)) {
            try {
                createDirectories(outputPath);
            } catch (IOException e) {
                throw new RuntimeException("Problem occurred while creating output directory.", e);
            }
        }
    }

    private void startImageLoaderThread(Path imagePath, AtomicInteger imagesCount, AtomicInteger loadedCount,
                                        AtomicBoolean areAllImagesLoaded) {
        ImageLoader imageLoader = new ImageLoader(imagePath, images, imagesCount, loadedCount, areAllImagesLoaded);
        Thread.ofVirtual().start(imageLoader);
    }

    private boolean isExtensionValid(String imagePath) {
        String extension = getFileExtension(imagePath);
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");
    }

    private String getFileExtension(String imagePath) {
        String extension = "";

        int i = imagePath.lastIndexOf('.');
        int p = Math.max(imagePath.lastIndexOf('/'), imagePath.lastIndexOf('\\'));

        if (i > p) {
            extension = imagePath.substring(i + 1);
        }

        return extension;
    }

}
