package proiect;

import java.util.ArrayList;
import java.util.List;

//The Subject interface
interface Subject {
	void follow(Follower observer);
	void unfollow(Follower observer);
	void notifyObservers();
}

public class ContentCreator implements Subject,Person {
    private final String name;					//numele canalului
    private final List<Follower> followers;		//lista tuturor abonatilor
    private String lastContent;					//numele continutului cel mai nou(videoclip etc.)
    
    
    public ContentCreator(String name) {
        this.name = name;
        followers = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    //posteaza continut nou
    public void setContent(String content) {	
        this.lastContent = content;
        notifyObservers();   
    }
    
    //obtine cel mai nou continut
    public String getContent() {
        return lastContent;
    }
    
    //adauga la lista de abonati a creatorului
    public void follow(Follower subscriber) {
        followers.add(subscriber);
    }
    
    //elimina din lista de abonati a creatorului
    public void unfollow(Follower subscriber) {
        followers.remove(subscriber);
    }
    
    //trimite notificari abonatilor
    @Override
    public void notifyObservers() {
        for (Follower subscriber : followers) {
            subscriber.update(lastContent);
        }
    }
    
    //lista abonati
    public String [] getFollowers() {
    	String[] names = new String[followers.size()];
	     for (int i = 0; i < followers.size(); i++) {
	         names[i] = (followers.get(i)).getName();
	     }
	     return names;
    }
}