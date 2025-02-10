package edu.ncsu.csc216.issue_manager.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.issue_manager.model.issue.Issue;

/**
 * Tests the IssueWriter class.
 * 
 * @author Michael Warstler
 */
public class IssueWriterTest {

	/**
	 * Test for the IssueWriter class.
	 * 
	 * Citing Molly Owens' instructor response in piazza.
	 */
	@Test
	public void testInstance() {
		IssueWriter writer = new IssueWriter();
		assertTrue(writer instanceof IssueWriter);
	}

	/**
	 * Test writeIssuesToFile() method.
	 */
	@Test
	public void testWriteIssuesToFile() throws FileNotFoundException {
		// Inputs in a valid list of issues.
		ArrayList<Issue> issues = IssueReader.readIssuesFromFile("test-files/valid_issue_records.txt");

		// Attempt to make output file.
		try {
			IssueWriter.writeIssuesToFile("test-files/actual_issue_output.txt", issues);
		} catch (IllegalArgumentException e) {
			fail("Cannot write to activity records file");
		}
		checkFiles("test-files/expected_issue_output.txt", "test-files/actual_issue_output.txt");
	}

	/**
	 * Citing method created from ActivityRecordIO in the WolfScheduler project.
	 * 
	 * Helper method to compare two files for the same contents
	 * 
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	public void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new File(expFile));
				Scanner actScanner = new Scanner(new File(actFile));) {

			// Check that contents of expected matches actual.
			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}

			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}
}
