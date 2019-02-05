/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import com.qaant.structures.Qunderlying;

/**
 *
 * @author pauli
 */
public class QEFHull extends QBinomialJRudd implements QOptionable{
    static {modelMap.put(5,"Explicit Finite Hull");}
    
    private double ds, timeStep,coef;
    private int noTimeSteps;
    private double tasaTimeStep;
    private double[] vOld;
    private double[] vNew;
    private double[] vPayoff;
    private double[] aHull;
    private double[] bHull;
    private double[] cHull;
    
    
    
    
    public QEFHull(){super();}
    public QEFHull(char tipoEjercicio, Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);
        
    }
    public QEFHull(char tipoEjercicio, char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
       
    }

    @Override
    public void runModel(){
        ds=strike*2/steps;
        vOld    = new double[steps+1];
        vNew    = new double[steps+1];
        vPayoff = new double[steps+1];
        aHull   = new double[steps+1];
        bHull   = new double[steps+1];
        cHull   = new double[steps+1];
    

        timeStep = ds *ds/ (impliedVol *impliedVol * 4 * strike *strike);
        noTimeSteps = (int)(dayYear / timeStep) + 1;
    
        timeStep = dayYear / noTimeSteps;
  
        tasaTimeStep = rate * timeStep;
        coef = 1 / (1 + tasaTimeStep);
        double UndMax=underlyingNPV*2;
        double UndStep=UndMax/steps;
        double primeAtNode;
       
        for (int j = 0; j <= steps; j++){
            aHull[j]=coef*(-0.5*tasaTimeStep *j + 0.5*impliedVol*impliedVol*timeStep*j*j);
            bHull[j]=coef*(1-impliedVol*impliedVol*timeStep*j*j);
            cHull[j]=coef*(0.5*tasaTimeStep *j  + 0.5*impliedVol*impliedVol*timeStep*j*j);
            vPayoff[j]=payoff(UndStep*j,strike,cpFlag);
            vOld[j]=vPayoff[j];    
        }
        if (callPut == CALL){
		//Call
		vNew[0] = 0;
	}else{
		//Put
		vNew[steps] = 0;
	}
        double prePrime=0;
    
        for (int timeLap=1;timeLap<=noTimeSteps;timeLap++){
        
            prePrime=vOld[steps/2];
        
            for(int i=1;i<=steps-1;i++){    
           
                primeAtNode=aHull[i]*vOld[i-1]+bHull[i]*vOld[i]+cHull[i]*vOld[i+1];
                if (tipoEjercicio == EUROPEAN){
				//European
                    vNew[i]=primeAtNode;
		}else{
                    //American
                    vNew[i] = Math.max(vPayoff[i], primeAtNode);
		}
            }
        
            System.arraycopy(vNew,0,vOld,0,vNew.length);          
        }
    
        int gridPoint=steps/2;
        prima   =vOld[gridPoint];
        delta   =(vOld[gridPoint+1]-vOld[gridPoint-1])/(UndStep*(gridPoint+1)-UndStep*(gridPoint-1));

        gamma   =((vOld[gridPoint-2]-prima)/(UndStep*(gridPoint-2)-UndStep*(gridPoint))-delta)/(UndStep*(gridPoint-1)-UndStep*(gridPoint));
        theta   =(prePrime-prima)/(daysToExpiration/noTimeSteps);
   
        /*
        BlackScholesModel BSoption;
        BSoption =new BlackScholesModel(anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0);
         vega= BSoption.getVega();
         rho = BSoption.getRho();
        */ 
    }
    
}
