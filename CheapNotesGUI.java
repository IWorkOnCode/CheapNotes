import javax.swing.JFrame;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
//import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
//import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
//import javax.swing.JSeparator;
import javax.swing.JToolBar;
//import javax.swing.ListModel;
//import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
//import javax.swing.SwingUtilities;
//import javax.swing.WindowConstants;
//import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.awt.*;

//import com.javaquizplayer.examples.notesapp.NotesAppGui.HelpActionListener;

//import com.javaquizplayer.examples.notesapp.NotesAppGui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;


/*representation of the GUI for CheapNotes. While this GUI is modified visually in some ways to accommodate how CheapNotes will work 
(such as the addition of the resume button or functions in getListWithScrollPane to get the names of the files already in 
 the database), this is copied from an example, however CheapNotes uses a different Database system.
 GUI skeleton gotten from https://javaquizplayer.com/examples/notesapp-using-swing-h2database-JPA-example.html#Build_the_GUI*/
public class CheapNotesGUI {

	private JFrame frame;
	private JList<String> fileList;
	private DefaultListModel<String> listContent = new DefaultListModel<String>();
	private JTextField textFld;
	private JTextArea textArea;	
	private JButton newButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton resumeButton;
	private DatabaseConnector connection = new DatabaseConnector();

	private static final Font FONT_FOR_PROGRAM =
			new Font("Arial", Font.PLAIN, 16);	


	public CheapNotesGUI() {
		frame = new JFrame("CheapNotes - Simple and to the Point");
		addWidgetsToFrame(frame);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //got this code from template
		frame.setLocation(350, 50); // x, y
		frame.setResizable(false);
		frame.pack();		
		frame.setVisible(true);
	}


	private void addWidgetsToFrame(JFrame frame) {
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());

		pane.add(getListInScrollPane(), getConstraintsForList());		
		pane.add(getTextField(), getConstraintsForTextFld());		
		pane.add(getTextAreaInScrollPane(), getConstraintsForTextArea());	
		pane.add(getToolBarWithButtons(), getConstraintsForButtonToolBar());
	}

	private JTextField getTextField() {
		textFld = new JTextField("", 25);
		textFld.setFont(FONT_FOR_PROGRAM);
		textFld.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, rt
		return textFld;
	}

	//creates the section where all the names of the files are listed to be selected
	private JScrollPane getListInScrollPane() {
		//creates a list
		fileList = new JList<String>(listContent);
		fileList.setBorder(new EmptyBorder(5, 5, 5, 5));
		fileList.setFixedCellHeight(26);
		fileList.setFont(FONT_FOR_PROGRAM);		
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//reads the database to put all the names of the saved files in the list
		ArrayList<String> nameArray = new ArrayList<String>();

		nameArray = connection.readContent();

		for (int i = 0; i < nameArray.size(); i++) {
			listContent.addElement(nameArray.get(i));
		}

		//puts the list in a place with a scrollbar
		JScrollPane scroller = new JScrollPane(fileList);
		scroller.setPreferredSize(new Dimension(250, 350)); // wxh
		scroller.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


		return scroller;
	}


	private JScrollPane getTextAreaInScrollPane() {

		textArea = new JTextArea("");
		textArea.setFont(FONT_FOR_PROGRAM);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, rt

		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setPreferredSize(new Dimension(550, 0)); // wxh
		scroller.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);



		return scroller;
	}

	private JToolBar getToolBarWithButtons() {

		newButton = new JButton("New");	
		saveButton = new JButton("Save");		
		deleteButton = new JButton("Delete");
		resumeButton = new JButton("Resume");

		JToolBar toolBar = getToolBarForButtons();
		toolBar.add(newButton);
		toolBar.addSeparator(new Dimension(2, 0));
		toolBar.add(saveButton);
		toolBar.addSeparator(new Dimension(2, 0));
		toolBar.add(deleteButton);
		toolBar.addSeparator(new Dimension(2, 0));	
		toolBar.add(resumeButton);
		toolBar.addSeparator(new Dimension(2, 0));

		//sends the buttons to buttonFunctions to give them functionality
		buttonFunctions(newButton, saveButton, deleteButton, resumeButton);

		return toolBar;
	}


	private JToolBar getToolBarForButtons() {

		JToolBar toolBar = new JToolBar();
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		return toolBar;
	}

	private GridBagConstraints getConstraintsForList() {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.insets = new Insets(12, 12, 11, 11); // top, left, bottom, right
		return c;
	}

	private GridBagConstraints getConstraintsForTextFld() {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(12, 0, 11, 11);
		return c;
	}

	private GridBagConstraints getConstraintsForTextArea() {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 11, 11);
		return c;
	}

	private GridBagConstraints getConstraintsForButtonToolBar() {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 12, 11, 11);
		return c;
	}

	//gives functionality to the buttons
	private void buttonFunctions(JButton newButton, JButton saveButton, JButton deleteButton, JButton resumeButton) {
		//DatabaseConnector connection = new DatabaseConnector();


		//creates a new blank space for another file to save
		newButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if (textFld.getText().isEmpty() && textArea.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "New file already exists; please fill it in.");

				else {
					textFld.setText("");
					textArea.setText("");
				}
			}
		});

		//either saves the data to the database, or updates the database if the name is the same
		//created entirely by me and not copied from the template
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				//int saveCheck = 0;

				//if either the title or text is blank, print exception
				if (textFld.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a title.");
				}

				if (textArea.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter text.");
				}

				//else, check if any other files in the database have the same name. If they do, then update the file instead
				//of creating a new one
				else {
					if (listContent.contains(textFld.getText())) {
						connection.update(textFld.getText(), textArea.getText());
					}

					else {
						connection.saveNewFile(textFld.getText(), textArea.getText());
						listContent.addElement(textFld.getText());
					}

					JOptionPane.showMessageDialog(null, "File saved.");
				}
			}
		});

		//deletes the file from the database
		deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				//catch the user if the file isn't selected
				if (fileList.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select file to delete.");
				}

				//catch the user if the file isn't opened
				else if (fileList.getSelectedValue() != null && textFld.getText().equals("") && textArea.getText().equals("")){
					JOptionPane.showMessageDialog(null, "File must be opened to be deleted.");
				}

				//actually delete the file if you wish to
				else {
					int check = JOptionPane.showConfirmDialog(frame, "Do you wish to delete this file?", 
							"Delete File", JOptionPane.YES_NO_OPTION);

					if (check == 0) {
						connection.delete(textFld.getText(), textArea.getText());
						listContent.removeElement(textFld.getText());
						textFld.setText("");
						textArea.setText("");
						JOptionPane.showMessageDialog(null, "File deleted.");
					}
				}
			}
		});

		//allows you to open files in the database already
		resumeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if (fileList.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select file to resume editing.");
				}
				else {
					String[] fileContents = new String[2];
					fileContents = connection.openFile(fileList.getSelectedValue());
					textFld.setText(fileContents[0]);
					textArea.setText(fileContents[1]);
				}
			}
		});

	}

}
