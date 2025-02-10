package edu.ncsu.csc216.issue_manager.model.issue;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.issue_manager.model.issue.Issue.IssueType;
import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.issue_manager.model.command.Command.Resolution;

/**
 * Tests the Issue class.
 * 
 * @author Michael Warstler
 */
public class IssueTest {

	// Valid constants for an Issue that do not change.
	/** Issue id number */
	private static final int ID = 5;
	/** Issue summary */
	private static final String SUMMARY = "Issue summary";
	/** Issue note */
	private static final String NOTE = "Issue note";
	/** Issue Owner */
	private static final String OWNER = "Issue owner";

	/** Constant string for "Enhancement" issue type */
	private static final String I_ENHANCEMENT = "Enhancement";
	/** Constant string for "Bug" issue type */
	private static final String I_BUG = "Bug";
	/** Constant string for new state's name */
	private static final String NEW_NAME = "New";
	/** Constant string for the working state's name */
	private static final String WORKING_NAME = "Working";
	/** Constant string for the confirmed state's name */
	private static final String CONFIRMED_NAME = "Confirmed";
	/** Constant string for the verifying state's name */
	private static final String VERIFYING_NAME = "Verifying";
	/** Constant string for the closed state's name */
	private static final String CLOSED_NAME = "Closed";
	/** String representation of resolution FIXED */
	private static final String R_FIXED = "Fixed";
	/** String representation of resolution DUPLICATE */
	private static final String R_DUPLICATE = "Duplicate";
	/** String representation of resolution ionWONTFIX */
	private static final String R_WONTFIX = "WontFix";

	/**
	 * Test Issue constructor with parameters (int id, IssueType issueType, String
	 * summary, String note)
	 */
	@Test
	public void testValidSmallConstructor() {
		// Create valid Issue
		Issue myIssue = new Issue(ID, IssueType.ENHANCEMENT, SUMMARY, NOTE);

		// Test getters
		assertAll("Issue getters", () -> assertEquals(ID, myIssue.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, myIssue.getStateName()),
				() -> assertEquals(I_ENHANCEMENT, myIssue.getIssueType(), "incorrect issue type name"),
				() -> assertEquals(SUMMARY, myIssue.getSummary(), "incorrect summary"),
				() -> assertEquals(null, myIssue.getOwner()),
				() -> assertFalse(myIssue.isConfirmed(), "issue is not confirmed"),
				() -> assertEquals(null, myIssue.getResolution()), () -> assertEquals(1, myIssue.getNotes().size()));

		// Test invalid input and proper exception returns for each parameter
		// Invalid ID
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Issue(-3, IssueType.ENHANCEMENT, SUMMARY, NOTE));
		assertEquals("Issue cannot be created.", e1.getMessage());

		// Invalid Issue type
		Exception e2 = assertThrows(IllegalArgumentException.class, () -> new Issue(ID, null, SUMMARY, NOTE));
		assertEquals("Issue cannot be created.", e2.getMessage());

		// Invalid summary
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> new Issue(ID, IssueType.ENHANCEMENT, "", NOTE));
		assertEquals("Issue cannot be created.", e3.getMessage());

		// Invalid note
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> new Issue(ID, IssueType.ENHANCEMENT, SUMMARY, ""));
		assertEquals("Issue cannot be created.", e4.getMessage());
	}

	/**
	 * Test constructor with parameters (int id, String state, String issueType,
	 * String summary, String owner, boolean confirmed, String resolution,
	 * ArrayList(String) notes)
	 */
	@Test
	public void testLongCosntructor() {
		// Create an array list and add the NOTE constant. TODO - may change/remove the
		// notes list stuff here.
		ArrayList<String> notesList = new ArrayList<String>();
		notesList.add("Issue note");
		// Create a string to check if the proper output comes out of the
		// getNotesString()
		String s = "-" + NOTE + "\n";

		// Create a valid Issue.
		Issue myIssue = new Issue(ID, NEW_NAME, I_BUG, SUMMARY, null, false, null, notesList);

		// Test getters with valid parameters.
		assertAll("Issue getters", () -> assertEquals(ID, myIssue.getIssueId(), "incorrect id"),
				() -> assertEquals(NEW_NAME, myIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(I_BUG, myIssue.getIssueType(), "incorrect issue type name"),
				() -> assertEquals(SUMMARY, myIssue.getSummary(), "incorrect summary"),
				() -> assertEquals(null, myIssue.getOwner()),
				() -> assertFalse(myIssue.isConfirmed(), "issue is not confirmed"),
				() -> assertNull(myIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals(1, myIssue.getNotes().size()), () -> assertEquals(s, myIssue.getNotesString()));

		// NEW test to fix JENKINS.
		Issue tsIssue = new Issue(7, "Working", "Enhancement", "summary", "owner", false, "", notesList);
		assertAll("Issue getters", () -> assertEquals(7, tsIssue.getIssueId(), "incorrect id"),
				() -> assertEquals(WORKING_NAME, tsIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(I_ENHANCEMENT, tsIssue.getIssueType(), "incorrect issue type name"),
				() -> assertEquals("summary", tsIssue.getSummary(), "incorrect summary"),
				() -> assertEquals("owner", tsIssue.getOwner()),
				() -> assertFalse(tsIssue.isConfirmed(), "issue is not confirmed"),
				() -> assertNull(tsIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals(1, tsIssue.getNotes().size()), () -> assertEquals(s, myIssue.getNotesString()));

		// Test invalid constructors.
		// Notes list that is size 0.
		assertThrows(IllegalArgumentException.class,
				() -> new Issue(1, NEW_NAME, I_BUG, "summary", "", false, "", new ArrayList<String>()));
		// Resolution in a state that doesn't allow it.
		assertThrows(IllegalArgumentException.class,
				() -> new Issue(1, CONFIRMED_NAME, I_BUG, "summary", null, false, "WontFix", notesList));
		// A non-confirmed bug in working state.
		assertThrows(IllegalArgumentException.class,
				() -> new Issue(1, WORKING_NAME, I_BUG, "summary", "owner", false, "", notesList));
		// Resolution is not Fixed in verifying state.
		assertThrows(IllegalArgumentException.class,
				() -> new Issue(1, VERIFYING_NAME, I_BUG, "summary", "owner", false, "", notesList));
	}

	/**
	 * Tests the private setState() method through attempting to create Issues with
	 * various State parameters.
	 */
	@Test
	public void testSetState() {
		// Create notes for Issue.
		ArrayList<String> notesList = new ArrayList<String>();
		notesList.add("[New] Issue note");

		// Null state parameter
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Issue(ID, null, I_BUG, SUMMARY, null, false, R_DUPLICATE, notesList));
		assertEquals("Issue cannot be created.", e1.getMessage());

		// State name not one of the valid states.
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Issue(ID, "Mystery State", I_BUG, SUMMARY, null, false, R_DUPLICATE, notesList));
		assertEquals("Issue cannot be created.", e2.getMessage());

		// Test valid names Working, Verifying, Confirmed, and Closed
		Issue issue1 = new Issue(ID, WORKING_NAME, I_BUG, SUMMARY, OWNER, true, null, notesList);
		assertEquals(WORKING_NAME, issue1.getStateName(), "incorrect state name");

		Issue issue2 = new Issue(ID, CONFIRMED_NAME, I_BUG, SUMMARY, null, false, null, notesList);
		assertEquals(CONFIRMED_NAME, issue2.getStateName(), "incorrect state name");

		Issue issue3 = new Issue(ID, VERIFYING_NAME, I_BUG, SUMMARY, OWNER, false, R_FIXED, notesList);
		assertEquals(VERIFYING_NAME, issue3.getStateName(), "incorrect state name");

		Issue issue4 = new Issue(ID, CLOSED_NAME, I_BUG, SUMMARY, null, false, R_DUPLICATE, notesList);
		assertEquals(CLOSED_NAME, issue4.getStateName(), "incorrect state name");
	}

	/**
	 * Test toString() method.
	 */
	@Test
	public void testToString() {
		// Create ArrayList for notes.
		ArrayList<String> notesList = new ArrayList<String>();
		notesList.add("[New] Issue note");
		notesList.add("[Working] Issue note");

		// Create a valid Issue.
		Issue myIssue = new Issue(ID, NEW_NAME, I_BUG, SUMMARY, null, false, null, notesList);
		String expected = "*5,New,Bug,Issue summary,null,false,\n" + "-" + "[" + NEW_NAME + "] " + NOTE + "\n" + "-"
				+ "[" + WORKING_NAME + "] " + NOTE + "\n";
		assertEquals(expected, myIssue.toString());
	}

	/**
	 * Test update() method.
	 */
	@Test
	public void testValidUpdate() {
		// Create valid Issue
		Issue issue1 = new Issue(ID, IssueType.ENHANCEMENT, SUMMARY, NOTE);
		assertEquals(NEW_NAME, issue1.getStateName());
		assertEquals(null, issue1.getOwner());

		// Create an Assign command and give it to the current issue. Check for state,
		// owner, and notes updating.
		Command assignCommand = new Command(CommandValue.ASSIGN, OWNER, null, "Assigning user to issue.");
		issue1.update(assignCommand);
		assertAll("Issue transitions from New to Working",
				() -> assertEquals(OWNER, issue1.getOwner(), "incorrect owner"),
				() -> assertEquals(WORKING_NAME, issue1.getStateName(), "incorrect state name"),
				() -> assertEquals(null, issue1.getResolution(), "incorrect resolution"), // resolution should be blank.
				() -> assertEquals("-[New] Issue note\n-[Working] Assigning user to issue.\n",
						issue1.getNotesString()));
		// Give resolve command to send issue to verifying state.
		Command verifyCommand = new Command(CommandValue.RESOLVE, "New owner", Resolution.FIXED, "Sent to verifying.");
		issue1.update(verifyCommand);
		assertAll("Issue transitions from Working to Verifying",
				() -> assertEquals(OWNER, issue1.getOwner(), "incorrect owner"), // owner should not change.
				() -> assertEquals(VERIFYING_NAME, issue1.getStateName(), "incorrect state name"),
				() -> assertEquals(Command.R_FIXED, issue1.getResolution(), "incorrect resolution"),
				() -> assertEquals(
						"-[New] Issue note\n-[Working] Assigning user to issue.\n-[Verifying] Sent to verifying.\n",
						issue1.getNotesString()));
		// Give verify command to send issue to closed state.
		Command closeCommand = new Command(CommandValue.VERIFY, "New owner", null, "Sent to closed.");
		issue1.update(closeCommand);
		assertAll("Issue transitions from Verifying to Closed",
				() -> assertEquals(OWNER, issue1.getOwner(), "incorrect owner"), // owner should not change.
				() -> assertEquals(CLOSED_NAME, issue1.getStateName(), "incorrect state name"),
				() -> assertEquals(Command.R_FIXED, issue1.getResolution(), "incorrect resolution"), // resolution
																										// should not
																										// change.
				() -> assertEquals(
						"-[New] Issue note\n-[Working] Assigning user to issue.\n-[Verifying] Sent to verifying.\n-[Closed] Sent to closed.\n",
						issue1.getNotesString()));
		// Give reopen command to send issue to working
		Command reopenCommand = new Command(CommandValue.REOPEN, "New owner", null, "Sent to working.");
		issue1.update(reopenCommand);
		assertAll("Issue transitions from Closed to Working",
				() -> assertEquals(OWNER, issue1.getOwner(), "incorrect owner"), // owner should not change.
				() -> assertEquals(WORKING_NAME, issue1.getStateName(), "incorrect state name"),
				() -> assertEquals(null, issue1.getResolution(), "incorrect resolution"), // resolution removed.
				() -> assertEquals(
						"-[New] Issue note\n-[Working] Assigning user to issue.\n-[Verifying] Sent to verifying.\n-[Closed] Sent to closed.\n-[Working] Sent to working.\n",
						issue1.getNotesString()));

		// Additional tests for transitions directly from other states, including error
		// conditions.
		Issue issue2 = new Issue(ID, IssueType.ENHANCEMENT, SUMMARY, NOTE);
		issue2.update(assignCommand); // sends issue to working state.
		Command verifyCommand2 = new Command(CommandValue.RESOLVE, "", Resolution.WONTFIX, "Sent to closed.");
		issue2.update(verifyCommand2); // sends to closed state.
		assertAll("Issue transitions from Working to Closed",
				() -> assertEquals(OWNER, issue2.getOwner(), "incorrect owner"),
				() -> assertEquals(CLOSED_NAME, issue2.getStateName(), "incorrect state name"),
				() -> assertEquals(R_WONTFIX, issue2.getResolution(), "incorrect resolution"), // resolution removed.
				() -> assertEquals(
						"-[New] Issue note\n-[Working] Assigning user to issue.\n-[Closed] Sent to closed.\n",
						issue2.getNotesString()));
		// Attempt to give invalid command when in closed state.
		Command invalidCommand = new Command(CommandValue.VERIFY, "", null, "Invalid command for closed state");
		Exception e1 = assertThrows(UnsupportedOperationException.class, () -> issue2.update(invalidCommand));
		assertEquals("Invalid information.", e1.getMessage());

		// Create valid Issue. Send to Confirmed --> Closed
		Issue issue3 = new Issue(ID, IssueType.BUG, SUMMARY, NOTE);
		Command confirmCommand = new Command(CommandValue.CONFIRM, "", null, "Sent to confirmed.");
		issue3.update(confirmCommand);
		// Send issue to closed state
		issue3.update(verifyCommand2);
		assertAll("Issue transitions from Confirmed to Closed",
				() -> assertEquals(null, issue3.getOwner(), "incorrect owner"),
				() -> assertEquals(CLOSED_NAME, issue3.getStateName(), "incorrect state name"),
				() -> assertEquals(R_WONTFIX, issue3.getResolution(), "incorrect resolution"),
				() -> assertEquals("-[New] Issue note\n-[Confirmed] Sent to confirmed.\n-[Closed] Sent to closed.\n",
						issue3.getNotesString()));
	}

	/**
	 * Test updateState() method from New State.
	 */
	@Test
	public void testNewStateUpdateState() {
		// Create valid bug and enhancement Issues
		Issue bugIssue = new Issue(ID, IssueType.BUG, SUMMARY, NOTE);
		Issue enhancementIssue = new Issue(ID, IssueType.ENHANCEMENT, SUMMARY, NOTE);

		// Create resolve commands
		Command resolveFixed = new Command(CommandValue.RESOLVE, OWNER, Resolution.FIXED, "Resolving issue.");
		Command resolveWorksForMe = new Command(CommandValue.RESOLVE, OWNER, Resolution.WORKSFORME, "Resolving issue.");

		// test for invalid resolve issue command.
		Exception e1 = assertThrows(UnsupportedOperationException.class, () -> bugIssue.update(resolveFixed));
		assertEquals("Invalid information.", e1.getMessage());
		Exception e2 = assertThrows(UnsupportedOperationException.class,
				() -> enhancementIssue.update(resolveWorksForMe));
		assertEquals("Invalid information.", e2.getMessage());

		// Confirm command
		Command confirmBug = new Command(CommandValue.CONFIRM, null, null, "Sent to confirmed.");

		// Test for confirming a bug.
		assertEquals(NEW_NAME, bugIssue.getStateName());
		bugIssue.update(confirmBug);
		assertAll("Issue transitions from New to Confirmed",
				() -> assertEquals(null, bugIssue.getOwner(), "incorrect owner"),
				() -> assertEquals(CONFIRMED_NAME, bugIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(null, bugIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals("-[New] Issue note\n-[Confirmed] Sent to confirmed.\n", bugIssue.getNotesString()));
	}

	/**
	 * Test updateState() method from Confirmed State.
	 */
	@Test
	public void testConfirmedStateUpdateState() {
		// Create valid bug Issue
		Issue bugIssue = new Issue(ID, IssueType.BUG, SUMMARY, NOTE);

		// Confirm command
		Command confirmBug = new Command(CommandValue.CONFIRM, null, null, "Sent to confirmed.");
		bugIssue.update(confirmBug);
		// check that in confirmed state.
		assertAll("Issue transitions from New to Confirmed",
				() -> assertEquals(null, bugIssue.getOwner(), "incorrect owner"),
				() -> assertEquals(CONFIRMED_NAME, bugIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(null, bugIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals("-[New] Issue note\n-[Confirmed] Sent to confirmed.\n", bugIssue.getNotesString()));

		// Send to working with assign command.
		Command assignCommand = new Command(CommandValue.ASSIGN, OWNER, null, "Assigning user to issue.");
		bugIssue.update(assignCommand);
		assertAll("Issue transitions from Confirmed to Working",
				() -> assertEquals(OWNER, bugIssue.getOwner(), "incorrect owner"),
				() -> assertEquals(WORKING_NAME, bugIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(null, bugIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals(
						"-[New] Issue note\n-[Confirmed] Sent to confirmed.\n-[Working] Assigning user to issue.\n",
						bugIssue.getNotesString()));
	}

	/**
	 * Test updateState() method from Verifying State.
	 */
	@Test
	public void testVerifyingStateUpdateState() {
		// Create a valid issue
		Issue enhancementIssue = new Issue(ID, IssueType.ENHANCEMENT, SUMMARY, NOTE);

		// Create an Assign command and give it to the current issue. Sends to working.
		Command assignCommand = new Command(CommandValue.ASSIGN, OWNER, null, "Assigning user to issue.");
		enhancementIssue.update(assignCommand);

		// Give resolve command to send issue to verifying state.
		Command verifyCommand = new Command(CommandValue.RESOLVE, OWNER, Resolution.FIXED, "Sent to verifying.");
		enhancementIssue.update(verifyCommand);

		// Give reopen command to send issue to back working
		Command reopenCommand = new Command(CommandValue.REOPEN, "New owner", null, "Sent to working.");
		enhancementIssue.update(reopenCommand);

		// Confirm issue successfully went to working again.
		assertAll("Issue transitions from Verifying to Working",
				() -> assertEquals(OWNER, enhancementIssue.getOwner(), "incorrect owner"), // owner should not change
				() -> assertEquals(WORKING_NAME, enhancementIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(null, enhancementIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals(
						"-[New] Issue note\n-[Working] Assigning user to issue.\n-[Verifying] Sent to verifying.\n-[Working] Sent to working.\n",
						enhancementIssue.getNotesString()));
	}

	/**
	 * Test updateState() method from Closed State.
	 */
	@Test
	public void testClosedStateUpdateState() {
		// Create valid bug and enhancement Issues
		Issue bugIssue = new Issue(ID, IssueType.BUG, SUMMARY, NOTE);

		// Create confirm, resolve, and reopen commands.
		Command confirmBug = new Command(CommandValue.CONFIRM, null, null, "Sent to confirmed.");
		Command resolveWontFix = new Command(CommandValue.RESOLVE, OWNER, Resolution.WONTFIX, "Resolving issue.");
		Command reopen = new Command(CommandValue.REOPEN, null, null, "Reopen issue");

		// Move bug from new -> confirm - > closed.
		bugIssue.update(confirmBug);
		bugIssue.update(resolveWontFix);
		// Confirm bug is in closed.
		assertAll("Issue transitions from New to Confirmed to Closed",
				() -> assertEquals(null, bugIssue.getOwner(), "incorrect owner"),
				() -> assertEquals(CLOSED_NAME, bugIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(R_WONTFIX, bugIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals("-[New] Issue note\n-[Confirmed] Sent to confirmed.\n-[Closed] Resolving issue.\n",
						bugIssue.getNotesString()));
		// Send bug back to confirmed state due to no owner.
		bugIssue.update(reopen);
		assertAll("Issue transitions from Closed to Confirmed",
				() -> assertEquals(null, bugIssue.getOwner(), "incorrect owner"),
				() -> assertEquals(CONFIRMED_NAME, bugIssue.getStateName(), "incorrect state name"),
				() -> assertEquals(null, bugIssue.getResolution(), "incorrect resolution"),
				() -> assertEquals(
						"-[New] Issue note\n-[Confirmed] Sent to confirmed.\n-[Closed] Resolving issue.\n-[Confirmed] Reopen issue\n",
						bugIssue.getNotesString()));
	}
}
