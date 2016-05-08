/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Website: https://github.com/MinhasKamal/Intellectron     *
* License:  GNU General Public License version-3           *
***********************************************************/

package com.minhaskamal.intellectron.neuralnetwork;

import java.util.LinkedList;
import com.minhaskamal.intellectron.neuralnetwork.neuron.Neuron;

public class NeuronLayer {
	public double learningRate;
	private LinkedList<Neuron> neurons;
	private double[] errors;
	private double[] outputs;
	
	/**
	 * 
	 * @param numberOfNeurons
	 * @param numberOfDendritesInEachNeuron without bias neuron
	 * @param learningRate
	 */
	public NeuronLayer(int numberOfNeurons, int numberOfDendritesInEachNeuron, double learningRate) {
		this.learningRate = learningRate;
		
		numberOfDendritesInEachNeuron++;//one each for bias input
		this.neurons = new LinkedList<Neuron>();
		for(int i=0; i<numberOfNeurons; i++){
			this.neurons.add(new Neuron(numberOfDendritesInEachNeuron));
		}
		
		this.errors = new double[this.neurons.size()];
		this.outputs = new double[this.neurons.size()+1];//with bias output
	}
	
	public NeuronLayer(int numberOfNeurons, NeuronLayer previousNeuronLayer, double learningRate) {
		this(numberOfNeurons, previousNeuronLayer.neurons.size(), learningRate);
	}
	
	///////////////////////////////PROCESS//////////////////////////////////
	
	public void process(double[] inputs){
		for(int i=0; i<outputs.length-1; i++){
			this.outputs[i] = neurons.get(i).processSignal(inputs);
		}
		this.outputs[outputs.length-1] = 1;	//bias
	}
	
	public double[] getOutputs(){
		return this.outputs;
	}
	
	/////////////////////////////ERROR CALCULATION///////////////////////////////
	
	public void calculateErrors(double[] expectedOutputs){
		for(int i=0; i<this.errors.length; i++){
			this.errors[i] = outputs[i]*(1-outputs[i])*(expectedOutputs[i]-outputs[i]);
		}
	}
	
	public void calculateErrors(NeuronLayer followingLayer){
		for(int i=0; i<this.errors.length; i++){
			double integratedEffectOnError=0;
			
			for(int j=0; j<followingLayer.errors.length; j++){
				integratedEffectOnError += followingLayer.errors[j]*followingLayer.neurons.get(j).getWeight(i);
			}
			
			this.errors[i] = outputs[i]*(1-outputs[i])*integratedEffectOnError;
		}
	}
	
	public double[] getErrors(){
		return this.errors;
	}
	
	///////////////////////////////RSET WEIGHTS///////////////////////////////
	
	public void learn(NeuronLayer previousLayer){
		this.learn(previousLayer.outputs);
	}
	
	public void learn(double[] previousInputSignals){
		int i=0;
		for(Neuron neuron: this.neurons){
			neuron.learn(this.errors[i]*this.learningRate, previousInputSignals);
			i++;
		}
	}
	
	public String toString(){
		String string = "<neuron-layer>\n";
		
		string+= "<learning-rate>"+this.learningRate+"</learning-rate>\n";
		
		for(Neuron neuron: this.neurons){
			string += neuron.toString()+"\n";
		}
		
		string += "</neuron-layer>";
		
		return string;
	}
}