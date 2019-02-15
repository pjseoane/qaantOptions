/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.threadModels;

import com.qaant.optionModels.QImpliedVolCalc;
import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author pauli
 */
public abstract class TimpliedVolCalc implements Runnable{
    
    double primaModelo,optionMktValue, volatModel,impliedVol;
    protected int MAXITERATIONS =50;
    protected double ACCURACY   =0.00001;
    
    TimpliedVolCalc (double primaModelo,double optionMktValue,double volatModel){
        this.primaModelo    =primaModelo;
        this.optionMktValue =optionMktValue;
        this.volatModel     =volatModel;
    }
    
    
    @Override
    public void run(){
        
        if(optionMktValue>0){
            double volMin;
            double volMax;
        
            if(primaModelo<=optionMktValue){
                volMin=volatModel;
                volMax=volMin*3;
                }else{
                volMin=volatModel/3;
                volMax=volatModel;
                }
        //definicion de funcion para mandar a algo de impVlt (la dif entre valor mercado y valor teorico, buscamos que sea cero)      
        DoubleUnaryOperator opt1 = xVlt-> optionMktValue - modelGetPrima(xVlt);
        impliedVol= QImpliedVolCalc.bisection(opt1, volMin, volMax, MAXITERATIONS, ACCURACY);
        }
   
    }   
    public double getImpliedVol2(){
        return impliedVol;
    }
    
    abstract public double modelGetPrima(double volForLambda); // cada modelo da su funcion
    
}
