import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Random;

import javax.management.timer.Timer;

import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.tokenizers.*;
import weka.core.DistanceFunction;


public class KMeans {
	
	  private static final String USAGE = String.format("This program performs K-Means clustering on the given dataset.\n\n"
	          + "Required options:\n"
	          + "(-c) [int]         Number of clusters to find.\n"
	          + "(-i) [string]      Input dataset to perform clustering on."
	          + "-m) [int]          Maximum number of iterations before K-Means\n"
	          + "                   terminates.  Default value 1000.\n"
	          + "(-s) [int]         Random seed. ");
	 
	  private static int NumOfClusters =2;
	  private static int NumOfElements = 12;
	  private static int MaxIterations;
	  private static int Seed;
	  public static Hashtable[] data = new Hashtable[NumOfElements];
	  public static int[] assignments = new int[NumOfElements];
	  private static Hashtable[] Centroids = new Hashtable[NumOfClusters];
		  
	  public static void main(String[] args) throws NumberFormatException, IOException {
		
		  Clustering(".//test", "2", "3", "10");
	  }
		
	  public static void Clustering(String inputFile, String clusters, String seed, String maxIteration) {
	  //public static void main(String[] args) throws NumberFormatException, IOException {
			
	      
	    Timer timer = new Timer();
	    try {
	      // Get the data set path.
	      
	      if (inputFile.length() == 0)
	        throw new IllegalArgumentException();
	     
	      // Load input dataset.
	      
	      // Create the KMeans object.
	      // Gather parameters and validation of options.
	      int m = 1000;
	      if (maxIteration.length() != 0)
	      {
	        m = Integer.parseInt(maxIteration);
	        if (m < 0)
	        {
	          System.out.println("[Fatal] Invalid value for maximum iterations(" +
	              maxIteration + ")! Must be greater than or equal to 0..");
	          System.exit(-1);         
	        }
	        else if(m == 0)
	        {
	          m = Integer.MAX_VALUE;
	        }
	      }
	     
	   
	      MaxIterations = m;
		     
	      if (clusters.length() == 0)
	      {
	        throw new IllegalArgumentException();
	      }
	      else
	      {
	        int c = Integer.parseInt(clusters);
	        System.out.println("Number of cluster: " + c);
	        if (c < 1)
	        {
	          System.out.println("[Fatal] Invalid number of clusters requested (" +
	              clusters + ")! Must be greater than or equal to 1.");
	          System.exit(-1);
	        }
	       
	        NumOfClusters = c;
	      }     
	     
	      if (seed.length() != 0)
	        Seed = Integer.parseInt(seed);
	           
	      
	      // Perform K-Means clustering.
	      //timer.StartTimer("total_time");
	      timer.start();
	      
	      //initialize variables
	      
	      for(int i=0; i<NumOfElements; i++)
	    	  data[i] = new Hashtable();
	      
	      
	      
	      
	      //loading data
	      File directory = new File(".//test");
		  String[] list = directory.list();
		  for (int i=0; i<list.length ; i++){
				
			  if(list[i].indexOf(".DS_Store") ==-1) {
				    FileReader doc = new FileReader(".//test//"+ list[i]);	
				    BufferedReader b_doc = new BufferedReader(doc);
				    String line = "";
				    while((line = b_doc.readLine()) !=null){
				    	String[] words = line.split("###");
				    	if(words.length >= 2)
				    		data[i].put(words[0], words[1]);
				    	
				    }
				    System.out.println("Size of file " + i + " (" + list[i] + ") is: " + data[i].size());
			  }
			   
		  }
			
		  System.out.println("Finish loading data..........");
	      
	      for (int i=0; i<NumOfClusters; i++)
	      {
	    	  Centroids[i] = new Hashtable();
	      }
	    
	     
	      
	      //initialize clusters
	      initClusters();
	      
	      for (int j=0; j<NumOfClusters; j++)
	    	  System.out.println("Centroids " + j   + ": "+ Centroids[j]);
	    
	      System.out.println("Finish init cluster.........");
		     
	      for (int i=0; i<MaxIterations; i++){
	    	  
	    	  System.out.println("Clustering time " + (i+1) + ".........");
	  		
	    	  buildClusterer();
	    	  updateCentroids();     	  //update centroids
	    	  for (int j=0; j<NumOfClusters; j++)
		    	  System.out.println("Centroids " + j + " : " + Centroids[j]);
		    
	    		
	      }
	      
	      
	      for (int i=0; i<NumOfClusters; i++)
	    	  System.out.println("Centroids: " + Centroids[i]);
	      
	      //getAssignments
	      
	      for (int i=0; i<list.length; i++)
	    	  System.out.println(list[i] + " is assigned to cluster: " +  assignments[i]);
	      
	      //timer.StopTimer("total_time");
	      timer.stop();
	      
	      //timer.PrintTimer("total_time");
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  
	  
	  
	  private static void initClusters(){
		  
		  Random Rand = new Random();
		  for (int i=0; i<NumOfClusters; i++){
			
			  int randomNum = Rand.nextInt(NumOfElements);
			  if(!data[randomNum].isEmpty())
				 	 Centroids[i] = data[randomNum];
				 	  
			
		  }
		  
		  
		  
		 /* for (int i=0; i<data.length; i++){
			  //calculate distance of the current instance and each centroid
			  
			  int cluster = 0;
			  int Distance = 0;
			  for(int j=0; j<Centroids.length; j++)
			  {
				  //double d = 0.0;
				  int d = (int) HammingDistance(data[i], Centroids[j] );
				  System.out.println("Hamming distance from element " + i + ", to cluster " + j + ": "  + d);
				  if(d > Distance ){
					  Distance = d;
					  cluster = j;
				  }
				  
			  }
			  
			  //assign cluster number to i
			assignments[i] = cluster;  
			System.out.println("assign element " + i + " to cluster: " + cluster);
			 
		  }*/
		  
	  }
	  
	  
	  private static void buildClusterer() throws NumberFormatException, IOException{
		  
		  //assign each instances to centroids
		  
		  for (int i=0; i<data.length; i++){
			  //calculate distance of the current instance and each centroid
			  
			  int cluster = -1;
			  double closestDistance = 10000000.0;
			  for(int j=0; j<Centroids.length; j++)
			  {
				  //double d = 0.0;
				  double d = JSDDistance(data[i], Centroids[j] );
				  System.out.println("element " + i + ", cluster " + j + ": "  + d);
				  if(d < closestDistance ){
					  closestDistance = d;
					  cluster = j;
				  }
				  
			  }
			  
			  
			  //assign the current item (document) to its cluster
			  System.out.println("assign element " + i + " to cluster: " + cluster);
			  
			  assignments[i] = cluster;
			  
			  
		  }
		  
		  
		  
		  
		  
	  }
	  
   //In case we use Hamming Distance
   private static void buildClusterer2() throws NumberFormatException, IOException{
		  
		  //assign each instances to centroids
	   	int[] new_assignments = new int[NumOfElements];
		
		  for (int i=0; i<data.length; i++){
			  //calculate distance of the current instance and each centroid
			  
			  int otherCluster = 0;
			  if(assignments[i] == 0)
				  otherCluster = 1;
		
			  int hammingDistToCurrCluster = 0;
			  int hammingDistToOtherCluster = 0;
			
			  for(int j=0; j<data.length; j++)
				  
			  {
				  if(j!=i){
					  if(assignments[j]== otherCluster)
						  hammingDistToOtherCluster += HammingDistance(data[i], data[j]);
					  else
						  hammingDistToCurrCluster += HammingDistance(data[i], data[j]);
					  }
				  
			  }
			  
			  int newcluster =0;
			  
			  if(hammingDistToOtherCluster>hammingDistToCurrCluster)
				  newcluster = otherCluster;
			  else
				  newcluster = assignments[i];
			  
			  //assign the current item (document) to its cluster
			  System.out.println("distance from " + i + " to other cluster: " + otherCluster + ": "+ hammingDistToOtherCluster);
				
			  System.out.println("distance from " + i + " to its curr cluster " + assignments[i] + ": "+ hammingDistToCurrCluster);
				
			  System.out.println("assign element " + i + " to cluster: " + newcluster);
			  new_assignments[i] = newcluster;
				
			  
		  }
		  
		  //update new clusters
		  for (int i=0; i<data.length; i++){
			  assignments[i] = new_assignments[i];	
		  }
		
		  
		  
	  }
	 
	  
	  private static void updateCentroids() {
		  
		  for(int i=0; i<Centroids.length; i++)
		  {
			  Hashtable tmp = new Hashtable();
			  
			  int clusterElements = 0;
			  for (int j=0; j<data.length ; j++ ){
				  if(assignments[j] == i)
				  {
					  clusterElements ++;
					  Object[] datakeys = data[j].keySet().toArray();
					  for (int k = 0; k< datakeys.length; k++)
						  if(!tmp.containsKey(datakeys[k]))
							  tmp.put(datakeys[k], data[j].get(datakeys[k]));
						  else
							  tmp.put(datakeys[k], Double.parseDouble(data[j].get(datakeys[k]).toString()) + Double.parseDouble(tmp.get(datakeys[k]).toString()));
					  
					 		  	
				  }
			  }
			  
			  //normalize new centroid vector
				
			  Object[] tmpKeys = tmp.keySet().toArray();
			  Object[] tmpValues = tmp.values().toArray();
	
			  for (int j = 0; j<tmp.size(); j++)
				  tmp.put(tmpKeys[j], Double.parseDouble(tmpValues[j].toString())/clusterElements);
			  
			  System.out.println("numOfElements in cluster " + i + " : " + clusterElements);
			  //Centroids[i].clear();
			  Centroids[i] = tmp;
			    
			  
			  
		  }
		  
		  
	  }
	
	
	  
	  private static double HammingDistance(Hashtable h1, Hashtable h2){
		  
		  Object[] keys1 =  h1.keySet().toArray();
		  
		  double dist = 0.0;
		  
		  for (int i=0; i< keys1.length; i++){
			  
			  if(h2.containsKey(keys1[i]))
				  dist++;
		  }
		  
		  return dist;
		  
	  }
	  

	  
	  private static double cosineDistance(Hashtable h1, Hashtable h2){
		  
		  Object[] keys1 =  h1.keySet().toArray();
		  
		  double dist = 0.0;
		  
		  for (int i=0; i< keys1.length; i++){
			  
			  if(h2.containsKey(keys1[i]))
				  dist+= Double.parseDouble(h2.get(keys1[i]).toString()) * Double.parseDouble(h1.get(keys1[i]).toString());
			 
		  }
		  
		  return dist;
		  
	  }
	  
	  
	  
	  
	  
	  private static double JSDDistance(Hashtable h1, Hashtable h2) throws NumberFormatException, IOException{
		  
			
			Hashtable all_dists = new Hashtable ();
			
			Object[] keys = h1.keySet().toArray();
			Double dist = 0.0;
			for (int i=0; i<keys.length; i++){
				double p = 1.0*Double.parseDouble(h1.get(keys[i]).toString());
				
				double q = 0.0;
				if(h2.containsKey(keys[i])){
					q = 1.0*Double.parseDouble(h2.get(keys[i]).toString());
					
				}
				else //use laplace smoothing to avoid division to zero
				{
					//p += 0.0001;
					q = 0.0001; //????
				}
				
				
				dist += 0.5* p* (Math.log10(p/q)/Math.log10(2));		
				
				
			}
			
			//second q(x)logq(x)/p(x)
			Object[] qkeys = h2.keySet().toArray();
			
			for (int i=0; i<qkeys.length; i++){
				double q = 1.0*Double.parseDouble(h2.get(qkeys[i]).toString());
				double p = 0.0;
				if(h1.containsKey(qkeys[i])){
					p = 1.0*Double.parseDouble(h1.get(qkeys[i]).toString());
					
				}
				else //use laplace smoothing to avoid division to zero
				{
					p = 0.0001;
					//p = 0.0001; //????
					
				}
				dist += 0.5* q* (Math.log10(q/p)/Math.log10(2));		
				
			}
		
			return dist;

	  }
	  
	  
	  
	
	}