package com.jeremy.lychee.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;

public class FileHelper {
	/**
	 * @param fromFile
	 * @param toFile
	 */
	public static void copyFile(InputStream fromFile, OutputStream toFile)
			throws IOException {
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = fromFile.read(buffer)) > 0) {
				toFile.write(buffer, 0, length);
			}
		}
		// Close the streams
		finally {
			try {
				if (toFile != null) {
					try {
						toFile.flush();
					} finally {
						toFile.close();
					}
				}
			} finally {
				if (fromFile != null) {
					fromFile.close();
				}
			}
		}
	}

	/**
	 * @param fromFile
	 * @param toFile
	 */
	public static void copyFile(String fromFile, String toFile)
			throws IOException {
		copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
	}

	/**
	 * 
	 * @param fromFile
	 * @param toFile
	 */
	public static void copyFile(File fromFile, File toFile) throws IOException {
		copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
	}

	/**
	 * @param fromFile
	 * @param toFile
	 */
	public static void copyFile(FileInputStream fromFile,
			FileOutputStream toFile) throws IOException {
		FileChannel fromChannel = fromFile.getChannel();
		FileChannel toChannel = toFile.getChannel();

		try {
			fromChannel.transferTo(0, fromChannel.size(), toChannel);
		} finally {
			try {
				if (fromChannel != null) {
					fromChannel.close();
				}
			} finally {
				if (toChannel != null) {
					toChannel.close();
				}
			}
		}
	}

	/**
	 * Parses a file containing sql statements into a String array that contains
	 * only the sql statements. Comments and white spaces in the file are not
	 * parsed into the String array. Note the file must not contained malformed
	 * comments and all sql statements must end with a semi-colon ";" in order
	 * for the file to be parsed correctly. The sql statements in the String
	 * array will not end with a semi-colon ";".
	 * 
	 * @param sqlFile
	 *            - String containing the path for the file that contains sql
	 *            statements.
	 * 
	 * @return String array containing the sql statements.
	 */
	public static String[] parseSqlFile(String sqlFile) throws IOException {
		return parseSqlFile(new BufferedReader(new FileReader(sqlFile)));
	}

	/**
	 * Parses a file containing sql statements into a String array that contains
	 * only the sql statements. Comments and white spaces in the file are not
	 * parsed into the String array. Note the file must not contained malformed
	 * comments and all sql statements must end with a semi-colon ";" in order
	 * for the file to be parsed correctly. The sql statements in the String
	 * array will not end with a semi-colon ";".
	 * 
	 * @param sqlFile
	 *            - InputStream for the file that contains sql statements.
	 * 
	 * @return String array containing the sql statements.
	 */
	public static String[] parseSqlFile(InputStream sqlFile) throws IOException {
		return parseSqlFile(new BufferedReader(new InputStreamReader(sqlFile)));
	}

	/**
	 * Parses a file containing sql statements into a String array that contains
	 * only the sql statements. Comments and white spaces in the file are not
	 * parsed into the String array. Note the file must not contained malformed
	 * comments and all sql statements must end with a semi-colon ";" in order
	 * for the file to be parsed correctly. The sql statements in the String
	 * array will not end with a semi-colon ";".
	 * 
	 * @param sqlFile
	 *            - Reader for the file that contains sql statements.
	 * 
	 * @return String array containing the sql statements.
	 */
	public static String[] parseSqlFile(Reader sqlFile) throws IOException {
		return parseSqlFile(new BufferedReader(sqlFile));
	}

	/**
	 * Parses a file containing sql statements into a String array that contains
	 * only the sql statements. Comments and white spaces in the file are not
	 * parsed into the String array. Note the file must not contained malformed
	 * comments and all sql statements must end with a semi-colon ";" in order
	 * for the file to be parsed correctly. The sql statements in the String
	 * array will not end with a semi-colon ";".
	 * 
	 * @param sqlFile
	 *            - BufferedReader for the file that contains sql statements.
	 * 
	 * @return String array containing the sql statements.
	 */
	public static String[] parseSqlFile(BufferedReader sqlFile)
			throws IOException {
		String line;
		StringBuilder sql = new StringBuilder();
		String multiLineComment = null;

		while ((line = sqlFile.readLine()) != null) {
			line = line.trim();

			// Check for start of multi-line comment
			if (multiLineComment == null) {
				// Check for first multi-line comment type
				if (line.startsWith("/*")) {
					if (!line.endsWith("}")) {
						multiLineComment = "/*";
					}
					// Check for second multi-line comment type
				} else if (line.startsWith("{")) {
					if (!line.endsWith("}")) {
						multiLineComment = "{";
					}
					// Append line if line is not empty or a single line comment
				} else if (!line.startsWith("--") && !line.equals("")) {
					sql.append(line);
				} // Check for matching end comment
			} else if (multiLineComment.equals("/*")) {
				if (line.endsWith("*/")) {
					multiLineComment = null;
				}
				// Check for matching end comment
			} else if (multiLineComment.equals("{")) {
				if (line.endsWith("}")) {
					multiLineComment = null;
				}
			}

		}

		sqlFile.close();

		return sql.toString().split(";");
	}
}