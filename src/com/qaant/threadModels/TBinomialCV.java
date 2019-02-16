/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.threadModels;


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

public class TBinomialCV extends TGenericModel implements Runnable{
    static {modelMap.put(5,"Binomial CV- QAANT");}
    
    
    public TBinomialCV(){super();}
    public TBinomialCV(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public TBinomialCV(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    }
    
    @Override
    public void run(){
        startTime   =System.currentTimeMillis();
        if (opcionConVida && strike !=0){
            opcionConVida();
        }else{
            opcionSinVida();
        }
      
    impliedVol=calcImpliedVlt();
    fillDerivativesArray();
    }
    
    private void opcionConVida(){
               
        TBinomialCRR amerOpt = new TBinomialCRR('A',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
        TBinomialCRR euroOpt = new TBinomialCRR('E',tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
        TBlackScholes bsOpt = new TBlackScholes(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
   
        Thread worker0= new Thread(amerOpt);
        Thread worker1= new Thread(euroOpt);
        Thread worker2= new Thread(bsOpt);
        
        worker0.start();
        worker1.start();
        worker2.start();
           
        try{
            worker0.join();
            worker1.join();
            worker2.join();
        }
        catch (InterruptedException e){
        }
        
        prima=amerOpt.getPrima()+bsOpt.getPrima()-euroOpt.getPrima();
        delta=amerOpt.getDelta()+bsOpt.getDelta()-euroOpt.getDelta();
        gamma=amerOpt.getGamma()+bsOpt.getGamma()-euroOpt.getGamma();
        vega =amerOpt.getVega()+bsOpt.getVega()-euroOpt.getVega();
        theta=amerOpt.getTheta()+bsOpt.getTheta()-euroOpt.getTheta();
        rho  =amerOpt.getRho()+bsOpt.getRho()-euroOpt.getRho();
           
    }
   
    @Override
    protected double modelGetPrima(double volForLambda){
       TBinomialCV opt= new TBinomialCV(tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,-1,steps); 
       opt.run();
       return opt.getPrima();
        
    }
}
