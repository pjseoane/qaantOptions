/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import com.qaant.structures.Qunderlying;

/**
 * call american futures da el european ????
 * @author paulino.seoane ****NO ANDA BIEN EN FUTURES PUT; PUT CALL PARITY NO DA
 */
public class QEFWilmott extends QEFHull implements QOptionable {
    static {modelMap.put(6,"Explicit Finite Wilmott");}
    
    private double ds, dummy, timeStep;
    private int noTimesteps,nearestGridP;
    private double tasaTimeStep;
    private double half_volatility2;
    private double ds2;
    private double dssqd;

    private double thetaUp, thetaDn;
   // private double d;
    private int k;
    private double primaUp, primaDn;
    private double deltaUp, deltaDn;
    private double gammaUp, gammaDn;
    
    public QEFWilmott(){};
    public QEFWilmott(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue,steps);
        
    }
    public QEFWilmott(char tipoEjercicio,Qunderlying und, char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        super(tipoEjercicio,und, callPut, strike, daysToExpiration, rate, optionMktValue, steps);                                     
           
    }
    
    @Override
    public void runModel(){
        ds = strike * 2 / steps;
        q=(tipoContrato==STOCK) ? 0:rate;
        underlyingNPV=underlyingNPV*Math.exp(-q*dayYear);
        
        nearestGridP = (int)(underlyingNPV / ds);
        dummy = (underlyingNPV - nearestGridP*ds) / ds;
        timeStep = (ds*ds) / (volatModel*volatModel * 4 * strike*strike);
        noTimesteps = (int)(dayYear / timeStep) + 1;
        timeStep = dayYear / noTimesteps;


	//Boundary Conditions

	double[] vOld = new double[steps + 1];
	double[] vNew = new double[steps + 1];
	double[] vPayoff = new double[steps + 1];
	double[] vJdssqd = new double[steps];
	double[] vTasajds = new double[steps];


	        
	for (int j = 0; j <= steps; j++){
			vOld[j] = payoff(j*ds,strike,cpFlag);
			vPayoff[j] = vOld[j];
	}

	for (int j = 1; j <= (steps - 1); j++){
			vJdssqd[j] = (j*ds)*(j*ds);
			vTasajds[j] = (rate*j*ds);
	}

	tasaTimeStep = rate*timeStep;
	half_volatility2 = (volatModel*volatModel) / 2;
	ds2 = 2 * ds;
	dssqd = ds*ds;

	if (callPut == CALL){
		//Call
		vNew[0] = 0;
	}else{
		//Put
		vNew[steps] = 0;
	}

	for (int i = 1; i <= noTimesteps; i++){
		for (int j = 1; j <= (steps - 1); j++){
			delta = (vOld[j + 1] - vOld[j - 1]) / ds2;
			gamma = (vOld[j + 1] - 2 * vOld[j] + vOld[j - 1]) / dssqd;

			vNew[j] = vOld[j] + timeStep*(half_volatility2*vJdssqd[j] * gamma + vTasajds[j] * delta - rate*vOld[j]);
		}

		if (callPut == CALL){
                        //Call
			vNew[steps] = 2 * vNew[steps - 1] - vNew[steps - 2];
			//Wilmott page 624
		}else{
			//Put
			vNew[0] = (1 - tasaTimeStep)*vOld[0];
		}
		//Calculo de Theta
		if (i == noTimesteps){
			thetaUp = (vOld[nearestGridP] - vNew[nearestGridP]) / (timeStep * 365);
			thetaDn = (vOld[nearestGridP + 1] - vNew[nearestGridP + 1]) / (timeStep * 365);
		}
		for (int j = 0; j < steps; j++){

        		if (tipoEjercicio == EUROPEAN){
				//European
				vOld[j] = vNew[j];
			}else{
				//American
				vOld[j] = Math.max(vPayoff[j], vNew[j]);
			}
		}
	}
	d = 1 - dummy;
	k = nearestGridP;

	//0:Prima
	primaUp = vOld[k];
	primaDn = vOld[k + 1];
	prima = d*primaUp + dummy*primaDn;

	//1:Delta
	deltaUp = (vOld[k + 1] - vOld[k - 1]) / (2 * ds);
	deltaDn = (vOld[k + 2] - vOld[k]) / (2 * ds);
	delta = d*deltaUp + dummy*deltaDn;

	//2:Gamma
	gammaUp = (vOld[k + 1] - 2 * vOld[k] + vOld[k - 1]) / (ds*ds);
	gammaDn = (vOld[k + 2] - 2 * vOld[k + 1] + vOld[k]) / (ds*ds);
	gamma = d*gammaUp + dummy*gammaDn;

	//4:Theta
	theta = (d*thetaUp + dummy*thetaDn);

	/*	
	//Lo siguiente funciona bien es para obtener Vega Y Rho por Black & Scholes
        //Vega y Rho van por BlackScholes
        //Hay que ver vega pq se usa para Implied V.
        //No se puede caclcular vega desde aca dentro pq la recursivad de llamar al mismo metodo no tiene corte.
        
       // anUnderlying.setUnderlyingValue(underlyingNPV);
        BlackScholesModel BSoption;
        BSoption =new BlackScholesModel(anUnderlying, callPut, strike, daysToExpiration, tasa,impliedVol,0);
                                        
        vega= BSoption.getVega();
        rho = BSoption.getRho();
        
       */
               
		
}
     @Override
    protected double modelGetPrima(double volForLambda){
       return new QEFWilmott(tipoEjercicio,tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,-1,steps).getPrima();
     }
}