package edu.ncsu.csc216.issue_manager.model.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.issue_manager.model.command.Command.Resolution;
import edu.ncsu.csc216.issue_manager.model.io.IssueReader;
import edu.ncsu.csc216.issue_manager.model.issue.Issue;
import edu.ncsu.csc216.issue_manager.model.issue.Issue.IssueType;

/**
 * Tests the IssueManager class.
 * 
 * @author Michael Warstler
 */
public class IssueManagerTest {

	/** Constant string for "Enhancement" issue type */
	private static final String I_ENHANCEMENT = "Enhancement";
	/** Constant string for "Bug" issue type */
	private static final String I_BUG = "Bug";
	/** Constant string for new state's name */
	private static final String NEW_NAME = "New";
	/** Constant string for the confirmed state's name */
	private static final String CONFIRMED_NAME = "Confirmed";
	/** Constant string for the closed state's name */
	private static final String CLOSED_NAME = "Closed";
	/** Issue Owner */
	private static final String OWNER = "Issue owner";
	/** String representation of resolution ionWONTFIX */
	private static final String R_WONTFIX = "WontFix";

	/**
	 * Test the getInstance() method of IssueManager. Piazza post response by
	 * Gustavo Padron states this method does not need to be tested, but this was
	 * added to meet the method coverage requirements.
	 */
	@Test
	public void testGetInstance() {
		assertTrue(IssueManager.getInstance() instanceof IssueManager);
	}

	/**
	 * Test loadIssuesFromFile() method.
	 */
	@Test
	public void testLoadIssuesFromFile() {
		// Create IssueManager and create new list.
		IssueManager issueManager = new IssueManager();
		issueManager.createNewIssueList();

		// Read in a valid file of issues and confirm size
		ArrayList<Issue> issues = IssueReader.readIssuesFromFile("test-files/valid_issue_records.txt");
		assertEquals(5, issues.size());

		// Read in same valid file through IssueManager
		issueManager.loadIssuesFromFile("test-files/valid_issue_records.txt");
		// Check that the size is the same
		assertEquals(5, issueManager.getIssueListAsArray().length);
	}

	/**
	 * Test saveIssuesToFile() method.
	 */
	@Test
	public void testSaveIssuesToFile() {
		// Create IssueManager and create new list.
		IssueManager issueManager = new IssueManager();
		issueManager.createNewIssueList();

		// Read in valid issues through the manager.
		issueManager.loadIssuesFromFile("test-files/valid_issue_records.txt");

		// Try to save to file and check if correct.
		try {
			issueManager.saveIssuesToFile("test-files/actual_issue_output.txt");
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

			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}

			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}

	/**
	 * Test getIssueListAsArray() method.
	 */
	@Test
	public void testGetIssueListAsArray() {
		// Create IssueManager and create new list.
		IssueManager issueManager = new IssueManager();
		issueManager.createNewIssueList();

		// Add the issues through IssueManager
		issueManager.addIssueToList(IssueType.BUG, "summary", "note"); // id 1
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 2
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 3

		// Call getIssueListAsArray() method.
		Object[][] issueListArray = issueManager.getIssueListAsArray();
		// Check values in the array.
		assertAll("First row", () -> assertEquals(1, issueListArray[0][0], "incorrect id"),
				() -> assertEquals(NEW_NAME, issueListArray[0][1]),
				() -> assertEquals(I_BUG, issueListArray[0][2], "incorrect issue type name"),
				() -> assertEquals("summary", issueListArray[0][3], "incorrect summary"));
		// check last issue
		assertAll("Last row", () -> assertEquals(3, issueListArray[2][0], "incorrect id"),
				() -> assertEquals(NEW_NAME, issueListArray[2][1]),
				() -> assertEquals(I_ENHANCEMENT, issueListArray[2][2], "incorrect issue type name"),
				() -> assertEquals("summary", issueListArray[2][3], "incorrect summary"));

		// Check size before and after DELETING an issue.
		assertEquals(3, issueListArray.length);
		issueManager.deleteIssueById(2); // delete id 2
		Object[][] modifiedListArray = issueManager.getIssueListAsArray();
		assertEquals(2, modifiedListArray.length);
	}

	/**
	 * Test getIssueListAsArrayByIssueType() method.
	 */
	@Test
	public void testGetIssueListAsArrayByIssueType() {
		// Create IssueManager and create new list.
		IssueManager issueManager = new IssueManager();
		issueManager.createNewIssueList();

		// Add the issues through IssueManager
		issueManager.addIssueToList(IssueType.BUG, "summary", "note"); // id 1
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 2
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 3

		// Call getIssueListAsArray() method.
		Object[][] issueListArray = issueManager.getIssueListAsArrayByIssueType(I_ENHANCEMENT);
		// Check values in the array.
		assertAll("First row", () -> assertEquals(2, issueListArray[0][0], "incorrect id"),
				() -> assertEquals(NEW_NAME, issueListArray[0][1]),
				() -> assertEquals(I_ENHANCEMENT, issueListArray[0][2], "incorrect issue type name"),
				() -> assertEquals("summary", issueListArray[0][3], "incorrect summary"));
		// check last issue
		assertAll("Last row", () -> assertEquals(3, issueListArray[1][0], "incorrect id"),
				() -> assertEquals(NEW_NAME, issueListArray[1][1]),
				() -> assertEquals(I_ENHANCEMENT, issueListArray[1][2], "incorrect issue type name"),
				() -> assertEquals("summary", issueListArray[1][3], "incorrect summary"));
	}

	/**
	 * Test getIssueById() method.
	 */
	@Test
	public void testGetIssueById() {
		// Create IssueManager and create new list.
		IssueManager issueManager = new IssueManager();
		issueManager.createNewIssueList();

		// Add the issues through IssueManager
		issueManager.addIssueToList(IssueType.BUG, "summary", "note"); // id 1
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 2
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 3
		issueManager.addIssueToList(IssueType.BUG, "summary", "note"); // id 4
		issueManager.addIssueToList(IssueType.BUG, "summary", "note"); // id 5

		// Get issue by an id and check values.
		Issue issueId1 = issueManager.getIssueById(1);
		assertAll("Issue getters", () -> assertEquals(1, issueId1.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, issueId1.getStateName()),
				() -> assertEquals(I_BUG, issueId1.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", issueId1.getSummary(), "incorrect summary"),
				() -> assertEquals(null, issueId1.getOwner()),
				() -> assertFalse(issueId1.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, issueId1.getResolution()), () -> assertEquals(1, issueId1.getNotes().size()));

		// Get middle issue by id.
		Issue issueId3 = issueManager.getIssueById(3);
		assertAll("Issue getters", () -> assertEquals(3, issueId3.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, issueId3.getStateName()),
				() -> assertEquals(I_ENHANCEMENT, issueId3.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", issueId3.getSummary(), "incorrect summary"),
				() -> assertEquals(null, issueId3.getOwner()),
				() -> assertFalse(issueId3.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, issueId3.getResolution()), () -> assertEquals(1, issueId3.getNotes().size()));

		// Get last issue by id.
		Issue issueId5 = issueManager.getIssueById(5);
		assertAll("Issue getters", () -> assertEquals(5, issueId5.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, issueId5.getStateName()),
				() -> assertEquals(I_BUG, issueId5.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", issueId5.getSummary(), "incorrect summary"),
				() -> assertEquals(null, issueId5.getOwner()),
				() -> assertFalse(issueId5.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, issueId5.getResolution()), () -> assertEquals(1, issueId5.getNotes().size()));
	}

	/**
	 * Test executeCommand() method.
	 */
	@Test
	public void testExecuteCommand() {
		// Create IssueManager and create new list.
		IssueManager issueManager = new IssueManager();
		issueManager.createNewIssueList();

		// Add these issues through IssueManager
		issueManager.addIssueToList(IssueType.BUG, "summary", "note"); // id 1
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 2
		issueManager.addIssueToList(IssueType.ENHANCEMENT, "summary", "note"); // id 3

		// Get the issues back from the issue manager.
		Issue issue1 = issueManager.getIssueById(1);
		Issue issue3 = issueManager.getIssueById(3);

		// Checks issue id 1 after giving it the Assign command.
		Command confirmCommand = new Command(CommandValue.CONFIRM, OWNER, null, "Confirming a bug.");
		issueManager.executeCommand(1, confirmCommand);
		assertAll("Issue transitions from New to Confirmed",
				() -> assertEquals(null, issue1.getOwner(), "incorrect owner"),
				() -> assertEquals(CONFIRMED_NAME, issue1.getStateName(), "incorrect state name"),
				() -> assertEquals(null, issue1.getResolution(), "incorrect resolution"),
				() -> assertEquals("-[New] note\n-[Confirmed] Confirming a bug.\n", issue1.getNotesString()));

		// Checks issue id 3 after giving it the Resolve command.
		Command resolveCommand = new Command(CommandValue.RESOLVE, OWNER, Resolution.WONTFIX, "Resolving issue.");
		issueManager.executeCommand(3, resolveCommand);
		assertAll("Issue transitions from New to Closed",
				() -> assertEquals(null, issue3.getOwner(), "incorrect owner"),
				() -> assertEquals(CLOSED_NAME, issue3.getStateName(), "incorrect state name"),
				() -> assertEquals(R_WONTFIX, issue3.getResolution(), "incorrect resolution"),
				() -> assertEquals("-[New] note\n-[Closed] Resolving issue.\n", issue3.getNotesString()));
	}
}
