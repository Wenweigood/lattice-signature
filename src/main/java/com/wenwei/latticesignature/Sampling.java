package com.wenwei.latticesignature;

import java.util.Random;

public class Sampling {
    static Random random = new Random();

    public static int[] GaussianSampling(int dimension, double sigma, int[] center) {
        int[] vector = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = (int) (sigma * random.nextGaussian()) + center[i];
        }
        return vector;
    }

    public static int[][] SampleZ(int n, int m, int q) {//sample from Z_q
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = random.nextInt(q);
            }
        }
        return matrix;
    }

    public static int[][] SampleS(int m, int k, int d) {//sample S from {-d,...,0,...,,d}
        int[][] matrix = new int[m][k];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                matrix[i][j] = random.nextInt(2*d+1)-d;
            }
        }
        return matrix;
    }
}
