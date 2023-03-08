package proiect;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.ListModel;

import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import java.awt.Color;

public class Main {

	private JFrame frame; // fereastra gui
	
	private JPanel leftPanel; 	// jumatatea stanga
	private JPanel rightPanel;	// jumatatea dreapta
	
	private JLabel titleCreatorLabel; 		// titlu "Creator"
	private JLabel registerCreatorLabel; 	// label "Creator sign up:"
	private JLabel creatorNameLabel; 		// label "Name"
	private JLabel selectCreatorLabel; 		// label "Creators:"
	private JLabel followersLabel; 			// label "Followers:"
	private JLabel titleFollowerLabel; 		// titlu "User"
	private JLabel registerFollowerLabel; 	// label "User sign up:"
	private JLabel followerNameLabel; 		// label "Name"
	private JLabel selectFollowerLabel; 	// label "Users:"
	private JLabel toFollowLabel; 			// label "Search"
	private JLabel followedCreatorsLabel; 	// label "Following"
	private JLabel notificationsLabel; 		// label "Notifications:"
	
	private JPanel registrationPanelCreator; 	// panou de inregistrare Creator
	private JPanel followersPanel;				// panou de indicare urmaritori
	private JPanel submitContentPanel;			// panou submit content
	private JPanel registrationPanelFollower; 	// panou de inregistrare Follower
	private JPanel toFollowPanel;				// panou de follow/unfollow
	private JPanel followedCreatorsPanel;		// panou de indicare creatori urmariti
	
	private JButton registerCreatorButton; 	// butonul de RegisterC
	private JButton registerFollowerButton; // butonul de RegisterF
	private JButton submitContentButton; 	// butonul de Submit Content
	private JButton toFollowNameButton; 	// butonul de Follow
	private JButton toUnfollowNameButton;   // butonul de Unfollow
	
	private JTextField submitContentTextField; 			// campul de text prin care se da submit la content
	private JTextField registerCreatorNameTextField; 	// campul de text prin care se da register la un creator
	private JTextField registerFollowerNameTextField; 	// campul de text prin care se da register la un follower
	private JTextField toFollowNameTextField; 			// campul de text prin care se introduce numele creatorului ce va fi urmarit
	private JTextArea notificationsTextArea; 			// zona de text unde se afiseaza notificarile

	private final DefaultListModel<ContentCreator> contentCreatorsDLM; 	// lista cu referintele tuturor creatorilor
	private final DefaultListModel<Follower> followersDLM; 				// lista cu referintele tuturor followerilor

	private JList<String> selectCreatorJList; 		// lista gui pentru selectie creatori
	private JList<String> selectFollowerJList; 		// lista gui pentru selectie followeri
	private JList<String> seeFollowedCreatorsJList; // lista gui pentru vizualizare creatori urmariti
	private JList<String> seeFollowersJList; 		// lista gui pentru vizualizare urmaritori

	private JScrollPane selectCreatorScrollPane; 		// scroll pane pentru selectCreatorJList
	private JScrollPane selectFollowerScrollPane; 		// scroll pane pentru selectFollowerJList
	private JScrollPane seeFollowedCreatorsScrollPane; 	// scroll pane pentru seeFollowedCreatorsJList
	private JScrollPane seeFollowersScrollPane; 		// scroll pane pentru seeFollowersJList

	/**
	 * Porneste aplicatia.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creaza aplicatia.
	 */
	public Main() {
		contentCreatorsDLM = new DefaultListModel<>();
		followersDLM = new DefaultListModel<>();

		seeFollowedCreatorsJList = new JList<String>();
		seeFollowersJList = new JList<String>();

		addDefaultUsers();

		selectCreatorJList = new JList<String>(getNameList(contentCreatorsDLM));
		selectFollowerJList = new JList<String>(getNameList(followersDLM));

		selectCreatorScrollPane = new JScrollPane(selectCreatorJList);
		selectFollowerScrollPane = new JScrollPane(selectFollowerJList);
		seeFollowedCreatorsScrollPane = new JScrollPane();
		seeFollowersScrollPane = new JScrollPane();

		notificationsTextArea = new JTextArea();

		initGUI();
	}

	/**
	 * Listener pentru selectie elemente din cele 2 liste selectCreatorJList si selectFollowerJList
	 */
	ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {

			if (!e.getValueIsAdjusting()) {
				@SuppressWarnings("unchecked")
				JList<String> sourceList = (JList<String>) e.getSource();

				if (sourceList == selectFollowerJList) { 									// Sursa este selectFollowerJList, lista din care selectezi urmaritorii
					String nameOfSelectedUser = sourceList.getSelectedValue();
					Follower selectedUser = null;

					// Obtine referinta urmaritorului dupa numele selectat din lista
					selectedUser = getPersonInstance(followersDLM, nameOfSelectedUser);
					
					// Updateaza lista cu creatori urmariti
					seeFollowedCreatorsJList = new JList<String>(selectedUser.getFollowedCreators());
					seeFollowedCreatorsScrollPane.setViewportView(seeFollowedCreatorsJList);

					// Updateaza notificarile
					updateNotificationsArea(selectedUser);
				} 
				else if (sourceList == selectCreatorJList) {								// Sursa este selectCreatorJList, lista din care selectezi creatorii				
					String nameOfSelectedCreator = sourceList.getSelectedValue();
					ContentCreator selectedCreator = null;
					
					// Obtine referinta creatorului dupa numele selectat din lista
					selectedCreator = getPersonInstance(contentCreatorsDLM, nameOfSelectedCreator);
					
					// Updateaza lista cu urmaritori
					seeFollowersJList = new JList<String>(selectedCreator.getFollowers());
					seeFollowersScrollPane.setViewportView(seeFollowersJList);

				}
			}
		}
	};

	/**
	 * Listener pentru toate butoanele din pane-ul Creator
	 */
	ActionListener creatorButtonsListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton sourceButton = (JButton) e.getSource();

			if (sourceButton.getText().equals("RegisterC")) { 								// Sursa este butonul de Register al panoului din stanga
				// Numele creatorului de inregistrat
				String nameCreator = registerCreatorNameTextField.getText();
				// Liste cu toate numele
				String[] nameListC = getNameList(contentCreatorsDLM);
				String[] nameListF = getNameList(followersDLM);

				// Goleste text field-ul
				registerCreatorNameTextField.setText(""); 

				if (!nameCreator.equals("") && isUnique(nameListC, nameCreator) && isUnique(nameListF, nameCreator)) { // Numele este unic global si nu este ""
					// Creaza un nou creator
					ContentCreator newContentCreator = new ContentCreator(nameCreator);
					contentCreatorsDLM.addElement(newContentCreator);
					
					// Updateaza lista din care selectezi creatorii
					selectCreatorJList = null;
					selectCreatorJList = new JList<String>(getNameList(contentCreatorsDLM));
					// Update la vizualizare lista
					selectCreatorScrollPane.setViewportView(selectCreatorJList);
					// Adauga listener din nou(cel vechi este pentru referinta pierduta)
					selectCreatorJList.addListSelectionListener(listSelectionListener);
				}
				else
				{
					// Do nothing
				}
			} 
			else if (sourceButton.getText().equals("Submit Content")) { 					// Sursa este butonul de Submit Content
				// Numele contentului
				String contentField = submitContentTextField.getText();
				// Numele contentului nu poate fi gol
				if (!contentField.equals("")) {
					// Numele creatorului selectat
					String selectedContentCreatorName = selectCreatorJList.getSelectedValue();
					// Obtine instanta creatorului selectat dupa nume
					ContentCreator selectedContentCreator = getPersonInstance(contentCreatorsDLM, selectedContentCreatorName);
		
					if (selectedContentCreator != null) {	//Daca exista
						// Trimite notificari
						selectedContentCreator.setContent(selectedContentCreatorName+" a postat "+contentField); 
						submitContentTextField.setText("");
					}
					else
					{
						// Do nothing
					}
				}
			}
		}
	};

	/**
	 * Listener pentru toate butoanele din pane-ul Follower
	 */
	ActionListener followerButtonsListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton sourceButton = (JButton) e.getSource(); // Ce buton a fost apasat

			if (sourceButton.getText().equals("RegisterF")) { 								// Sursa este butonul de Register al panoului din dreapta
				// Numele urmaritorului de inregistrat
				String nameFollower = registerFollowerNameTextField.getText();
				registerFollowerNameTextField.setText("");

				// Liste cu toate numele
				String[] nameListC = getNameList(contentCreatorsDLM);
				String[] nameListF = getNameList(followersDLM);

				if (!nameFollower.equals("") && isUnique(nameListF, nameFollower) && isUnique(nameListC, nameFollower)) {	// Numele e unic si nu este ""
					// Creaza un urmaritor nou
					Follower newFollower = new Follower(nameFollower);
					followersDLM.addElement(newFollower);
					// Updateaza lista din care selectezi urmaritorii
					selectFollowerJList = null;
					selectFollowerJList = new JList<String>(getNameList(followersDLM));
					// Update la vizualizare lista
					selectFollowerScrollPane.setViewportView(selectFollowerJList);
					// Adauga listener din nou(cel vechi este pentru referinta pierduta)
					selectFollowerJList.addListSelectionListener(listSelectionListener);
				}
			} 
			else if (sourceButton.getText().equals("Follow")) { 							// Sursa este butonul de Follow
				// Salvez numele creatorului care urmeaza a fi urmarit
				String creatorGoGetta = toFollowNameTextField.getText();
				// Salvez selectia
				String selectedFollowerName = selectFollowerJList.getSelectedValue(); 

				// Daca am text in caseta, am selectat user si nu exista pe lista de following
				if (!creatorGoGetta.equals("") && selectedFollowerName != null && isUniqueJList(seeFollowedCreatorsJList, creatorGoGetta)) {
					ContentCreator creatorToFollow = null;
					Follower selectedFollower = null;
					
					// Iau instanta creatorului de urmarit dupa nume
					creatorToFollow = getPersonInstance(contentCreatorsDLM, creatorGoGetta);
					
					// Salvez lista de nume a tuturor urmaritorilor dupa lista DLM si caut indexul dupa nume, pentru a obtine referinta
					selectedFollower = getPersonInstance(followersDLM, selectedFollowerName);

					if (creatorToFollow != null&&selectedFollower != null) // Daca creatorul exista si a fost selectat un urmaritor din lista
					{
						toFollowNameTextField.setText("");
						// Follow
						creatorToFollow.follow(selectedFollower);
						selectedFollower.follow(creatorToFollow);
						
						// Updateaza lista cu creatori urmariti, lista cu urmaritori si vederile listelor in GUI
						String[] followedCreators = selectedFollower.getFollowedCreators();
						seeFollowedCreatorsJList = new JList<String>(followedCreators);
						seeFollowedCreatorsScrollPane.setViewportView(seeFollowedCreatorsJList);
						
						String[] followers = creatorToFollow.getFollowers();
						seeFollowersJList = new JList<String>(followers);
						seeFollowersScrollPane.setViewportView(seeFollowersJList);
						
						// Updateaza notificarile
						String notif = "You are now following " + creatorGoGetta;
						selectedFollower.update(notif);
						updateNotificationsArea(selectedFollower);
					} 
					else { // Daca numele creatorului introdus nu exista sau nu s-a selectat un urmaritor
						// Nu s-a gasit nici un rezultat
						notificationsTextArea.setText("No results found.");
					}
				}
			}
			else if(sourceButton.getText().equals("Unfollow"))	// Sursa este butonul de Unfollow
			{
				// Creatorul caruia i se da unfollow
				String unfollowCreator = toFollowNameTextField.getText();
				// Salvez utilizatorul selectat
				String selectedFollowerName = selectFollowerJList.getSelectedValue();
				
				if (!unfollowCreator.equals("") && selectedFollowerName != null) {
					Follower selectedFollower = null;
					ContentCreator creatorToUnfollow = null;

					// Iau instanta creatorului de urmarit dupa nume
					creatorToUnfollow = getPersonInstance(contentCreatorsDLM, unfollowCreator);
					// Salvez lista de nume a tuturor urmaritorilor dupa lista DLM si caut indexul dupa nume, pentru a obtine referinta
					selectedFollower = getPersonInstance(followersDLM, selectedFollowerName);
					
					if(creatorToUnfollow!=null&&selectedFollower!=null){// Daca a fost selectat un utilizator si creatorul a fost gasit
						// Liste temporare pentru a stoca urmaritorii creatorului creatorToUnfollow
						// si pentru a stoca creatorii urmariti de selectedFollower
						String[] followedCreators = selectedFollower.getFollowedCreators();
						String[] followers = creatorToUnfollow.getFollowers();
						// Cauta numele
						for (String creator : followedCreators) {
							if (creator.equals(unfollowCreator)) {
								// Nume gasit -> unfollow
								creatorToUnfollow.unfollow(selectedFollower);
								selectedFollower.unfollow(creatorToUnfollow);
								
								// Updateaza lista cu creatori urmariti
								List<String> list = new ArrayList<String>(Arrays.asList(followedCreators));
								list.remove(creator);
								followedCreators = list.toArray(new String[0]);
								seeFollowedCreatorsJList = new JList<String>(followedCreators);
								// Updateaza lista cu urmaritori
								list = new ArrayList<String>(Arrays.asList(followers));
								System.out.println(list.size());
								list.remove(selectedFollowerName);
								System.out.println(list.size());
								followedCreators = list.toArray(new String[0]);
								seeFollowersJList = new JList<String>(followers);
								System.out.println(seeFollowersJList.getModel().getElementAt(0));
								// Updateaza vizualizarea listelor in GUI						
								seeFollowersScrollPane.setViewportView(seeFollowersJList);
								seeFollowedCreatorsScrollPane.setViewportView(seeFollowedCreatorsJList);
								
								// Updateaza notificarile
								String notif = "You have unfollowed " + unfollowCreator;
								selectedFollower.update(notif);
								updateNotificationsArea(selectedFollower);
								
								break;
							}
						}
					}
					else if(creatorToUnfollow==null||selectedFollower==null){// Daca nu a fost selectat un urmaritor sau nu a fost gasit creatorul
						// Do nothing
					}
				}
			}
		}
	};

	/**
	 * Initializeaza GUI.
	 */
	private void initGUI() {

		// fereastra mare
		frame = new JFrame("basically instagram");
		frame.setBounds(100, 100, 800, 500);

		// Initializare panou stanga
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 390, 461);
		frame.getContentPane().add(leftPanel);
		leftPanel.setLayout(null);

		// Initializare panou dreapta
		rightPanel = new JPanel();
		rightPanel.setBounds(394, 0, 390, 461);
		frame.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);

		// eticheta titlu "Creator"
		titleCreatorLabel = new JLabel("Creator");
		titleCreatorLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		titleCreatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleCreatorLabel.setBounds(0, 0, 390, 42);
		leftPanel.add(titleCreatorLabel);

		// panou register Creator
		registrationPanelCreator = new JPanel();
		registrationPanelCreator.setBounds(10, 53, 370, 47);
		leftPanel.add(registrationPanelCreator);
		registrationPanelCreator.setLayout(null);

		// eticheta register Creator
		registerCreatorLabel = new JLabel("Creator sign up:");
		registerCreatorLabel.setBounds(0, 0, 380, 23);
		registrationPanelCreator.add(registerCreatorLabel);

		// eticheta nume Creator
		creatorNameLabel = new JLabel("Name");
		creatorNameLabel.setBounds(0, 28, 49, 14);
		registrationPanelCreator.add(creatorNameLabel);

		// caseta text register Creator
		registerCreatorNameTextField = new JTextField();
		registerCreatorNameTextField.setColumns(10);
		registerCreatorNameTextField.setBounds(59, 25, 200, 20);
		registrationPanelCreator.add(registerCreatorNameTextField);

		// buton register Creator
		registerCreatorButton = new JButton("RegisterC");
		registerCreatorButton.setBounds(269, 24, 101, 23);
		registrationPanelCreator.add(registerCreatorButton);
		registerCreatorButton.addActionListener(creatorButtonsListener);
		registerCreatorButton.setEnabled(true);

		// eticheta caseta selectie
		selectCreatorLabel = new JLabel("Creators:");
		selectCreatorLabel.setBounds(10, 100, 380, 23);
		leftPanel.add(selectCreatorLabel);

		// panou de scroll Creator
		selectCreatorScrollPane.setBounds(10, 125, 370, 100);
		leftPanel.add(selectCreatorScrollPane);
		selectCreatorJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectCreatorJList.addListSelectionListener(listSelectionListener);

		// eticheta titlu "Follower"
		titleFollowerLabel = new JLabel("User");
		titleFollowerLabel.setBounds(151, 5, 87, 25);
		titleFollowerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleFollowerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		rightPanel.add(titleFollowerLabel);

		// panou register Follower
		registrationPanelFollower = new JPanel();
		registrationPanelFollower.setBounds(10, 52, 370, 47);
		rightPanel.add(registrationPanelFollower);
		registrationPanelFollower.setLayout(null);

		// eticheta register Follower
		registerFollowerLabel = new JLabel("User sign up:");
		registerFollowerLabel.setBounds(0, 0, 380, 23);
		registrationPanelFollower.add(registerFollowerLabel);

		// eticheta nume Follower
		followerNameLabel = new JLabel("Name");
		followerNameLabel.setBounds(0, 28, 49, 14);
		registrationPanelFollower.add(followerNameLabel);

		// caseta text Follower
		registerFollowerNameTextField = new JTextField();
		registerFollowerNameTextField.setColumns(10);
		registerFollowerNameTextField.setBounds(59, 25, 200, 20);
		registrationPanelFollower.add(registerFollowerNameTextField);

		registerFollowerButton = new JButton("RegisterF");
		registerFollowerButton.setBounds(269, 24, 101, 23);
		registrationPanelFollower.add(registerFollowerButton);
		registerFollowerButton.addActionListener(followerButtonsListener);
		registerFollowerButton.setEnabled(true);

		selectFollowerLabel = new JLabel("Users:");
		selectFollowerLabel.setBounds(10, 101, 370, 23);
		rightPanel.add(selectFollowerLabel);

		selectFollowerScrollPane.setBounds(10, 125, 370, 100);
		rightPanel.add(selectFollowerScrollPane);
		selectFollowerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectFollowerJList.addListSelectionListener(listSelectionListener);

		// panou Followers
		followersPanel = new JPanel();
		followersPanel.setBounds(10, 236, 370, 86);
		leftPanel.add(followersPanel);
		followersPanel.setLayout(null);

		submitContentPanel = new JPanel();
		submitContentPanel.setBounds(10, 427, 370, 34);
		leftPanel.add(submitContentPanel);

		// LABEL FOLLOWERS
		followersLabel = new JLabel("Followers:");
		followersLabel.setBounds(0, 0, 370, 14);
		followersPanel.add(followersLabel);

		// SCROLLPANE FOLLOWERS
		seeFollowersScrollPane.setViewportView(seeFollowersJList);
		seeFollowersScrollPane.setBounds(0, 25, 370, 61);
		followersPanel.add(seeFollowersScrollPane);

		submitContentTextField = new JTextField();
		submitContentTextField.setPreferredSize(new Dimension(200, 24));
		submitContentPanel.add(submitContentTextField);

		submitContentButton = new JButton("Submit Content");
		submitContentPanel.add(submitContentButton);
		submitContentButton.addActionListener(creatorButtonsListener);

		toFollowPanel = new JPanel();
		toFollowPanel.setBounds(10, 236, 185, 86);
		rightPanel.add(toFollowPanel);
		toFollowPanel.setLayout(null);

		// caseta text toFollow
		toFollowNameTextField = new JTextField();
		toFollowNameTextField.setBounds(0, 25, 175, 24);
		toFollowNameTextField.setPreferredSize(new Dimension(200, 24));
		toFollowPanel.add(toFollowNameTextField);

		toFollowNameButton = new JButton("Follow");
		toFollowNameButton.addActionListener(followerButtonsListener);
		toFollowNameButton.setBounds(0, 52, 85, 23);
		toFollowPanel.add(toFollowNameButton);

		toFollowLabel = new JLabel("Search:");
		toFollowLabel.setBounds(0, 0, 185, 14);
		toFollowPanel.add(toFollowLabel);
		
		toUnfollowNameButton = new JButton("Unfollow");
		toUnfollowNameButton.addActionListener(followerButtonsListener);
		toUnfollowNameButton.setBounds(90, 52, 85, 23);
		toFollowPanel.add(toUnfollowNameButton);

		followedCreatorsPanel = new JPanel();
		followedCreatorsPanel.setBounds(195, 236, 185, 86);
		rightPanel.add(followedCreatorsPanel);
		followedCreatorsPanel.setLayout(null);

		// LABEL FOLLOWING
		followedCreatorsLabel = new JLabel("Following:");
		followedCreatorsLabel.setBounds(0, 0, 185, 14);
		followedCreatorsPanel.add(followedCreatorsLabel);

		// SCROLLPANE FOLLOWING
		seeFollowedCreatorsScrollPane.setViewportView(seeFollowedCreatorsJList);
		seeFollowedCreatorsScrollPane.setBounds(0, 25, 185, 61);
		followedCreatorsPanel.add(seeFollowedCreatorsScrollPane);

		notificationsTextArea.setEditable(false);
		notificationsTextArea.setBackground(new Color(211, 211, 211));
		notificationsTextArea.setBounds(10, 347, 370, 103);
		rightPanel.add(notificationsTextArea);

		notificationsLabel = new JLabel("Notifications:");
		notificationsLabel.setBounds(10, 322, 370, 14);
		rightPanel.add(notificationsLabel);

		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Adauga utilizatorii de baza
	 */
	private void addDefaultUsers() {
		ContentCreator antonioVelcu = new ContentCreator("Antonio Velcu");
		ContentCreator dwayne = new ContentCreator("Dwayne Johnson");
		ContentCreator taylorJohnson = new ContentCreator("Taylor Swift");
		ContentCreator tomFord = new ContentCreator("Tom Ford");

		Follower maria = new Follower("Maria");
		Follower ionut = new Follower("Ionut");
		Follower sisi = new Follower("Sisi");
		Follower robert = new Follower("Robert");
		Follower costel = new Follower("Costel");
		Follower dumitru = new Follower("Dumitru");

		antonioVelcu.follow(maria);
		maria.follow(antonioVelcu);
		antonioVelcu.follow(robert);
		robert.follow(antonioVelcu);
		antonioVelcu.follow(dumitru);
		dumitru.follow(antonioVelcu);

		tomFord.follow(sisi);
		sisi.follow(tomFord);
		tomFord.follow(dumitru);
		dumitru.follow(tomFord);

		dwayne.follow(ionut);
		ionut.follow(dwayne);
		dwayne.follow(robert);
		robert.follow(dwayne);
		dwayne.follow(sisi);
		sisi.follow(dwayne);
		dwayne.follow(costel);
		costel.follow(dwayne);

		taylorJohnson.follow(robert);
		robert.follow(taylorJohnson);
		taylorJohnson.follow(dumitru);
		dumitru.follow(taylorJohnson);

		contentCreatorsDLM.addElement(antonioVelcu);
		contentCreatorsDLM.addElement(dwayne);
		contentCreatorsDLM.addElement(taylorJohnson);
		contentCreatorsDLM.addElement(tomFord);

		followersDLM.addElement(maria);
		followersDLM.addElement(ionut);
		followersDLM.addElement(sisi);
		followersDLM.addElement(robert);
		followersDLM.addElement(costel);
		followersDLM.addElement(dumitru);
	}

	/**
	 * Updateaza zona de text cu notificari in functie de selectedUser
	 * @param selectedUser - utilizatorul selectat
	 */
	private void updateNotificationsArea(Follower selectedUser) {
		int i;
		String allNotifications = new String("");
		String[] notifications = selectedUser.getNotifications();
		if (notifications.length > 0) {
			for (i = 0; i < notifications.length - 1; i++) {
				allNotifications = allNotifications + notifications[i] + "\n";
			}
			allNotifications = allNotifications + notifications[i];
		}
		notificationsTextArea.setText(allNotifications);
		
	}
	
	/**
	 * Verifica unicitatea unui nume intr-o lista tip JList
	 * @param nameList - lista cu nume
	 * @param name - numele cautat
	 * @return true - daca e unic numele in lista
	 * 		   false - daca nu e unic numele in lista
	 */
	private boolean isUniqueJList(JList<String> nameList, String name) {
		ListModel<String> model = nameList.getModel();
		
		for (int i = 0; i < model.getSize(); i++) {
			Object o = model.getElementAt(i);
			if (o.toString().equals(name))
				return false;
		}
		return true;
	}
	
	/**
	 * Verifica unicitatea unui nume intr-o lista
	 * @param nameList - lista cu nume
	 * @param name - numele cautat
	 * @return true - daca e unic numele in lista
	 * 		   false - daca nu e unic numele in lista
	 */
	private boolean isUnique(String[] nameList, String name) {
		for (String item : nameList)
			if (item.equals(name))
				return false;
		return true;
	}

	
	/**
	 * Creaza o lista cu numele utilizatorilor dintr-o lista DLM
	 * @param <T> - ContentCreator/Follower
	 * @param list - lista DLM
	 * @return - un array de String cu nume
	 */
	private <T extends Person> String[] getNameList(DefaultListModel<T> list) {
		String[] nameList = new String[list.getSize()];
		for (int i = 0; i < list.getSize(); i++)
			nameList[i] = (list.get(i)).getName();

		return nameList;
	}

	/**
	 * Returneaza indexul creatorului dupa nume, daca creatorul nu exista returneaza -1
	 * @param nameList - lista cu nume
	 * @param searchName - numele cautatului
	 * @return indexul
	 */
	private int getIndexByName(String[] nameList, String searchName) {
		int i = 0;
		for (String name : nameList) {
			if (name.equals(searchName))
				return i;
			i++;
		}
		return -1;
	}

	/**
	 * Verifica daca numele exista in lista
	 * @param nameList - lista cu nume
	 * @param name - numele de verificat
	 * @return numele daca exista sau null daca nu exista
	 */
	private String checkName(String[] nameList, String name) {
		for (String creator : nameList)
			if (creator.equals(name))
				return creator;

		return null;
	}
	
	/**
	 * Returneaza instanta unei persoane dupa nume
	 * @param <T> - ContentCreator/Follower
	 * @param instanceList - lista cu instante
	 * @param name - numele instantei cautate
	 * @return instanta cautata
	 */
	private <T extends Person> T getPersonInstance(DefaultListModel<T> instanceList,String name) {	
		for (int i = 0; i < instanceList.size(); i++) {
			if (instanceList.getElementAt(i).getName().equals(name)) {
				return instanceList.getElementAt(i);
			}
		}
		return null;
	}
}