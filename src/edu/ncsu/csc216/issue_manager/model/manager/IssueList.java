package edu.ncsu.csc216.issue_manager.model.manager;

import java.util.ArrayList;

import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.issue.Issue;
import edu.ncsu.csc216.issue_manager.model.issue.Issue.IssueType;

/**
 * Maintains a List of Issues and a counter which represents the id of the next
 * Issue to add to the list. Can add newly created issues to the list, sorting
 * issues by id, return all issues in a list, return issues by type, return an
 * issue by id, delete an issue with a given id, and execute a command on a
 * specific issue.
 * 
 * @author Michael Warstler
 */
public class IssueList {

	/** List of issue objects */
	private ArrayList<Issue> issues;
	/** Counter to keep track of latest issue id */
	private int counter;

	/**
	 * Constructs an issue list by reseting the counter. 
	 */
	public IssueList() {
		// Must be 0 according to Jenkins
		counter = 0;
		issues = new ArrayList<Issue>();
	}

	/**
	 * Creates a NEW Issue object based on the given parameters and the counter,
	 * which will be the latest issue id. After issue creation, this is added to the
	 * issue list.
	 * 
	 * @param issueType is the type of issue object (bug/enhancement)
	 * @param summary   is the summary of the issue object
	 * @param note      is the note for the issue
	 * @return is the ID of the issue added.
	 */
	public int addIssue(IssueType issueType, String summary, String note) {
		// Increment counter prior to adding, then add the issue to the list.
		Issue issueToAdd = new Issue(++counter, issueType, summary, note);
		issues.add(issueToAdd); // adds to issues list at the last spot.
		return counter; // counter should hold the id value of latest issue added.
	}

	/**
	 * Adds a provided list of issues. Duplicate issues in a list are ignored.
	 * Issues are sorted to the main issue list. After adding all Issues, the
	 * counter is set to the id of the last issue in the list. Utilizes private
	 * helper method addIssue(Issue) to check for duplicates and adding a single
	 * issue in sorted order.
	 * 
	 * @param issues is a list of issue objects to try to add to main list.
	 */
	public void addIssues(ArrayList<Issue> issues) {
		// Reset the issues field to a new ArrayList.
		this.issues = new ArrayList<Issue>();

		// Send each issue to the private addIssue method.
		for (int i = 0; i < issues.size(); i++) {
			addIssue(issues.get(i));
		}
	}

	/**
	 * Helper method to the addIssues() method which checks for duplicate Issues and
	 * adds a single issue in sorted order.
	 * 
	 * @param issue is the issue object to attempt to add to issues field.
	 */
	private void addIssue(Issue issue) {
		// Get the issue to add's id.
		int addingIssueId = issue.getIssueId();
		boolean isDuplicate = false;
		boolean added = false;

		// Check for duplicates in the existing list
		for (int j = 0; j < issues.size(); j++) {
			if (addingIssueId == issues.get(j).getIssueId()) {
				isDuplicate = true;
			}
		}

		// If not a duplicate, add the new issue to the correct location (by id)
		if (!isDuplicate) {
			for (int k = 0; k < issues.size(); k++) {
				if (addingIssueId < issues.get(k).getIssueId()) {
					issues.add(k, issue); // adds new issue to location of k, pushing all other issues to higher index.
					added = true;
					break;
				}
			}
			if (!added) { // if the issue has not been added yet and is valid, add it to end of list.
				issues.add(issue);
				counter = issues.get(issues.size() - 1).getIssueId(); // The counter is now the last id in the list															
			}
		}
	}

	/**
	 * Gets the list of issues.
	 * 
	 * @return is issues field (the main issue list)
	 */
	public ArrayList<Issue> getIssues() {
		return issues;
	}

	/**
	 * Gets a list of issues by the designated issueType.
	 * 
	 * @param issueType is a string representation of the issue's type.
	 *                  (Bug/Enhancement)
	 * @return is a list of issues by the parameter type.
	 * @throws IllegalArgumentException if issueType parameter is null/empty.
	 */
	public ArrayList<Issue> getIssuesByType(String issueType) {
		if (issueType == null || "".equals(issueType)) {
			throw new IllegalArgumentException("Inavalid issue type.");
		}

		// Create array list to hold specific type issues.
		ArrayList<Issue> issuesByType = new ArrayList<Issue>();
		// Add only matching type issues to new list.
		for (int i = 0; i < issues.size(); i++) {
			if (issues.get(i).getIssueType().equals(issueType)) {
				issuesByType.add(issues.get(i));
			}
		}
		return issuesByType;
	}

	/**
	 * Gets an issue from the issue list that matches the parameter id. If the issue
	 * does not exist in the list, then method returns null.
	 * 
	 * @param id is the issue id to search for.
	 * @return is Issue object with matching parameter id or null if not found.
	 */
	public Issue getIssueById(int id) {
		for (int i = 0; i < issues.size(); i++) {
			if (id == issues.get(i).getIssueId()) {
				return issues.get(i);
			}
		}
		return null;
	}

	/**
	 * Updates an issue in the list through an execution of a command. If issue does
	 * not exist in the list, then nothing is done.
	 * 
	 * @param id of issue to execute a command with.
	 * @param c  is command given to the issue.
	 */
	public void executeCommand(int id, Command c) {
		// Attempt to execute the command at the given parameter id.
		try {
			getIssueById(id).update(c);
		} catch (NullPointerException e) {
			// No need to throw exception if id does not exist in the list according to
			// instructions.
		}
	}

	/**
	 * Removes issue from the list with the given id.
	 * 
	 * @param id of issue to delete from list.
	 */
	public void deleteIssueById(int id) {
		for (int i = 0; i < issues.size(); i++) {
			if (id == issues.get(i).getIssueId()) {
				issues.remove(i);
				// Decrement the counter if the id removed was the largest in the list.
				if (counter == id) {
					counter--;
				}
			}
		}
	}
}
