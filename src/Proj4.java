/***
 * @file: Proj4.java
 * @description: Main class for the project. It runs timing tests for operations on a hash table using
 *               different lists.
 * @author: Keira Yu
 * @date: December 5, 2024
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj4 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        // Store data into an arraylist
        ArrayList<Movie> movieList = new ArrayList<>();

        while (inputFileNameScanner.hasNextLine() && movieList.size() < numLines) {
            String line = inputFileNameScanner.nextLine();
            String[] data = line.split(",");
            String title = data[0];
            int totalGross = Integer.parseInt(data[1]);
            String releaseDate = data[2];
            String distributor = data[3];

            Movie movie = new Movie(title, totalGross, releaseDate, distributor);
            movieList.add(movie);
        }
        inputFileNameScanner.close();

        ArrayList<Movie> sortedList = new ArrayList<>(movieList);
        ArrayList<Movie> shuffledList = new ArrayList<>(movieList);
        ArrayList<Movie> reversedList = new ArrayList<>(movieList);
        Collections.sort(sortedList);
        Collections.shuffle(shuffledList);
        Collections.sort(reversedList, Collections.reverseOrder());

        FileWriter writer = new FileWriter("analysis.txt",true);

        System.out.println("Number of Lines Evaluated: " + numLines);
        operate("Sorted", sortedList, writer);
        operate("Shuffled", shuffledList, writer);
        operate("Reversed", reversedList, writer);

        writer.close();
    }

    private static void operate(String label, ArrayList<Movie> movieList, FileWriter writer) throws IOException {
        SeparateChainingHashTable<Movie> hashTable = new SeparateChainingHashTable<>();

        // Measure insertion time
        long startTime = System.nanoTime();
        for (Movie movie : movieList) {
            hashTable.insert(movie);
        }
        long insertTime = System.nanoTime() - startTime;

        // Measure search time
        startTime = System.nanoTime();
        for (Movie movie : movieList) {
            hashTable.contains(movie);
        }
        long searchTime = System.nanoTime() - startTime;

        // Measure deletion time
        startTime = System.nanoTime();
        for (Movie movie : movieList) {
            hashTable.remove(movie);
        }
        long deleteTime = System.nanoTime() - startTime;

        // Print to screen
        System.out.println(label + " List:");
        System.out.printf("Insertion Time: %d ns%n", insertTime);
        System.out.printf("Search Time: %d ns%n", searchTime);
        System.out.printf("Deletion Time: %d ns%n", deleteTime);

        // Write to output file
        writer.write(String.format("%s,%d,%d,%d,%d%n", label, movieList.size(), insertTime, searchTime, deleteTime));
    }


}
