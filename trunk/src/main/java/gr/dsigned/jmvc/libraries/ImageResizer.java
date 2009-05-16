/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.libraries;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author user
 */
public class ImageResizer {
    public static final int HEIGHT = 0;
    public static final int WIDTH = 1;
    
    public static void resizeProportionally(File inputFile, int dimension, String suffix, int whichDimension) throws IOException {
        File parentDir = inputFile.getParentFile();
        String extention = inputFile.getName();
        int index = extention.lastIndexOf('.');
        String name = extention.substring(0, index);
        name = name.substring(0, name.length() - 2);
        extention = extention.substring(index + 1);
        BufferedImage bi = ImageIO.read(inputFile);
        final float width = bi.getWidth();
        final float height = bi.getHeight();
        int outputHeight = 0;
        int outputWidth = 0;
        float ratio = 0;
        switch (whichDimension) {
            case HEIGHT:
                ratio = height/width ;
                outputHeight = dimension;
                outputWidth = Math.round(dimension / ratio);
                break;
            case WIDTH:
                ratio = width / height;
                outputWidth = dimension;
                outputHeight = Math.round(dimension / ratio);
                break;
            }
        Image image = bi.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH);
        BufferedImage bo = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_RGB);
        bo.getGraphics().drawImage(image, 0, 0, null);
        ImageIO.write(bo, extention, new File(parentDir, name + suffix + "." + extention));
    }

    /**
     * Method that resizes an existing file given the dimension, the suffix and the targetDir
     * that you want to save the new resized image into.
     * @param inputFile
     * @param whichDimension
     * @param dimension
     * @param suffix
     * @param targetDir
     * @return the new resized image File that is created
     * @throws java.io.IOException
     */
    public static File resizeProportionally(File inputFile,int whichDimension, int dimension, String suffix, String targetDir) throws IOException {
        File fileDir = new File(targetDir) ;
        fileDir.mkdirs();        
        String extention = inputFile.getName();
        int index = extention.lastIndexOf('.');
        String name = extention.substring(0, index);
        name = name.substring(0, name.length() - 2);
        extention = extention.substring(index + 1);
        BufferedImage bi = ImageIO.read(inputFile);
        final float width = bi.getWidth();
        final float height = bi.getHeight();
        int outputHeight = 0;
        int outputWidth = 0;
        float ratio = 0;
        switch (whichDimension) {
            case HEIGHT:
                ratio = height / width;
                outputHeight = dimension;
                outputWidth = Math.round(dimension / ratio);
                break;
            case WIDTH:
                ratio = width / height;
                outputWidth = dimension;
                outputHeight = Math.round(dimension / ratio);
                break;
            }
        Image image = bi.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH);
        BufferedImage bo = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_RGB);
        bo.getGraphics().drawImage(image, 0, 0, null);
        File newFile = new File(fileDir, name + suffix + "." + extention);
        ImageIO.write(bo, extention, newFile);
        return newFile;
    }
    /**
     * Method that resizes an existing file given the dimension, the suffix and the targetDir
     * that you want to save the new resized image into.
     * @param inputFile
     * @param whichDimension
     * @param dimension
     * @param suffix
     * @param targetDir
     * @return the new resized image File that is created
     * @throws java.io.IOException
     */
    public static File resizeImageProportionally(BufferedImage inputImage,int whichDimension, int dimension, String extention, File targetFile) throws IOException {        
        BufferedImage bi = inputImage;
        final float width = bi.getWidth();
        final float height = bi.getHeight();
        int outputHeight = 0;
        int outputWidth = 0;
        float ratio = 0;
        switch (whichDimension) {
            case HEIGHT:
                ratio = height / width;
                outputHeight = dimension;
                outputWidth = Math.round(dimension / ratio);
                break;
            case WIDTH:
                ratio = width / height;
                outputWidth = dimension;
                outputHeight = Math.round(dimension / ratio);
                break;
            }
        Image image = bi.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH);
        BufferedImage bo = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_RGB);
        bo.getGraphics().drawImage(image, 0, 0, null);
        File newFile = targetFile;
        ImageIO.write(bo, extention, newFile);
        return newFile;
    }

}
