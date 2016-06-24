package pkg420.homework.pkg2;

import java.lang.reflect.Array;
import java.util.Hashtable;
import java.lang.Float;

public class Homework2 {
    /**
     * Quantizes data into bins that are in increments of two (ie 2 <= x < 4)
     * @param input         The list of doubles to be quantized
     * @param roundingVal   Rounds to the nearest multiple of n for binning (nearest multiple of 4, 5)
     * @return Quantized data, binned and sorted.
     */
    public static double[] binData(double[] input, int roundingVal)
    {
        int arraySize = Array.getLength(input);
        double[] binned = new double[arraySize];

        //Quantize the data by rounding it to the nearest value
        for(int i = 0; i < arraySize; i++)
        {
            binned[i] = roundingVal*(Math.round(input[i]/roundingVal));
        }

        return binned;
    }

    /**
     * Converts an int[] to a hashtable representing a histogram.
     * @param input         int[] to be converted to a histogram
     * @return              Hashtable representing a histogram
     */
    public static Hashtable<Integer, Integer> makeHistogram(int[] input)
    {
        int arraySize = Array.getLength(input);
        Hashtable<Integer, Integer> histogram = new Hashtable<Integer, Integer>();

        for(int i = 0; i < arraySize; i++)               //Make a histogram out of the data in the form of a hashtable.
        {
            if (histogram.containsKey(input[i]))
            {
                histogram.put(input[i], (histogram.get(input[i])+1));   //Increment 1 to value in histogram if another one is found
            }
            else
            {
                histogram.put(input[i], 1);     //Add value to hashtable and set it's value to 1
            }
        }
        //System.out.println("\n" + histogram.toString());
        return histogram;
    }


    /**
     * Calculates the optimal threshold given a set of data
     * @param input         Array which we calculate the threshold on
     * @return              Optimal Otsu threshold
     */
    public static int otsuTreshold(int[] input)
    {
        Hashtable<Integer, Integer> histogram;
        histogram = makeHistogram(input);
        int arraySize = input.length;
        float sum = 0;
        float sumB = 0;
        int wB = 0;
        int wF = 0;
        float varMax = 0;
        int threshold = 0;

        for(Integer key : histogram.keySet())   //For each key in the hashtable, sum up total value of
        {                                       //all of the keys * values
            sum += key * histogram.get(key);
        }

        float tempCompare = Float.MAX_VALUE;       //Set to arbitrarily large value to test against smaller values.
                                            //Used to determine the minimum varBetween

        for(Integer key : histogram.keySet())   //For each key in the hashtable, calculate threshold. Try to find best.
        {
            wB += histogram.get(key);
            if(wB == 0) continue;
            wF = arraySize - wB;

            if(wF == 0) break;

            sumB += (float) (key * histogram.get(key));
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween < tempCompare)
            {
                tempCompare = varBetween;
            }

            if(varBetween > varMax)     //Determine if the current key is the threshold
            {
                varMax = varBetween;
                threshold = key;
            }
            //System.out.println(threshold + "\t" + varBetween);
            //System.out.println(varBetween);
        }



        System.out.println("Minimum mixed variance: " + tempCompare);
        System.out.println("Threshold: " + threshold);
        return threshold;
    }

    public static int[] binarize(int[] input)
    {
        int threshold = otsuTreshold(input);
        int size = input.length;
        int[] binarized = new int[size];

        for(int i = 0; i < size; i++)       //Binarize the data
        {
            if(input[i] > threshold)
            {
                binarized[i] = 1;
            }
            else
            {
                binarized[i] = 0;
            }
        }
        return binarized;
    }


    /**
     * The main method of the program. Will quantize data and perform otsu's method to cluster data.
     * @param args      Not Used
     */
    public static void main(String[] args)
    {
        //Provided data from the Speed Observations file. 128 Elements.
        double[] speeds =
                {
                        53.3,70.3,54.87,61.3,67.79,42.04,56.66,56.78,53.75,
                        63.19,52.87,52.9,57.64,44.81,65.51,52.86,67.22,57.26,
                        53.53,46.58,49.03,58.58,56.67,65.64,67.23,43.95,71.78,
                        50.95,48.16,62.55,52.31,56.91,53.96,60.77,53.66,52.74,
                        55.81,53.74,64.14,54.38,69.67,65.01,58.56,56.14,60.26,
                        58.59,55.94,54.63,53.45,64.45,54.02,67.31,62.55,63.55,
                        66.08,52.59,56.22,54.86,52.64,55.14,52.45,64.2,66.19,
                        56.41,58.2,64.65,65.91,49.57,60.65,70.03,61.07,61.43,
                        62.79,52.71,64.55,61.28,54.4,69.13,54.94,53.81,53.5,
                        58.97,55.82,63.19,57.33,66.28,59.25,69.42,54.01,56.85,
                        63.95,54.39,40.06,61.99,60.31,66.53,71.01,54.45,63.01,
                        66.24,62.07,45.47,53.95,63.45,57.35,65.67,49.42,49.2,
                        53.72,55.03,64.1,42.73,62.73,48.28,63.39,58.48,65,52.6,
                        52.62,67.37,56.72,59.73,53.16,65.3,57.68,54.64,63.74,50.09
                };

        //Provided data from the Mystery Date file. 40 Elements.
        int[] mysteryData =
                {
                        7,8,23,9,8,16,10,22,36,26,10,7,7,12,19,18,6,9,16,12,7,
                        14,18,10,38,28,12,8,9,11,32,19,32,19,18,15,16,20,8,16
                };

        int arraySize = Array.getLength(speeds);
        double[] quantizedData = new double[arraySize];
        quantizedData = binData(speeds, 2);             //The quantized data, rounded by default to the nearest 2

        int[] data = new int[arraySize];

        for(int i = 0; i < arraySize; i++)      //Cast quantized data to ints
        {
            data[i] = (int) quantizedData[i];
        }

        int[] binarizedData = binarize(data);

        System.out.println("Speed Data:\n----------");

        //Print results from calculations on the speed data
        System.out.print("Binned Speeds:                   \t||||\t");
        for(int i = 0; i < arraySize; i++)  //Print out the quantized data
        {
            System.out.print(data[i] + "\t");
        }
        System.out.println();
        System.out.print("Is Above threshold? (0 No, 1 Yes)\t||||\t");
        for(int i = 0; i < arraySize; i++)  //Print out the binarized data
        {
            System.out.print(binarizedData[i] + "\t");
        }

        System.out.println("\n===========");
        System.out.println("Mystery Data:\n----------");

        int[] binarizedMysteryData = binarize(mysteryData);

        arraySize = binarizedMysteryData.length;

        System.out.print("Binned Speeds:                   \t||||\t");
        //Print results from calculations on mystery data
        for(int i = 0; i < arraySize; i++)  //Print out the quantized data
        {
            System.out.print(mysteryData[i] + "\t");
        }
        System.out.println();
        System.out.print("Is Above threshold? (0 No, 1 Yes)\t||||"
                + "\t");
        for(int i = 0; i < arraySize; i++)  //Print out the binarized data
        {
            System.out.print(binarizedMysteryData[i] + "\t");
        }
    }
}
