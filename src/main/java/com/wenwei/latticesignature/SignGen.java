package com.wenwei.latticesignature;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SignGen {
    public static void main(String[] args) {
        int n = 512;
        int d = 1;
        int dimension = 2 * n;
        int k_ = 14;
        int q = (int) Math.pow(2, 24);
        int sigma = (int) (12 * d * k_ * Math.sqrt(dimension));
        int k = 512;
        double M = Math.pow(Math.E, 12 * d * k_ * Math.sqrt(dimension) / sigma + Math.pow(d * k_ * Math.sqrt(dimension) / 2 / sigma, 2));
        //int dimension = 1024;
        //double sigma = 5376;
        //int q = (int) Math.pow(2, 24);
        //int k=512;
        //int d=1;
        //double M=2.72;

        //sign
        int[][] A = Sampling.SampleZ(n, dimension, q);//verification key 1
        int[][] S = Sampling.SampleS(dimension, k, d);//signing key
        int[][] T = mul(A, S);//verification key 2
        String message = "123";
        int[] signz=null;
        int[] signc=null;
        boolean ready=false;
        int count=1;
        while(!ready) {
            System.out.println("第"+count+"次");
            count++;
            int[] y = Sampling.GaussianSampling(dimension, sigma, new int[dimension]);
            int[] c = H_512(vector2str(mul(A, y)) + message);
            int[] z = add(mul(S, c), y);
            if(RejectSampling.isoutput(dimension,sigma,M,new int[dimension],mul(S,c),z)){
                ready=true;
                signz=z;
                signc=c;
            }
        }

        //verify
        if (norm(signz) <= 2 * sigma * Math.sqrt(dimension) &&
                isequals(signc, H_512(vector2str(sub(mul(A, signz), mul(T, signc))) + message))) {
            System.out.println("Accept");
        } else {
            System.out.println("reject");
        }

    }

    public static int[] H_512(String inStr) {//先获得512bit sha哈希值，再自己设计方法将0,1->-1,0,1
        MessageDigest sha = null;

        try {
            sha = MessageDigest.getInstance("SHA-512");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sha.update(inStr.getBytes(StandardCharsets.UTF_8));
        byte[] digest = sha.digest();

        int[] result = new int[digest.length * 8];
        for (int i = 0; i < digest.length; i++) {
            int[] temp = byteToBitOfArray(digest[i]);
            for (int j = 0; j < 8; j++) {
                result[i * 8 + j] = temp[j];
            }
        }

        for (int i = digest.length * 8-1; i > 0; i--) {
            if (result[i] == 0) continue;
            if (result[i - 1] == 1)
                result[i] = -1;
        }
        return result;
    }

    public static int[] byteToBitOfArray(byte b) {
        int[] array = new int[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (int) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }
    public static int[][] mul(int[][] A, int[][] B){
        if(A[0].length!=B.length)
            return null;
        int[][] result=new int[A.length][B[0].length];
        for(int i=0;i<A.length;i++){
            for(int j=0;j<B[0].length;j++){
                int temp=0;
                for(int k=0;k<A[0].length;k++){
                    temp+=A[i][k]*B[k][j];
                }
                result[i][j]=temp;
            }
        }
        return result;
    }
    public static int[] mul(int[][] A, int[] B){
        if(A[0].length!=B.length)
            return null;
        int[] result=new int[A.length];
        for(int i=0;i<A.length;i++){
                int temp=0;
                for(int j=0;j<A[0].length;j++){
                    temp+=A[i][j]*B[j];
                }
                result[i]=temp;
        }
        return result;
    }
    public static String vector2str(int[] v){
        String result="";
        for(int i=0;i<v.length;i++){
            result+=v[i];
        }
        return result;
    }
    public static int[] add(int[] A, int[] B){
        int[] result=new int[A.length];
        for(int i=0;i<A.length;i++){
            result[i]=A[i]+B[i];
        }
        return result;
    }
    public static int[] sub(int[] A, int[] B){
        int[] result=new int[A.length];
        for(int i=0;i<A.length;i++){
            result[i]=A[i]-B[i];
        }
        return result;
    }
    public static int norm(int[] v){
        int result=0;
        for(int i=0;i<v.length;i++){
            result+=v[i]*v[i];
        }
        return result;
    }
    public static boolean isequals(int[] A, int[] B){
        for(int i=0;i<A.length;i++){
            if(A[i]!=B[i]){
                return false;
            }
        }
        return true;
    }
}
