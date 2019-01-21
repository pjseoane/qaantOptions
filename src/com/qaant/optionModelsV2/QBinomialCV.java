/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;

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

public class QBinomialCV extends QAbstractModel implements QOptionable{
    public QBinomialCV(){super();}
    public QBinomialCV(cUnderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public QBinomialCV(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    }
    
    @Override
    public void runModel(){
    QBinomialJRudd amerOpt = new QBinomialJRudd('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    QBinomialJRudd euroOpt = new QBinomialJRudd('E',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    QBlackScholes bsOpt             = new QBlackScholes(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
   
    prima=amerOpt.getPrima()+bsOpt.getPrima()-euroOpt.getPrima();
    delta=amerOpt.getDelta()+bsOpt.getDelta()-euroOpt.getDelta();
    gamma=amerOpt.getGamma()+bsOpt.getGamma()-euroOpt.getGamma();
    vega =amerOpt.getVega()+bsOpt.getVega()-euroOpt.getVega();
    theta=amerOpt.getTheta()+bsOpt.getTheta()-euroOpt.getTheta();
    rho  =amerOpt.getRho()+bsOpt.getRho()-euroOpt.getRho();
           
    }
   
    
     @Override
    protected double modelGetPrima(double x){
        return new QBinomialCV(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
    }
}
