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
        
        
        prima=99.99;
        delta=-0.99;
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
