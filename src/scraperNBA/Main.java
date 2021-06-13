package scraperNBA;

import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Main {

	public static void main(String[] args) {
		//read input as full name
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter a player name and surename here like [name surename]: ");
        String playerFullName = myObj.nextLine();
        System.out.println(playerFullName);  
        
        //scraper
        getData(playerFullName); 
       
        
        //test data
        /*
        System.out.println("Tyler Cook");  
        getData("Tyler Cook");
        
        System.out.println("Seth Curry");  
        getData("Seth Curry");
        
        System.out.println("Hamidou Diallo");  
        getData("Hamidou Diallo");
        
        System.out.println("Jae Crowder");  
        getData("Jae Crowder");
        
        System.out.println("Al-Farouq Aminu");  
        getData("Al-Farouq Aminu");
		*/
	}
	
	private static char[] stringToCharArray(String text) {
		char[] arr = new char[text.length()];
		for (int i = 0; i < text.length(); i++) {
        	arr[i] = text.charAt(i);
        }
		return arr;
	}
	
	private static String[] getNameAndSurename(char[] fullName) {
		Boolean space = false;
		String name = "", surename = "";
		String[] arr = new String[2];
		
       //getting name & surename for url
       for(int i = 0;i < fullName.length; i++) {
    		//checks for " " space between name and surename
    		if(fullName[i] == ' ') {
        		space = true;
        		i++;
            }
    		//the first part is name
    		if(!space){
    			name += fullName[i];
    		}
    		//last part is surename
    		if(space){
    			surename += fullName[i];
    		}		
        }
       
       arr[0] = name;
       arr[1] = surename;
       return arr;
	}
	
	private static String getProperURL(String name, String surename) {
		//proper url: "https://www.basketball-reference.com/players/surenameInitial/surename(max lenght 5)name(first two letters)01.html" all must be lowercase to work!!!
		String url = "";
		//check for proper lenght
		if(surename.length() < 5){
	    	   url = "https://www.basketball-reference.com/players" + "/" + surename.substring(0,1).toLowerCase() + "/" + surename.toLowerCase() + name.substring(0,2).toLowerCase() + "01.html";
	       }
	       else {
	    	   url = "https://www.basketball-reference.com/players" + "/" + surename.substring(0,1).toLowerCase() + "/" + surename.substring(0,5).toLowerCase() + name.substring(0,2).toLowerCase() + "01.html";
	       }
		
		return url;
	}
	
	private static void scraperNBA(String url) throws IOException {
		//getting html
    	Document doc = Jsoup.connect(url).get();
    	//selecting proper elements to get number of seasons 
    	Elements reg = doc.select("tbody > tr[id~=per_game.*]");
        Elements ply = doc.select("tbody > tr[id~=playoffs_per_game.*]");

        for (int i = 0; i < reg.size() - ply.size(); i++) {
        	//get season and 3-point avg data
            Element s = doc.select("tbody > tr > th[data-stat=season]").get(i);
            Element el = doc.select("td[data-stat=fg3a_per_g]").get(i);
            System.out.println(s.text() + " " + el.text());
        } 
	}
	
	private static void getData(String playerFullName) {
		//convert string to char array
        char[] fullName = stringToCharArray(playerFullName);
        
        //get seperate name = arr[0] and surename = arr[1]
        String[] arr = getNameAndSurename(fullName);
       
        //adapt url for search
        String url = getProperURL(arr[0], arr[1]);
        
        //call scraper method to print data
        try {
			scraperNBA(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
