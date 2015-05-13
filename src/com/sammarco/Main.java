package com.sammarco;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    /**
     * The driver method for ArtistPairEvaluator to determine duplicate artist pairs in a file containing a list of lists of artists.
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the input file: ");
        String inputFileName = sc.next();
        System.out.print("Enter the minimum matches we're interested in: ");
        int matchMin = Integer.parseInt(sc.next()); //How many duplicate pairs do we count before considering it a result.
        System.out.print("Enter the output file: ");
        String outputFileName = sc.next();
        try {
            ArtistPairEvaluator artistEvaluator = new ArtistPairEvaluator(inputFileName, matchMin,
                    CountMinSketch.DEFAULT_EPSILON, CountMinSketch.DEFAULT_DELTA);
            long now = System.currentTimeMillis();
            artistEvaluator.findBrutePairs();
            long doneTime = System.currentTimeMillis() - now;
            System.out.println("Runtime Find Brute Pairs: " + doneTime / 1000.0);
            now = System.currentTimeMillis();
            artistEvaluator.findPairs();
            doneTime = System.currentTimeMillis() - now;
            System.out.println("Runtime Find Pairs: " + doneTime / 1000.0);
            artistEvaluator.saveResults(outputFileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
