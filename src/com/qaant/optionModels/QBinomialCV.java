/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import com.qaant.optionModelsV2.QOptionable;
import com.qaant.structures.Qunderlying;

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
    public QBinomialCV(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public QBinomialCV(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    }
    
    @Override
    public void runModel(){
        pModelName="Binomial CRR- QAANT";
        modelNumber=3;
        
        commonVarsSetup();
        //hay que checkear el tema de life aca, por si se cambia la variable de dias con un setter   
        if (opcionConVida){
           
            runThisModel();
            impliedVol=getImpliedVlt();
        }else{
            opcionSinVida();
        }
         fillDerivativesArray();
    }
    private void runThisModel(){
        
        
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
    protected double modelGetPrima(double volForLambda){
        return new QBinomialCV(tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
    }
}
