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
public class TBinomialCRR extends TBinomialJR implements Runnable{
    static {modelMap.put(3,"Binomial CRR- Thread QAANT");}
   
    
    
    public TBinomialCRR(){super();}
    public TBinomialCRR(char tipoEjercicio, Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
      
    }
    public TBinomialCRR(char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
       
    }

    @Override
    
    public void run() {
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
            TBinomialCRR optVega=new TBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, volatModel+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
            TBinomialCRR optRho=new TBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration, rate+0.0001, -1, steps);
            
            Thread worker1= new Thread(optVega);
            Thread worker2= new Thread(optRho);
            
            worker1.start();
            worker2.start();
            
             try{
                    worker1.join();
                    worker2.join();
                  
                }
                catch (InterruptedException e){
                }
            
                        
            vega=optVega.getPrima()-prima;
            rho=(optRho.getPrima()-prima)*100;
        }
    } 
    
    @Override
    protected double modelGetPrima(double volForLambda){
        TBinomialCRR opt = new TBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,-1,steps);
        opt.run();
        return opt.getPrima();
    }
   
}

