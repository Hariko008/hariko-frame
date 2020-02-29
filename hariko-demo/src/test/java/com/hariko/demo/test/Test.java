package com.hariko.demo.test;



public class Test {
    public static void main(String[] args) {
        Exception e = new NullPointerException();

        getDuplicateNum("sss", e.getMessage());
    }

    public static int getDuplicateNum(String a, String ... args) {
        String[] str = {null};
        System.out.println(str[0]);

        System.out.println(args.length);
        System.out.println((char[]) null);
        System.out.println(args[0].getClass());
        System.out.println(args[0].substring(1));
        return 0;
    }
}
