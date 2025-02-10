package edu.ncsu.csc216.issue_manager.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.issue_manager.model.command.Command;
import edu.ncsu.csc216.issue_manager.model.command.Command.Resolution;
import edu.ncsu.csc216.issue_manager.model.issue.Issue;
import edu.ncsu.csc216.issue_manager.model.issue.Issue.IssueType;
import edu.ncsu.csc216.issue_manager.model.manager.IssueManager;

/**
 * Container for the IssueManager that has the menu options for new issue 
 * files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 */
public class IssueManagerGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Issue Manager";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New Issue list menu item. */
	private static final String NEW_TITLE = "New";
	/** Text for the Load Issue list menu item. */
	private static final String LOAD_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing Issues. */
	private JMenuItem itemNewIssueList;
	/** Menu item for loading a file containing Issues. */
	private JMenuItem itemLoadIssueList;
	/** Menu item for saving the issue list. */
	private JMenuItem itemSaveIssueList;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify IssueListPanel for CardLayout. */
	private static final String ISSUE_LIST_PANEL = "IssueListPanel";
	/** Constant to identify NewPanel for CardLayout. */
	private static final String NEW_PANEL = "NewPanel";
	/** Constant to identify WorkingPanel for CardLayout. */
	private static final String WORKING_PANEL = "WorkingPanel";
	/** Constant to identify ConfirmedPanel for CardLayout. */
	private static final String CONFIRMED_PANEL = "ConfirmedPanel";
	/** Constant to identify VerifyingPanel for CardLayout. */
	private static final String VERIFYING_PANEL = "VerifyingPanel";
	/** Constant to identify ClosedPanel for CardLayout. */
	private static final String CLOSED_PANEL = "ClosedPanel";
	/** Constant to identify CreateIssuePanel for CardLayout. */
	private static final String CREATE_ISSUE_PANEL = "CreateIssuePanel";
	/** Issue List panel - we only need one instance, so it's final. */
	private final IssueListPanel pnlIssueList = new IssueListPanel();
	/** New panel - we only need one instance, so it's final. */
	private final NewPanel pnlNew = new NewPanel();
	/** Working panel - we only need one instance, so it's final. */
	private final WorkingPanel pnlWorking = new WorkingPanel();
	/** Confirmed panel - we only need one instance, so it's final. */
	private final ConfirmedPanel pnlConfirmed = new ConfirmedPanel();
	/** Verifying panel - we only need one instance, so it's final. */
	private final VerifyingPanel pnlVerifying = new VerifyingPanel();
	/** Closed panel - we only need one instance, so it's final. */
	private final ClosedPanel pnlClosed = new ClosedPanel();
	/** Add Issue panel - we only need one instance, so it's final. */
	private final AddIssuePanel pnlAddIssue = new AddIssuePanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a IssueManagerGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the IssueManager.
	 */
	public IssueManagerGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlIssueList, ISSUE_LIST_PANEL);
		panel.add(pnlNew, NEW_PANEL);
		panel.add(pnlWorking, WORKING_PANEL);
		panel.add(pnlConfirmed, CONFIRMED_PANEL);
		panel.add(pnlVerifying, VERIFYING_PANEL);
		panel.add(pnlClosed, CLOSED_PANEL);
		panel.add(pnlAddIssue, CREATE_ISSUE_PANEL);
		cardLayout.show(panel, ISSUE_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing issues or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewIssueList = new JMenuItem(NEW_TITLE);
		itemLoadIssueList = new JMenuItem(LOAD_TITLE);
		itemSaveIssueList = new JMenuItem(SAVE_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewIssueList.addActionListener(this);
		itemLoadIssueList.addActionListener(this);
		itemSaveIssueList.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveIssueList.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewIssueList);
		menu.add(itemLoadIssueList);
		menu.add(itemSaveIssueList);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use IssueManager's singleton to create/get the sole instance.
		IssueManager model = IssueManager.getInstance();
		if (e.getSource() == itemNewIssueList) {
			//Create a new issue list
			model.createNewIssueList();
			itemSaveIssueList.setEnabled(true);
			pnlIssueList.updateTable(null);
			cardLayout.show(panel, ISSUE_LIST_PANEL);
			validate();
			repaint();			
		} else if (e.getSource() == itemLoadIssueList) {
			//Load an existing issue list
			try {
				model.loadIssuesFromFile(getFileName(true));
				itemSaveIssueList.setEnabled(true);
				pnlIssueList.updateTable(null);
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveIssueList) {
			//Save current issue list
			try {
				model.saveIssuesToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveIssuesToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if using an open/load dialog, false for save dialog
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the IssueManager application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new IssueManagerGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the list of issues.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class IssueListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Button for creating a new Issue */
		private JButton btnAddNewIssue;
		/** Button for deleting the selected issue in the list */
		private JButton btnDeleteIssue;
		/** Button for editing the selected issue in the list */
		private JButton btnEditIssue;
		/** Button for listing bugs */
		private JButton btnFilterByBug;
		/** Button for listing enhancements */
		private JButton btnFilterByEnhancement;
		/** Button that will show all issues that are currently managed */
		private JButton btnShowAllIssues;
		/** JTable for displaying the list of issues */
		private JTable issuesTable;
		/** TableModel for Issues */
		private IssueTableModel issueTableModel;
		
		/**
		 * Creates the issue list.
		 */
		public IssueListPanel() {
			super(new BorderLayout());
			
			//Set up the JPanel that will hold action buttons
			btnShowAllIssues = new JButton("Show All Issues");
			btnShowAllIssues.addActionListener(this);
			btnFilterByEnhancement = new JButton("List Enhancements");
			btnFilterByEnhancement.addActionListener(this);
			btnFilterByBug = new JButton("List Bugs");
			btnFilterByBug.addActionListener(this);
			btnAddNewIssue = new JButton("Add New Issue");
			btnAddNewIssue.addActionListener(this);
			btnDeleteIssue = new JButton("Delete Selected Issue");
			btnDeleteIssue.addActionListener(this);
			btnEditIssue = new JButton("Edit Selected Issue");
			btnEditIssue.addActionListener(this);
			
			
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(2, 3));
			pnlActions.add(btnShowAllIssues);
			pnlActions.add(btnFilterByEnhancement);
			pnlActions.add(btnFilterByBug);
			pnlActions.add(btnAddNewIssue);
			pnlActions.add(btnDeleteIssue);
			pnlActions.add(btnEditIssue);
			
			
						
			//Set up table
			issueTableModel = new IssueTableModel();
			issuesTable = new JTable(issueTableModel);
			issuesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			issuesTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
			issuesTable.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(issuesTable);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddNewIssue) {
				//If the add button is clicked switch to the createIssuePanel
				cardLayout.show(panel,  CREATE_ISSUE_PANEL);
			} else if (e.getSource() == btnDeleteIssue) {
				//If the delete button is clicked, delete the issue
				int row = issuesTable.getSelectedRow();
				if (row == -1 || row >= issueTableModel.getRowCount()) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "No issue selected.");
				} else {
					try {
						int issueId = Integer.parseInt(issueTableModel.getValueAt(row, 0).toString());
						IssueManager.getInstance().deleteIssueById(issueId);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, "No issue selected.");
					}
				}
				updateTable(null);
			} else if (e.getSource() == btnEditIssue) {
				//If the edit button is clicked, switch panel based on state
				int row = issuesTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "No issue selected.");
				} else {
					try {
						int issueId = Integer.parseInt(issueTableModel.getValueAt(row, 0).toString());
						String stateName = IssueManager.getInstance().getIssueById(issueId).getStateName();
						if (stateName.equals(Issue.CONFIRMED_NAME)) {
							cardLayout.show(panel, CONFIRMED_PANEL);
							pnlConfirmed.setIssueInfo(issueId);
						} 
						if (stateName.equals(Issue.NEW_NAME)) {
							cardLayout.show(panel, NEW_PANEL);
							pnlNew.setIssueInfo(issueId);
						} 
						if (stateName.equals(Issue.WORKING_NAME)) {
							cardLayout.show(panel, WORKING_PANEL);
							pnlWorking.setIssueInfo(issueId);
						} 
						if (stateName.equals(Issue.VERIFYING_NAME)) {
							cardLayout.show(panel, VERIFYING_PANEL);
							pnlVerifying.setIssueInfo(issueId);
						}  
						if (stateName.equals(Issue.CLOSED_NAME)) {
							cardLayout.show(panel, CLOSED_PANEL);
							pnlClosed.setIssueInfo(issueId);
						} 
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, "No issue selected.");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, "No issue selected.");
					}
				}
			} else if (e.getSource() == btnFilterByBug) {
				updateTable(IssueType.BUG);
			} else if (e.getSource() == btnFilterByEnhancement) {
				updateTable(IssueType.ENHANCEMENT);
			} else if (e.getSource() == btnShowAllIssues) {
				updateTable(null);
			}
			IssueManagerGUI.this.repaint();
			IssueManagerGUI.this.validate();
		}
		
		public void updateTable(IssueType issueType) {
			if (issueType == null) {
				issueTableModel.updateIssueData();
			} else {
				issueTableModel.updateIssueDataByType(issueType);
			}
		}
		
		/**
		 * IssueTableModel is the object underlying the JTable object that displays
		 * the list of Issues to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 */
		private class IssueTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Issue ID", "Issue State", "Issue Type", "Issue Summary"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the IssueTableModel by requesting the latest information
			 * from the IssueTableModel.
			 */
			public IssueTableModel() {
				updateIssueData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col index of column
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row index of row
			 * @param col index of column
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param col location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Issue information from the IssueManager.
			 */
			private void updateIssueData() {
				IssueManager m = IssueManager.getInstance();
				data = m.getIssueListAsArray();
			}
			
			/**
			 * Updates the given model with Issue information for the 
			 * given issue type from the IssueManager.
			 * @param issueType issue type to search for.
			 */
			private void updateIssueDataByType(IssueType issueType) {
				try {
					IssueManager m = IssueManager.getInstance();
					if (issueType == IssueType.ENHANCEMENT) {
						data = m.getIssueListAsArrayByIssueType("Enhancement");
					} else if(issueType == IssueType.BUG) {
						data = m.getIssueListAsArrayByIssueType("Bug");
					}
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, e.getMessage());
				}
			}
		}
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a new issue.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class NewPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IssueInfoPanel that presents the Issue's information to the user */
		private IssueInfoPanel pnlIssueInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for owner id field */
		private JLabel lblOwnerId;
		/** Text field for owner id */
		private JTextField txtOwnerId;
		/** Label for resolution */
		private JLabel lblResolution;
		/** ComboBox for resolution options */
		private JComboBox<String> comboResolution;
		/** Assign action */
		private JButton btnAssign;
		/** Confirm action */
		private JButton btnConfirm;
		/** Resolve action */
		private JButton btnResolve;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Issue's id */
		private int issueId;
		
		/**
		 * Constructs the JPanel for editing a Issue in the NewState.
		 */
		public NewPanel() {
			pnlIssueInfo = new IssueInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Issue Information");
			pnlIssueInfo.setBorder(border);
			pnlIssueInfo.setToolTipText("Issue Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblOwnerId = new JLabel("Owner Id");
			txtOwnerId = new JTextField(15);
			lblResolution = new JLabel("Resolution");
			comboResolution = new JComboBox<String>();
			btnAssign = new JButton("Assign");
			btnConfirm = new JButton("Confirm");
			btnResolve = new JButton("Resolve");
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnConfirm.addActionListener(this);
			btnResolve.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlOwner = new JPanel();
			pnlOwner.setLayout(new GridLayout(1, 2));
			pnlOwner.add(lblOwnerId);
			pnlOwner.add(txtOwnerId);
			
			JPanel pnlResolution = new JPanel();
			pnlResolution.setLayout(new GridLayout(1, 2));
			pnlResolution.add(lblResolution);
			pnlResolution.add(comboResolution);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnAssign);
			pnlBtnRow.add(btnConfirm);
			pnlBtnRow.add(btnResolve);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlOwner, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlResolution, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlIssueInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the IssueInfoPanel with the given issue data.
		 * @param issueId id of the issue
		 */
		public void setIssueInfo(int issueId) {
			this.issueId = issueId;
			pnlIssueInfo.setIssueInfo(this.issueId);
			
			comboResolution.removeAllItems();
			
			comboResolution.addItem("Duplicate");
			comboResolution.addItem("Won't Fix");
			if (IssueManager.getInstance().getIssueById(issueId).getIssueType().contentEquals(Issue.I_BUG)) {
				comboResolution.addItem("Works for Me");
				btnConfirm.setEnabled(true);
				btnAssign.setEnabled(false);
			} else {
				btnConfirm.setEnabled(false);
				btnAssign.setEnabled(true);
			}
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Take care of note.
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			if (e.getSource() == btnAssign) {
				String ownerId = txtOwnerId.getText();
				if (ownerId == null || "".equals(ownerId)) {
					//If owner id is invalid, show an error message
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to issue list
					try {
						Command c = new Command(Command.CommandValue.ASSIGN, ownerId, null, note);
						IssueManager.getInstance().executeCommand(issueId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
				}
			} else if (e.getSource() == btnConfirm) {
				//Try a Command.  If command fails, go back to issue list
				try {
					Command c = new Command(Command.CommandValue.CONFIRM, null, null, note);
					IssueManager.getInstance().executeCommand(issueId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
					reset = false;
				}
			} else if (e.getSource() == btnResolve) {
				//Get resolution
				int idx = comboResolution.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "Invalid information.");
					reset = false;
				} else {				
					Resolution r = null;
					switch (idx) {
					case 0:
						r = Resolution.DUPLICATE;
						break;
					case 1:
						r = Resolution.WONTFIX;
						break;
					case 2:
						r = Resolution.WORKSFORME;
						break;
					default:
						r = null;
					}
					//Try a command.  If problem, go back to issue list.
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, null, r, note);
						IssueManager.getInstance().executeCommand(issueId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			}
			if (reset) {
				//All buttons lead to back issue list if valid info for owner
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				pnlIssueList.updateTable(null);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
				//Reset fields
				txtOwnerId.setText("");
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an working issue.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class WorkingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IssueInfoPanel that presents the Issue's information to the user */
		private IssueInfoPanel pnlIssueInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for selecting a resolution */
		private JLabel lblResolution;
		/** ComboBox for resolutions */
		private JComboBox<String> comboResolution;
		/** Resolve action */
		private JButton btnResolve;
		/** Cancel action */
		private JButton btnCancel;
		/** Current issue's id */
		private int issueId;

		/**
		 * Constructs a JPanel for editing a Issue in the Working State.
		 */
		public WorkingPanel() {
			pnlIssueInfo = new IssueInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Issue Information");
			pnlIssueInfo.setBorder(border);
			pnlIssueInfo.setToolTipText("Issue Information");
			
			lblResolution = new JLabel("Resolution");
			comboResolution = new JComboBox<String>();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnResolve = new JButton("Resolve");
			btnCancel = new JButton("Cancel");
			
			btnResolve.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			JPanel pnlResolution = new JPanel();
			pnlResolution.setLayout(new GridLayout(1, 2));
			pnlResolution.add(lblResolution);
			pnlResolution.add(comboResolution);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnResolve);
			pnlBtnRow.add(btnCancel);

			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlResolution, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(txtNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlIssueInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the IssueInfoPanel with the given issue data.
		 * @param issueId id of the issue
		 */
		public void setIssueInfo(int issueId) {
			this.issueId = issueId;
			pnlIssueInfo.setIssueInfo(this.issueId);
			
			comboResolution.removeAllItems();
			
			comboResolution.addItem("Fixed");
			comboResolution.addItem("Duplicate");
			comboResolution.addItem("Won't Fix");
			if (IssueManager.getInstance().getIssueById(issueId).getIssueType().contentEquals(Issue.I_BUG)) {
				comboResolution.addItem("Works for Me");
			}
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnResolve) {
				//Handle note
				String note = txtNote.getText();
				if ("".equals(note)) {
					note = null;
				}
				//Get resolution
				int idx = comboResolution.getSelectedIndex();
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "Invalid information.");
				} else {
					Resolution r = null;
					switch (idx) {
					case 0: 
						r = Resolution.FIXED;
						break;
					case 1:
						r = Resolution.DUPLICATE;
						break;
					case 2:
						r = Resolution.WONTFIX;
						break;
					case 3:
						r = Resolution.WORKSFORME;
						break;
					default:
						r = null;
					}
					//Try a command.  If problem, go back to issue list.
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, null, r, note);
						IssueManager.getInstance().executeCommand(issueId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			} 
			if (reset) {
				//All buttons lead to back issue list
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				pnlIssueList.updateTable(null);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
				//Reset fields
				comboResolution.setSelectedIndex(0);
				txtNote.setText("");
			}
			//Otherwise, stay on panel
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a confirmed issue
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ConfirmedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IssueInfoPanel that presents the Issue's information to the user */
		private IssueInfoPanel pnlIssueInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for owner id field */
		private JLabel lblOwnerId;
		/** Text field for owner id */
		private JTextField txtOwnerId;
		/** Label for resolution */
		private JLabel lblResolution;
		/** ComboBox for resolution options */
		private JComboBox<String> comboResolution;
		/** Assign action */
		private JButton btnAssign;
		/** Resolve action */
		private JButton btnResolve;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Issue's id */
		private int issueId;
		
		/**
		 * Constructs the JPanel for editing a Issue in the UnconfirmedState.
		 */
		public ConfirmedPanel() {
			pnlIssueInfo = new IssueInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Issue Information");
			pnlIssueInfo.setBorder(border);
			pnlIssueInfo.setToolTipText("Issue Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblOwnerId = new JLabel("Owner Id");
			txtOwnerId = new JTextField(15);
			lblResolution = new JLabel("Resolution");
			comboResolution = new JComboBox<String>();

			btnAssign = new JButton("Assign");
			btnResolve = new JButton("Resolve");
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnResolve.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			GridBagConstraints c = new GridBagConstraints();
			
			JPanel pnlOwner = new JPanel();
			pnlOwner.setLayout(new GridLayout(1, 2));
			pnlOwner.add(lblOwnerId);
			pnlOwner.add(txtOwnerId);
			
			JPanel pnlResolution = new JPanel();
			pnlResolution.setLayout(new GridLayout(1, 2));
			pnlResolution.add(lblResolution);
			pnlResolution.add(comboResolution);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnAssign);
			pnlBtnRow.add(btnResolve);
			pnlBtnRow.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlOwner, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlResolution, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlIssueInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);		
		}
		
		/**
		 * Set the IssueInfoPanel with the given issue data.
		 * @param issueId id of the issue
		 */
		public void setIssueInfo(int issueId) {
			this.issueId = issueId;
			pnlIssueInfo.setIssueInfo(this.issueId);
			
			comboResolution.removeAllItems();
			
			comboResolution.addItem("Won't Fix");
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Take care of note
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			if (e.getSource() == btnResolve) {
				//Get resolution
				int idx = comboResolution.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "Invalid information.");
				} else {				
					Resolution r = null;
					switch (idx) {
					case 0:
						r = Resolution.WONTFIX;
						break;
					default:
						r = null;
					}
					//Try a command.  If problem, go back to issue list.
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, null, r, note);
						IssueManager.getInstance().executeCommand(issueId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			} else if (e.getSource() == btnAssign) {
				String ownerId = txtOwnerId.getText();
				if (ownerId == null || "".equals(ownerId)) {
					//If owner id is invalid, show an error message
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to issue list
					try {
						Command c = new Command(Command.CommandValue.ASSIGN, ownerId, null, note);
						IssueManager.getInstance().executeCommand(issueId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
				}
			}
			if (reset) {
				//Add buttons lead to back issue list
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				pnlIssueList.updateTable(null);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
				//Reset note
				txtNote.setText("");
				txtOwnerId.setText("");
			}
		}
		
	}

	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a verifying issue.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class VerifyingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IssueInfoPanel that presents the Issue's information to the user */
		private IssueInfoPanel pnlIssueInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Verify action */
		private JButton btnVerify;
		/** Reopen action */
		private JButton btnReopen;
		/** Cancel action */
		private JButton btnCancel;
		/** Current issue's id */
		private int issueId;

		/**
		 * Constructs a JFrame for editing a Issue in the Verifying State.
		 */
		public VerifyingPanel() {
			pnlIssueInfo = new IssueInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Issue Information");
			pnlIssueInfo.setBorder(border);
			pnlIssueInfo.setToolTipText("Issue Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnVerify = new JButton("Verify");
			btnReopen = new JButton("Reopen");
			btnCancel = new JButton("Cancel");
			
			btnVerify.addActionListener(this);
			btnReopen.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");

			GridBagConstraints c = new GridBagConstraints();
						
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnVerify);
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlIssueInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the IssueInfoPanel with the given issue data.
		 * @param issueId id of the issue
		 */
		public void setIssueInfo(int issueId) {
			this.issueId = issueId;
			pnlIssueInfo.setIssueInfo(this.issueId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			//Handle note
			String note = txtNote.getText();
			boolean reset = true;
			if ("".equals(note)) {
				note = null;
			}
			if (e.getSource() == btnVerify) {
				//Try command.  If problem, go to issue list.
				try {
					Command c = new Command(Command.CommandValue.VERIFY, null, null, note);
					IssueManager.getInstance().executeCommand(issueId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
					reset = false;
				}
			} else if (e.getSource() == btnReopen) {
				//Try command.  If problem, go to issue list.
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null, null, note);
					IssueManager.getInstance().executeCommand(issueId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back issue list
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				pnlIssueList.updateTable(null);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
				//Reset note
				txtNote.setText("");
			}
			//Stay here if error.
		}
	}

	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a closed issue.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ClosedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IssueInfoPanel that presents the Issue's information to the user */
		private IssueInfoPanel pnlIssueInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Reopen action */
		private JButton btnReopen;
		/** Cancel action */
		private JButton btnCancel;
		/** Current issue's id */
		private int issueId;

		/**
		 * Constructs a JPanel for editing a Issue in the ClosedState.
		 */
		public ClosedPanel() {
			pnlIssueInfo = new IssueInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Issue Information");
			pnlIssueInfo.setBorder(border);
			pnlIssueInfo.setToolTipText("Issue Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnReopen = new JButton("Reopen");
			btnCancel = new JButton("Cancel");
			
			btnReopen.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");

			GridBagConstraints c = new GridBagConstraints();
						
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlIssueInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the IssueInfoPanel with the given issue data.
		 * @param issueId id of the issue
		 */
		public void setIssueInfo(int issueId) {
			this.issueId = issueId;
			pnlIssueInfo.setIssueInfo(this.issueId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnReopen) {
				String note = txtNote.getText();
				if ("".equals(note)) {
					note = null;
				}
				//Try command.  If problem, go back to issue list
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null, null, note);
					IssueManager.getInstance().executeCommand(issueId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, iae.getMessage());
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(IssueManagerGUI.this, uoe.getMessage());
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back issue list
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				pnlIssueList.updateTable(null);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
				txtNote.setText("");
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the issue.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class IssueInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for summary */
		private JLabel lblSummary;
		/** Field for summary */
		private JTextArea txtSummary;
		/** Label for owner */
		private JLabel lblOwner;
		/** Field for owner */
		private JTextField txtOwner;
		/** Label for issue type */
		private JLabel lblIssueType;
		/** Field for issue type */
		private JTextField txtIssueType;
		/** Label for confirmed */
		private JLabel lblConfirmed;
		/** Field for confirmed */
		private JTextField txtConfirmed;
		/** Label for resolution */
		private JLabel lblResolution;
		/** Field for resolution */
		private JTextField txtResolution;
		/** Label for notes */
		private JLabel lblNotes;
		/** Field for notes */
		private JTextArea txtNotes;
		
		/** 
		 * Construct the panel for the issue information.
		 */
		public IssueInfoPanel() {
			super(new GridBagLayout());
			
			lblId = new JLabel("Issue Id");
			lblState = new JLabel("Issue State");
			lblSummary = new JLabel("Issue Summary");
			lblOwner = new JLabel("Owner");
			lblIssueType = new JLabel("Issue Type");
			lblConfirmed = new JLabel("Confirmed");
			lblResolution = new JLabel("Resolution");
			lblNotes = new JLabel("Notes");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtSummary = new JTextArea(15, 3);
			txtOwner = new JTextField(15);
			txtIssueType = new JTextField(15);
			txtConfirmed = new JTextField(15);
			txtResolution = new JTextField(15);
			txtNotes = new JTextArea(30, 5);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtSummary.setEditable(false);
			txtOwner.setEditable(false);
			txtIssueType.setEditable(false);
			txtConfirmed.setEditable(false);
			txtResolution.setEditable(false);
			txtNotes.setEditable(false);
			
			JScrollPane summaryScrollPane = new JScrollPane(txtSummary, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			JScrollPane notesScrollPane = new JScrollPane(txtNotes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - ID and State
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 4));
			row1.add(lblId);
			row1.add(txtId);
			row1.add(lblState);
			row1.add(txtState);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row1, c);
			
			//Row 2 - Summary title
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblSummary, c);
			
			//Row 3 - Summary text area
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(summaryScrollPane, c);
			
			//Row 4 - Issue Type & Owner
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 4));
			row4.add(lblIssueType);
			row4.add(txtIssueType);
			row4.add(lblOwner);
			row4.add(txtOwner);
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row4, c);
			
			//Row 5 - Issue Type & Confirmed
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 4));
			row5.add(lblConfirmed);
			row5.add(txtConfirmed);
			row5.add(lblResolution);
			row5.add(txtResolution);
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row5, c);
			
			
			//Row 6 - Notes title
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNotes, c);
			
			//Row 7 - Notes text area
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 4;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(notesScrollPane, c);
		}
		
		/**
		 * Adds information about the issue to the display.  
		 * @param issueId the id for the issue to display information about.
		 */
		public void setIssueInfo(int issueId) {
			//Get the issue from the model
			Issue i = IssueManager.getInstance().getIssueById(issueId);
			if (i == null) {
				//If the issue doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(IssueManagerGUI.this, "Invalid information.");
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + i.getIssueId());
				txtState.setText(i.getStateName());
				txtSummary.setText(i.getSummary());
				txtOwner.setText(i.getOwner());
				txtIssueType.setText(i.getIssueType());
				txtConfirmed.setText("" + i.isConfirmed());
				String resolutionString = i.getResolution();
				if (resolutionString == null) {
					txtResolution.setText("");
				} else {
					txtResolution.setText("" + resolutionString);
				}
				txtNotes.setText(i.getNotesString());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new issue.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class AddIssuePanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for issue type */
		private JLabel lblIssueType;
		/** Combo box for issue type */
		private JComboBox<String> comboIssueType;
		/** Label for identifying summary text field */
		private JLabel lblSummary;
		/** Text field for entering summary information */
		private JTextArea txtSummary;
		/** Label for identifying note text field */
		private JLabel lblNote;
		/** Text field for entering note information */
		private JTextArea txtNote;
		/** Button to add a issue */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the JPanel for adding new issues to the 
		 * manager.
		 */
		public AddIssuePanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblIssueType = new JLabel("Issue Type");
			comboIssueType = new JComboBox<String>();
			comboIssueType.addItem("Enhancement");
			comboIssueType.addItem("Bug");
			lblSummary = new JLabel("Issue Summary");
			txtSummary = new JTextArea(1, 30);
			lblNote = new JLabel("Issue Note");
			txtNote = new JTextArea(5, 30);
			btnAdd = new JButton("Add Issue to List");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Builds issue type panel, which is a 1 row, 2 col grid
			JPanel pnlIssueType = new JPanel();
			pnlIssueType.setLayout(new GridLayout(1, 2));
			pnlIssueType.add(lblIssueType);
			pnlIssueType.add(comboIssueType);
			
			//Creates scroll for note text area
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			//Build button panel, which is a 1 row, 2 col grid
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 2));
			pnlButtons.add(btnAdd);
			pnlButtons.add(btnCancel);
			
			//Adds all panels to main panel
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlIssueType, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblSummary, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(txtSummary, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlButtons, c);
			
			//Empty panel to cover the bottom portion of the screen
			JPanel pnlFiller = new JPanel();
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 10;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlFiller, c);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				//Add issue to the list
				int idx = comboIssueType.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(IssueManagerGUI.this, "Issue cannot be created.");
				} else {				
					IssueType type = null;
					switch (idx) {
					case 0:
						type = IssueType.ENHANCEMENT;
						break;
					case 1:
						type = IssueType.BUG;
						break;
					default:
						type = null;
					}
					String summary = txtSummary.getText();
					String note = txtNote.getText();
					//Get instance of model and add issue
					try {
						IssueManager.getInstance().addIssueToList(type, summary, note);
					} catch (IllegalArgumentException exp) {
						reset = false;
						JOptionPane.showMessageDialog(IssueManagerGUI.this, "Issue cannot be created.");
					}
				}
			} 
			if (reset) {
				//All buttons lead to back issue list
				cardLayout.show(panel, ISSUE_LIST_PANEL);
				pnlIssueList.updateTable(null);
				IssueManagerGUI.this.repaint();
				IssueManagerGUI.this.validate();
				//Reset fields
				txtSummary.setText("");
				txtNote.setText("");
			}
		}
	}
}