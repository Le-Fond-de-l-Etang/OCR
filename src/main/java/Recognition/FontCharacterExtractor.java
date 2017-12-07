package Recognition;

import javax.imageio.ImageIO;
import javax.naming.spi.DirectoryManager;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontCharacterExtractor {
    /**
     * Create a 91*51 image with the specified Char written in the specified font
     * @param font font you want to use
     * @param s Character to print
     * @return
     */
    private static BufferedImage stringToBufferedImage(String font,String s) {
        //First, we have to calculate the string's width and height

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();

        //Set the font to be used when drawing the string
        Font f = new Font(font, Font.PLAIN, 78);
        g.setFont(f);

        //Get the string visual bounds
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = f.getStringBounds(s, frc);
        //Release resources
        g.dispose();

        //Then, we have to draw the string on the final image

        //Create a new image where to print the character
        img = new BufferedImage((int) Math.ceil(rect.getHeight()), (int) Math.ceil(rect.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
        g = img.getGraphics();
        g.setColor(Color.black); //Otherwise the text would be white
        g.setFont(f);

        //Calculate x and y for that string
        FontMetrics fm = g.getFontMetrics();


        int x = (int) Math.round(rect.getHeight()/2 - fm.stringWidth(s)/2 );
        int y = fm.getAscent(); //getAscent() = baseline

        g.drawString(s, x, y);

        //Release resources
        g.dispose();

        //Return the image
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
            String s = Character.toString(c);
            extractChar(font,s);
        }
    }
/**
 * write to file lowerCase alphabetical characters from the font
 */
    public static void extractminChar(String font){
        for(int i=97;i<=122;i++) {

            char c = (char)i ;
            String s = Character.toString(c);
            extractChar(font,s);
        }
    }

    /**
     * write to file numerical characters from the font
     * @param font
     */
    public static void extractNumChar(String font){
        for(int i=48;i<=57;i++) {
            char c = (char)i ;
            String s = Character.toString(c);
            extractChar(font,s);
        }
    }

    /**
     * Write an image in the folder images/font
     * @param font font you want to use
     * @param s character to write
     */
    private static void extractChar(String font,String s){

        BufferedImage imageNumber = stringToBufferedImage(font,s);
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
