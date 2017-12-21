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
     * Convert all the images of a folder into arrays. Use the first char of the file name as matching value.
     * @param path Path of the folder containing images
     * @return Map associating characters with array of doubles.
     */
    public static Map<Character, double[]> readImagesFromFolder(String path, int imageWidth, int imageHeight) {
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
                    BufferedImage originalImage = getImageFromFile(listOfFiles[i]);
                    BufferedImage image = resizeImage(originalImage, imageWidth, imageHeight);

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
    private static BufferedImage getImageFromFile(File file) throws IOException {
        BufferedImage image = null;
        image = ImageIO.read(file);
        return image;
    }

    /**
     * Convert an image to an array of lightness (HSB) values
     * @param image Image to read
     * @return Array of values for each pixel
     */
    public static double[] transformImageToArray(BufferedImage image) {
        float hsv[] = new float[3];
        double[] array = new double[image.getWidth() * image.getHeight()];
        for (int x=0 ; x<image.getWidth(); x++) {
            for (int y=0; y<image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                if ((rgb>>24) == 0x00) {
                    array[y * image.getWidth() + x] = 1;
                } else {
                    Color.RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, hsv);
                    array[y * image.getWidth() + x] = hsv[2];
                }
            }
        }
        return array;
    }

    /**
     * Resize image with given width and height
     * @param imageOrigin Image to resize
     * @param width New width
     * @param height New height
     * @return Resized image
     */
    private static BufferedImage resizeImage(BufferedImage imageOrigin, int width, int height) {
        Image image = imageOrigin.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return resizedImage;
    }



    /**
     * Write an image file to disk
     * @param image Image to save
     * @param name Name with png extension
     */
    public static void writeImageToDisk(BufferedImage image, String name) {
        File dir = new File("test/");
        dir.mkdir();
        File outputFile = new File("images/"+name);
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
