import javax.swing.SwingUtilities;

public class MainFile {

	
		public static void main(String [] args) {
			
			SwingUtilities.invokeLater(new Runnable() {
	    
				public void run() {
	        
					new CheapNotesGUI();
				}
			});
		}

	

}
