package com.minhaskamal.intellectron;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.minhaskamal.intellectron.neuralnetwork.NeuralNetwork;
import com.minhaskamal.intellectron.util.FileIO;

public class NeuralNetworkImplementation {
	
	private NeuralNetwork neuralNetwork;
	
	public NeuralNetworkImplementation(int[] numbersOfNeuronsInLayers, double learningRateOfAllLayers, int numberOfInputs) {
		this.neuralNetwork = new NeuralNetwork(numbersOfNeuronsInLayers, learningRateOfAllLayers, numberOfInputs);
	}
	
	public NeuralNetworkImplementation(String filePath) {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
			NodeList nodeList = document.getElementsByTagName(NeuralNetwork.NEURAL_NETWORK_TAG);
	        Node neuralNetworkNode = nodeList.item(0);
			this.neuralNetwork = new NeuralNetwork(neuralNetworkNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void train(double[][] inputs, double[][] outputs){
		for(int i=0; i<inputs.length; i++){
			this.neuralNetwork.processForward(inputs[i]);
			this.neuralNetwork.calculateErrors(outputs[i]);
			this.neuralNetwork.learn(inputs[i]);
		}
	}
	
	public double test(double[][] inputs, double[][] outputs, double tolerance){
		double accuracy = 0;
		
		for(int i=0; i<inputs.length; i++){
			double[] predictedOutput = predict(inputs[i]);
			
			if(isEqual(predictedOutput, outputs[i], tolerance)){
				accuracy++;
			}
		}
		
		accuracy = accuracy/inputs.length;
		
		return accuracy;
	}
	
	private boolean isEqual(double[] predictedOutput, double[] expectedOutput, double tolerance){
		for(int i=0; i<predictedOutput.length; i++){
			double diffrence = Math.abs(predictedOutput[i]-expectedOutput[i]);
			if(diffrence>tolerance){
				return false;
			}
		}
		
		return true;
	}
	
	public double[] predict(double[] input){
		this.neuralNetwork.processForward(input);
		return neuralNetwork.getOutputs();
	}
	
	public double[] generate(double[] seed){
		return neuralNetwork.processBackward(seed);
	}
	
	public void dump(String filePath){
		try {
			FileIO.writeWholeFile(filePath, this.neuralNetwork.dump());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
