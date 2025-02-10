package edu.ncsu.csc216.issue_manager.model.command;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.issue_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.issue_manager.model.command.Command.Resolution;

/**
 * Tests the Command class.
 * 
 * @author Michael Warstler
 */
public class CommandTest {

	/**
	 * Tests creation of a Command object by testing getters and invalid command
	 * parameters.
	 */
	@Test
	public void testCommand() {
		// Test setting a valid command
		Command issueCommand = new Command(CommandValue.RESOLVE, "ownerId", Resolution.DUPLICATE, "Notes");

		// Test getters
		assertAll("Command getters",
				() -> assertEquals(CommandValue.RESOLVE, issueCommand.getCommand(), "incorrect command value"),
				() -> assertEquals("ownerId", issueCommand.getOwnerId(), "incorrect owner Id"),
				() -> assertEquals(Resolution.DUPLICATE, issueCommand.getResolution(), "incorrect resolution value"),
				() -> assertEquals("Notes", issueCommand.getNote(), "incorrect note"));

		// Test invalid parameters and respective throws.
		// Null Command value
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Command(null, "ownerId", Resolution.DUPLICATE, "Notes"));
		assertEquals("Invalid information.", e1.getMessage());
		
		// CommandValue == Assign, ownerId is null/empty.
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ASSIGN, null, Resolution.DUPLICATE, "Notes"));
		assertEquals("Invalid information.", e2.getMessage());
		
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ASSIGN, "", Resolution.DUPLICATE, "Notes"));
		assertEquals("Invalid information.", e3.getMessage());
		
		// CommandValue is Resolve, Resolution is null
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.RESOLVE, "ownerId", null, "Notes"));
		assertEquals("Invalid information.", e4.getMessage());
		
		// Empty/null note.
		Exception e5 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ASSIGN, "ownerId", Resolution.DUPLICATE, null));
		assertEquals("Invalid information.", e5.getMessage());
		
		Exception e6 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ASSIGN, "ownerId", Resolution.DUPLICATE, ""));
		assertEquals("Invalid information.", e6.getMessage());
	}
}
