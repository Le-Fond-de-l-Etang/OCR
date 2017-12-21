package Recognition;

import javax.imageio.ImageIO;
import javax.naming.spi.DirectoryManager;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FontCharacterExtractor {
    private static String[] learningFonts = {"Arial"};

    public static Map<Character, double[]> readAllFonts() {
        Map<Character, double[]> processedChars = new HashMap<>();
        for (String font : learningFonts) {
            for (char character : CharacterMapping.recognizedCharacters) {
                BufferedImage characterImage = converCharToBufferedImage(character, font, 24);
                //ImageReader.writeImageToDisk(characterImage, "test.png");
                double[] array = ImageReader.transformImageToArray(characterImage);
                processedChars.put(character, array);
            }
        }
        return processedChars;
    }

    /**
     * Create an image with the specified character written in the specified font
     * @param character Character to print
     * @param font Font you want to use
     * @param size Size of the written text
     * @return Image with given text
     */
    private static BufferedImage converCharToBufferedImage(char character, String font, int size) {
        String string = character + "";
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();

        Font f = new Font(font, Font.PLAIN, size);
        g.setFont(f);

        // Get the string visual bounds
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = f.getStringBounds(string, frc);
        g.dispose();

        // Draw the string on the final image
        img = new BufferedImage((int) Math.ceil(rect.getHeight()), (int) Math.ceil(rect.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
        g = img.getGraphics();
        g.setColor(Color.black);
        g.setFont(f);

        //Calculate x and y for that string
        FontMetrics fm = g.getFontMetrics();
        int x = (int) Math.round(rect.getHeight()/2 - fm.stringWidth(string)/2 );
        int y = fm.getAscent();

        g.drawString(string, x, y);
        g.dispose();
        return img;
    }

    /**
     * write to file upper case ajphabetical characters from the font
     *
     * @param font
     */
    public static void extractMajChar(String font){
        for(int i=65;i<=90;i++) {

            char c = (char)i ;
            extractChar(font,c);
        }
    }
/**
 * write to file lowerCase alphabetical characters from the font
 */
    public static void extractminChar(String font){
        for(int i=97;i<=122;i++) {

            char c = (char)i ;
            String s = Character.toString(c);
            extractChar(font,c);
        }
    }

    /**
     * write to file numerical characters from the font
     * @param font
     */
    public static void extractNumChar(String font){
        for(int i=48;i<=57;i++) {
            char c = (char)i ;
            extractChar(font,c);
        }
    }

    /**
     * Write an image in the folder images/font
     * @param font font you want to use
     * @param s character to write
     */
    private static void extractChar(String font,char s){

        BufferedImage imageNumber = converCharToBufferedImage(s, font, 78);
        File dir = new File("images/"+font);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File outputfile = new File("images/"+font +"/"+s+".png");
        try {
            ImageIO.write(imageNumber, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
