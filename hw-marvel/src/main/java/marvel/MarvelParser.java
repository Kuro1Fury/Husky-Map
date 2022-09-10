/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package marvel;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Parser utility to load the Marvel Comics dataset.
 */
public class MarvelParser {

    /**
     * Reads the Marvel Universe dataset. Each line of the input file contains a character name and a
     * comic book the character appeared in, separated by a comma character
     *
     * @spec.requires filename is a valid file in the resources/data folder.
     * @param filename the file that will be read
     * @return a map which key is book name and value is character
     */
    public static Map<String,ArrayList<String>> parseData(String filename) {
        List<String> lines = readLines(filename);

        Map<String,ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
        for (var line : lines) {
            String chara = line.split(",")[0];
            String book = line.split(",")[1];
            if (result.containsKey(book)) {
                result.get(book).add(chara);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(chara);
                result.put(book, list);
            }
        }
        return result;

        // You'll need to:
        //  - Split each line into its individual parts
        //  - Collect the data into some convenient data structure(s) to return to the graph building code
    }

    /**
     * Reads all lines contained within the provided data file, which is located
     * relative to the data/ folder in this parser's classpath.
     *
     * @param filename The file to read.
     * @throws IllegalArgumentException if the file doesn't exist, has an invalid name, or can't be read
     * @return A new {@link List<String>} containing all lines in the file.
     */
    private static List<String> readLines(String filename) {
        // You can use this code as an example for getting a file from the resources folder
        // in a project like this. If you access data files elsewhere in your code, you'll need
        // to use similar code. If you use this code elsewhere, don't forget:
        //   - Replace 'MarvelParser' in `MarvelParser.class' with the name of the class you write this in
        //   - If the class is in src/main, it'll get resources from src/main/resources
        //   - If the class is in src/test, it'll get resources from src/test/resources
        //   - The "/" at the beginning of the path is important
        // Note: Most students won't re-write this code anywhere, this explanation is just for completeness.
        InputStream stream = MarvelParser.class.getResourceAsStream("/data/" + filename);
        if (stream == null) {
            // stream is null if the file doesn't exist.
            // We want to handle this case so we don't try to call
            // readLines and have a null pointer exception.
            throw new IllegalArgumentException("No such file: " + filename);
        }
        return new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.toList());
    }

//    public static void main(String[] args) {
//        Set<String[]> result = parseData("marvel.csv");
//        for(String[] data : result) {
//            System.out.println(data[0]);
//            System.out.print(data[1]);
//        }
//    }
}
