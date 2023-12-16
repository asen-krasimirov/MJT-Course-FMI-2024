package bg.sofia.uni.fmi.mjt.image;

import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageLoader implements Runnable {

    private final Path imagePath;
    private final Queue<Image> images;
    private final AtomicInteger loadedCount;
    private final AtomicInteger imagesCount;
    private final AtomicBoolean areAllImagesLoaded;

    public ImageLoader(Path imagePath, Queue<Image> images, AtomicInteger imagesCount, AtomicInteger loadedCount,
                       AtomicBoolean areAllImagesLoaded) {
        this.imagePath = imagePath;
        this.images = images;
        this.loadedCount = loadedCount;
        this.imagesCount = imagesCount;
        this.areAllImagesLoaded = areAllImagesLoaded;
    }

    @Override
    public void run() {
        Image image = Image.loadImage(imagePath);
        synchronized (images) {
            images.offer(image);

            if (loadedCount.incrementAndGet() == imagesCount.get()) {
                areAllImagesLoaded.set(true);
            }

            images.notifyAll();
        }
    }

}
