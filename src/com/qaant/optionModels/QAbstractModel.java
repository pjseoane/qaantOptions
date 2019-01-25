/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;


import com.qaant.optionModelsV2.QOptionable;
import com.qaant.optionModelsV2.QImpliedVolCalc;
import java.util.function.DoubleUnaryOperator;
import com.qaant.structures.Qunderlying;
//import com.qaant.optionModels.Optionable
/**
 *
 * @author pseoane
 */
public abstract class QAbstractModel extends Qunderlying implements QOptionable{
    
    enum TipoOpcion {CALL,PUT}
    enum eDerivatives{PRIMA, DELTA, GAMMA, VEGA,THETA,RHO,IV}
    enum TipoEjercicio {AMERICAN,EUROPEAN}
    
    public final static char CALL='C';
    public final static char PUT='P';
             
    public final static char EUROPEAN='E';
    public final static char AMERICAN='A';
    
    protected char tipoEjercicio, callPut;
    protected int cpFlag,modelNumber;
    protected int steps=1; //steps es 1 para BS y Whaley para no alterar el calculo de z
    protected double strike,daysToExpiration, rate,optionMktValue=0;
    protected double z,dayYear, sqrDayYear,volatModel,underlyingNPV;
    protected double prima=-2,delta=-2,gamma=-2,vega=-2,theta=-2,rho=-2,impliedVol=0; 
    protected boolean opcionConVida;
    protected double[][] derivativesArray = new double[1][10];
    protected double startTime, elapsedTime;
    protected String pModelName;

    //Para calculos de implied vol
    protected int MAXITERATIONS =50;
    protected double ACCURACY   =0.00001;
    
    public QAbstractModel (){build();}
    public QAbstractModel (Qunderlying und, char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und);
        
        this.underlyingHistVolatility   =und.getUnderlyingHistVlt();
        this.callPut                    =callPut;  
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.rate                       =rate;
        this.optionMktValue             =optionMktValue;
        build();
    }
    public QAbstractModel (char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
       
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.callPut                    =callPut;  
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.rate                       =rate;
        this.optionMktValue             =optionMktValue;
        build();
    }
    
    //Constructores para modelos numericos
    public QAbstractModel (char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
       
        this.tipoEjercicio              =tipoEjercicio;
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.callPut                    =callPut;  
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.rate                       =rate;
        this.optionMktValue             =optionMktValue;
        this.steps                      =steps;
        build();
    }
    public QAbstractModel (char tipoEjercicio,Qunderlying und, char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(und);
        
        this.tipoEjercicio              =tipoEjercicio;
        this.underlyingHistVolatility   =und.getUnderlyingHistVlt();
        this.callPut                    =callPut;  
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.rate                       =rate;
        this.optionMktValue             =optionMktValue;
        this.steps                      =steps;
        build();
    }
    private void build(){
             
        startTime=System.currentTimeMillis();
        runModel();
        elapsedTime = System.currentTimeMillis() - startTime;
       
        
    }
     protected void commonVarsSetup(){
        this.dayYear              =daysToExpiration/365;
        this.sqrDayYear           =Math.sqrt(dayYear);
        this.volatModel           =underlyingHistVolatility;
        this.cpFlag               =(callPut==CALL)?1:-1;
        this.opcionConVida        =daysToExpiration>0;
        this.z                    =Math.exp(-rate*dayYear/steps);
        this.underlyingNPV        =underlyingValue*Math.exp(-dividendRate*dayYear);
     }
     
     public static double modelChooser(){
         QOptionable c = new QBlackScholes();
         return 33.33;
     }
   
  
    @Override
    abstract public void runModel(); //Cada modelo implementa runModel()
   
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
    
       
    public void opcionSinVida(){
        delta=cpFlag;  
        gamma=vega=theta=rho=0;
        prima = payoff(underlyingValue,strike,cpFlag);
        //delta=(prima==0)?0:cpFlag;
       
    }
    public double payoff(double underlyingValue, double strike, int cpFlag){
        return Math.max((underlyingValue - strike) * cpFlag, 0);
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
    public String getOptionString(){
        StringBuilder builder =new StringBuilder();
        // builder.append("Ticker-");
        //builder.append(anUnderlying.getTicker());
        builder.append(modelNumber);
        builder.append("-");
        builder.append(pModelName);
        builder.append("/Option->");
        builder.append(callPut);
        builder.append("/strike->");
        builder.append(strike);
        builder.append("/prima->");
        builder.append(prima);
        builder.append("/delta->");
        builder.append(delta);
        builder.append("/gamma->");
        builder.append(gamma);
        builder.append("/vega->");
        builder.append(vega);
        builder.append("/theta->");
        builder.append(theta);
        builder.append("/rho->");
        builder.append(rho);
        builder.append("/optionMktValue->");
        builder.append(optionMktValue);
        builder.append("/impVlt->");
        builder.append(volatModel);
        builder.append("z");
        return builder.toString();
    }//end getString
    
    public void setDaysToExpiration(double days){
        this.daysToExpiration=days;
    }
    
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
    
    public double getOptionMktValue(){return optionMktValue;}
    public char getTipoEjercicio(){return tipoEjercicio;}
    public char getCallPut(){return callPut;}
    public double getStrike(){return strike;}
    public double getDaysToExpiration(){return daysToExpiration;}
    public double getTasa(){return rate;}
    public double getValueToFind(int i){
     
        if (i>9){i=0;}
        return derivativesArray[0][i];
    }   
    
   
    
}
