
import java.io.*;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.core.converters.ConverterUtils.DataSource;
/**
 * Example class that converts HTML files stored in a directory structure into
 * and ARFF file using the TextDirectoryLoader converter. It then applies the
 * StringToWordVector to the data and feeds a J48 classifier with it.
 * 
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class wekaClassifiers {

	/**
	 * Expects the first parameter to point to the directory with the text
	 * files. In that directory, each sub-directory represents a class and the
	 * text files in these sub-directories will be labeled as such.
	 * 
	 * @param args
	 *            the commandline arguments
	 * @throws Exception
	 *             if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		
		//----------------------------------
		/*
		 * Loading arff text files
		 */
		//---------------------------------

		ArffLoader loader = new ArffLoader();
		loader.setFile(new File("train.arff"));
		Instances dataTrain = loader.getDataSet();
		dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
	
		
		// System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
		// train Naive Bayes and output model
		//NaiveBayes c = new NaiveBayes();
		//J48 c = new J48();
		//MultilayerPerceptron c = new MultilayerPerceptron();
		SMO c = new SMO();		
		c.buildClassifier(dataTrain);
		
		
		// get the testing file
		ArffLoader loaderTest = new ArffLoader();
		loaderTest.setFile(new File("test.arff"));
		Instances dataTest = loaderTest.getDataSet();
		dataTest.setClassIndex(dataTrain.numAttributes() - 1);
		
		//System.out.println("\n\nImported data:\n\n" + dataTest);
		
		//dataTest.setClassIndex(0);
		
		
		//System.out.println("Attribute 0 : "  + dataFilteredTest.attribute(0));				
		
       Evaluation eTest = new Evaluation(dataTrain);
        eTest.evaluateModel(c, dataTest);
		
        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);
        System.out.println(eTest.toClassDetailsString());
        System.out.println(eTest.correct()/eTest.numInstances());
        
        //System.out.println("confusion matrix: " + eTest.confusionMatrix()[1][0] + eTest.confusionMatrix()[1][1] );
		//System.out.println("\n\n SMO Classifier model:\n\n" + c);
        
		
		/*for (int i=0; i<dataTest.numInstances(); i++){
			double d = c.classifyInstance(dataTest.instance(i));
			//System.out.print("ID: " + dataTest.instance(i).value(0));
			System.out.print(", actual: " + dataTest.classAttribute().value((int) dataTest.instance(i).classValue()));
			System.out.println(", predicted: " + dataTrain.classAttribute().value((int) d));
			 		
		}*/
	}
	
	public static double classify(String trainFile, String testFile) throws Exception{
		
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(trainFile));
		Instances dataTrain = loader.getDataSet();
		dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
	
		
		// System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
		// train Naive Bayes and output model
		//NaiveBayes c = new NaiveBayes();
		//J48 c = new J48();
		//MultilayerPerceptron c = new MultilayerPerceptron();
		SMO c = new SMO();		
		c.buildClassifier(dataTrain);
		
		
		// get the testing file
		ArffLoader loaderTest = new ArffLoader();
		loaderTest.setFile(new File(testFile));
		Instances dataTest = loaderTest.getDataSet();
		dataTest.setClassIndex(dataTrain.numAttributes() - 1);
		
		//System.out.println("\n\nImported data:\n\n" + dataTest);
		
		//dataTest.setClassIndex(0);
		
		
		//System.out.println("Attribute 0 : "  + dataFilteredTest.attribute(0));				
		
       Evaluation eTest = new Evaluation(dataTrain);
        eTest.evaluateModel(c, dataTest);
		
        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);
        System.out.println(eTest.toClassDetailsString());
        
        //System.out.println("confusion matrix: " + eTest.confusionMatrix()[0][0] + " " +  eTest.confusionMatrix()[0][1] + "\n" + eTest.confusionMatrix()[1][0] + " " +  eTest.confusionMatrix()[1][1] );
        System.out.println (eTest.weightedPrecision());
        return (eTest.weightedPrecision()) ;
        
        //System.out.println("confusion matrix: " + eTest.confusionMatrix()[1][0] + eTest.confusionMatrix()[1][1] );
		//System.out.println("\n\n SMO Classifier model:\n\n" + c);
        
		
		/*for (int i=0; i<dataTest.numInstances(); i++){
			double d = c.classifyInstance(dataTest.instance(i));
			//System.out.print("ID: " + dataTest.instance(i).value(0));
			System.out.print(", actual: " + dataTest.classAttribute().value((int) dataTest.instance(i).classValue()));
			System.out.println(", predicted: " + dataTrain.classAttribute().value((int) d));
			 		
		}*/
		
	}
	
	
}