/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;

import com.qaant.optionModels.QImpliedVolCalc;
import com.qaant.optionModels.QOptionable;
import com.qaant.structures.Qoption;
import com.qaant.structures.Qunderlying;
import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author pauli
 */
public abstract class QmodelMask extends Qoption implements QOptionable{
    protected char tipoEjercicio;
    protected double startTime, elapsedTime;
    protected double prima=-2,delta=-2,gamma=-2,vega=-2,theta=-2,rho=-2,impliedVol=0; 
    protected String  pModelName;
    protected int modelNumber;
    protected double[][] derivativesArray = new double[1][10];
    
    enum TipoEjercicio {AMERICAN,EUROPEAN}
    public final static char EUROPEAN='E';
    public final static char AMERICAN='A';
    
    //Para calculos de implied vol
    protected int MAXITERATIONS =50;
    protected double ACCURACY   =0.00001;
    
   
    public QmodelMask(){}
    public QmodelMask(Qoption opt, char tipoEjercicio){
        super (opt);
        this.tipoEjercicio  =tipoEjercicio;
        build();
    }
    public QmodelMask(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
        build();
    }
    public QmodelMask(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,char tipoEjercicio,char steps){
        super(und,callPut, strike, daysToExpiration, rate, optionMktValue);
        this.tipoEjercicio  =tipoEjercicio;
        this.steps          =steps;
        build();
    }
    
    public QmodelMask(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und,callPut, strike, daysToExpiration, rate, optionMktValue);
        build();
    }
    
    private void build(){
        startTime=System.currentTimeMillis();
        runModel();
        elapsedTime = System.currentTimeMillis() - startTime;
        fillDerivativesArray();
    }
    
    @Override
    abstract public void runModel(); //Cada modelo implementa runModel()
    protected void opcionSinVida(){
        delta=cpFlag;  
        gamma=vega=theta=rho=0;
        prima = payoff;
        impliedVol=0;
        //delta=(prima==0)?0:cpFlag;
    }
    @Override
     public void fillDerivativesArray(){
        derivativesArray[0][0]=prima;
        derivativesArray[0][1]=delta;
        derivativesArray[0][2]=gamma;
        derivativesArray[0][3]=vega;
        derivativesArray[0][4]=theta;
        derivativesArray[0][5]=rho;
        derivativesArray[0][6]=optionMktValue;
        derivativesArray[0][7]=impliedVol;
        derivativesArray[0][8]=elapsedTime;
        derivativesArray[0][9]=modelNumber;
     }
     
    public double[][] getDerivativesArray(){return derivativesArray;}
    public double getImpliedVlt(){
    impliedVol=volatModel;
        
        if(optionMktValue>0 && opcionConVida){
            double min;
            double max;
        
            if(prima<=optionMktValue){
                min=volatModel;
                max=min*3;
                }else{
                    min=0;// impliedVol/3;
                    max=volatModel;
            }
        
        //definicion de funcion para mandar a algo de impVlt (la dif entre valor mercado y valor teorico, buscamos que sea cero)      
        DoubleUnaryOperator opt1 = xVlt-> optionMktValue - modelGetPrima(xVlt);
        impliedVol= QImpliedVolCalc.ivNewton(opt1, min, max, MAXITERATIONS, ACCURACY);
              
        }
    return impliedVol;
    
    }
   // abstract protected double funcTest(double x);
    abstract protected double modelGetPrima(double volForLambda);
    
    @Override
    public String getModelName(){return pModelName;}
    @Override
    public double getPrima(){return prima;}
    @Override
    public double getDelta(){return delta;}
    @Override
    public double getGamma(){return gamma;}
    @Override
    public double getVega() {return vega;}
    @Override
    public double getTheta(){return theta;}
    @Override
    public double getRho()  {return rho;}
    
}
