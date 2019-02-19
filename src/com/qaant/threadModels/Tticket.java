/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.threadModels;

//import bookPosition.*;

import com.qaant.structures.Qoption;


/**
 *
 * @author pauli
 */
public class Tticket extends Qoption implements Runnable{
    protected TGenericModel option;
    protected double lots;
    protected double price;
    protected double multiplier;
  
    protected double[][] PLOutput;
    
    public Tticket(){};
   
    public Tticket(TGenericModel option,double lots, double price, double multiplier, int nodes){
        super(option.getTipoContrato(),option.getUnderlyingValue(),option.getUnderlyingHistVlt(),option.getDividendRate() ,option.getCallPut(), option.getStrike(),option.getDaysToExpiration(),option.getTasa(),option.getOptionMktValue(),option.getModelSteps(),nodes);
        this.option =option;
        this.lots   =lots;
        this.price  =price;
        this.multiplier=multiplier;
       
    }
    
    @Override
    public void run(){
         PLOutput = new double [1][nodes+1];
        for (int i=0;i<nodes+1;i++){
            option.setOptionUndValue(undPriceRange[0][i]);
            option.run();
            PLOutput[0][i]=(option.getPrima()-price)*lots*multiplier;
        
        }
    }
    public double[][] getPLOutput(){
       
        return PLOutput;
    }
}
