/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import com.qaant.structures.Qunderlying;

/**
 *
 * @author pauli
 */
public class QBinomialCRR extends QBinomialJRudd implements QOptionable{
    public QBinomialCRR(){super();}
    public QBinomialCRR(char tipoEjercicio, Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public QBinomialCRR(char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
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
    protected double modelGetPrima(double volForLambda){
       return new QBinomialCRR(tipoEjercicio,tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
       
    }
    

}

