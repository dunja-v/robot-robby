package hr.fer.zemris.projekt.algorithms.neural.ffann;

import hr.fer.zemris.projekt.Move;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.neural.ActivationFunction;
import hr.fer.zemris.projekt.algorithms.neural.Layer;
import hr.fer.zemris.projekt.algorithms.neural.Mapper;
import hr.fer.zemris.projekt.algorithms.neural.NeuralNetworkException;
import hr.fer.zemris.projekt.grid.Field;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

/**
 * @author Kristijan Vulinovic
 * @version 1.0.0
 */
public class FFANN implements Robot {
    private Layer[] layers;
    private int numberOfWeights;

    private double standardizedFitness;

    public FFANN(int[] layout, ActivationFunction[] activationFunctions){
        Objects.requireNonNull(layout, "No layout given!");
        Objects.requireNonNull(activationFunctions, "No activation functions given!");
        if (layout.length != activationFunctions.length){
            throw new NeuralNetworkException("The number of neuron layers and activation functions do not natch!");
        }

        layers = new Layer[layout.length];

        for (int i = 0; i < layout.length; ++i){
            layers[i] = new Layer(layout[i], activationFunctions[i]);
            if (i > 0){
                layers[i - 1].setNext(layers[i]);
            }
        }

        numberOfWeights = 0;
        for (Layer layer : layers){
            numberOfWeights += layer.numberOfWeights();
        }
    }

    public int getNumberOfWeights(){
        return numberOfWeights;
    }

    public double[] getWeights(){
        double[] weights = new double[getNumberOfWeights()];

        int index = 0;
        for (Layer layer : layers){
            double[] tmpWidths = layer.getWeights();
            System.arraycopy(tmpWidths, 0, weights, index, tmpWidths.length);
            index += tmpWidths.length;
        }

        return weights;
    }

    public int getNumberOfLayers(){
        return layers.length;
    }

    public Layer[] getLayers(){
        return layers;
    }

    public void setWeights(double[] weights){
        if (weights.length != getNumberOfWeights()){
            throw new NeuralNetworkException("Invalid number of widths given!");
        }

        int index = 0;
        for (Layer layer : layers){
            if (layer.numberOfWeights() == 0) continue;

            double[] tmpWidths = new double[layer.numberOfWeights()];
            System.arraycopy(weights, index, tmpWidths, 0, tmpWidths.length);
            layer.setWeightMatrix(tmpWidths);
            index += tmpWidths.length;
        }
    }

    @Override
    public Move nextMove(Field current, Field left, Field right, Field up, Field down) {
        Mapper mapper = Mapper.getInstance();

        RealVector input = mapper.codeInput(up, left, right, down, current);
        RealVector output = calculateOutput(input);

        return mapper.decodeOutput(output);
    }

    public void setStandardizedFitness(double standardizedFitness){
        this.standardizedFitness = standardizedFitness;
    }

    @Override
    public double standardizedFitness() {
        return standardizedFitness;
    }

    private RealVector calculateOutput(RealVector input) {
        layers[0].giveInput(input);

        RealVector output = null;
        for (Layer layer : layers) {
            output = layer.calculateOutput();
        }

        return output;
    }

    public FFANN copy() {
        int[] layout = new int[layers.length];
        ActivationFunction[] activationFunctions = new ActivationFunction[layers.length];

        for (int i = 0; i < layers.length; ++i){
            layout[i] = layers[i].numberOfNeurons();
            activationFunctions[i] = layers[i].getActivationFunction();
        }

        FFANN copy = new FFANN(layout, activationFunctions);

        return copy;
    }
}
