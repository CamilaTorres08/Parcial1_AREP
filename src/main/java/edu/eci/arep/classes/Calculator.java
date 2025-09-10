package edu.eci.arep.classes;

import java.lang.reflect.Array;
import java.util.LinkedList;

public class Calculator {
    LinkedList<Double> values = new LinkedList<>();
    public Calculator(){

    }
    public int add(double number){
        values.add(number);
        return values.size();
    }

    public Object[] list(){
        return values.toArray();
    }
    public Double median(){
        Double sum = 0.0;
        for(Double value : values){
            sum += value;
        }

        int totalNumbers = values.size();
        if(totalNumbers != 0){
            return ((double) 1/totalNumbers) * sum;
        }
        return null;
    }
    public Double estandarDesv(){
        Double sum = 0.0;
        Double median = median();
        for(Double value : values){
            double result =  value - median;
            sum += Math.pow(result,2);
        }
        double rest = values.size() - 1;
        if(rest != 0){
            double mult = (1/rest) * sum;
            return Math.sqrt(mult);
        }
        return null;
    }
    public void clear(){
        this.values = new LinkedList<>();
    }
    public int total(){
        return values.size();
    }

}
