# Genetic Algorithm: Perceptron

The goal of this project was to take what we learned about making genetic algorithms, and apply it to a problem of our choosing.
I decided to model a perceptron and train a population of perceptrons using the genetic algorithm.
The fitness function of the perceptron is determined by how well it is able to recognize a 7x8 pixel image of a digit.

I first started with a simple fitness function that gives me 100 if the prediction is true and 0 if the prediction is false.
This worked fine but this only created a perceptron that can match its prediction to the changing environment, but it doesn't
take into account whether the environment is not any of the other digits. So I also added negative reinforcement to the fitness
function by subtracting from the score if it thinks that the environment is more than one digit.

**The Setup**

First, I drew the digits 0 through 9 using Microsoft Paint and saved them as png images. Then I wrote a program in Java that reads
each image and converts it into a Lisp list of 1s and 0s based on if the pixel is black or white. The Java program saves the lists
into a file that Lisp can then read.

Ex: 9 = ( 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0 1 0 0 0 1 0 0 1 0 0 0 1 0 0 1 0 0 0 1 0 0 1 0 0 0 1 0 0 0 1 1 1 0 0 0 0 0 0 0 0 0 )

![Digits](./digits.png)

**Image Converter Program (Java)**

```java
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class ImageConvert {

    public static void main(String args[]) throws Exception {
        File file = new File("numbers.txt");
        file.createNewFile();

        // Convert every image file to a binary image string for lisp
        FileWriter writer = new FileWriter("numbers.txt");
        for (int i=0; i<10; i++) {
            String imageString = loadImage("num"+i+".png");
            writer.write(imageString);
        }
        writer.close();
    }

    public static String loadImage(String fileName) throws Exception {
        // Load the image
        BufferedImage img = ImageIO.read(new File(fileName));
        int width = img.getWidth();
        int height = img.getHeight();
        
        // Start the result string: '(' starts a list in Lisp
        String res = "(";

        // Go through every pixel and add 1s or 0s to the string based on the color of the pixel
        for (int y=0; y < height; y++)
            for (int x=0; x < width; x++)
                // getRGB -> convert to blue (0-255) -> convert to binary (0-1) -> invert using xor (0->1 or 1->0)
                res += " "+ (((img.getRGB(x, y) & 0xFF) / 255) ^ 1 );

        // End the result string: ')' ends a list in lisp
        res += " )\n";
        
        return res;
    }

}
```

**Results**

The result of the program is a bunch of trained perceptrons that can recognize a single digit and differentiate it from the others.
The training was done on just the 10 images that I drew so the accuracy metrics only apply to how well the perceptron recognizes
those specific images. It can differentiate between most digits pretty well, but gets hung up on ones that are similar, like 1 and 7
or 0 and 8.

The next step would be to modify this to work with multiple images for every digit and to test on a separate
set of images from the training set. 

It would be interesting to put 10 of these perceptrons together to make a network that can output which specific digit it's looking at.
Also making the perceptrons output a probability rather than a 1 or 0 would allow the network to figure out what the most probable digit is.

