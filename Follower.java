package proiect;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

interface Person{
	public String getName();
}

//The Observer interface
interface Observer {
	void update(String contentName);
}

//The Concrete Observer class, representing the follower
public class Follower implements Observer,Person {
	public String name;
	private final List<String> followerNotifications;
	private final List<ContentCreator> followedCreators;		//lista de creatori la care s-a abonat
 
	 public Follower(String name) {
		 this.name = name;
	     followerNotifications = new ArrayList<>();
	     followedCreators = new ArrayList<>();
	 }
 
	 public String getName() {
		 return name;
	 }
	 
	 //Obtine numele tuturor creatorilor abonati
	 public String[] getFollowedCreators() {
	     String[] names = new String[followedCreators.size()];
	     for (int i = 0; i < followedCreators.size(); i++) {
	         names[i] = followedCreators.get(i).getName();
	     }
	     return names;
	 }
 
	 public void follow(ContentCreator contentCreator) {
	     followedCreators.add(contentCreator);
	 }
	 
	 public void unfollow(ContentCreator contentCreator) {
	     followedCreators.remove(contentCreator);
	 }
	 
	 @Override
	 public void update(String contentName) {
		// data curenta (doar ora/minut/secunda)
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime localTime = LocalTime.now();
			
		followerNotifications.add("["+dtf.format(localTime)+"]: " + contentName + ".");
	 }
	 public String[] getNotifications(){
		 String[] notifications = new String[followerNotifications.size()];
	     for (int i = 0; i < followerNotifications.size(); i++) {
	    	 notifications[i] = followerNotifications.get(i);
	     }
	     return notifications;
	 }
	 public void printNotifications() {
		 String[] notifications = getNotifications();
		 for (String notification : notifications) {
	    	 System.out.println(notification);
	     }
		 
	 }
}