package test;

import com.minhaskamal.intellectron.NeuralNetworkImplementation;

public class NeuralNetworkImplementationTest {
	public static void main(String[] args) {
		int[] numbersOfNeuronsInLayers = new int[]{4, 1};
		NeuralNetworkImplementation neuralNetworkImplementation = new NeuralNetworkImplementation(numbersOfNeuronsInLayers, 0.1, 2);
		
		//input only, bias is handled internally//
		double[][] inputs = new double[][]{
			{0, 0},
			{0, 1},
			{1, 0},
			{1, 1}
		};
		//output//
		double[][] outputs = new double[][]{
			{1},
			{0},
			{0},
			{1}
		};
		
		int cycle = 10000;
		for(int i=0; i<cycle; i++){
			neuralNetworkImplementation.train(inputs, outputs);
		}
		
		double accuracy = neuralNetworkImplementation.test(inputs, outputs);
		System.out.println(accuracy);
	}
}