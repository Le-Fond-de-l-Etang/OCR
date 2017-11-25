package Recognition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageReader {
    /**
     * Convert all the images of a folder into arrays. Use the first char of the file as matching value.
     * @param path Path of the folder containing images
     * @return Map associating characters with array of doubles.
     */
    public Map<Character, double[]> readImagesFromFolder(String path) {
        Map<Character, double[]> images = new HashMap<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            System.out.println("No such directory : " + path + ".");
            return images;
        }
        for (int i=0; i<listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                char character = listOfFiles[i].getName().charAt(0);
                try {
                    BufferedImage image = getImageFromFile(listOfFiles[i]);
                    if (image != null) {
                        double[] imageArray = transformImageToArray(image);
                        images.put(character, imageArray);
                    } else {
                        System.out.println("File " + listOfFiles[i].getName() + " could not be read.");
                    }
                } catch (IOException e) {
                    System.out.println("File " + listOfFiles[i].getName() + " could not be read.");
                }
            }
        }
        return images;
    }

    /**
     * Get a buffered image from a file
     * @param file Image file
     * @return Image
     * @throws IOException File can't be read as an image
     */
    private BufferedImage getImageFromFile(File file) throws IOException {
        BufferedImage image = null;
        image = ImageIO.read(file);
        return image;
    }

    /**
     * Convert an image to an array of lightness values
     * @param image Image to read
     * @return Array of values for each pixel
     */
    private double[] transformImageToArray(BufferedImage image) {
        float hsv[] = new float[3];
        double[] array = new double[image.getWidth() * image.getHeight()];
        for (int x=0 ; x<image.getWidth(); x++) {
            for (int y=0; y<image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                Color.RGBtoHSB((rgb>>16)&0xff, (rgb>>8)&0xff, rgb&0xff, hsv);
                array[y*image.getWidth()+x] = hsv[2];
            }
        }
        return array;
    }
}
