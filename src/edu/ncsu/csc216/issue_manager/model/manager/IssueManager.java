package edu.ncsu.csc216.issue_manager.model.manager;

import java.util.ArrayList;

import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.io.IssueReader;
import edu.ncsu.csc216.issue_manager.model.io.IssueWriter;
import edu.ncsu.csc216.issue_manager.model.issue.Issue;
import edu.ncsu.csc216.issue_manager.model.issue.Issue.IssueType;

/**
 * Controls the creation and modification of IssueList (s). Implements Singleton
 * design pattern (only one IssueManager ever created). All parts of
 * IssueManagerGUI interact with IssueManager at all times.
 * 
 * @author Michael Warstler
 */
public class IssueManager {

	/** Issue list containing multiple issues */
	private IssueList issueList = new IssueList();
	/** Static instance of this IssueManager */
	private static IssueManager singleton;

	/**
	 * Gets the instance of this IssueManager.
	 * 
	 * @return is this IssueManager
	 */
	public static IssueManager getInstance() {
		if (singleton == null) {
			singleton = new IssueManager();
		}
		return singleton;
	}

	/**
	 * Sends contents of issue list to the designated file name. Uses IssueWriter to
	 * perform action of saving to file.
	 * 
	 * @param fileName is name of file to save to.
	 */
	public void saveIssuesToFile(String fileName) {
		IssueWriter.writeIssuesToFile(fileName, issueList.getIssues());
	}

	/**
	 * Loads in issues from designated file name. Uses IssueReader to perform action
	 * of reading the file. Stores the array list into the issueList.
	 * 
	 * @param fileName is name of file to read.
	 */
	public void loadIssuesFromFile(String fileName) {
		issueList.addIssues(IssueReader.readIssuesFromFile(fileName));
	}

	/**
	 * Updates the global issueList reference to point to a new IssueList object.
	 * The old IssueList is deleted.
	 */
	public void createNewIssueList() {
		issueList = new IssueList();
	}

	/**
	 * Gets a 2D array that consists of 1 row for every issue. Each row contains 4
	 * columns, which are designated as the following: index 0 = Issue id number,
	 * index 1 = Issue state name, index 2 = Issue type, index 3 = Issue summary.
	 * Uses all issues from the issue list.
	 * 
	 * @return is 2D array containing issues from the issue list in the format
	 *         above.
	 */
	public Object[][] getIssueListAsArray() {
		Object[][] issueListArray = new Object[issueList.getIssues().size()][4];
		for (int i = 0; i < issueListArray.length; i++) {
			issueListArray[i][0] = issueList.getIssues().get(i).getIssueId();
			issueListArray[i][1] = issueList.getIssues().get(i).getStateName();
			issueListArray[i][2] = issueList.getIssues().get(i).getIssueType();
			issueListArray[i][3] = issueList.getIssues().get(i).getSummary();
		}
		return issueListArray;
	}

	/**
	 * Gets a 2D array that consists of 1 row for every issue. Each row contains 4
	 * columns, which are designated as the following: index 0 = Issue id number,
	 * index 1 = Issue state name, index 2 = Issue type, index 3 = Issue summary.
	 * Only uses issues with the matching issue type.
	 * 
	 * @param issueType is Enhancement/Bug.
	 * @return is 2D array containing issues from the issue list (by matching issue
	 *         type) in the format above. Returns an empty array if no issue type is
	 *         found.
	 * @throws IllegalArgumentException is parameter is null.
	 */
	public Object[][] getIssueListAsArrayByIssueType(String issueType) {
		// Check for valid issueType
		if (issueType == null) {
			throw new IllegalArgumentException("Invalid issue type");
		}

		// Check to see if parameter is either "Bug" or "Enhancement"
		if (issueType.equals(Issue.I_BUG) || issueType.equals(Issue.I_ENHANCEMENT)) {
			ArrayList<Issue> issuesByType = issueList.getIssuesByType(issueType);
			Object[][] issueListArray = new Object[issuesByType.size()][4];
			// Add Issue parameters to 2D array.
			for (int i = 0; i < issueListArray.length; i++) {
				issueListArray[i][0] = issuesByType.get(i).getIssueId();
				issueListArray[i][1] = issuesByType.get(i).getStateName();
				issueListArray[i][2] = issuesByType.get(i).getIssueType();
				issueListArray[i][3] = issuesByType.get(i).getSummary();
			}
			return issueListArray;
		} else {
			Object[][] emptyArray = new Object[0][0];
			return emptyArray;
		}
	}

	/**
	 * Gets the issue from the issue list based on the id parameter.
	 * 
	 * @param id of issue to get.
	 * @return is issue from list with matching id.
	 */
	public Issue getIssueById(int id) {
		return issueList.getIssueById(id);
	}

	/**
	 * Executes a command for an issue with matching id from the issue list.
	 * 
	 * @param id of issue to enact command on.
	 * @param c  is command to give to issue.
	 */
	public void executeCommand(int id, Command c) {
		issueList.executeCommand(id, c);
	}

	/**
	 * Deletes an issue with matching id parameter from the issue list.
	 * 
	 * @param id of issue to delete.
	 */
	public void deleteIssueById(int id) {
		issueList.deleteIssueById(id);
	}

	/**
	 * Adds a new Issue object to the issue list based on the issue type
	 * (Enhancement/Bug), summary, and note.
	 * 
	 * @param issueType is either type Enhancement or Bug
	 * @param summary   is a summary for the new issue.
	 * @param note      is a note for the new issue.
	 */
	public void addIssueToList(IssueType issueType, String summary, String note) {
		issueList.addIssue(issueType, summary, note);
	}
}
