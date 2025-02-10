package edu.ncsu.csc216.issue_manager.model.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.issue_manager.model.command.Command.Resolution;
import edu.ncsu.csc216.issue_manager.model.issue.Issue;
import edu.ncsu.csc216.issue_manager.model.issue.Issue.IssueType;

/**
 * Tests the IssueList class.
 * 
 * @author Michael Warstler
 */
public class IssueListTest {

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
	 * Test constructor for IssueList.
	 */
	@Test
	public void testIssueList() {
		IssueList issueList = new IssueList();
		assertEquals(0, issueList.getIssues().size()); // nothing added to list.
	}

	/**
	 * Test addIssue(IssueType issueType, String summary, String note) method.
	 */
	@Test
	public void testAddIssue() {
		IssueList issueList = new IssueList();
		// Counter should increment from 0 to 1.
		assertEquals(1, issueList.addIssue(IssueType.BUG, "summary", "note"));
		
		// Add another issue and check counter.
		assertEquals(2, issueList.addIssue(IssueType.ENHANCEMENT, "summary", "note"));
	}

	/**
	 * Test addIssues() method.
	 */
	@Test
	public void testAddIssues() {
		// Create valid issues
		Issue issue1 = new Issue(1, IssueType.BUG, "summary", "note");
		Issue issue3 = new Issue(3, IssueType.BUG, "summary", "note");
		Issue issue2 = new Issue(2, IssueType.ENHANCEMENT, "summary", "note");

		// Create array list for above issues
		ArrayList<Issue> issues = new ArrayList<Issue>();
		issues.add(issue1);
		issues.add(issue3);
		issues.add(issue2);

		// Create issueList and add the issues array
		IssueList issueList = new IssueList();
		issueList.addIssues(issues);
		// Check that both issues have been added.
		assertEquals(3, issueList.getIssues().size());
		// Check that issues in the issueList were sorted correctly by id.
		assertEquals(1, issueList.getIssues().get(0).getIssueId(), "id should be 1");
		assertEquals(2, issueList.getIssues().get(1).getIssueId(), "id should be 2");
		assertEquals(3, issueList.getIssues().get(2).getIssueId(), "id should be 3");

		// Create duplicate issue for issues ArrayList.
		Issue issue4 = new Issue(2, IssueType.ENHANCEMENT, "summary", "note");
		issues.add(issue4);

		// Create new IssueList and add issues ArrayList with 4 issues
		IssueList issueList2 = new IssueList();
		issueList2.addIssues(issues);
		// Check that only original 3 issues have been added
		assertEquals(3, issueList2.getIssues().size());
	}

	/**
	 * Test getIssuesByType() method.
	 */
	@Test
	public void testGetIssuesByType() {
		// Create valid issues
		Issue issue1 = new Issue(1, IssueType.BUG, "summary", "note");
		Issue issue2 = new Issue(2, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue3 = new Issue(3, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue4 = new Issue(4, IssueType.BUG, "summary", "note");
		Issue issue5 = new Issue(5, IssueType.BUG, "summary", "note");
		ArrayList<Issue> issues = new ArrayList<Issue>();
		issues.add(issue1);
		issues.add(issue2);
		issues.add(issue3);
		issues.add(issue4);
		issues.add(issue5);

		// Create IssueList and add above ArrayList
		IssueList issueList = new IssueList();
		issueList.addIssues(issues);

		// Get issues by type and check values.
		ArrayList<Issue> enhancementIssues = issueList.getIssuesByType(I_ENHANCEMENT);
		assertEquals(2, enhancementIssues.size());
		ArrayList<Issue> bugIssues = issueList.getIssuesByType(I_BUG);
		assertEquals(3, bugIssues.size());
	}

	/**
	 * Test getIssueById() method.
	 */
	@Test
	public void testGetIssueById() {
		// Create valid issues
		Issue issue1 = new Issue(1, IssueType.BUG, "summary", "note");
		Issue issue2 = new Issue(2, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue3 = new Issue(3, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue4 = new Issue(4, IssueType.BUG, "summary", "note");
		Issue issue5 = new Issue(5, IssueType.BUG, "summary", "note");
		ArrayList<Issue> issues = new ArrayList<Issue>();
		issues.add(issue1);
		issues.add(issue2);
		issues.add(issue3);
		issues.add(issue4);
		issues.add(issue5);

		// Create IssueList and add above ArrayList
		IssueList issueList = new IssueList();
		issueList.addIssues(issues);

		// Get issue by an id and check values.
		Issue issueId1 = issueList.getIssueById(1);
		assertAll("Issue getters", () -> assertEquals(1, issueId1.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, issueId1.getStateName()),
				() -> assertEquals(I_BUG, issueId1.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", issueId1.getSummary(), "incorrect summary"),
				() -> assertEquals(null, issueId1.getOwner()),
				() -> assertFalse(issueId1.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, issueId1.getResolution()), () -> assertEquals(1, issueId1.getNotes().size()));

		// Get middle issue by id.
		Issue issueId3 = issueList.getIssueById(3);
		assertAll("Issue getters", () -> assertEquals(3, issueId3.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, issueId3.getStateName()),
				() -> assertEquals(I_ENHANCEMENT, issueId3.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", issueId3.getSummary(), "incorrect summary"),
				() -> assertEquals(null, issueId3.getOwner()),
				() -> assertFalse(issueId3.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, issueId3.getResolution()), () -> assertEquals(1, issueId3.getNotes().size()));

		// Get last issue by id.
		Issue issueId5 = issueList.getIssueById(5);
		assertAll("Issue getters", () -> assertEquals(5, issueId5.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, issueId5.getStateName()),
				() -> assertEquals(I_BUG, issueId5.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", issueId5.getSummary(), "incorrect summary"),
				() -> assertEquals(null, issueId5.getOwner()),
				() -> assertFalse(issueId5.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, issueId5.getResolution()), () -> assertEquals(1, issueId5.getNotes().size()));
		
		// Attempt to get an id that does not exist
		assertNull(issueList.getIssueById(90));
		// Create new issue list and attempt to get issue by id.
		IssueList emptyIssueList = new IssueList();
		assertNull(emptyIssueList.getIssueById(1));
	}

	/**
	 * Test executeCommand() method.
	 */
	@Test
	public void testExecuteCommand() {
		// Create valid issues
		Issue issue1 = new Issue(1, IssueType.BUG, "summary", "note");
		Issue issue2 = new Issue(2, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue3 = new Issue(3, IssueType.ENHANCEMENT, "summary", "note");
		ArrayList<Issue> issues = new ArrayList<Issue>();
		issues.add(issue1);
		issues.add(issue2);
		issues.add(issue3);
		
		// Create IssueList and add above ArrayList
		IssueList issueList = new IssueList();
		issueList.addIssues(issues);
		
		// Checks issue id 1 after giving it the Assign command.
		Command confirmCommand = new Command(CommandValue.CONFIRM, OWNER, null, "Confirming a bug.");
		issueList.executeCommand(1, confirmCommand);
		assertAll("Issue transitions from New to Confirmed",
				() -> assertEquals(null, issue1.getOwner(), "incorrect owner"),
				() -> assertEquals(CONFIRMED_NAME, issue1.getStateName(), "incorrect state name"),
				() -> assertEquals(null, issue1.getResolution(), "incorrect resolution"), 
				() -> assertEquals("-[New] note\n-[Confirmed] Confirming a bug.\n",
						issue1.getNotesString()));
		
		// Checks issue id 3 after giving it the Resolve command.
		Command resolveCommand = new Command(CommandValue.RESOLVE, OWNER, Resolution.WONTFIX, "Resolving issue.");
		issueList.executeCommand(3, resolveCommand);
		assertAll("Issue transitions from New to Closed",
				() -> assertEquals(null, issue3.getOwner(), "incorrect owner"),
				() -> assertEquals(CLOSED_NAME, issue3.getStateName(), "incorrect state name"),
				() -> assertEquals(R_WONTFIX, issue3.getResolution(), "incorrect resolution"), 
				() -> assertEquals("-[New] note\n-[Closed] Resolving issue.\n",
						issue3.getNotesString()));	
	}

	/**
	 * Test deleteIssueById() method.
	 */
	@Test
	public void testDeleteIssueById() {
		// Create valid issues
		Issue issue1 = new Issue(1, IssueType.BUG, "summary", "note");
		Issue issue2 = new Issue(2, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue3 = new Issue(3, IssueType.ENHANCEMENT, "summary", "note");
		Issue issue4 = new Issue(4, IssueType.BUG, "summary", "note");
		Issue issue5 = new Issue(5, IssueType.BUG, "summary", "note");
		ArrayList<Issue> issues = new ArrayList<Issue>();
		issues.add(issue1);
		issues.add(issue2);
		issues.add(issue3);
		issues.add(issue4);
		issues.add(issue5);

		// Create IssueList and add above ArrayList
		IssueList issueList = new IssueList();
		issueList.addIssues(issues);
		
		// Remove issue id = 1.
		issueList.deleteIssueById(1);
		// Confirm that id = 1 is gone.
		assertNull(issueList.getIssueById(1));
		assertEquals(4, issueList.getIssues().size());
		
		// Remove issue id = 3.
		issueList.deleteIssueById(3);
		assertNull(issueList.getIssueById(3));
		assertEquals(3, issueList.getIssues().size());
		
		// Remove issue id = 5
		issueList.deleteIssueById(5);
		assertNull(issueList.getIssueById(5));
		assertEquals(2, issueList.getIssues().size());
	}
}
