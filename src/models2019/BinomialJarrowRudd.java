/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author pseoane
 */
public class BinomialJarrowRudd extends BinomialCRR2019 implements Optionable{
    public BinomialJarrowRudd(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut,  strike, daysToExpiration, rate, optionMktValue, steps);
}
    @Override
     public void runModel(){
        System.out.println("Run Model JR....");
        pModelName="Binomial Jarrow-Rudd";
        modelNumber=4;
        
        dayYear=daysToExpiration/365;
        double h=dayYear/steps;
        double z=Math.exp(rate*h);
        double drift=(tipoContrato=='F')? 1: z;
        
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        double firstTerm=(rate*0.5*Math.pow(volatModel,2))*h;
        double secondTerm=volatModel*Math.sqrt(h);
        double u= Math.exp(firstTerm+secondTerm);
        double d= Math.exp(firstTerm-secondTerm);
        
        double p=(drift-d)/(u-d);
        
        double[][] undTree=buildUnderlyingTree(u,d);
        double[][] optTree=buildOptionTree(undTree,p,z);
        
        prima=optTree[0][0];
        delta=(optTree[1][1] - optTree[1][0]) / (undTree[1][1] - undTree[1][0]);
        gamma=((optTree[2][0] - optTree[2][1]) / (undTree[2][0] - undTree[2][1]) - (
                    optTree[2][1] - optTree[2][2]) / (undTree[2][1] - undTree[2][2])) / (
                                 (undTree[2][0] - undTree[2][2]) / 2);
        theta=(optTree[2][1] - optTree[0][0]) / (2 * 365 * h);
        
        vega=0;
        rho=0;
        
        if(optionMktValue>-1){
            BinomialJarrowRudd optJR=new BinomialJarrowRudd(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
            vega=optJR.getPrima();
                    
        }
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
