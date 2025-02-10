package edu.ncsu.csc216.issue_manager.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.issue_manager.model.issue.Issue;

/**
 * Tests the IssueReader class.
 * 
 * @author Michael Warstler
 */
public class IssueReaderTest {

	/** Constant string for "Enhancement" issue type */
	private static final String I_ENHANCEMENT = "Enhancement";
	/** Constant string for "Bug" issue type */
	private static final String I_BUG = "Bug";
	/** Constant string for new state's name */
	private static final String NEW_NAME = "New";
	/** Constant string for the working state's name */
	private static final String WORKING_NAME = "Working";
	/** Constant string for the closed state's name */
	private static final String CLOSED_NAME = "Closed";
	/** String representation of resolution ionWONTFIX */
	private static final String R_WONTFIX = "WontFix";

	/**
	 * Test for the IssueReader class.
	 * 
	 * Citing Molly Owens' instructor response in piazza.
	 */
	@Test
	public void testInstance() {
		IssueReader reader = new IssueReader();
		assertTrue(reader instanceof IssueReader);
	}
	
	
	/**
	 * Test readIssuesFromFile() method with a valid issue file.
	 */
	@Test
	public void testReadIssuesFromValidFile() {
		try {
			ArrayList<Issue> issues = IssueReader.readIssuesFromFile("test-files/valid_issue_records.txt");
			assertEquals(5, issues.size());

			// Confirm that Issues were read in properly.
			// Check first issue (issue id 1)
			assertEquals(1, issues.get(0).getIssueId(), "First issue is id = 1");
			assertEquals(NEW_NAME, issues.get(0).getStateName(), "Issue is New State");
			assertEquals(I_ENHANCEMENT, issues.get(0).getIssueType(), "Issue is Enhancement type");
			assertEquals("Issue description", issues.get(0).getSummary());
			assertEquals(null, issues.get(0).getOwner(), "Issue has no owner");
			assertFalse(issues.get(0).isConfirmed(), "Issue is not confirmed");
			assertEquals(null, issues.get(0).getResolution(), "Issue has no resolution");
			assertEquals("-[New] Note 1\n", issues.get(0).getNotesString());

			// Test middle case (issue id 7)
			assertEquals(7, issues.get(2).getIssueId(), "Issue id = 7");
			assertEquals(WORKING_NAME, issues.get(2).getStateName(), "Issue is Working State");
			assertEquals(I_BUG, issues.get(2).getIssueType(), "Issue is Bug type");
			assertEquals("Issue description", issues.get(2).getSummary());
			assertEquals("owner", issues.get(2).getOwner());
			assertTrue(issues.get(2).isConfirmed(), "Issue is confirmed");
			assertEquals(null, issues.get(2).getResolution(), "Issue has no resolution");
			assertEquals("-[New] Note 1\n-[Confirmed] Note 2\n-[Working] Note 3\n", issues.get(2).getNotesString());

			// Check last issue from file (issue id 15)
			assertEquals(15, issues.get(4).getIssueId(), "Issue id = 15");
			assertEquals(CLOSED_NAME, issues.get(4).getStateName(), "Issue is Closed State");
			assertEquals(I_ENHANCEMENT, issues.get(4).getIssueType(), "Issue is Enhancement type");
			assertEquals("Issue description", issues.get(4).getSummary());
			assertEquals("owner", issues.get(4).getOwner());
			assertFalse(issues.get(4).isConfirmed(), "Issue is not confirmed");
			assertEquals(R_WONTFIX, issues.get(4).getResolution(), "Issue has WontFix resolution");
			assertEquals(
					"-[New] Note 1\nthat goes on a new line\n-[Working] Note 2\n-[Verifying] Note 3\n-[Working] Note 4\n-[Verifying] Note 5\n-[Closed] Note 6\n",
					issues.get(4).getNotesString());

		} catch (IllegalArgumentException e) {
			fail("Unexpected error reading test-files/valid_issue_records.txt"); // Failed due to FileNotFoundException
			// on a valid file.
		}
	}

	
	/**
	 * Test readIssuesFromFile() method with an invalid issue file.
	 */
	@Test
	public void testReadIssuesFromInvalidFile() {	
			Exception e1 = assertThrows(IllegalArgumentException.class, () -> IssueReader.readIssuesFromFile("test-files/issue17.txt"));
			assertEquals("Unable to load file.", e1.getMessage());
			
			Exception e2 = assertThrows(IllegalArgumentException.class, () -> IssueReader.readIssuesFromFile("test-files/invalid_issue_list.txt"));
			assertEquals("Unable to load file.", e2.getMessage());		
	}


}
