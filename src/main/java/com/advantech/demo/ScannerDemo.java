/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.demo;

import java.util.Scanner;

/**
 *
 * @author Wei.Cheng
 */
public class ScannerDemo {

    public static void main(String[] args) {
        OUTER:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please input your name: ");
            System.out.printf("Hello! %s!\n\n", scanner.nextLine());

            INNER:
            while (true) {
                System.out.print("EXIT? (Y/N) : ");
                String inputText = scanner.next();
                switch (inputText) {
                    case "Y":
                    case "y":
                        break OUTER;
                    case "N":
                    case "n":
                        break INNER;
                    default:
                        System.out.println("Please check your input");
                }
            }
        }
    }
}
