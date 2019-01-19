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

/*
Solo Para American
Modelo Con tecnica de estabilizacioon Control Variate
binomial american+bseuropean-binomialeuropean.
Hull pag 333 y 351
*/

public class QBinomialControlVariate extends QAbstractModel implements QOptionable{
    public QBinomialControlVariate(){super();}
    public QBinomialControlVariate(cUnderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public QBinomialControlVariate(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    }
    
    @Override
    public void runModel(){
    QBinomialJarrowRudd amerOpt = new QBinomialJarrowRudd('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    QBinomialJarrowRudd euroOpt = new QBinomialJarrowRudd('E',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    QBlackScholes bsOpt             = new QBlackScholes(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
   
    prima=amerOpt.getPrima()+bsOpt.getPrima()-euroOpt.getPrima();
    delta=amerOpt.getDelta()+bsOpt.getDelta()-euroOpt.getDelta();
    gamma=amerOpt.getGamma()+bsOpt.getGamma()-euroOpt.getGamma();
    vega =amerOpt.getVega()+bsOpt.getVega()-euroOpt.getVega();
    theta=amerOpt.getTheta()+bsOpt.getTheta()-euroOpt.getTheta();
    rho  =amerOpt.getRho()+bsOpt.getRho()-euroOpt.getRho();
           
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new QBinomialControlVariate(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
               
        impliedVol= QImpliedVolCalc.bisection(opt1, min, max, iter, precision);
              
        }
    return impliedVol;}
}
