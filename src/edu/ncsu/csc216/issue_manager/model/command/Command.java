package edu.ncsu.csc216.issue_manager.model.command;

/**
 * Creates objects that encapsulate user actions/transitions that cause the
 * state of an Issue to update. Uses enumeration for possible commands that
 * cause transitions due to a small number of predefined constant values. Class
 * fields include Command ownerId, note, command value, and resolution. 
 * 
 * @author Michael Warstler
 */
public class Command {

	/** String representation of resolution FIXED */
	public static final String R_FIXED = "Fixed";
	/** String representation of resolution DUPLICATE */
	public static final String R_DUPLICATE = "Duplicate";
	/** String representation of resolution WONTFIX */
	public static final String R_WONTFIX = "WontFix";
	/** String representation of resolution WORKSFORME */
	public static final String R_WORKSFORME = "WorksForMe";

	/** Owner's Id */
	private String ownerId;
	/** Issue notes */
	private String note;
	/** Command value */
	private CommandValue c;
	/** Command resolution */
	private Resolution r;

	/**
	 * Enumeration type for possible commands to designate to an issue. Possible
	 * commands are assign, confirm, resolve, verify, and reopen.
	 * 
	 * @author Michael Warstler
	 */
	public enum CommandValue {
		/** Assigns an owner to an issue */
		ASSIGN, 
		/** Confirms an issue - sends to working */
		CONFIRM, 
		/** Resolves an issue - sends to closed */
		RESOLVE,
		/** Verifies an issue - sends to verifying */
		VERIFY, 
		/** Reopens an issue */
		REOPEN
	}

	/**
	 * Enumeration type for possible resolutions to close an issue with. Possible
	 * resolutions are fixed, duplicate, wontfix,and worksforme.
	 * 
	 * @author Michael Warstler
	 */
	public enum Resolution {
		/** Issue becomes fixed */
		FIXED, 
		/** A duplicate issue */
		DUPLICATE, 
		/** Issue will not be fixed */
		WONTFIX, 
		/** Issue works for me */
		WORKSFORME
	}

	/**
	 * Constructs a Command object with a given command value, ownerId, resolution,
	 * and note. Not all parameters are required (some can be null or ignored).
	 * 
	 * @param c       is enumeration type of command value assigned to the command.
	 * @param ownerId is the ownerId (if assigned) to the command.
	 * @param r       is the enumeration type resolution assigned to the command.
	 * @param note    is the note assigned to this command.
	 * @throws IllegalArgumentException for following cases: - CommandValue == null
	 *                                  - CommandValue == ASSIGN and ownerId ==
	 *                                  null/empty - CommandValue == RESOLVE and
	 *                                  Resolution == null - note == null/empty
	 */
	public Command(CommandValue c, String ownerId, Resolution r, String note) {
		// Check that CommandValue exists
		if (c == null) {
			throw new IllegalArgumentException("Invalid information.");
		}

		// Check that CommandValue == Assign, that ownerId is not null/empty.
		if (c == CommandValue.ASSIGN && ("".equals(ownerId) || ownerId == null)) {
			throw new IllegalArgumentException("Invalid information.");
		}

		// Check that CommandValue == Resolve, that Resolution is not null
		if (c == CommandValue.RESOLVE && r == null) {
			throw new IllegalArgumentException("Invalid information.");
		}

		// Check that note is not null/empty.
		if ("".equals(note) || note == null) {
			throw new IllegalArgumentException("Invalid information.");
		}

		// Set Command's fields
		this.ownerId = ownerId;
		this.note = note;
		this.c = c;
		this.r = r;
	}

	/**
	 * Returns the CommandValue for this command.
	 * 
	 * @return is CommandValue assigned to this command.
	 */
	public CommandValue getCommand() {
		return c;
	}

	/**
	 * Returns the ownerId field.
	 * 
	 * @return is ownerId string.
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * Returns the Resolution for this command.
	 * 
	 * @return is Resolution type assigned to this command.
	 */
	public Resolution getResolution() {
		return r;
	}

	/**
	 * Returns the note field.
	 * 
	 * @return is the note string.
	 */
	public String getNote() {
		return note;
	}

}
