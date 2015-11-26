import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;


public class UserPanel extends JPanel
{
      private Backend backend;         //instance of Backend
    	private ArrayList<User> users;   //array of users
      private JButton addManually;     //add a user manually by their IP address
      private JButton checkForNew;     //check for new onine users
      
      public UserPanel(ArrayList<User> u, Backend b)
      //PRE:
      //POST:
      {
         JPanel mainPan;               //main panel for this class
         
         users = u;
         backend = b;
         
         mainPan = new JPanel();
         mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.PAGE_AXIS)); //from top to bottom
         //mainPan.setLayout(new GridLayout(2,1));
         add(mainPan);

         
         addManually = new JButton("Add an user by IP");
         mainPan.add(addManually);
         
         checkForNew = new JButton("Find online users");
         mainPan.add(checkForNew);
         
      }
}