package com.sammarco;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <p>CountMinSketch is a probabilistic data structure that serves as a frequency table in a stream of data.
 * It uses hash functions to map elements to frequencies in sublinear space, at the expense of a higher incidence of
 * overcounting some elements. However, this false positive diminishes as more elements are added.
 * <br/>
 * More information can be found here: <a href="http://en.wikipedia.org/wiki/Count%E2%80%93min_sketch">Count Min Sketch</a>
 *
 * <p>This java implementation has been based on the python implementation of a heap sketch linked below
 * which in turn is based on the C version originally by G. Cormode and S. Muthukrisnan
 *
 * Created by Matt Sammarco
 * @see <a href="https://tech.shareaholic.com/2012/12/03/the-count-min-sketch-how-to-count-over-large-keyspaces-when-about-right-is-good-enough/">How to min sketch over large keyspaces</a>
 */
public class CountMinSketch {
    // Epsilon and Delta determine the accuracy of the sketch.
    // These particular values are reflective of the domain and of the error margin.
    public static final float DEFAULT_EPSILON = 0.00001f;
    public static final float DEFAULT_DELTA = 0.0005f;

    private int [][] matrix;
    private int d;
    private int w;

    private static final String HASH_NAME = "MD5"; // SHA1 can be used or murmurhash is another option for increased precision.
    private static final MessageDigest DIGEST_FUNCTION;
    static { // The digest method is reused between instances
        MessageDigest tmp;
        try {
            tmp = java.security.MessageDigest.getInstance(HASH_NAME);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        DIGEST_FUNCTION = tmp;
    }


    /**
     * Creates CountMinSketch based of the precision variables epsilon and delta. The error of the min count is within a
     * factor of epsilon with probability delta.
     *
     * @param epsilon This factor is used to derive the size of the sketch matrix
     * @param delta This factor is used to derive the size of the sketch matrix
     */
    public CountMinSketch(float epsilon, float delta) throws IllegalArgumentException {
        if (epsilon <= 0 || epsilon >= 1) {
            throw new IllegalArgumentException("Epsilon must be between 0 and 1.");
        }
        if (delta <= 0 || delta >= 1) {
            throw new IllegalArgumentException("Delta must be between 0 and 1.");
        }
        this.d = (int) Math.ceil(Math.exp(1.0) / epsilon);
        this.w = (int) Math.ceil(Math.log(1.0 / delta));
        this.matrix = new int[w][d];
    }

    /**
     * Create the CountMinSketch class with sketch matrix of w x d. For greater accuracy larger values of w and d can
     * be given.
     *
     * @param w The width of the CountMinSketch matrix
     * @param d The depth of the CountMinSketch matrix
     */
    public CountMinSketch(int w, int d) throws IllegalArgumentException {
        if (w <= 0 || d <= 0) {
            throw new IllegalArgumentException("The CountMinSketch matrix must have a depth and width greater than 0.");
        }
        this.w = w;
        this.d = d;
        this.matrix = new int[w][d];
    }

    /**
     * Computes hashes for a given string element and inserts these hashes into our matrix. This is really doing two
     * things. The update of the matrix sketch, and tracking the minimum count to be returned.
     *
     * @param element The thing you want to insert
     * @return The estimated minimum count found of this element after insertion
     */
    public int insert(String element) {
        int minCount = -1; // min count stores the minimum value found during matrix insertion.
        int k = 0;
        byte salt = 0;
        while (k < w) {
            DIGEST_FUNCTION.update(salt);
            salt++;
            byte[] digest = DIGEST_FUNCTION.digest(element.getBytes());
            // Split the digest into ints to derive as indices
            for (int i = 0; i < digest.length / 4 && k < w; i++) {
                int hash = 0;
                for (int j = (i * 4); j < (i * 4) + 4; j++) {
                    hash <<= 8;
                    hash |= ((int) digest[j]) & 0xFF;
                }
                int j = java.lang.Math.abs(hash % d);
                int count = ++matrix[k][j];
                if (count >= 0 && (minCount == -1 || count < minCount)) {
                    minCount = count;
                }
                k++;
            }
        }
        return minCount;
    }
}
