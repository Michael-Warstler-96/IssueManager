package edu.ncsu.csc216.issue_manager.model.io;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.ncsu.csc216.issue_manager.model.issue.Issue;

/**
 * Reads contents from given fileName and attempts to create a list of issue
 * objects. Uses helper method processIssue(String) to acquire specific issue
 * state's from the text in the file.
 * 
 * @author Michael Warstler
 */
public class IssueReader {

	/**
	 * Reads in file text from parameter fileName and attempts to create a list of
	 * issues. Contents of fileName are initially kept as one string. Helper method
	 * processIssue() is called to specifically process each issue detected the the
	 * file.
	 * 
	 * @param fileName to read issues from.
	 * @return is an array list of issue objects created from the file.
	 * @throws IllegalArgumentException if there is an error in processing the file
	 *                                  or the file cannot be found.
	 */
	public static ArrayList<Issue> readIssuesFromFile(String fileName) {
		try {
			Scanner fileReader = new Scanner(new FileInputStream(fileName)); // create scanner to read file.
			ArrayList<Issue> issues = new ArrayList<Issue>(); // create empty array list.
			String fileContents = "";
			// Read entire contents of a file and organize into one string based on expected
			// input.
			while (fileReader.hasNextLine()) {
				fileContents += fileReader.nextLine() + "\n";
			}
			// Create a new scanner that will read the fileContents string and separate
			// contents into issues by a delimiter.
			Scanner fileFormatReader = new Scanner(fileContents);
			fileFormatReader.useDelimiter("\\r?\\n?[*]");
			// For each "issue" read in via the scanner with delimiter, use processIssue to
			// get an Issue object.
			while (fileFormatReader.hasNext()) {
				Issue processedIssue = processIssue(fileFormatReader.next());
				issues.add(processedIssue);
			}

			// Close Scanners
			fileFormatReader.close();
			fileReader.close();
			// Return the ArrayList with all the issues
			return issues;

		} catch (FileNotFoundException | IllegalArgumentException e) {
			// If any issues were found trying to read the file or process contents.
			throw new IllegalArgumentException("Unable to load file.");
		}
	}

	/**
	 * Processes contents from a designated string representing an issue. After
	 * reading in Issue parameters, creates an Issue object. If any invalid data
	 * formats are required, an exception is allowed to propagate to the caller in
	 * the IssueReader method readIssuesFromFile.
	 * 
	 * @param issue is line of text for processing
	 * @return is an issue object created from processing the line(s) of text.
	 */
	private static Issue processIssue(String issue) {
		// Initialize local variables to create an Issue object with.
		int id = 0;
		String state = "";
		String issueType = "";
		String summary = "";
		String owner = "";
		boolean confirmed = false;
		String resolution = "";
		ArrayList<String> notes = new ArrayList<String>();

		// Create scanner to view entire Issue string (parameters/fields line and notes
		// lines)
		Scanner issueScanner = new Scanner(issue);
		// Separate the parameters line into its own String
		String issueFields = issueScanner.nextLine();
		// Assign a new scanner to look at exclusively the parameters/fields line.
		Scanner issueFieldsScanner = new Scanner(issueFields);
		issueFieldsScanner.useDelimiter(","); // Delimiter for this line is ','.

		// Go through Issue text and assign values between delimiter to specific Issue
		// parameters.
		id = issueFieldsScanner.nextInt();
		// Read in simple strings for state, issue type, summary, and owner
		state = issueFieldsScanner.next();
		issueType = issueFieldsScanner.next();
		summary = issueFieldsScanner.next();
		owner = issueFieldsScanner.next();
		// Read in boolean for confirmed.
		confirmed = issueFieldsScanner.nextBoolean();
		// Read in string resolution.
		if (issueFieldsScanner.hasNext()) {
			resolution = issueFieldsScanner.next(); // Last token either has resolution or "".
		} else {
			resolution = "";
		}

		// Get Issue notes using the main issueScanner with a unique delimiter. Each
		// token processed via delimiter/scanner is added to the notes array list.
		// Citing assistance from Arsalaan Khan: .trim() String method to remove
		// unnecessary whitespace from a read in note.
		issueScanner.useDelimiter("\r?\n?[-]");
		while (issueScanner.hasNext()) {
			notes.add(issueScanner.next().trim());
		}

		// Close Scanners.
		issueScanner.close();
		issueFieldsScanner.close();

		// Create an Issue with read in contents.
		Issue processedIssue = new Issue(id, state, issueType, summary, owner, confirmed, resolution, notes);
		return processedIssue;
	}
}
