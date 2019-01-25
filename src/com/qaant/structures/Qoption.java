/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.structures;

/**
 *
 * @author pauli
 */
public class Qoption extends Qunderlying{
    enum TipoOpcion {CALL,PUT};
    //enum TipoEjercicio {AMERICAN,EUROPEAN};
    
    public final static char CALL='C';
    public final static char PUT='P';
             
    //public final static char EUROPEAN='E';
    //public final static char AMERICAN='A';
    protected char callPut;
    
    protected double strike,daysToExpiration, rate,volatModel=0,optionMktValue=0;
    protected double dayYear, sqrDayYear,payoff,z,underlyingNPV;
    protected int cpFlag,steps=1;
    protected boolean opcionConVida;
    
    public Qoption(){}
    
    
    public Qoption(Qoption op){
        super(op.tipoContrato,op.underlyingValue,op.underlyingHistVolatility,op.dividendRate);
        this.callPut            =op.callPut;
        this.strike             =op.strike;
        this.daysToExpiration   =op.daysToExpiration;
        this.rate               =op.rate;
        this.optionMktValue     =op.optionMktValue;
        this.volatModel         =op.underlyingHistVolatility;
        
        build();
    }
    
    public Qoption(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super (und);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =und.underlyingHistVolatility;
        build();
    }
                   
    public Qoption(char tipoContrato,double undValue,double underlyingHistVolatility,double undDivRate ,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super (tipoContrato,undValue,underlyingHistVolatility,undDivRate);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =underlyingHistVolatility;
        build();
    }
    
    
    private void build(){
        this.dayYear              =daysToExpiration/365;
        this.sqrDayYear           =Math.sqrt(dayYear);
        this.cpFlag               =(callPut==CALL)?1:-1;
        this.opcionConVida        =daysToExpiration>0;
       // this.volatModel           =underlyingHistVolatility;
        this.z                    =Math.exp(-rate*dayYear/steps);
        this.underlyingNPV        =underlyingValue*Math.exp(-dividendRate*dayYear);
        
    }
    public void setRiskFreeRate(double rate){
        this.rate=rate;
        build();
        
    }
     public void setOptionMktValue(double mktValue){
        this.optionMktValue=mktValue;
        //build();
    }
    
    public void setOptionType(char opt){
        this.callPut=opt;
        build();
    }
    public void setVolatModel(double vlt){
        this.volatModel=vlt;
        build();
    }
    
    
    public void setDaysToExpiration(double days){
        this.daysToExpiration=days;
        build();
    }
    public double getDaysToExpiration(){
        return daysToExpiration;
    }
   
    public double getPayoff(){
        return Math.max((underlyingValue - strike) * cpFlag, 0);
        }
   
}
