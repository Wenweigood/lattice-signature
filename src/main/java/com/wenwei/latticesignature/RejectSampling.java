package com.wenwei.latticesignature;

import java.util.Random;

public class RejectSampling {
    static Random random=new Random();
    public static void main(String[] args) {
        int temp=1;
        System.out.println(-temp);
    }

    public static boolean isoutput(int dimension, double sigma, double M, int[] center1, int[] center2, int[] z){
        if(random.nextDouble(1)<probability(dimension,sigma,M,center1,center2,z))
            return true;
        return false;
    }

    public static double probability(int dimension, double sigma, double M, int[] center1, int[] center2, int[] z) {
        double p=f(dimension, sigma, center1, z) / f(dimension, sigma, center2, z) / M;
        System.out.println("概率："+p);
        return p>1?1.0:p;
    }

    public static double f(int dimension, double sigma, int[] center, int[] x) {
        int temp = 0;
        for (int i = 0; i < center.length; i++) {
            temp += (center[i] - x[i]) * (center[i] - x[i]);
        }
        temp = -temp;
        return Math.pow(Math.E, temp / (2 * sigma * sigma));//前缀在分子分母一样，忽略，不忽略时过小（等于0.0）
        //Math.pow(1.0 / Math.sqrt(2 * Math.PI * sigma * sigma), dimension)* Math.pow(Math.E, temp / (2 * sigma * sigma));
    }
}
