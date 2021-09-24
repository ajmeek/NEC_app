package com.example.neuroscience;

import java.util.Random;

// Helps generate patches from audio files
public class  AudioPatchHelper {

    // Generates patches from byte array
    public static double[][] getPatches(byte[] buffer, int bufferSize, int ds, int numPatches){

        // DS = Downsampling, for example, skip every 1, 2, or 3 values
        // numPatches = Number of patches

        // Width of each sample
        int width = 100;

        double audioPatches[][] = new double[numPatches][width];
        //Generate matrix for patches
        for(int i = 0; i < numPatches; i++){
            for(int f = 0; f < width; f++){
                audioPatches[i][f] = 0;
            }
        }

        // RNG
        Random rand = new Random();

        // Buffer size = 5120 bytes
        // Assuming ds = 3, max = 4818, if ds = 5, max = 4622
        int max = ((bufferSize/2)-ds*width-2);
        int start = 0, stop = 0;

        double doubleBuffer[] = new double[bufferSize/2];
        // Convert byte buffer to 1-d 16-bit int array
        int n = -1;
        short p;
        for (int i = 0; i < bufferSize/2; i++) {

            doubleBuffer[i] = twoBytesToDouble(buffer[n+1], buffer[n+2]);
            n += 2;
        }

        // Get different "random" clips from audio
        // Basically, assign each index i the buffer values from a:b in each subindex j
        // Python does this way cooler than I am
        for(int i = 0; i < numPatches; i++){

            // Starting/stopping index of buffer
            start = rand.nextInt(max);
            stop = (start + ds*width-1);

            int j = 0;
            // Decrement by data size (?)
            while(start < stop){
                audioPatches[i][j] =  doubleBuffer[start]; //buffer[start] & 0xff;
                j++;
                start += ds;

            }
            // for(int j = 0; j < stop; j = j-ds){
            // Cast each byte as an int and save it in array
            // This was the alternative to converting the buffer to a 1-d array
            //    audioPatches[i][j] = buffer[start] & 0xff;

            // }
            //audioPatches[i][i] = buffer[max][max+ds*width-1];

        }
        return audioPatches;

    }

    // Combines 2 bytes into a 16 bit int ( since wav files are 16 bit?)
    public static double twoBytesToDouble(byte b1, byte b2) {
        short tmp =  (short) (((b1 << 8) | (b2 & 0xFF)));

        return (double) (tmp) ;// & 0xFFFF);
        //return (int) (((b1 << 8) | (b2 & 0xFF)) & 0xFFFF);

    }
    public static short twoBytesToShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }

    // Transposing function for array
    public static double[][] transpose(final double[][] matrix){
        final int m = matrix.length;
        final int n = matrix[0].length;
        final double[][] transposed = new double[n][m];
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; ++j) {
                transposed[i][j] = matrix [j][i];
            }
        }
        return transposed;
    }
}
