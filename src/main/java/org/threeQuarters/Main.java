package org.threeQuarters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path videoRootPath = Paths.get(".","cache","video");

    public void test() throws IOException {
        System.out.println(getClass().getClassLoader().getResource("").getPath().substring(1));
        System.out.println(new File("").getCanonicalPath());
        System.out.println(videoRootPath.toString());
    }

    public static void main(String[] args) {
        try {
            new Main().test();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}