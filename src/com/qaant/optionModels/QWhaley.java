/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import org.apache.commons.math3.distribution.NormalDistribution;
import com.qaant.structures.Qunderlying;

/**
 *
 * @author pauli
 */
public class QWhaley extends QAbstractModel implements QOptionable{
    
    static {modelMap.put(2,"Whaley Barone Adesi- QAANT");}
    
    protected double q,b;
    
    public QWhaley(){super();}
    public QWhaley(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und, callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    public QWhaley(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    
    @Override
    //public void runModel(){
            public void run() {
    //recalcula un modelo BS para obtener las greeks por BS
   
    QBlackScholes optW= new QBlackScholes(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike, daysToExpiration, rate, 0);
        
    prima=optW.getPrima();
    delta=optW.getDelta();
    gamma=optW.getGamma();
    vega=optW.getVega();
    theta=optW.getTheta();
    rho=optW.getRho();
    
    if(tipoContrato=='F' || callPut=='P') {
         wWhaley();
        }
      
    }
   
    
     private void wWhaley(){
         
        pModelName="Whaley QAANT";
        modelNumber=2;
        tipoEjercicio =AMERICAN;
        
       
        //     q=(tipoContrato==STOCK) ? dividendRate:rate; 
            //q: si es una accion q es el dividendo, si es un futuro q se toma la rate para descontar el valor futr a presente 
            //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
        
         double xx;
         
         switch(tipoContrato){
          case STOCK:
              q=dividendRate;
              b=rate;
              break;
          
          case FUTURES:    
              q=rate;
              b=0;
              break;
        }
        double vlt2 = volatModel*volatModel;
        double VltSqrDayYear = volatModel*sqrDayYear;
	double h = 1 - z; //descuento para valor presente
        
        double alfa = 2 * rate / vlt2;
	double beta = 2 * (b-q) / vlt2;

	double lambda = (-(beta - 1) + cpFlag*Math.sqrt((beta - 1)*(beta-1) + 4 * alfa / h)) / 2;

	double eex = Math.exp(-q*dayYear);//descuento por dividendos

	double s1 = strike;
	double zz = 1 / Math.sqrt(2 * Math.PI);
	double zerror = 1;
	do
	{
                double d1=(Math.log(s1 / strike) + ((rate-q) + vlt2/2)*dayYear) / VltSqrDayYear;
                xx=(1-eex*new NormalDistribution().cumulativeProbability(cpFlag*d1)); 
                
		double corr = s1 / lambda*xx;

                QBlackScholes option=new QBlackScholes (tipoContrato,s1,volatModel,dividendRate, callPut,strike, daysToExpiration,rate,0);
                                                                
                double mBlackScholes = option.getPrima();
                double rhs = mBlackScholes + cpFlag*corr;

		double lhs = cpFlag*(s1 - strike);
		zerror = lhs - rhs;
                double nd1 = zz*Math.exp(-0.5*d1*d1); //standard normal prob?
		double slope = cpFlag*(1 - 1 / lambda)*xx + 1 / lambda*(eex*nd1) * 1 / VltSqrDayYear;
		s1 = s1 - zerror / slope;

	} while (Math.abs(zerror)>0.000001);

	double a = cpFlag*s1 / lambda*xx;
	
	switch (callPut)
	{
	case CALL: //Call
		if (underlyingValue >= s1){
		        prima = underlyingValue - strike;
		}else{
                        prima += a*Math.pow((underlyingValue / s1), lambda);
                }
		break;

	case PUT: //Put
		if (underlyingValue <= s1){
                        prima = strike - underlyingValue;
		}else{
                	prima += a*Math.pow((underlyingValue / s1), lambda);   
                }
               //prima=10.1;
		break;
	}
    }
    
    
     @Override
    protected double modelGetPrima(double volForLambda){
        return new QWhaley(tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,-1).getPrima();
    }
       
}
