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
public class JarrowRudd2019 extends BlackScholes2019 implements Optionable{
    protected int steps;
    
    public JarrowRudd2019(){}
    public JarrowRudd2019(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        //super(tipoContrato,underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike,daysToExpiration, rate, optionMktValue);
        
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
        this.steps                =steps;
        buildJarrowRudd();
    }
    private void buildJarrowRudd(){
    
        pModelName="Binomial Jarrow-Rudd ver2019";
        modelNumber=4;
        
        double startTime=System.currentTimeMillis();
        cpFlag=(callPut==CALL)?1:-1;
        
        opcionConVida=(daysToExpiration>0);
        
        if (opcionConVida){
            
            runModel();
            
        }else{
            opcionSinVida();
        } 
        impliedVol=getImpliedVlt();
        elapsedTime = System.currentTimeMillis() - startTime;
               
       
        fillDerivativesArray();
    
    }
    @Override
     public void runModel(){
        System.out.println("Run Model JR....");
        prima=88.88;
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new JarrowRudd2019(tipoEjercicio, tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
        //impliedVol=.4444;
              
    }
    return impliedVol;
    }
}
