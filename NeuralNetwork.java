/**
 * Constructs a neural network, using a learn rate specified by the
 * constructor. It is given data in the form of a 3-dimensional array.
 * The first array corresponds to the data item, the second to the pixel
 * color array of the data item
 */

import java.util.*;

public class NeuralNetwork {
	Perceptron finalNode;
	Perceptron[] hNodesL1;
	Perceptron[] hNodesL2;
	double[][][] DataSet;
	int h12Inputs;
	int MAX;
    String set1_type;
    String set2_type;
    int iterations;

	/**
	 * Initializes the NN
	 * 
	 * @param data
	 *            : The data array given to the NN
	 */
    public NeuralNetwork(double[][][] data, int sections, double learnRate, int MAX, String[] types) {
	// set the types
	set1_type = types[0];
	set2_type = types[1];
	iterations = MAX;

		// Get data set
		DataSet = data;
		this.MAX = MAX;

		h12Inputs = (int) Math.sqrt(sections);

		// Initialize the hidden nodes, layer 1. Each gets sqrt(sections) input
		hNodesL1 = new Perceptron[h12Inputs * 3];
		for (int i = 0; i < h12Inputs * 3; i++) {
			hNodesL1[i] = new Perceptron(new double[h12Inputs]);
			hNodesL1[i].threshold = -50;
		}

		// Initialize the hidden nodes, layer 2. These each get input from
		// h12inputs of the first layer of hidden nodes
		hNodesL2 = new Perceptron[3];
		for (int i = 0; i < 3; i++) {
			hNodesL2[i] = new Perceptron(new double[h12Inputs]);
			hNodesL2[i].threshold = -.2;
		}

		finalNode = new Perceptron(new double[hNodesL2.length]);

		// Initialize weights
		finalNode.weights[0] = 0.5;
		finalNode.weights[1] = 0.8;
		finalNode.weights[2] = 0.2;
		finalNode.threshold = -0.1;
		runNN(learnRate);
	}

	/**
	 * Runs the NN learning algorithm
	 * 
	 * @param learnRate
	 *            : The speed at which the NN learns
	 */
	public void runNN(double learnRate) {

		// Initialize some variables
		int output, myOutput, correct, inCorrect;
		ArrayList<Integer> training1 = new ArrayList<Integer>();

		// begin learning algorithm. Has MAX iterations
		for (int z = 0; z < MAX; z++) {

			// update learn rate: simulated annealing appraoch
			learnRate -= learnRate / (MAX * 1.5);
			correct = 0;
			inCorrect = 0;

			// shuffle array of training data every 20th iteration
			if (z % 20 == 0) {
				training1 = new ArrayList<Integer>();
				for (int i = 0; i < DataSet.length; i++) {
					training1.add(i);
				}
				Collections.shuffle(training1);
			}

			// train on every item of the data set
			for (int p = 0; p < DataSet.length; p++) {
				
				output = (int) DataSet[training1.get(p)][3][0];
				setInputs(training1.get(p), DataSet); 
				myOutput = setOutputs(); 
				
				if (myOutput == output)
					correct++;
				else {
					inCorrect++;
					//update error and delta
					updateErrorDelta(output);
					// update weights
					updateWeights(learnRate);
				}
			}

			if (z % 3000 == 0) {
			    //System.out.println("Correct Training: " + correct
			    //		+ " Incorrect: " + inCorrect);
			}

			// test data on set
			correct = 0; inCorrect = 0;
			int redRight = 0; int greenRight = 0;
			for (int p = 0; p < DataSet.length; p++) {
				output = (int) DataSet[p][3][0];
				setInputs(p, DataSet); // set the inputs 
				myOutput = setOutputsTEST(); 
				
				if (myOutput == output) { 
					if (myOutput == 0)
						redRight++;
					else
						greenRight++;
					correct++;
				} else 
					inCorrect++;
			}

			double percent = (double) correct / (correct + inCorrect);
			if (percent > .91 || z % 2000 == 0 || z == (MAX - 1)) {
// 				System.out.println("Correct Testing: " + correct
// 						+ " Incorrect: " + inCorrect + " Percentage: "
// 						+ percent + " z: " + z + " Red right: " + redRight
// 						+ " Green right: " + greenRight);
			}
			if ((percent > 0.93 && z > (z/1.5)) || percent > 0.93) {
			    //				System.out.println("This took: " + z + " iterations: "
			    //		+ percent);
			    iterations = z;
				break;
			}
		}
	}

	/**
	 * Update the errors and deltas of theNN
	 * @param output: The correct classification of the data item
	 */
	public void updateErrorDelta(int output) {
		finalNode.error = output - finalNode.sigmoid;
		finalNode.delta = finalNode.sigmoid
				* (1 - finalNode.sigmoid) * finalNode.error;

		// for hidden nodes layer2
		for (int i = 0; i < hNodesL2.length; i++) {
			hNodesL2[i].error = finalNode.weights[i]
					* finalNode.delta;
			hNodesL2[i].delta = hNodesL2[i].sigmoid
					* (1 - hNodesL2[i].sigmoid) * hNodesL2[i].error;
		}

		// for hidden nodes layer1
		for (int i = 0; i < hNodesL1.length; i++) {
			hNodesL1[i].error = 0;
			hNodesL1[i].error += hNodesL2[i / h12Inputs].delta
					* hNodesL2[i / h12Inputs].weights[i % 3];
			hNodesL1[i].delta = hNodesL1[i].sigmoid
					* (1 - hNodesL1[i].sigmoid) * hNodesL1[i].error;
		}
	}
	
	/**
	 * Update the weights of the NN
	 */
	public void updateWeights(double learnRate) {
		// for final node
		for (int i = 0; i < finalNode.weights.length; i++) {
			finalNode.weights[i] += learnRate * finalNode.delta
					* finalNode.pInputs[i].sigmoid;
		}

		// for hidden layer 1
		// weight += learn*deltaI*inputtoI
		for (int i = 0; i < hNodesL1.length; i++) {
			for (int j = 0; j < hNodesL1[i].weights.length; j++) {
				hNodesL1[i].weights[j] += learnRate * hNodesL1[i].delta
						* hNodesL1[i].inputs[j];
			}
		}

		// for layer 2
		for (int i = 0; i < hNodesL2.length; i++) {
			for (int j = 0; j < hNodesL2[i].weights.length; j++) {
				hNodesL2[i].weights[j] += learnRate * hNodesL2[i].delta
						* hNodesL2[i].pInputs[j].sigmoid;
			}
		}
	}
	
	/**
	 * Set the inputs to the NN
	 */
	public void setInputs(int p, double[][][] data) {
		// Set hidden layer 1 inputs
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < h12Inputs; j++) {
				// get the subset
				double[] newArray = Arrays.copyOfRange(data[p][i], j
						* h12Inputs, (j + 1) * h12Inputs);
				hNodesL1[j * (i + 1)].setInputs(newArray);
			}
		}

		// Set hidden layer 2 inputs
		for (int i = 0; i < hNodesL2.length; i++) {
			hNodesL2[i].setInputsP(Arrays.copyOfRange(hNodesL1, i * h12Inputs,
					(i + 1) * h12Inputs));
		}

		finalNode.setInputsP(hNodesL2); // set final input
	}

	/**
	 * Set the final output of the NN
	 */
	public int setOutputs() {
		if (finalNode.sigmoid > .51)
			return 1;
		else if (finalNode.sigmoid < .49)
			return 0;
		return -1;
	}

	/**
	 * Set the final output of the NN for testing purposes (never outputs -1)
	 */
	public int setOutputsTEST() {
		if (finalNode.sigmoid > .5)
			return 1;
		else if (finalNode.sigmoid < .5)
			return 0;
		return -1;
	}
	
	/**
	 * Test data on the current NN
	 * @param data: Input the data to be tested
	 */
	public void testData(double[][][] data) {
		int correct = 0;
		int incorrect = 0;
		int output, myOutput;
		int zero_correct = 0;
		int one_correct = 0;
		int q = 0;

		//System.out.println("\nNumber FileName Prediction Actual Iterations Type TypeAgainst");
		for (int p = 0; p < data.length; p++) {
			output = (int) data[p][3][0];


			setInputs(p, data);
			myOutput = setOutputsTEST();

			if (output == 0)
			    System.out.println(p+","+set1_type+p+".jpeg"+","+myOutput+","+output+","+iterations+","+set1_type+","+set2_type);
			else if (output == 1){
			    System.out.println(q+","+set2_type+q+".jpeg"+","+myOutput+","+output+","+iterations+","+set2_type+","+set1_type);
			    q++;
			}

			if (output == 0 && myOutput == 0)
			    zero_correct++;
			else if (output == 1 && myOutput == 1)
			    one_correct++;

			if (myOutput == output)
				correct++;
			else 
				incorrect++;
		}

// 		System.out.println("\n"+zero_correct + " items were correctly classified from class 1");
// 		System.out.println(one_correct + " items were correctly classified from class 2");
// 		System.out.println("Correct: " + correct + " Incorrect: " + incorrect);
	}

}
