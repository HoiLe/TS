
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.*;
import twitter4j.conf.*;

public class Twitter4J {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setOAuthConsumerKey("MORhb7uR2pzyiD0Bf1X8OIHhn");
	    cb.setOAuthConsumerSecret("c8xsMCM9LItdW1I9VfynactRlNACKImVMngutb4xL0yFmCMp2y");
	    cb.setOAuthAccessToken("222342647-NRrcdUpYVqqG0W7XYVivg5wZlFgevohM3fj4yrIk");
	    cb.setOAuthAccessTokenSecret("oIzkMIviuN8jwZXco4xbr2H0ju0IL6FyeM0CEzQXxCEZr");

	    Twitter twitter = new TwitterFactory(cb.build()).getInstance();

	    
	    BufferedReader userList = new BufferedReader(new FileReader("userList.txt"));
		String user;
		
		System.out.println("new!");
		
		while ((user = userList.readLine()) != null) {
						
		 
			FileOutputStream out = new FileOutputStream("Tweets//" + user);
			
			int pageno = 1;	        
		    
		   while (pageno<=10) {
	
		      try {
	

		    	Paging page = new Paging(pageno, 1000);
		    	pageno++;
		        
		    	System.out.println(user);
		    	
		        List<Status> statusList = twitter.getUserTimeline(user, page);
		        
		        for(int i= 0; i<statusList.size(); i++){
		        	
		        	Status aStatus = statusList.get(i);
		        	if(!aStatus.isRetweet() && (aStatus.getInReplyToScreenName() == null) && (aStatus.getInReplyToStatusId()<0) && (aStatus.getInReplyToUserId()<0))
		        		out.write((aStatus.getText() + "\n\n").getBytes());
		        
		        }
		       

		        //if (statusList.size() == 0)
		          //break;
		      }
		      catch(TwitterException e) {
	
		        e.printStackTrace();
		      }
		   }
		      
		   out.close();
		  
		}
		

	}
	
	public static String hashtagRegex = "^#\\w+|\\s#\\w+";
	public static Pattern hashtagPattern = Pattern.compile(hashtagRegex);

	public static String urlRegex = "http+://[\\S]+|https+://[\\S]+";
	public static Pattern urlPattern = Pattern.compile(urlRegex);

	public static String mentionRegex = "^@\\w+|\\s@\\w+";
	public static Pattern mentionPattern = Pattern.compile(mentionRegex);

	
	public static String[] getHashtag(String text) {
	   String hashtags[] = null;
	   Matcher matcher = hashtagPattern.matcher(text);

	    if ( matcher.find() ) {
	        hashtags = new String[matcher.groupCount()];
	        for ( int i = 0; matcher.find(); i++ ) {
	                    //Also i'm getting an ArrayIndexOutOfBoundsException
	            hashtags[i] = matcher.group().replace(" ", "").replace("#", "");
	    	   // System.out.println("mentioned: " + hashtags[i]);

	        }
	    }

	    
	   return hashtags;

	}
	
	
	//get urls
	public static String[] getUrl(String text) {
		   String urls[] = null;
		   Matcher matcher = urlPattern.matcher(text);
		   
		 
		    if ( matcher.find() ) {
		        urls = new String[10];
		        
		      
		        for ( int i = 0; matcher.find(); i++ ) {
		                    //Also i'm getting an ArrayIndexOutOfBoundsException
		        	
		        	System.out.println("i: " + i);
		        	System.out.println("group: " + matcher.group());

		        	
		           // urls[i] = matcher.group();
		    	   // System.out.println("mentioned: " + hashtags[i]);

		        }
		    }

		    
		   return urls;

	}
	
	//get no_urls
	public static int getNoUrl(String text) {
		   Matcher matcher = urlPattern.matcher(text);
		   
		   int count = 0;
		 
		    if ( matcher.find() ) {
		        
		      
		        for ( int i = 0; matcher.find(); i++ ) {
		                    //Also i'm getting an ArrayIndexOutOfBoundsException
		        	
		        	System.out.println("i: " + i);
		        	System.out.println("url: " + matcher.group());
		        	count++;

		        	
		
		        }
		    }

		    
		   return count;

	}
	
	
	public static String[] getMention(String text) {
		   String mentions[] = null;
		   Matcher matcher = mentionPattern.matcher(text);

		    if ( matcher.find() ) {
		        mentions = new String[matcher.groupCount()];
		        for ( int i = 0; matcher.find(); i++ ) {
		                    //Also i'm getting an ArrayIndexOutOfBoundsException
		            mentions[i] = matcher.group();
		    	   // System.out.println("mentioned: " + hashtags[i]);

		        }
		    }

		    
		   return mentions;

	}
		




}
