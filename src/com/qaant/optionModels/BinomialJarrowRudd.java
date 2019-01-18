/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author pseoane
 */
public class BinomialJarrowRudd extends BinomialCRR2019 implements Optionable{
    protected double u,d,p,z;
    protected double[][]undTree,optTree,underlyingTree;
    
    
    
    public BinomialJarrowRudd(){}
    public BinomialJarrowRudd(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut,  strike, daysToExpiration, rate, optionMktValue, steps);
}
    @Override
     public void runModel(){
       // System.out.println("Run Model JR....");
        pModelName="Binomial Jarrow-Rudd";
        modelNumber=4;
        
        
        dayYear=daysToExpiration/365;
        double h=dayYear/steps;
        z=Math.exp(-rate*h);
        double drift=(tipoContrato=='F')? 1: Math.exp(rate*h);
        
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        double firstTerm=(rate-0.5*Math.pow(volatModel,2))*h;
        double secondTerm=volatModel*Math.sqrt(h);
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
        theta=(optTree[2][1] - optTree[0][0]) / (2 * 365 * h);
        
        vega=0;
        rho=0;
        
        if(optionMktValue>-1){
            BinomialJarrowRudd optJR=new BinomialJarrowRudd(tipoEjercicio,tipoContrato, underlyingValue, volatModel+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
            //System.out.print("Volat ..."+(volatModel));
            vega=optJR.getPrima()-prima;
            
            optJR=new BinomialJarrowRudd(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration, rate+0.01, -1, steps);
            //System.out.print("Volat ..."+(volatModel));
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new BinomialJarrowRudd(tipoEjercicio, tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
        //impliedVol=.4444;
              
    }
    return impliedVol;
    }
}
