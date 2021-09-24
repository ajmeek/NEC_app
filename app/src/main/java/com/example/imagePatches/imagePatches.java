package com.example.imagePatches;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.util.Random;

import static java.lang.Math.floorDiv;

//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;

public class imagePatches {


    static double findMatrixMean(double m[][], int w, int h) {

        double sum = 0;
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                sum += m[i][j];
            }
        }
        return sum / (w * h);
    }

    static double findArrayMean(double array[]) {
        double sum = 0;

        for(int i = 0; i < array.length; i++)
            sum += array[i];

        return sum / array.length;
    }

    static double findVariance(double matrix[][], int w, int h, double mean) {
        double sum = 0;

        double [][] m = new double[matrix.length][];
        for(int i = 0; i < matrix.length; i++)
            m[i] = matrix[i].clone();

        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                m[i][j] -= mean;
                m[i][j] *= m[i][j];
            }
        }

        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                sum += m[i][j];
            }
        }

        return sum / (w * h);
    }

    static double[][] subMatrix(double[][] matrix, int row, int col, int width){
        double[][] sub = new double[width][width];

        for(int i = row; i < row+width; i++) {
            for(int j = col; j < col+width; j++) {
                int x = i - row;
                int y = j - col;

                sub[x][y] = matrix[i][j];
            }
        }

        return sub;
    }

    static double[] reshapeMatrix(double[][] matrix, int size) {
        double[] reshaped = new double[size];
        int index = 0;

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                reshaped[index] = matrix[i][j];
                index++;
            }
        }

        return reshaped;
    }

    static double[][] reshapeArray(double array[], int row, int col){
        double[][] rMatrix = new double[row][col];

        int i = 0;

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                rMatrix[r][c] = array[i++];
            }
        }

        return rMatrix;
    }

    static double findMax(double[] array) {
        double max = array[0];
        for(int i = 1; i < array.length; i++) {
            if(array[i] > max)
                max = array[i];
        }

        return max;
    }

    static double findMax(double[][] array) {
        double max = array[0][0];
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                if(array[i][j] > max)
                    max = array[i][j];
            }
        }

        return max;
    }

    static double findMin(double[] array) {
        double min = array[0];
        for(int i = 1; i < array.length; i++) {
            if(array[i] < min)
                min = array[i];
        }

        return min;
    }

    static double findMin(double[][] array) {
        double min = array[0][0];
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                if(array[i][j] < min)
                    min = array[i][j];
            }
        }

        return min;
    }

    static void print2dArray(double[][] array) {
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                System.out.print(new DecimalFormat("#.###").format(array[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public static double[][] get_patches(int nPatches, int pWidth, Bitmap bitmap){

        int numPatches = nPatches;
        int patchWidth = pWidth;


        int numPixels = patchWidth * patchWidth;
        int patchCount = 0;
        int tryCount = 0;

        double[][] patchSample = new double[patchWidth][patchWidth];
        double[] patch = new double[numPixels];
        double[][] imgPatches = new double[numPixels][numPatches];

        Bitmap image = bitmap;

        //Load image to be processed
        //BufferedImage image = null;
//        try {
//            image = ImageIO.read(bw_patch_collect.class.getResource(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        double[][] imageArray = new double[imageWidth][imageHeight];	//create pixel array

        //Convert individual color pixel to greyscale
        for(int i = 0; i < imageWidth; i++) {
            for(int j = 0; j < imageHeight; j++) {
                int rgb = image.getPixel(i,j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                //THIS IS A LINEAR APROXIMATION
                imageArray[i][j] = Math.round(0.3*r + .59*g + .11*b);
                //UPDATE THIS LATER FOR TRUE GREYSCALE
            }
        }

        //calculate mean of matrix
        double mean = findMatrixMean(imageArray, imageWidth, imageHeight);

        //subtract mean from each value in the matrix
        for(int i = 0; i < imageWidth; i++) {
            for(int j = 0; j < imageHeight; j++) {
                imageArray[i][j] -= mean;
            }
        }
        //calculate Variance of new matrix
        double var = findVariance(imageArray, imageWidth, imageHeight, findMatrixMean(imageArray, imageWidth, imageHeight));
        //calculate standard deviation of new matrix
        double std = Math.sqrt(var);

        //divide each value in the matrix by the standard deviation
        for(int i = 0; i < imageWidth; i++) {
            for(int j = 0; j < imageHeight; j++) {
                imageArray[i][j] /= std;
            }
        }

        while(patchCount < numPatches && tryCount < numPatches) {
            tryCount += 1;

            //Create random X and Y value for patch
            int px = new Random().nextInt(imageWidth - patchWidth + 1);
            int py = new Random().nextInt(imageHeight - patchWidth + 1);

//          int px = ThreadLocalRandom.current().nextInt(0, imageWidth - patchWidth + 1);
//          int py = ThreadLocalRandom.current().nextInt(0, imageHeight - patchWidth + 1);

            //px = 200;
            //py = 150;

            patchSample = subMatrix(imageArray, px, py, patchWidth);	//grab patch from image array
            double patchStd = Math.sqrt(findVariance(patchSample, patchWidth, patchWidth, findMatrixMean(patchSample, patchWidth, patchWidth)));	//find standard deviation of the patch

            if(patchStd > 0.0) {
                patch = reshapeMatrix(patchSample, numPixels);	//Reshape 2-dimensional array to 1-dimensional array
                double patchMean = findArrayMean(patch);		//calculate mean of the array
                for(int i = 0; i < patch.length; i++) {
                    patch[i] = patch[i] - patchMean;			//subtract mean from each element in array
                }
                for(int i = 0; i < patch.length; i++) {
                    imgPatches[i][patchCount] = patch[i];		//copy individual patch into array of patches
                }
                patchCount += 1;
            }
        }

        return imgPatches;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap showPatches(double[][] prePatches, int showPatchNum) {

        double[][] patches = prePatches;

        int dataDim = patches.length;				//number of patches in the 2-dimensional array
        int patchWidth = (int)Math.sqrt(dataDim);	//calculate  width of a patch

        double[][] displayPatch = new double[dataDim][showPatchNum];	//final 2-dimensional with all patches and gaps between patches

        double[] patch = new double[dataDim];

        for(int i = 0; i < showPatchNum; i++) {
            int patch_i = i;
            for(int j = 0; j < dataDim; j++) {
                patch[j] = patches[j][patch_i];
            }
            double pmax = findMax(patch);
            double pmin = findMin(patch);

            //Not entirely sure why we need this if statement since it should ALWAYS run
            //But I will keep it in because the base code does
            if(pmax > pmin) {
                for(int k = 0; k < patch.length; k++) {
                    patch[k] = (patch[k] - pmin) / (pmax - pmin);	//convert values so they range from 0 to 1
                }
            }

            for(int j = 0; j < dataDim; j++) {
                displayPatch[j][i] = patch[j];
            }
        }

        int bw = 5;			//Border Width
        int pw = patchWidth;

        int patchesY = (int)Math.sqrt(showPatchNum);
        int patchesX = (int)Math.ceil(((double)showPatchNum) / patchesY);

        double[][] patchImg = new double[(pw + bw) * patchesX - bw][patchesY * (pw + bw) - bw];

        double dpMax = findMax(displayPatch);

        for(int i = 0; i < patchImg.length; i++) {
            for(int j = 0; j < patchImg[i].length; j++) {
                patchImg[i][j] = dpMax;
            }
        }

        for (int i = 0; i < showPatchNum; i++) {
            int y_i = floorDiv(i,  patchesY);
            int x_i = i % patchesY;

            double[][] reshaped = new double[pw][pw];
            int x = 0;
            for (int j = 0; j < reshaped.length; j++) {
                for (int j2 = 0; j2 < reshaped[j].length; j2++) {
                    reshaped[j][j2] = displayPatch[x++][i];
                }
            }

            int rX = 0;
            for(int row = x_i * (pw + bw); row < x_i * (pw + bw) + pw; row++) {
                int rY = 0;
                for(int col = y_i * (pw + bw); col < y_i * (pw + bw) + pw; col++) {
                    patchImg[row][col] = reshaped[rX][rY];
                    rY++;
                }
                rX++;
            }
        }


        int xLength = patchImg.length;
        int yLength = patchImg[0].length;
        //BufferedImage buff = new BufferedImage(xLength, yLength, BufferedImage.TYPE_3BYTE_BGR);		//create blank image to be written to
        Bitmap buff = Bitmap.createBitmap(xLength, yLength, Bitmap.Config.ARGB_8888);
        buff.setHasAlpha(false);
        for(int x = 0; x < xLength; x++) {
            for(int y = 0; y < yLength; y++) {
                int value = (int)(patchImg[x][y] * 255);	//convert pixel value to 0 to 255 range
                int rgb = value << 16 | value << 8 | value;	//assign red, green, and blue values to the same to create a greyscale image
                //Log.d("Color", String.valueOf(rgb));
                buff.setPixel(x, y, rgb);		//assign pixel to rbg value
            }
        }

        return buff;
    }

    public static double[][] transpose(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        double[][] transposed = new double[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }
}
