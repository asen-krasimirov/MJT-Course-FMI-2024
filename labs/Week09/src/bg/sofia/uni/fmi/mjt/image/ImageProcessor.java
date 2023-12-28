package bg.sofia.uni.fmi.mjt.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageProcessor implements Runnable {

    private final Queue<Image> images;
    private final String outputDirectory;
    private final AtomicBoolean areAllImagesLoaded;

    public ImageProcessor(Queue<Image> images, String outputDirectory, AtomicBoolean areAllImagesLoaded) {
        this.images = images;
        this.outputDirectory = outputDirectory;
        this.areAllImagesLoaded = areAllImagesLoaded;
    }

    @Override
    public void run() {
        while (true) {
            Image imageToProcess = getImageFromQueue();

            if (imageToProcess == null) {
                break;
            }

            Image result = convertToBlackAndWhite(imageToProcess);
            saveImageInOutputDirectory(result);
        }
    }

    private Image getImageFromQueue() {
        synchronized (images) {
            while (images.isEmpty()) {
                try {
                    if (areAllImagesLoaded.get() && images.isEmpty()) {
                        return null;
                    }

                    images.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Image Processor thread interrupted before finishing.", e);
                }
            }

            Image imageToProcess=images.poll();;

            return imageToProcess;
        }
    }

    private Image convertToBlackAndWhite(Image image) {
        BufferedImage processedData =
            new BufferedImage(image.data.getWidth(), image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        processedData.getGraphics().drawImage(image.data, 0, 0, null);

        return new Image(image.name, processedData);
    }

    private void saveImageInOutputDirectory(Image imageToSave) {
        try {
            Path outputPath = Path.of(outputDirectory + File.separator + imageToSave.name);
            ImageIO.write(imageToSave.data, "png", outputPath.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred while saving to " + outputDirectory + imageToSave.name, e);
        }
    }

}
