package edu.ncsu.csc216.issue_manager.model.issue;

import java.util.ArrayList;

import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.command.Command.Resolution;

/**
 * Class gets/sets values for issue id, summary, owner, confirmed status, notes,
 * issue type, state, and resolution. Creates an Issue object along with 5 types
 * of inner objects which includes NewState, WorkingState, ConfirmedState, and
 * ClosedState. Contains IssueState interface to implement behavior in
 * previously mentioned inner classes. Inner state classes demonstrate finite
 * state machine structure of allowing Issue object to transition between
 * different states, each with its own characteristics. Class is utilized by
 * IssueList class.
 * 
 * @author Michael Warstler
 */
public class Issue {

	/** Constant string for "Enhancement" issue type */
	public static final String I_ENHANCEMENT = "Enhancement";
	/** Constant string for "Bug" issue type */
	public static final String I_BUG = "Bug";
	/** Constant string for new state's name */
	public static final String NEW_NAME = "New";
	/** Constant string for the working state's name */
	public static final String WORKING_NAME = "Working";
	/** Constant string for the confirmed state's name */
	public static final String CONFIRMED_NAME = "Confirmed";
	/** Constant string for the verifying state's name */
	public static final String VERIFYING_NAME = "Verifying";
	/** Constant string for the closed state's name */
	public static final String CLOSED_NAME = "Closed";

	/** Final instance of the NewState inner class */
	private final IssueState newState = new NewState();
	/** Final instance of the WorkingState inner class */
	private final IssueState workingState = new WorkingState();
	/** Final instance of the ConfirmedState inner class */
	private final IssueState confirmedState = new ConfirmedState();
	/** Final instance of the VerifyingState inner class */
	private final IssueState verifyingState = new VerifyingState();
	/** Final instance of the ClosedState inner class */
	private final IssueState closedState = new ClosedState();

	/** Issue's unique id */
	private int issueId;
	/** Current state of the issue */
	private IssueState state;
	/** Type of issue */
	private IssueType issueType;
	/** Issue summary */
	private String summary;
	/** Issue's owner */
	private String owner;
	/** Issue's confirmed status */
	private boolean confirmed;
	/** Issue's resolution type */
	private Resolution resolution;
	/** Issue's note list */
	private ArrayList<String> notes = new ArrayList<String>();

	/**
	 * Enumeration object related to the possible values an Issue can be
	 * (Enhancement or bug).
	 * 
	 * @author Michael Warstler
	 */
	public enum IssueType {
		/** An enhancement issue. Cannot be confirmed. */
		ENHANCEMENT, 
		/** A bug issue. */
		BUG
	}

	/**
	 * Constructor for Issue object based on id, issue type, summary, and note.
	 * Missing fields are initialized to null or false. State is set to new.
	 * 
	 * @param id        of issue.
	 * @param issueType of issue (bug/enhancement).
	 * @param summary   of issue.
	 * @param note      of issue.
	 * @throws IllegalArgumentException if issueType is null.
	 */
	public Issue(int id, IssueType issueType, String summary, String note) {
		// Check for valid issueType (not null).
		if (issueType == null) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}

		// Initialize fields to parameter values.
		setIssueId(id);
		state = newState;
		this.issueType = issueType;
		setSummary(summary);
		owner = null;
		confirmed = false;
		resolution = null;
		addNote(note);
	}

	/**
	 * Constructor for Issue object based on id, state, issue type, summary, owner,
	 * confirmed status, resolution, and notes.
	 * 
	 * @param id         of issue.
	 * @param state      of issue. (New, Working, Confirmed, Verifying, Closed)
	 * @param issueType  of issue (bug/enhancement)
	 * @param summary    of issue.
	 * @param owner      of issue.
	 * @param confirmed  status for issue (true/false)
	 * @param resolution type for issue (FIXED, DUPLICATE, WONTFIX, WORKSFORME)
	 * @param notes      of issue.
	 */
	public Issue(int id, String state, String issueType, String summary, String owner, boolean confirmed,
			String resolution, ArrayList<String> notes) {
		setIssueId(id);
		setState(state);
		setIssueType(issueType);
		setSummary(summary);
		setOwner(owner);
		setConfirmed(confirmed);
		setResolution(resolution);
		setNotes(notes);
	}

	/**
	 * Sets the issue's id.
	 * 
	 * @param id of issue to set.
	 * @throws IllegalArgumentException if parameter is less than 1.
	 */
	private void setIssueId(int id) {
		// Check for validity
		if (id < 1) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}
		issueId = id;
	}

	/**
	 * Returns the issue's id.
	 * 
	 * @return is the issue id.
	 */
	public int getIssueId() {
		return issueId;
	}

	/**
	 * Sets the issue's state. Method receives a String value, which is used to
	 * determine which State instance to assign the current state field.
	 * 
	 * @param state is string representation of state.
	 * @throws IllegalArgumentException when parameter is null/empty or does not
	 *                                  match one of the possible state names.
	 */
	private void setState(String state) {
		// Check if state is null/empty.
		if (state == null || "".equals(state)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}
		// Check for state name not matching possibilities.
		if (!state.equals(NEW_NAME) && !state.equals(WORKING_NAME) && !state.equals(CONFIRMED_NAME)
				&& !state.equals(VERIFYING_NAME) && !state.equals(CLOSED_NAME)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}

		// Assign IssueState field a correct state based on read in parameter
		if (state.equals(NEW_NAME)) {
			this.state = newState; // assigns IssueState field to NewState().
		} else if (state.equals(WORKING_NAME)) {
			this.state = workingState;
		} else if (state.equals(CONFIRMED_NAME)) {
			this.state = confirmedState;
		} else if (state.equals(VERIFYING_NAME)) {
			this.state = verifyingState;
		} else {
			this.state = closedState;
		}
	}

	/**
	 * Gets an issue's current state name as a string.
	 * 
	 * @return is a string representation of its current state.
	 */
	public String getStateName() {
		if (state != null) {
			return state.getStateName();
		} else
			return null;
	}

	/**
	 * Sets the issue's type (enhancement/bug).
	 * 
	 * @param issueType is string representation of issue type.
	 * @throws IllegalArgumentException when parameter is null/empty or not
	 *                                  Bug/Enhancement
	 */
	private void setIssueType(String issueType) {
		// Check for null/empty
		if (issueType == null || "".equals(issueType)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}
		// Check for incorrect name
		if (!issueType.equals(I_ENHANCEMENT) && !issueType.equals(I_BUG)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}

		// Assign an issueType based on the string.
		if (issueType.equals(I_ENHANCEMENT)) {
			this.issueType = IssueType.ENHANCEMENT;
		} else {
			this.issueType = IssueType.BUG;
		}
	}

	/**
	 * Gets an issue's type (enhancement/bug). Returns the Issue's type object as a
	 * string representation.
	 * 
	 * @return is a string representation of the issue type.
	 */
	public String getIssueType() {
		if (issueType == IssueType.ENHANCEMENT) {
			return I_ENHANCEMENT;
		} else {
			return I_BUG;
		}
	}

	/**
	 * Sets the issue's summary field.
	 * 
	 * @param summary of the issue.
	 * @throws IllegalArgumentException when parameter is null/empty.
	 */
	private void setSummary(String summary) {
		// Check for validity.
		if (summary == null || "".equals(summary)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}
		this.summary = summary;
	}

	/**
	 * Gets the issue's summary.
	 * 
	 * @return is the issue's summary.
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets the issue's owner. Owners are required for issues in Working or
	 * Verifying. Owners are optional for Closed. Owners are empty for New and
	 * Confirmed.
	 * 
	 * @param owner of the issue.
	 * @throws IllegalArgumentException if the owner is invalid, thus issue cannot
	 *                                  be created.
	 */
	private void setOwner(String owner) {

		// Check for correct owner information depending on state field.
		// Owner must be assigned when state is Working or Verifying
		if ((state == workingState || state == verifyingState) && (owner == null || "".equals(owner))) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}
		// Owner must be empty for New and Confirmed state
		if ((state == newState || state == confirmedState) && owner != null && !"".equals(owner)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}

		// Set the owner field.
		if ("".equals(owner)) {
			this.owner = null;
		} else {
			this.owner = owner;
		}
	}

	/**
	 * Gets the issue's owner.
	 * 
	 * @return is the issue's owner.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Sets the issue's confirmed status. Enhancement type issues cannot be
	 * confirmed.
	 * 
	 * @param confirmed is true/false depending on confirmed state status.
	 * @throws IllegalArgumentException if confirmed status is invalid.
	 */
	private void setConfirmed(boolean confirmed) {
		// Issue cannot be enhancement and confirmed.
		if (issueType == IssueType.ENHANCEMENT && confirmed) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}

		// Issue of bug type must be confirmed when in working state.
		if (issueType == IssueType.BUG && state == workingState && !confirmed) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}
		this.confirmed = confirmed;
	}

	/**
	 * Gets the issue's confirmed status.
	 * 
	 * @return is confirmed status for issue.
	 */
	public boolean isConfirmed() {
		return confirmed;
	}

	/**
	 * Sets the issue's resolution type. Receives a string value which is used to
	 * set a resolution type. Invalid resolutions include anything other than Fixed
	 * for verifying state, null/empty for closed state, or anything that is not
	 * "Fixed", "Duplicate", "WontFix", or "WorksForMe".
	 * 
	 * @param resolution is string representation of resolution types.
	 * @throws IllegalArgumentException if resolution parameter is invalid.
	 */
	private void setResolution(String resolution) {

		// Check for null/empty conditions.
		if (resolution == null || "".equals(resolution)) {
			// Resolution can't be null/empty in closed or verifying state
			if (state == closedState || state == verifyingState) {
				throw new IllegalArgumentException("Issue cannot be created.");
			} else {
				this.resolution = null;
			}
		} else {
			// A resolution cannot exists in new, confirmed, or working unless it is
			// null/empty.
			if (state == newState || state == confirmedState || state == workingState) {
				throw new IllegalArgumentException("Issue cannot be created.");
			}
			// Verifying state can only have "Fixed" resolution.
			else if (state == verifyingState && !resolution.equals(Command.R_FIXED)) {
				throw new IllegalArgumentException("Issue cannot be created.");
			}
			// Set valid Resolution based on string parameter. State should be closed.
			else if (resolution.equals(Command.R_FIXED)) {
				this.resolution = Resolution.FIXED;
			} else if (resolution.equals(Command.R_DUPLICATE)) {
				this.resolution = Resolution.DUPLICATE;
			} else if (resolution.equals(Command.R_WONTFIX)) {
				this.resolution = Resolution.WONTFIX;
			} else if (resolution.equals(Command.R_WORKSFORME)) {
				if (issueType == IssueType.ENHANCEMENT) { // Enhancement cannot have WorksForMe
					throw new IllegalArgumentException("Issue cannot be created.");
				} else {
					this.resolution = Resolution.WORKSFORME;
				}
			}
			// Resolution string is something it cannot be.
			else {
				throw new IllegalArgumentException("Issue cannot be created.");
			}
		}
	}

	/**
	 * Gets the issue's resolution. Converts Resolution type to a string
	 * representation. If no resolution was set, returns null.
	 * 
	 * @return is a string representation of the issue resolution.
	 */
	public String getResolution() {
		if (resolution == Resolution.FIXED) {
			return Command.R_FIXED;
		} else if (resolution == Resolution.DUPLICATE) {
			return Command.R_DUPLICATE;
		} else if (resolution == Resolution.WONTFIX) {
			return Command.R_WONTFIX;
		} else if (resolution == Resolution.WORKSFORME) {
			return Command.R_WORKSFORME;
		} else {
			return null;
		}
	}

	/**
	 * Adds the parameter note to the notes list. The state name is put in square
	 * brackets and prepended to the parameter note. This is then added to the notes
	 * field. (Format is [State name] note...)
	 * 
	 * @param note is individual string note to add to list.
	 * @throws IllegalArgumentException if the note is null/empty.
	 */
	private void addNote(String note) {
		// Check for validity.
		if (note == null || "".equals(note)) {
			throw new IllegalArgumentException("Issue cannot be created.");
		}

		// Add note to notes array list.
		notes.add("[" + getStateName() + "] " + note);
	}

	/**
	 * Sets the issue's notes field. Notes list cannot be empty.
	 * 
	 * @param notes to be added to notes string array list.
	 * @throws IllegalArgumentException when notes is null/empty.
	 */
	private void setNotes(ArrayList<String> notes) {
		// Throws exception when notes ArrayList is empty.
		if (notes == null || notes.size() == 0) {
			throw new IllegalArgumentException("Issue cannot be created");
		}

		// Adds individual notes from the ArrayList to the field notes. If a note is
		// detected as null/empty, method throws exception.
		for (int i = 0; i < notes.size(); i++) {
			// Notes cannot be added if an individual note is null/empty
			if (notes.get(i) == null || "".equals(notes.get(i))) {
				throw new IllegalArgumentException("Issue cannot be created.");
			}
			// Add the valid note to the field notes.
			this.notes.add(notes.get(i));
		}
	}

	/**
	 * Gets the issue's notes.
	 * 
	 * @return is string array list of notes.
	 */
	public ArrayList<String> getNotes() {
		return notes;
	}

	/**
	 * Creates one string made of all notes on an issue, separated by a newline and
	 * starting each with a '-' character.
	 * 
	 * @return is single string representation of the notes for an issue.
	 */
	public String getNotesString() {
		String notesString = "";
		for (int i = 0; i < notes.size(); i++) {
			notesString += "-" + notes.get(i) + "\n";
		}
		return notesString;
	}

	/**
	 * Returns a unique comma separated value String of the desired issue fields.
	 * The format is * issueId,state name,type,description/summary,owner,confirmed
	 * status,resolution, newline, and notes.
	 * 
	 * @return is a string representation of the entire issue.
	 */
	@Override
	public String toString() {
		String resolutionOutput = "";
		String ownerOutput = null;
		if (resolution != null) {
			resolutionOutput = getResolution();
		}
		if (owner != null) {
			ownerOutput = getOwner();
		}

		return "*" + getIssueId() + "," + getStateName() + "," + getIssueType() + "," + summary + "," + ownerOutput
				+ "," + confirmed + "," + resolutionOutput + "\n" + getNotesString();
	}

	/**
	 * Drives the finite state machine by delegating to the current state's
	 * updateState(Command) method. Will catch an UnsupportedOperationException if
	 * attempting to update the state with the command parameter fails.
	 * 
	 * @param c is command type.
	 * @throws UnsupportedOperationException if an invalid command is given during a
	 *                                       specific state.
	 */
	public void update(Command c) throws UnsupportedOperationException {	
		state.updateState(c);
	}

	/*
	 ********************************************************************************************
	 * INNER INTERFACE (IssueState) AND INNER CLASSES (NewState, WorkingState,
	 * ConfirmedState, VerifyingState, ClosedState)
	 ********************************************************************************************
	 */

	/**
	 * Interface for states in the Issue State Pattern. All concrete issue states
	 * must implement the IssueState interface. The IssueState interface should be a
	 * private interface of the Issue class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private interface IssueState {

		/**
		 * Update the Issue based on the given Command. An UnsupportedOperationException
		 * is throw if the Command is not a valid action for the given state.
		 * 
		 * @param command Command describing the action that will update the Issue's
		 *                state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 *                                       for the given state.
		 */
		void updateState(Command command);

		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		String getStateName();
	}

	/**
	 * Class defining behavior for New State on an issue. Overrides IssueState
	 * methods to update state and get state name.
	 * 
	 * @author Michael Warstler
	 */
	private class NewState implements IssueState {

		/**
		 * Handles updating state when given a command. Additional Issue fields may be
		 * modified based on the command and transition. Enhancements/bugs can be
		 * resolved. Enhancements can be assigned. Bugs can be confirmed.
		 * 
		 * @param command is the command given to update the state.
		 * @throws UnsupportedOperationException if the command is not appropriate for
		 *                                       the current state.
		 */
		@Override
		public void updateState(Command command) {
			// Any invalid commands throw exception
			switch (command.getCommand()) {
			case ASSIGN: 
				// Enhancement issues can be ASSIGNED an owner to move to workingState.
				if (issueType == IssueType.ENHANCEMENT) {
					state = workingState;
					setOwner(command.getOwnerId());
					addNote(command.getNote());
					// Command resolution field is ignored.
					break;
				} else {
					throw new UnsupportedOperationException("Invalid information.");
				}

			case CONFIRM: 
				// Bug issues can be CONFIRMED to move to confirmedState.
				if (issueType == IssueType.BUG) {
					state = confirmedState;
					setConfirmed(true);
					addNote(command.getNote());
					// Command resolution and owner are ignored.
					break;
				} else {
					throw new UnsupportedOperationException("Invalid information.");
				}

			case RESOLVE: // move to closed.
				// Issues can't be Fixed from new state.
				if (command.getResolution() == Resolution.FIXED) {
					throw new UnsupportedOperationException("Invalid information.");
				}
				// Cannot resolve an Enhancement from new with WorksForMe
				if (command.getResolution() == Resolution.WORKSFORME && issueType == IssueType.ENHANCEMENT) {
					throw new UnsupportedOperationException("Invalid information.");
				}
				// Issue gets resolved and moves to closed state.
				state = closedState;
				addNote(command.getNote());
				resolution = command.getResolution();
				// Command owner is ignored.
				break;

			default:
				throw new UnsupportedOperationException("Invalid information.");
			}
		}

		/**
		 * Method returns name of state ("New")
		 * 
		 * @return is NEW_NAME constant
		 */
		@Override
		public String getStateName() {
			return NEW_NAME;
		}
	}

	/**
	 * Class defining behavior for Working State on an issue. Overrides IssueState
	 * methods to update state and get state name.
	 * 
	 * @author Michael Warstler
	 */
	private class WorkingState implements IssueState {

		/**
		 * Handles updating state when given a command. Additional Issue fields may be
		 * modified based on the command and transition. Only the RESOLVE resolution may
		 * transfer an issue from WorkingState. If the resolution is fixed, then the
		 * issue moves to ClosedState. If resolution is duplicate or wontfix, the issue
		 * will move to ClosedState. If the issue is a bug and has resolution
		 * worksforme, then it will also move to the ClosedState.
		 * 
		 * @param command is the command given to update the state.
		 * @throws UnsupportedOperationException if the command is not appropriate for
		 *                                       the current state.
		 */
		@Override
		public void updateState(Command command) {
			// Command owner is ignored.
			switch (command.getCommand()) {
			case RESOLVE: // move to verifying or closed.
				// Fixed resolution sends issue to verifying state.
				if (command.getResolution() == Resolution.FIXED) {
					state = verifyingState;
					resolution = command.getResolution();
					addNote(command.getNote());
					break;
				}
				// Duplicate and wontfix resolutions send state to closed. Worksforme sends to
				// closed only if issue type is bug.
				else if (command.getResolution() == Resolution.DUPLICATE
						|| command.getResolution() == Resolution.WONTFIX
						|| command.getResolution() == Resolution.WORKSFORME && issueType == IssueType.BUG) {
					state = closedState;
					resolution = command.getResolution();
					addNote(command.getNote());
					break;
				} else {
					throw new UnsupportedOperationException("Invalid information");
				}

			default:
				throw new UnsupportedOperationException("Invalid information.");
			}
		}

		/**
		 * Method returns name of state ("Working")
		 * 
		 * @return is WORKING_NAME constant
		 */
		@Override
		public String getStateName() {
			return WORKING_NAME;
		}
	}

	/**
	 * Class defining behavior for Confirmed State on an issue. Overrides IssueState
	 * methods to update state and get state name.
	 * 
	 * @author Michael Warstler
	 */
	private class ConfirmedState implements IssueState {

		/**
		 * Handles updating state when given a command. Additional Issue fields may be
		 * modified based on the command and transition. If command is assign, then
		 * issue's owner and notes are updated and issue is moved to WorkingState. If
		 * the command is Resolve, the resolution must be wontfix. The issue then moves
		 * to ClosedState.
		 * 
		 * @param command is the command given to update the state.
		 * @throws UnsupportedOperationException if the command is not appropriate for
		 *                                       the current state.
		 */
		@Override
		public void updateState(Command command) {
			switch (command.getCommand()) {
			case ASSIGN: // move to working
				state = workingState;
				setOwner(command.getOwnerId());
				addNote(command.getNote());
				// Command resolution field is ignored.
				break;

			case RESOLVE: // move to closed.
				if (command.getResolution() == Resolution.WONTFIX) {
					state = closedState;
					addNote(command.getNote());
					resolution = command.getResolution();
					// Command owner is ignored.
					break;
				} else {
					throw new UnsupportedOperationException("Invalid information.");
				}

			default:
				throw new UnsupportedOperationException("Invalid information.");
			}
		}

		/**
		 * Method returns name of state ("Confirmed")
		 * 
		 * @return is CONFIRMED_NAME constant
		 */
		@Override
		public String getStateName() {
			return CONFIRMED_NAME;
		}
	}

	/**
	 * Class defining behavior for Verifying State on an issue. Overrides IssueState
	 * methods to update state and get state name.
	 * 
	 * @author Michael Warstler
	 */
	private class VerifyingState implements IssueState {

		/**
		 * Handles updating state when given a command. Additional Issue fields may be
		 * modified based on the command and transition. Verify command sends issue to
		 * closed state. Reopen command sends issue to working state. Command note is
		 * added to the notes field.
		 * 
		 * @param command is the command given to update the state.
		 * @throws UnsupportedOperationException if the command is not appropriate for
		 *                                       the current state.
		 */
		@Override
		public void updateState(Command command) {
			switch (command.getCommand()) {
			case VERIFY:
				state = closedState;
				addNote(command.getNote());
				// Command resolution and owner fields are ignored.
				break;

			case REOPEN:
				state = workingState;
				resolution = null;
				addNote(command.getNote());
				// Command resolution and owner fields are ignored.
				break;

			default:
				throw new UnsupportedOperationException("Invalid information.");
			}

		}

		/**
		 * Method returns name of state ("Verifying")
		 * 
		 * @return is VERIFYING_NAME constant
		 */
		@Override
		public String getStateName() {
			return VERIFYING_NAME;
		}
	}

	/**
	 * Class defining behavior for Closed State on an issue. Overrides IssueState
	 * methods to update state and get state name.
	 * 
	 * @author Michael Warstler
	 */
	private class ClosedState implements IssueState {

		/**
		 * Handles updating state when given a command. Additional Issue fields may be
		 * modified based on the command and transition. Issues can only be reopened
		 * from closed state. If the issue is an enhancement with an owner, then state
		 * moves to working. If issue is a bug, then it moves to workingState if it has
		 * an owner and confirmed state if it does not have an owner. If none of the
		 * above, the issue moves to new state. When moved to a new state, issue
		 * resolution is removed and the command note is added to the notes field.
		 * 
		 * @param command is the command given to update the state.
		 * @throws UnsupportedOperationException if the command is not appropriate for
		 *                                       the current state.
		 */
		@Override
		public void updateState(Command command) {
			switch (command.getCommand()) {
			case REOPEN:
				resolution = null;
				// Enhancement with owner moves to WorkingState.
				if (issueType == IssueType.ENHANCEMENT && owner != null && !"".equals(owner)) {
					state = workingState;
				}

				// Confirmed bug with an owner moves to WorkingState. Without an owner moves to
				// ConfirmedState.
				else if (issueType == IssueType.BUG && confirmed) {
					if (owner != null && !"".equals(owner)) {
						state = workingState;
					} else {
						state = confirmedState;
					}
				} else {
					state = newState;
				}

				addNote(command.getNote());
				break;

			default:
				throw new UnsupportedOperationException("Invalid information.");
			}
		}

		/**
		 * Method returns name of state ("Closed")
		 * 
		 * @return is CLOSED_NAME constant
		 */
		@Override
		public String getStateName() {
			return CLOSED_NAME;
		}
	}
}
