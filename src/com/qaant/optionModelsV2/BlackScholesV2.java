/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;

import com.qaant.optionModels.ImpliedVolCalc;
import java.util.function.DoubleUnaryOperator;
import underlying.cUnderlying;

/**
 *
 * @author pseoane
 */
public class BlackScholesV2 extends QaantAbstractModel {
    
    public BlackScholesV2(){}
    public BlackScholesV2(cUnderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und, callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    public BlackScholesV2(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    
    @Override
    public void runModel(){
        System.out.println("xxBSxxxQAANT");
        prima=33.33;
        delta=44.44;
    }
    
    @Override
    public double getImpliedVlt() {
        impliedVol=volatModel;
        
        if(optionMktValue>0 && daysToExpiration>0){
            double min;
            double max;
            int iter=50;
            double precision=0.00001;
    
        if(prima<=optionMktValue){
            min=volatModel;
            max=min*3;
            }else{
                min=0;// impliedVol/3;
                max=volatModel;
            }
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new BlackScholesV2(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
              
        }
    return impliedVol;
    }
}
