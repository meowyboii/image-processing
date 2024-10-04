import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("IMAGE PROCESSING OPERATIONS");
        String path;
        Scanner reader = new Scanner(System.in);
        BufferedImage sourceImage;
        int height, width;

        while (true){
            System.out.print("Enter the file path of the image: ");
            path = reader.nextLine();
            try{
                sourceImage = ImageIO.read(new File(path));
                width = sourceImage.getWidth();
                height = sourceImage.getHeight();
                break;
            }
            catch (Exception e) {
                System.out.println("The file path is invalid.");
            }
        }

        int choice;
        do{
            System.out.println("\nSelect an image processing operation: ");
            System.out.println("1. Divide the image into 4 segments.");
            System.out.println("2. Flip the image horizontally or vertically.");
            System.out.println("3. Extract red, green, or blue channel.");
            System.out.println("4. Convert the image into grayscale.");
            System.out.println("5. Generate the histogram values of the image.");
            System.out.println("6. Apply histogram stretching.");
            System.out.println("7. Exit");

            System.out.print("Enter a number: ");
            choice = reader.nextInt();

            switch (choice){
                case 1:
                    segmentImage(sourceImage, height, width);
                    break;
                case 2:
                    int orientation;
                    System.out.println("Choose flip orientation: ");
                    System.out.println("1. Vertical");
                    System.out.println("2. Horizontal");

                    System.out.print("Enter a number: ");
                    orientation = reader.nextInt();

                    if (orientation==1){
                        flipVertically(sourceImage, height, width);
                    }
                    else{
                        flipHorizontally(sourceImage, height, width);
                    }
                    break;
                case 3:
                    int chosenColor;
                    System.out.println("Choose color channel to extract: ");
                    System.out.println("1. Red");
                    System.out.println("2. Green");
                    System.out.println("3. Blue");

                    System.out.print("Enter a number: ");
                    chosenColor = reader.nextInt();

                    if (chosenColor==1){
                        extractRedChannel(sourceImage, height, width);
                    }
                    else if (chosenColor==2) {
                        extractGreenChannel(sourceImage, height, width);
                    }
                    else{
                        extractBlueChannel(sourceImage, height, width);
                    }
                    break;
                case 4:
                    int grayscaleMethod;
                    System.out.println("Choose a grayscale method: ");
                    System.out.println("1. Average");
                    System.out.println("2. Luminosity");
                    System.out.println("3. Lightness");

                    System.out.print("Enter a number: ");
                    grayscaleMethod = reader.nextInt();

                    if (grayscaleMethod == 1){
                        getAverageGrayscale(sourceImage, height, width);
                    }
                    else if (grayscaleMethod == 2){
                        getLuminosityGrayscale(sourceImage, height, width);
                    }
                    else{
                        getLightnessGrayscale(sourceImage, height, width);
                    }
                    break;
                case 5:
                    generateHistogram(sourceImage, height, width);
                    break;
                case 6:
                    generateStretchedHistogram(sourceImage, height, width);
                    break;
                case 7:
                    System.out.println("Thanks for trying out this program!");
                    break;
                default:
                    System.out.println("Invalid Input!");
            }
        }while(choice!=7);
    }

    public static BufferedImage generateImage (BufferedImage sourceImage, BufferedImage outputImage, int startingIndexWidth,
                                        int startingIndexHeight, int endingIndexWidth, int endingIndexHeight){
        int indexWidth = 0, indexHeight = 0;
        for (int x = startingIndexWidth; x < endingIndexWidth; x++) {
            for (int y = startingIndexHeight; y < endingIndexHeight; y++) {
                int rgb = sourceImage.getRGB(x, y);
                outputImage.setRGB(indexWidth, indexHeight, rgb);
                indexHeight++;
            }
            indexHeight = 0;
            indexWidth++;
        }
        return outputImage;
    }

    public static void segmentImage (BufferedImage sourceImage, int height, int width){
        int halfHeight = height/2;
        int halfWidth = width/2;

        BufferedImage quadrant1 = new BufferedImage(width-halfWidth, halfHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage quadrant2 = new BufferedImage(halfWidth, halfHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage quadrant3 = new BufferedImage(halfWidth, height-halfHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage quadrant4 = new BufferedImage(width-halfWidth, height-halfHeight, BufferedImage.TYPE_INT_RGB);

        BufferedImage outputImage;

        try{
            //Generate 4 quadrants of the image
            outputImage = generateImage(sourceImage,quadrant2, 0,0,halfWidth,halfHeight);
            ImageIO.write(outputImage, "jpg", new File("src/segmentedOutput1.jpg"));

            outputImage = generateImage(sourceImage,quadrant1, halfWidth,0,width,halfHeight);
            ImageIO.write(outputImage, "jpg", new File("src/segmentedOutput2.jpg"));

            outputImage = generateImage(sourceImage,quadrant3, 0,halfHeight,halfWidth,height);
            ImageIO.write(outputImage, "jpg", new File("src/segmentedOutput3.jpg"));

            outputImage = generateImage(sourceImage,quadrant4, halfWidth,halfHeight,width,height);
            ImageIO.write(outputImage, "jpg", new File("src/segmentedOutput4.jpg"));

            System.out.println("The image was segmented successfully!");
        }
        catch (Exception e) {
            System.out.println("An error occurred! The segmentation of the image failed.");
        }
    }

    public static void flipHorizontally (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                outputImage.setRGB(width - x - 1, y, rgb);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void flipVertically (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                outputImage.setRGB(x, height - y - 1, rgb);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void extractRedChannel (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int pixelRed= getRedRGB(rgb);
                outputImage.setRGB(x, y, pixelRed);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void extractGreenChannel (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int pixelGreen= getGreenRGB(rgb);
                outputImage.setRGB(x, y, pixelGreen);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void extractBlueChannel (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int pixelBlue= getBlueRGB(rgb);
                outputImage.setRGB(x, y, pixelBlue);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void getAverageGrayscale (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int average = getAverageRGB(rgb);
                average = getGrayscale(average);
                outputImage.setRGB(x, y, average);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void getLuminosityGrayscale (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int pixelRed = getRed(rgb);
                int pixelGreen = getGreen(rgb);
                int pixelBlue = getBlue(rgb);
                int luminosityGrayscale = (int) (0.21 * pixelRed + 0.72 * pixelGreen + 0.07 * pixelBlue);
                luminosityGrayscale = getGrayscale(luminosityGrayscale);
                outputImage.setRGB(x, y, luminosityGrayscale);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void getLightnessGrayscale (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int pixelRed = getRed(rgb);
                int pixelGreen = getGreen(rgb);
                int pixelBlue = getBlue(rgb);
                int lightnessMax = Math.max((Math.max(pixelRed,pixelGreen)),pixelBlue);
                int lightnessMin = Math.min((Math.min(pixelRed,pixelGreen)),pixelBlue);
                int lightnessGrayscale = (int) (0.5 * (lightnessMax+lightnessMin));
                lightnessGrayscale = getGrayscale(lightnessGrayscale);
                outputImage.setRGB(x, y, lightnessGrayscale);
            }
        }
        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }
    }

    public static void generateHistogram (BufferedImage sourceImage, int height, int width){
        int[] histogram = new int[256];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int average = getAverageRGB(rgb);
                histogram[average]++;
            }
        }
        int darkestPixel = getDarkestPixel(histogram);
        int lightestPixel = getLightestPixel(histogram);

        System.out.println("Darkest Pixel Value Intensity: "+ darkestPixel);
        System.out.println("Lightest Pixel Value Intensity: "+ lightestPixel);
        System.out.println("Most Frequent Intensity: "+ getMaxFrequency(histogram));

        //Print original histogram values
        System.out.println("Histogram:");
        for (int i=0; i<histogram.length; i++){
            System.out.println("Pixel value "+i+": "+histogram[i]);
        }
    }

    public static Object[] getExtremePixels (BufferedImage sourceImage, int height, int width){
        int[] histogram = new int[256];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                int average = getAverageRGB(rgb);
                histogram[average]++;
            }
        }
        int darkestPixel = getDarkestPixel(histogram);
        int lightestPixel = getLightestPixel(histogram);
        return new Object[]{darkestPixel, lightestPixel};
    }

    public static void generateStretchedHistogram (BufferedImage sourceImage, int height, int width){
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Object[] extremePixels = getExtremePixels(sourceImage, height, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x,y);
                int average = getAverageRGB(rgb);
                int darkestPixel = (int) extremePixels[0];
                int lightestPixel = (int) extremePixels[1];
                int stretchedValue = (average-darkestPixel)*255/(lightestPixel-darkestPixel);

                //Output image
                stretchedValue = getGrayscale(stretchedValue);
                outputImage.setRGB(x,y,stretchedValue);
            }
        }

        try{
            ImageIO.write(outputImage, "jpg", new File("src/output.jpg"));
            System.out.println("Image histogram stretched successfully!");
        }
        catch (Exception e) {
            System.out.println("An error occurred in generating the image!");
        }

    }


    // function to separate the red channel
    public static int getRed(int pixel) {
        return (pixel & (255 << 16))>>16;
    }

    // function to separate the green channel
    public static int getGreen(int pixel) {
        return (pixel & (255 << 8))>>8;
    }

    // function to separate the blue channel
    public static int getBlue(int pixel) {
        return pixel & 255;
    }

    // function to get the RGB value with red channel only
    public static int getRedRGB(int pixel) {
        return (pixel & (255 << 16));
    }

    // function to get the RGB value with green channel only
    public static int getGreenRGB(int pixel) {
        return (pixel & (255 << 8));
    }

    // function to get the RGB value with blue channel only
    public static int getBlueRGB(int pixel) {
        return pixel & 255;
    }

    public static int getAverageRGB (int pixel){
        int pixelRed = getRed(pixel);
        int pixelBlue = getBlue(pixel);
        int pixelGreen = getGreen(pixel);

        return (pixelRed+pixelGreen+pixelBlue)/3;
    }

    public static int getGrayscale(int pixel) {
        return (pixel << 16)|(pixel << 8)|pixel;
    }

    public static int getDarkestPixel (int [] arr){
        for(int i=0; i<arr.length; i++){
            if (arr[i]!=0){
                return i;
            }
        }
        return 0;
    }

    public static int getLightestPixel (int [] arr){
        for(int i=arr.length-1; i>=0; i--){
            if (arr[i]!=0){
                return i;
            }
        }
        return 0;
    }

    public static int getMaxFrequency (int []arr){
        int max = 0, index = 0;
        for(int i=0; i<arr.length; i++){
            if (arr[i]>max){
                max = arr[i];
                index = i;
            }
        }
        return index;
    }

}