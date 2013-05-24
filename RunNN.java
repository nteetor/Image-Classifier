import java.io.File;
import java.io.FileFilter;

public class RunNN {
	/**
	 * Main method
	 * 
	 * @param data
	 */
	public static void main(String[] data) {
	    if (data.length < 4){
		System.out.println("usage: java RunNN [set1] [set2] [test1] [test2] iterations");
		System.exit(1);
	    }
	    
	    int iterations = 10000;
	    if (data.length == 5)
		iterations = Integer.parseInt(data[4]);
	    
	    // It's assumed all training sets will end in "Train"
	    String[] types = new String[2];
	    types[0] = data[0].substring(0,data[0].length()-5);
	    types[1] = data[1].substring(0,data[1].length()-5);
	    
	    //System.out.println(types[0]+" and  "+types[1]);
	    //	    System.exit(0);
		    
		int sections = 16;
		//0.05 learn rate is VERY good
		double[][][] theData = getData(data, sections);
		//used to be 5000
		NeuralNetwork c = new NeuralNetwork(theData, sections, 0.03, iterations, types);

		//NeuralNetwork c2 = new NeuralNetwork(theData, sections, 0.1,5000);
		//NeuralNetwork c3 = new NeuralNetwork(theData, sections, 0.08,5000);
		
		//System.out.println("TEST DATA ");
		//c.testData(getData(data,sections));
		//new String[]{"Tred", "Tgreen"}
		c.testData(getData(new String[]{data[2], data[3]},sections));
		//System.out.println();
		System.exit(0);
	}

	/**
	 * @param data
	 *            The folder names passed in from command line
	 * @param sections
	 *            How detailed the image should be
	 * @param dataLength
	 *            The length of the data
	 * @return The data set, a 3d array. First dimension: the index of the
	 *         image. Second dimension: the pixel color array to be accessed. 0
	 *         = red, 1 = green, 2 = blue 3 = class of p Third dimension: the section of the
	 *         image to be accessed.
	 */
	public static double[][][] getData(String[] data, int sections) {
		File readClass1 = new File(data[0]);
		File readClass2 = new File(data[1]);

		// Filters out hidden files
		FileFilter hiddenFilter = new FileFilter() {
			public boolean accept(File file) {
				return !file.isHidden();
			}
		};

		File[] Class1 = readClass1.listFiles(hiddenFilter);
		File[] Class2 = readClass2.listFiles(hiddenFilter);
		// f(class1) = 0; f(class2) = 1

		// use of double simply to make perceptron set up easier
		int totalSize = Class1.length + Class2.length;
		double[][][] DataSet = new double[totalSize][5][sections];
		int class1 = 0, class2 = 0;
		
		for (int i = 0; i < Class1.length; i++) {
			class1++;
			DataSet[i] = ImageParsing.pixelColor1(
					(readClass1.getName() + "/" + Class1[i].getName()),
					sections);
			DataSet[i][3] = new double[]{0};
		}
		//System.out.println("Loaded: " + class1 + " images from class 1.");
		for (int i = Class1.length; i < totalSize; i++) {
			class2++;
			DataSet[i] = ImageParsing.pixelColor1(
					(readClass2.getName() + "/" + Class2[i - Class1.length].getName()),
					sections);
			DataSet[i][3] = new double[]{1};
		}
		//System.out.println("Loaded: " + class2 + " images from class 2.");
		
		return DataSet;
	}
}
