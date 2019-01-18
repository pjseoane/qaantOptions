/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;

import underlying.cUnderlying;
//import com.qaant.optionModels.Optionable
/**
 *
 * @author pseoane
 */
public abstract class QaantAbstractModel extends cUnderlying implements QaantOptionable{
    
    enum TipoOpcion {CALL,PUT}
    enum eDerivatives{PRIMA, DELTA, GAMMA, VEGA,THETA,RHO,IV}
    enum TipoEjercicio {AMERICAN,EUROPEAN}
    
    public final static char CALL='C';
    public final static char PUT='P';
             
    public final static char EUROPEAN='E';
    public final static char AMERICAN='A';
    
    protected char tipoEjercicio, callPut;
    protected int cpFlag,modelNumber;
    protected double strike,daysToExpiration, rate;
    protected double z,dayYear, volatModel,underlyingNPV;
    protected double prima=-2,delta=-2,gamma=-2,vega=-2,theta=-2,rho=-2,optionMktValue=0,impliedVol=0; 
    protected boolean opcionConVida;
    protected double[][] derivativesArray = new double[1][10];
    protected double startTime, elapsedTime;
    protected String pModelName;
    
    
    public QaantAbstractModel(){build();}
    
    public QaantAbstractModel (cUnderlying und, char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und);
        
        this.underlyingHistVolatility   =und.getUnderlyingHistVlt();
        this.callPut                    =callPut;  
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.rate                       =rate;
        this.optionMktValue             =optionMktValue;
        build();
        
    }
    public QaantAbstractModel (char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
       
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.callPut                    =callPut;  
        this.strike                     =strike;
        this.daysToExpiration           =daysToExpiration;
        this.rate                       =rate;
        this.optionMktValue             =optionMktValue;
        build();
    }
    
    private void build(){
        //Common vars for all models.
        this.dayYear              =daysToExpiration/365;
        this.volatModel           =underlyingHistVolatility;
        this.cpFlag               =(callPut==CALL)?1:-1;
        this.opcionConVida        =daysToExpiration>0;
        this.z                    =Math.exp(-rate*dayYear);
        
        startTime=System.currentTimeMillis();
        
        if (opcionConVida){
           
            runModel(); // Abstract method, cada modelo implementa el suyo
            impliedVol=getImpliedVlt();
        }else{
            opcionSinVida();
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        fillDerivativesArray();
        
    }
    
   
    @Override
    abstract public void runModel(); //Cada modelo implementa runModel()
    @Override
    abstract public double getImpliedVlt();
    
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
