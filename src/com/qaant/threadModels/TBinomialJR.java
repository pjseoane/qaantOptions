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
public class TBinomialJR extends TGenericModel implements Runnable{
    static {modelMap.put(4,"Binomial JR- Thread QAANT");}
    
    protected double u,d,p,drift,firstTerm,secondTerm;
    protected double[][]undTree,optTree,underlyingTree;
      
    
    public TBinomialJR(){};
    public TBinomialJR(char tipoEjercicio, Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
       super(tipoEjercicio,und, callPut, strike,daysToExpiration,rate, optionMktValue,steps);
    }
    
    public TBinomialJR(char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
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
            
            drift       =(tipoContrato=='F')? 1: Math.exp(rate*interv);
            firstTerm   =(rate-0.5*Math.pow(volatModel,2))*interv;
            secondTerm  =volatModel*Math.sqrt(interv);
        
            u           = Math.exp(firstTerm+secondTerm);
            d           = Math.exp(firstTerm-secondTerm);
            p           =(drift-d)/(u-d);
        
            undTree     =buildUnderlyingTree();
            optTree     =buildOptionTree();
        
            prima       =optTree[0][0];
            delta       =(optTree[1][1] - optTree[1][0]) / (undTree[1][1] - undTree[1][0]);
            gamma       =((optTree[2][0] - optTree[2][1]) / (undTree[2][0] - undTree[2][1]) - (
                    optTree[2][1] - optTree[2][2]) / (undTree[2][1] - undTree[2][2])) / (
                                 (undTree[2][0] - undTree[2][2]) / 2);
        
            // theta=(optTree[2][1] - optTree[0][0]) / (2 * 365 * interv);
            theta       =0;
            vega        =0;
            rho         =0;
              
            if(optionMktValue>-1){
            
                TBinomialJR optTheta  =new TBinomialJR(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration-1, rate, -1, steps);
                TBinomialJR optVega   =new TBinomialJR(tipoEjercicio,tipoContrato, underlyingValue, volatModel+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
                TBinomialJR optRho    =new TBinomialJR(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration, rate+0.0001, -1, steps);
          
                Thread worker1= new Thread(optTheta);
                Thread worker2= new Thread(optVega);
                Thread worker3= new Thread(optRho);
            
                //Si se usa multithread aca con start() puede haber conflicto de variables???
                //worker1.run();
                //worker2.run();
                //worker3.run();
            
                
                worker1.start();
                worker2.start();
                worker3.start();
            
                try{
                    worker1.join();
                    worker2.join();
                    worker3.join();
                }
                catch (InterruptedException e){
                }
               
                theta   =optTheta.getPrima()-prima;
                vega    =optVega.getPrima()-prima;
                rho     =(optRho.getPrima()-prima)*100;
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
       
       TBinomialJR opt= new TBinomialJR(tipoEjercicio,tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,-1,steps); 
       opt.run();
       return opt.getPrima();
    }
}