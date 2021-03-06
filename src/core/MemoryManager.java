package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MemoryManager {
	// MAC
	// private static String INDEX_DIR =
	// "/Applications/Eclipse.app/Contents/MacOS/indexes/";
	// private static String OFFLINE_DATA =
	// "/Applications/Eclipse.app/Contents/MacOS/tweets/";
	private static String INDEX_DIR = "indexes/";
	private static String OFFLINE_DATA = "tweets/";
	// WINDOWS
//	private static String INDEX_DIR = "C:/Users/gdemos01/Desktop/History/eclipse/indexes/";
	// private static String OFFLINE_DATA =
	// "C:/Users/gdemos01/Desktop/History/eclipse//tweets/";
	// private static String INDEX_DIR;
	// private static String OFFLINE_DATA;

	public static void setPaths(String base) {
		INDEX_DIR = base + INDEX_DIR;
		OFFLINE_DATA = base + OFFLINE_DATA;
		return;
	}

	/**
	 * Saves a serializable tweet to memory.
	 * 
	 * @param serObj
	 * @param filepath
	 */
	public static void writeTweetToFile(Tweet serObj, String filepath) {

		try {
			FileOutputStream fileOut = new FileOutputStream(OFFLINE_DATA + filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serObj);
			objectOut.close();
//			System.out.println("The Object  was successfully written to a file");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Tweet readTweetFromFile(String filepath) {

		try {
			FileInputStream fileIn = new FileInputStream(OFFLINE_DATA + filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			Tweet tweet = (Tweet) objectIn.readObject();

			// System.out.println("The Object has been read from the file. " +
			// tweet.getDocID() + " " + tweet.getText() + " obj=" + tweet.getDictionary());

			objectIn.close();
			return tweet;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static boolean storeIndex(InvertedIndex index) {
		// Check if the indexes directory does not exist and create it
		File directory = new File(INDEX_DIR);
		String filepath = INDEX_DIR + "index";
		if (!directory.exists()) {
			directory.mkdir();

			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}

		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOut = new FileOutputStream(INDEX_DIR + "index");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(index);
			objectOut.close();
			System.out.println("The Object  was succesfully written to a file");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public static InvertedIndex loadIndex() {
		try {
			FileInputStream fileIn = new FileInputStream(INDEX_DIR + "index");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			InvertedIndex storedIndex = (InvertedIndex) objectIn.readObject();

			// System.out.println("The Object has been read from the file.\n");
			objectIn.close();
			return storedIndex;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static boolean deleteIndex() {
		// Check if the indexes directory does not exist and create it
		File file = new File(INDEX_DIR + "index");

		if (file.delete()) {
			System.out.println("Index deleted from the index directory.");
			return true;
		} else {
			System.err.println("[!] Index doesn't exists in the index directory.");
			return false;
		}

	}

	public static InvertedIndex loadIndexState() {
		// Load index if exist along with the tweets
		File indexfile = new File(INDEX_DIR + "index");
		// File indexfile = getServletContext().getRealPath("");

		// Path p = Paths.get("~/eclipse-workspace/TweeagleAPI/");
		// String sa = p.toAbsolutePath().toString();
		// System.out.println(sa);

		if (!indexfile.exists()) {
			// The index does not exist, so do not load anything
			return null;
		}

		// System.out.println("Index:"+loadIndex());

		return loadIndex();
	}

	/**
	 * Loads every tweet from memory to an ArrayList.
	 * 
	 * @return
	 */
	public static ArrayList<Tweet> loadTweetsState() {
		// Load index if exist along with the tweets
		File tweetsDir = new File(OFFLINE_DATA);

		if (!tweetsDir.exists()) {
			// The index does not exist, so do not load anything
			return null;
		}

		// List all files of the corpus directory
		tweetsDir = new File(OFFLINE_DATA);
		String[] files = tweetsDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		// Load tweets to main memory
		for (String f : files) {
			tweets.add(readTweetFromFile(f));
		}

		return tweets;
	}

	public static void initializeTweetsDirectory() {
		// Check if the indexes directory does not exist and create it
		File directory = new File(OFFLINE_DATA);

		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}
	}

	public static boolean deleteTweet(Tweet tweet) {
		// Check if the tweet exist delete it
		File file = new File(OFFLINE_DATA + tweet.getDocID() + ".txt");
		if (file.delete()) {
			System.out.println("Tweet deleted from the index");
			return true;
		} else {
			System.err.println("[!] Tweet doesn't exists in the index");
			return false;
		}

	}

	public static void eraseCashe() {
		File offline_data = new File(OFFLINE_DATA);
		File index = new File(INDEX_DIR);
		File[] allContents = index.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				file.delete();
			}
		}
		allContents = offline_data.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				file.delete();
			}
		}
		System.out.println("Cache erased successfully!");
	}

}