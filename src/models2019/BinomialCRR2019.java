/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author pauli
 */
public class BinomialCRR2019 extends BlackScholes2019 implements Optionable{
    protected int steps;
    
    public BinomialCRR2019(){}
    public BinomialCRR2019(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        //super(tipoContrato,underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike,daysToExpiration, rate, optionMktValue);
        // puede servir en este modelp construir un BS para aproximar algun valor??
        
        this.tipoEjercicio        =tipoEjercicio;
        this.tipoContrato         =tipoContrato;
        this.underlyingValue      =underlyingValue;
        volatModel                =underlyingHistVolatility;
        this.dividendRate         =dividendRate;
        this.callPut              =callPut;  
        this.strike               =strike;
        this.daysToExpiration     =daysToExpiration;
        this.rate                 =rate;
        this.optionMktValue       =optionMktValue;
        this.steps                = ((int)(steps/2)+1)*2;
        
        build();
    }
    
    @Override
     public void runModel(){
        System.out.println("Run Model CRR..."+tipoEjercicio);
        pModelName="Binomial Cox-Ross-Rubinstein ver2019";
        modelNumber=3;
        
        //volatModel = underlyingHistVolatility;
        dayYear=daysToExpiration/365;
        double h=dayYear/steps;
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        
        double z=Math.exp(rate*h);
        
        
        
        prima=88.88;
        delta=underlyingNPV*cpFlag;
        //vega=;
     }
     protected double[][] buildUnderlyingTree(double u, double d){
            double underlyingTree[][]=new double[steps+1][steps+1];
            underlyingTree[0][0]=underlyingNPV;
            
            for(int i=1;i<steps+1;i++){
                underlyingTree[i][0]=underlyingTree[i-1][0]*u;
                
                for(int j=1;j<i+1;j++){
                    underlyingTree[i][j]=underlyingTree[i-1][j-1]*d;
                }
            }
            return underlyingTree;
     }
     protected double[][] buildOptionTree(double[][] underlyingTree,double p,double z){
            double optionTree[][]=new double[steps+1][steps+1];
            double px=1-p;
            
            for (int j=0;j<steps+1;j++){
                optionTree[steps][j] = Math.max(0, payoff(underlyingTree[steps][j],strike, cpFlag));
            }
            
            for (int m=0;m<steps;m++){
                int i=steps-m-1;
                for (int j=0;j<(i+1);j++){
                    optionTree[i][j]=(p*optionTree[i+1][j]+px*optionTree[i+1][j+1])*z;
                    
                    if (tipoEjercicio==AMERICAN){
                        optionTree[i][j]=Math.max(optionTree[i][j],payoff(underlyingTree[i][j],strike,cpFlag));
                        
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new BinomialCRR2019(tipoEjercicio, tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
        //impliedVol=.4444;
              
    }
    return impliedVol;
    }
}
