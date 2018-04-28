package hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A class that generates a perfect hash table.
 *
 * @author Stamatios Morellas (morellas@iastate.edu)
 */
public class PerfectHashGenerator {
	/**
	 * The number of rows in the T1 and T2 tables. Enough to fit most English words.
	 */
	private static final int TABLE_ROWS = 8;

	/**
	 * The number of columns in the T1 and T2 tables. Enough to fit all English
	 * letters.
	 */
	private static final int TABLE_COLUMNS = 64;

	// The main method that runs the entire program
	public static void main(String[] args) {
		if (null == args || 1 > args.length || 3 < args.length) {
			System.err.println("Usage: <word list> [prefix] [seed]");
			System.exit(1);
			return;
		}

		String prefix = args[1];
		
		// TODO - DONE
		
		// Create a new random number generator object
		Random rng = new Random();

		PerfectHashGenerator gen = new PerfectHashGenerator();

		try {
			gen.generate(args[0], prefix + "CHM92Hash", rng);
		}

		catch (IOException e) {
			System.err.println(e);
			System.exit(2);
			return;
		}
	}

	/**
	 * Generates the perfect hash table for the words in the indicated file, and
	 * writes the generated code to the appropriate file
	 * ({@code outputClassName + ".java"}).
	 *
	 * @param wordFileName
	 *            the name of the word file
	 * @param outputClassName
	 *            the name of the output class
	 * @param rng
	 *            the random number generator for the generated hash table
	 *
	 * @throws IOException
	 *             if the input file cannot be read or the output file cannot be
	 *             written
	 * @throws IllegalArgumentException
	 *             if the given output class name is not a valid Java identifier
	 */
	public void generate(String wordFileName, String outputClassName, Random rng) throws IOException, IllegalArgumentException {
		// TODO – DONE

		// Handle exceptions
		if (wordFileName == null || outputClassName == null || rng == null) {
			throw new IOException("One or more of the parameters in the generate() method call is null");
		}
		if (!outputClassName.matches("[A-Za-z_][A-Za-z_0-9]*")) {
			throw new IllegalArgumentException("invalid class name \"" + outputClassName + "\"");
		}
		
		// Create a list of words from the word file
		List<String> words = readWordFile(wordFileName);
		
		// Generate the tables
		int[][] table1 = new int[TABLE_ROWS][TABLE_COLUMNS];
		int[][] table2 = new int[TABLE_ROWS][TABLE_COLUMNS];
		
		// Create the modulus, which is 2n+1
		int mod = 2 * words.size() + 1;

		// Perform mapping
		Graph map = mapping(table1, table2, mod, rng, words);
		
		// Fill the gArray
		int[] gArray = map.fillGArray(words.size());
		
		// Create a new CodeGenerator object to write to the output
		CodeGenerator g = new CodeGenerator(table1, table2, gArray, mod, words);
		
		// Create a new PrintStream to write the output
		OutputStream output = new PrintStream(outputClassName + ".java");
		
		// Generate the perfect hash table to the output
		g.generate(output, outputClassName);
	}

	/**
	 * Generates the perfect hash table for the given words, and writes the
	 * generated code to the given stream.
	 *
	 * @param words
	 *            the list of words for which to generate a perfect hash table
	 * @param output
	 *            the stream to which to write the generated code
	 * @param outputClassName
	 *            the name of the output class
	 * @param rng
	 *            the random number generator for the generated hash table
	 *
	 * @throws IllegalArgumentException
	 *             if the given output class name is not a valid Java identifier
	 */
	public void generate(List<String> words, OutputStream output, String outputClassName, Random rng) throws IllegalArgumentException { // DONE
		// TODO – DONE
		
		// Handle Exceptions
		if (!outputClassName.matches("[A-Za-z_][A-Za-z_0-9]*")) {
			throw new IllegalArgumentException("invalid class name \"" + outputClassName + "\"");
		}
		
		// Generate the tables
		int[][] table1 = new int[TABLE_ROWS][TABLE_COLUMNS];
		int[][] table2 = new int[TABLE_ROWS][TABLE_COLUMNS];
		
		// Create the modulus, which is 2n+1
		int mod = 2 * words.size() + 1;
		
		// Perform mapping
		Graph map = mapping(table1, table2, mod, rng, words);
		
		// Fill the gArray
		int[] gArray = map.fillGArray(words.size());
		
		// Create a new CodeGenerator object to write to the output
		CodeGenerator g = new CodeGenerator(table1, table2, gArray, mod, words);
		
		// Generate the perfect hash table to the output
		g.generate(output, outputClassName);
	}

	/**
	 * Performs the mapping step for generating the perfect hash table.
	 *
	 * Precondition: the list of keys contains no duplicate values.
	 *
	 * @param table1
	 *            the T1 table
	 * @param table2
	 *            the T2 table
	 * @param modulus
	 *            the modulus
	 * @param rng
	 *            the random number generator to use
	 * @param words
	 *            the list of keys for the hash table
	 * @return the generated graph
	 *
	 * @throws IllegalArgumentException
	 *             if the modulus is not positive
	 */
	private Graph mapping(int[][] table1, int[][] table2, int modulus, Random rng, List<String> words) throws IllegalArgumentException {

		// Handle Exceptions
		if (modulus <= 0) { // Note to self: This might have to be "modulus <= 0"
			throw new IllegalArgumentException("The modulus is not positive");
		}

		Graph toRet;

		do {

			toRet = new Graph(modulus);

			for (int r = 0; r < TABLE_ROWS; ++r) {
				for (int c = 0; c < TABLE_COLUMNS; ++c) {
					table1[r][c] = rng.nextInt(modulus);
					table2[r][c] = rng.nextInt(modulus);
				}
			}

			for (int i = 0; i < words.size(); i++) {
				String w = words.get(i);
				int f1 = 0, f2 = 0;

				for (int j = 0; j < w.length(); ++j) {
					f1 += table1[j % TABLE_ROWS][w.charAt(j) % TABLE_COLUMNS];
					f2 += table2[j % TABLE_ROWS][w.charAt(j) % TABLE_COLUMNS];
				}

				f1 %= modulus;
				f2 %= modulus;

				toRet.addEdge(f1, f2, i, w);
			}
		} while (toRet.hasCycle());

		System.out.print(toRet);
		return toRet;
	}

	/**
	 * Reads the indicated file, making a list containing the lines within it.
	 *
	 * @param fileName
	 *            the file to read
	 * @return a list containing the lines of the indicated file
	 *
	 * @throws FileNotFoundException
	 *             if the indicated file cannot be read
	 */
	private List<String> readWordFile(String fileName) throws FileNotFoundException { // DONE
		// TODO – DONE
		
		// Declare a variable that will contain the lines from the file
		ArrayList<String> words = new ArrayList<String>();

		// Handle the potential exception
		if (fileName == null) {
			throw new FileNotFoundException();
		}

		// Create a new File with the given fileName
		File file = new File(fileName);

		// Create a new Scanner to read from the given file
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			words.add(sc.nextLine());
		}

		// Close the scanner at the end
		sc.close();

		return words;
	}
}
