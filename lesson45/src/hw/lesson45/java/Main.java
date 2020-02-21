package hw.lesson45.java;

import hw.lesson45.java.lesson45.Lesson45Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Lesson45Server("localhost", 9890).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
