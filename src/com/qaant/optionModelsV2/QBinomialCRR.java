/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;

import java.util.function.DoubleUnaryOperator;
import underlying.cUnderlying;

/**
 *
 * @author pauli
 */
public class QBinomialCRR extends QBinomialJRudd implements QOptionable{
    public QBinomialCRR(){super();}
    public QBinomialCRR(char tipoEjercicio, cUnderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public QBinomialCRR(char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    }

    @Override
    public void runModel(){
        pModelName="Binomial CRR- QAANT";
        modelNumber=3;
     
        interv=dayYear/steps;
        drift=(tipoContrato=='F')? 1: Math.exp(rate*interv);
        u = Math.exp(volatModel*Math.sqrt(interv));
        d = Math.exp(-volatModel*Math.sqrt(interv));
      
        p = (drift - d) / (u - d);
	
        undTree=buildUnderlyingTree();
        optTree=buildOptionTree();
        
        prima=optTree[0][0];
        delta=(optTree[1][1] - optTree[1][0]) / (undTree[1][1] - undTree[1][0]);
        gamma=((optTree[2][0] - optTree[2][1]) / (undTree[2][0] - undTree[2][1]) - (
                    optTree[2][1] - optTree[2][2]) / (undTree[2][1] - undTree[2][2])) / (
                                 (undTree[2][0] - undTree[2][2]) / 2);
        
        theta=(optTree[2][1] - optTree[0][0]) / (2 * 365 * interv);
        
        vega=0;
        rho=0;
        
        if(optionMktValue>-1){
            QBinomialCRR optCRR=new QBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, volatModel+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
            vega=optCRR.getPrima()-prima;
            
            optCRR=new QBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration, rate+0.01, -1, steps);
            rho=optCRR.getPrima()-prima;
        }
        
    }    
     @Override
    public double getImpliedVlt(){impliedVol=volatModel;
        
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new QBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
               
        impliedVol= QImpliedVolCalc.bisection(opt1, min, max, iter, precision);
              
        }
    return impliedVol;}

}

