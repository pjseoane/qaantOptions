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
    enum TipoEjercicio {AMERICAN,EUROPEAN};
    
    public final static char CALL='C';
    public final static char PUT='P';
             
    public final static char EUROPEAN='E';
    public final static char AMERICAN='A';
    protected char tipoEjercicio, callPut;
    
    protected double strike,daysToExpiration, rate,optionMktValue=0;
    protected double dayYear, sqrDayYear;
    protected int cpFlag;
    protected boolean opcionConVida;
    
    public Qoption(){}
    
    public Qoption(Qunderlying und,char tipoEjercicio,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und);
        this.tipoEjercicio      =tipoEjercicio;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        build();
    }
    public Qoption(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und);
       
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        build();
    }
    
    
    public Qoption(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
        this.tipoEjercicio      =tipoEjercicio;
        this.callPut            =callPut;
        this.strike             =strike;
        this.daysToExpiration   =daysToExpiration;
        this.rate               =rate;
        this.optionMktValue     =optionMktValue;
        build();
    }
    private void build(){
        this.dayYear              =daysToExpiration/365;
        this.sqrDayYear           =Math.sqrt(dayYear);
        this.cpFlag               =(callPut==CALL)?1:-1;
        this.opcionConVida        =daysToExpiration>0;
        
    }
    public void setDaysToExpiration(double days){
        this.daysToExpiration=days;
    }
}
