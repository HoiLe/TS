import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;


public class myClassifier {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//compare1();
		classifyAllAuthors(5);
		//classify2Authors(5);
	}
	
	
	public static void compare1() throws IOException{
		
		
		double recall = 0.0; double precision = 0.0; int total = 0;
		
		
		File directory1 = new File("Authors_4grams");
		String[] list1 = directory1.list();
		
		for(int j=0; j<list1.length; j++){
			
			int pos = 0; int neg = 0; int neutral = 0; 
			
			BufferedReader b_in = new BufferedReader(new FileReader("Authors_4grams//" + list1[j]));
			
			if(list1[j].indexOf(".DS_Store") == -1){
				
			String[] users = list1[j].split(".txt_");
			String user1 = users[0];
			//String user2 = users[1].substring(users[1].indexOf("_")+1);
			String user2 = users[1];
			
			Hashtable user1_features = new Hashtable();
			Hashtable user2_features = new Hashtable();
			
			String line;
			
			while((line = b_in.readLine() ) != null){
				user1_features.put(line, 0);
			}
			
			BufferedReader b_in2 = new BufferedReader(new FileReader("Authors_4grams//" + user2 + "_" + user1 + ".txt"));
			while((line = b_in2.readLine() ) != null){
				user2_features.put(line, 0);
			}
			
			File directory = new File("Tweets_4grams");
			String[] list = directory.list();
			
			for(int i=0; i<list.length; i++){
				String user = list[i].substring(0, list[i].indexOf(".txt_"));
				
				if(list[i].indexOf(".DS_Store") == -1 && user.equals(user1)){
					
					
					int score1 = 0;
					int score2 = 0;
					
					Hashtable user1_user = new Hashtable();
					Hashtable user2_user = new Hashtable();
					
					BufferedReader b_ = new BufferedReader(new FileReader("Tweets_4grams//" + list[i]));
					
					while((line = b_.readLine() ) != null){
						if(user1_features.containsKey(line)){
							score1++;
							user1_user.put(line, 0);
						}
							
						if(user2_features.containsKey(line)){
							score2++;
							user2_user.put(line, 0);
						}
							
					}
					
					
					System.out.println(list[i] + " " +  user1 + ":" + score1 + "(" + user1_user + ") " + user2 + ":" + score2 + "(" + user2_user + ")");
					
					if(score1>score2)
						pos++;
					
					else if(score1<score2)
						neg++;

					else 
						neutral++;
				
				
					
				}
				
				
			}
			
			System.out.println("pos: " + pos + " neg: " + neg + " neutral: " + neutral);
			
			if(pos+neg>0 ){
				recall += (pos+neg)*1.0/(pos+ neg + neutral);
				precision += pos*1.0/(pos+neg);
				total++;
			}
			
		}
		}

		
		System.out.println("recall: " + recall/total + " precision: " + precision/total );

	}
	

	//otherwise the method is used to compare between 2 authors by default
	public static void classifyAllAuthors(int count) throws IOException {
			
			
			File directory = new File("Authors_4grams");
			String[] list = directory.list();
			
		
			File directory1 = new File("Tweets_4grams");
			String[] list1 = directory1.list();
			
			
			double precision_avg = 0.0; double recall_average = 0; int total_cases = 0;
			
				
			for (int i=0; i<list.length; i++){
				if(!list[i].equals(".DS_Store") && i<(list.length*count)/5){
					
					
					String user = list[i].substring(0,list[i].indexOf(".txt"));
					
					BufferedReader buf = new BufferedReader(new FileReader("Authors_4grams//" + list[i]));
				
					
					String line = "";
					Hashtable map = new Hashtable();
					
					while((line = buf.readLine()) != null){
						map.put(line, 0);
						
					}
					
					buf.close();
									
					
					for (int j=0; j<list1.length; j++){
						int score1 = 0; int score2= 0;
						
						if(!list1[j].equals(".DS_Store")){
							if(list1[j].startsWith(user + "_"))
							{
								Hashtable map2 = new Hashtable();
								
								BufferedReader buf2 = new BufferedReader(new FileReader("Tweets_4grams//" + list1[j]));
								
								while((line = buf2.readLine()) != null){
									
									map2.put(line, 0);
									
									if(map.containsKey(line)){
										score1++;
										//System.out.println(list1[j] + ": " + line);
										
									}
									
								}
								
								buf2.close();
							
								boolean isNeg = false;
								boolean isNeutral = false;
								
								int pos = 0; int neg = 0; int total = 0;
								
								//other authors
								for (int k=0; k<list.length; k++){									
									if(!list[k].equals(".DS_Store") && list[k].indexOf(user +".txt")==-1){

										isNeg = false;
										isNeutral = false;
										
										score2=0;
		
										Hashtable map3 = new Hashtable();
										BufferedReader buf3 = new BufferedReader(new FileReader("Authors_4grams//" + list[k]));

										while((line = buf3.readLine()) != null){
											map3.put(line, 0);
										
										}

										buf3.close();
										
										Object[] t_keys = map2.keySet().toArray();
										
										for(int s=0; s<t_keys.length; s++)
											if(map3.containsKey(t_keys[s])){
												score2++;
												//System.out.println(list[k] + ": " + t_keys[s]);
											}
										
										
										
										if(score1 <score2){
											isNeg = true;				
											
										}
										else if(score1 ==score2){
											isNeutral = true;
										}
										
										//System.out.println(list1[j] + ":  " +  score1 + ":" + score2);
										
										if(!isNeg && !isNeutral)
											pos++;
										
										if(isNeg)
											neg++;
										
										total++;
									
									}
								} //end other authors
									
							
								System.out.println(list1[j] + " precision: " + (pos*1.0)/(pos+neg) + " recall:" + (pos+neg)*1.0/total + "\n");		
								
								if(pos+neg >0)
									precision_avg += (pos*1.0)/(pos+neg); 
									//System.out.println( precision_avg/total_cases );		
								
									
								if(total!=0){
										
									recall_average += (pos+neg)*1.0/total;
								
									total_cases++;
								}
								
								
							}
						
							
						}//END-INNER-IF
						
						
					}//END-ONEUSER
					
					
					
				
			}//END-IF
			}//END-FOR
				
			System.out.println("total cases: " +  total_cases + " precision: " + precision_avg/total_cases + " recall:" + recall_average/total_cases);		
		
			

	}//METHOD-END
		
		
		
public static void classify2Authors(int count) throws IOException {
		
		
		File directory = new File("Authors_4grams");
		String[] list = directory.list();
		
		File directory1 = new File("Tweets_4grams");
		String[] list1 = directory1.list();
		
		
		double precision_avg = 0.0; double recall_average = 0;; int total_cases = 0;
		
		
		//FileOutputStream outFile = new FileOutputStream(new File("3grams.txt"));
		
		
		//statistic per author
		for (int i=0; i<list.length; i++){
			if((list[i].indexOf(".DS_Store")==-1) && i<(list.length*count)/5){
				//System.out.println(list1[i]);
				
				int pos = 0; int neg = 0;; int total = 0;
				
				System.out.println(list[i]);
				String user = list[i].substring(0,list[i].indexOf(".txt_"));
				
				BufferedReader buf = new BufferedReader(new FileReader("Authors_4grams//" + list[i]));
				
				String line = "";
				Hashtable map = new Hashtable();
				
				while((line = buf.readLine()) != null){
					//String[] s = line.split(" ");
					map.put(line,0);
					
				}
				
				String user2 = list[i].substring(list[i].indexOf("_") + 1);
				
				
				BufferedReader buf2 = new BufferedReader(new FileReader("Authors_4grams//" + user2 + "_" +user+ ".txt"));
				
				
				Hashtable map2 = new Hashtable();
				while((line = buf2.readLine()) != null){
					
					map2.put(line, 0);
					
				}
				
				
				
				buf.close();
				buf2.close();
				
				for (int j=0; j<list1.length; j++){
					if(!list1[j].equals(".DS_Store")){
						//list[j] = list[j].toLowerCase();
						if(list1[j].startsWith(user + "_"))
						{
							int score1 = 0; int score2= 0; 
							
							BufferedReader buf3 = new BufferedReader(new FileReader("Tweets_4grams//" + list1[j]));
							
							
							while((line = buf3.readLine()) != null){
								//String[] s = line.split(" ");
								if(map.containsKey(line))
									score1++;
								
								if(map2.containsKey(line))
									score2++;
								
							}
							
							buf3.close();
							
							if(score1>score2)
								pos++;
							else if(score1 <score2)
								neg++;
							
							total++;
							
							System.out.println(list1[j] + " vs. " + user2 + ":" + score1 + ":" + score2);
					
						}
						
						
					}//END-IF
				}//END-FOR (End tweet set of an author)
				System.out.println(list[i] + " total: " +  total + " pos: " + pos + " neg:" + neg);		
				System.out.println(list[i] + " precision: " + (pos*1.0)/(pos+neg) + " recall:" + (pos+neg)*1.0/total + "\n");		
				
				
				if(pos+neg >0)
					precision_avg += (pos*1.0)/(pos+neg); 
					//System.out.println( precision_avg/total_cases );		
				
					
				if(total!=0){
						
					recall_average += (pos+neg)*1.0/total;
				
					total_cases++;
				}
				
				
				
				
			}//END-OUTER-IF
		}//END-OUTER-FOR
		
		System.out.println(" total case: " +  total_cases + " precision: " + precision_avg/total_cases + " recall:" + recall_average/total_cases);		
		
		
		
	}//END-METHOD
	

	
	public static void fpr() throws IOException{
		BufferedReader b_in = new BufferedReader(new FileReader(".//karpathy_yann_mix_4grams.txt"));
		Hashtable user_features = new Hashtable();
		
		String line;
		
		while((line = b_in.readLine() ) != null){
			user_features.put(line.toLowerCase(), 0);
		}
		
		File directory = new File("test_4grams");
		String[] list = directory.list();
		for(int i=0; i<list.length; i++){
			if(list[i].indexOf(".DS_Store") == -1){
				int score = 0;
				
				BufferedReader b_ = new BufferedReader(new FileReader(".//test_4grams//" + list[i]));
				
				while((line = b_.readLine() ) != null){
					if(user_features.containsKey(line.toLowerCase())){
						score++;
						//System.out.println(line.toLowerCase());
						
					}
				}
				
				System.out.println(list[i] + ": " + score);
				
			}
			
			
		}
		
		
		//cut-off
		


	}
	

}
