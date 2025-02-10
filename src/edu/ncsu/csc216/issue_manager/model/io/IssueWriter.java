package edu.ncsu.csc216.issue_manager.model.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import edu.ncsu.csc216.issue_manager.model.issue.Issue;

/**
 * Writes contents of an issue list to a designated output location. The printed
 * issue's are written in a format designated by the issue's toString() method.
 * 
 * @author Michael Warstler
 */
public class IssueWriter {

	/**
	 * Method receives a string with a file name to write to and a list of issue
	 * objects to write. Utilizes an issue object's toString() method to create the
	 * proper format.
	 * 
	 * @param fileName to write to.
	 * @param issues   are the list of issues to write out. Format given through
	 *                 issue toString() method.
	 * @throws IllegalArgumentException if there are issues trying to save to file.
	 */
	public static void writeIssuesToFile(String fileName, List<Issue> issues) {
		try {
			PrintStream fileWriter = new PrintStream(new File(fileName));
			// For each issue, print out the toString() output.
			for (int i = 0; i < issues.size(); i++) {
				fileWriter.print(issues.get(i).toString());
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to save file.");
		}
	}
}
