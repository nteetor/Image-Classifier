/**
 * The basic unit of a neural network
 */

import java.util.*;
public class Perceptron {
    double[] weights;
    double[] inputs;
    Perceptron[] pInputs;
    double threshold;
    double SUM;
    double error;
    double delta;
    double sigmoid;
    
    /**
     * Constructs a perceptron
     * @param weights The weights of the inputs
     * @param inputs 
     * @param threshold
     */
    public Perceptron(double[] weights, double[] inputs, int threshold) {
    	this.weights = weights;
    	this.inputs = inputs;
    	this.threshold = threshold;
    	error = 0;
    	delta = 0;
    	sigmoid = 0;
    }
    
    /**
     * Perceptron without given weights or a threshold
     */
    public Perceptron(double[] inputs) {
    	threshold = -0.6;
    	weights = new double[inputs.length];
    	for(int i = 0; i<weights.length; i++) {
    		//weights[i] = (rand.nextDouble()*(rand.nextInt(5) - 2))/(inputs.length);
    		weights[i] = (double)1/(inputs.length);
    	}
    	this.inputs = inputs;
    	error = 0;
    	delta = 0;
    	sigmoid = 0;
    }
    
    /**
     * Set the inputs to the perceptron
     */
    public void setInputs(double[] inputs) {
    	this.inputs = inputs;
    	computeSum();
    }
    
    /**
     * Set the inputs as perceptrons
     */
    public void setInputsP(Perceptron[] inputs) {
    	pInputs = inputs;
    	computeSumP();
    }
    
    /**
     * Compute sum
     */
    public void computeSum() {
    	SUM = threshold;
    	for(int i = 0; i<inputs.length; i++) {
    		SUM += weights[i]*inputs[i];
    	}
    	sigmoid = 1/(1+Math.exp(-SUM));
    }
    
    
    public void computeSumP() {
    	SUM = threshold;
    	for(int i = 0; i<pInputs.length; i++) {
    		SUM += weights[i]*pInputs[i].sigmoid;
    	}
    	sigmoid = 1/(1+Math.exp(-SUM));
    }
}
