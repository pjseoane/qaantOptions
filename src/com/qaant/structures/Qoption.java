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
    protected enum TipoOpcion {CALL,PUT};
       
    public final static char CALL='C';
    public final static char PUT='P';
    
    protected char callPut;
    
    protected double strike,daysToExpiration, rate,optionMktValue=0,volatModel;
    protected int steps=1;
   
    public Qoption(){}
    
    public Qoption(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super (und);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =und.underlyingHistVolatility;
    }
    
    public Qoption(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue, int steps){
        super (und);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =und.underlyingHistVolatility;
        this.steps              =steps;
    }
    
                   
    public Qoption(char tipoContrato,double undValue,double underlyingHistVolatility,double undDivRate ,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super (tipoContrato,undValue,underlyingHistVolatility,undDivRate);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =underlyingHistVolatility;
    }
    
    public Qoption(char tipoContrato,double undValue,double underlyingHistVolatility,double undDivRate ,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super (tipoContrato,undValue,underlyingHistVolatility,undDivRate);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =underlyingHistVolatility;
        this.steps              =steps;
    }

     public Qoption(char tipoContrato,double undValue,double underlyingHistVolatility,double undDivRate ,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps,int nodes){
        super (tipoContrato,undValue,underlyingHistVolatility,undDivRate,nodes);      
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        this.volatModel         =underlyingHistVolatility;
        this.steps              =steps;
     
    }
        
    public double getDaysToExpiration(){
        return daysToExpiration;
    }
      
   }
