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


public class QBinomialJRudd extends QAbstractModel implements QOptionable{
    protected double u,d,p,interv,drift;
    protected double[][]undTree,optTree,underlyingTree;  
    
    public QBinomialJRudd(){super();}
    public QBinomialJRudd(char tipoEjercicio, Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
    }
    public QBinomialJRudd(char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
    }
    
    @Override
    public void runModel(){
        startTime=System.currentTimeMillis();      
        pModelName="Binomial J-Rudd QAANT";
        modelNumber=4;
        
        
        //hay que checkear el tema de life aca, por si se cambia la variable de dias con un setter   
        if (opcionConVida){
            runThisModel();
            }else{
            opcionSinVida();
        }
        
        impliedVol=getImpliedVlt();
        elapsedTime = System.currentTimeMillis() - startTime;
        fillDerivativesArray();
    }
    private void runThisModel(){
        
        interv=dayYear/steps;
        
        drift=(tipoContrato=='F')? 1: Math.exp(rate*interv);
        
        double firstTerm=(rate-0.5*Math.pow(volatModel,2))*interv;
        double secondTerm=volatModel*Math.sqrt(interv);
        
        u= Math.exp(firstTerm+secondTerm);
        d= Math.exp(firstTerm-secondTerm);
        p=(drift-d)/(u-d);
        
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
            QBinomialJRudd optJR=new QBinomialJRudd(tipoEjercicio,tipoContrato, underlyingValue, volatModel+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
            vega=optJR.getPrima()-prima;
            
            optJR=new QBinomialJRudd(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration, rate+0.01, -1, steps);
            rho=optJR.getPrima()-prima;
        }


    }  
    protected double[][] buildUnderlyingTree(){
            undTree=new double[steps+1][steps+1];
            undTree[0][0]=underlyingNPV;
            
            for(int i=1;i<steps+1;i++){
                undTree[i][0]=undTree[i-1][0]*u;
                
                for(int j=1;j<i+1;j++){
                    undTree[i][j]=undTree[i-1][j-1]*d;
                }
            }
            return undTree;
     }
    protected double[][] buildOptionTree(){
            double optionTree[][]=new double[steps+1][steps+1];
            double px=1-p;
            
            for (int j=0;j<steps+1;j++){
                optionTree[steps][j] = Math.max(0, payoff(undTree[steps][j],strike, cpFlag));
            }
            
            for (int m=0;m<steps;m++){
                int i=steps-m-1;
                for (int j=0;j<(i+1);j++){
                    optionTree[i][j]=(p*optionTree[i+1][j]+px*optionTree[i+1][j+1])*z;
                    
                    if (tipoEjercicio==AMERICAN){
                        optionTree[i][j]=Math.max(optionTree[i][j],payoff(undTree[i][j],strike,cpFlag));
                        
                    }
                }
             
            }
        return optionTree;
        
        
    }
     @Override
    protected double modelGetPrima(double volForLambda){
        return new QBinomialJRudd(tipoEjercicio,tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
    }
}
