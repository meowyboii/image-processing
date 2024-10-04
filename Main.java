import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {


    public static void main(String[] args) {
        try {

            // Load source image
            BufferedImage sourceImage = ImageIO.read(new File("src/lena.jpg"));
            int height = sourceImage.getHeight();
            int width = sourceImage.getWidth();

            int currPixel, average, stretchedValue;
            int darkestPixel, lightestPixel;
            int[] histogram = new int[256];
            int[] histogramStretched = new int[256];
            BufferedImage outputGrayscale = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            BufferedImage outputStretched = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    currPixel=sourceImage.getRGB(x,y);
                    average = getAverageRGB(currPixel);
                    histogram[average]++;

                    //Output image
                    average = getGrayscale(average);
                    outputGrayscale.setRGB(x,y,average);
                }
            }

            //Save image output
            ImageIO.write(outputGrayscale, "jpg", new File("src/outputGrayscale.jpg"));

            darkestPixel = getDarkestPixel(histogram);
            lightestPixel = getLightestPixel(histogram);

            System.out.println("Darkest Pixel Value Intensity: "+ darkestPixel);
            System.out.println("Lightest Pixel Value Intensity: "+ lightestPixel);
            System.out.println("Most Frequent Intensity: "+ getMaxFrequency(histogram));

            //Print original histogram values
            System.out.println("Histogram:");
            for (int i=0; i<histogram.length; i++){
                System.out.println("Pixel value "+i+": "+histogram[i]);
            }

            //Stretch the histogram and increase contrast
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    currPixel=sourceImage.getRGB(x,y);
                    average = getAverageRGB(currPixel);
                    stretchedValue = (average-darkestPixel)*255/(lightestPixel-darkestPixel);
                    histogramStretched[stretchedValue]++;

                    //Output image
                    stretchedValue = getGrayscale(stretchedValue);
                    outputStretched.setRGB(x,y,stretchedValue);
                }
            }
            ImageIO.write(outputStretched, "jpg", new File("src/outputStretched.jpg"));

            //Print stretched histogram values
            System.out.println("Histogram Stretched:");
            for (int i=0; i<histogramStretched.length; i++){
                System.out.println("Pixel value "+i+": "+histogramStretched[i]);
            }

        } catch (Exception e) {
            System.out.println("An error occurred!");
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

    public static int getAverageRGB (int currPixel){
        int pixelRed = getRed(currPixel);
        int pixelBlue = getBlue(currPixel);
        int pixelGreen = getGreen(currPixel);

        return (pixelRed+pixelGreen+pixelBlue)/3;
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

    public static int getGrayscale(int pixel) {
        return (pixel << 16)|(pixel << 8)|pixel;
    }

}