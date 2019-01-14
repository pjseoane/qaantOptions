/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import java.util.function.DoubleUnaryOperator;
import static models2019.cBlackScholes2019.CALL;
import static models2019.cBlackScholes2019.PUT;
import org.apache.commons.math3.distribution.NormalDistribution;
import static underlying.cUnderlying.STOCK;

/**
 *
 * @author pauli
 */
public class cWhaley2019 extends cBlackScholes2019 implements Optionable{
    
    
    public cWhaley2019(){}
    
    public cWhaley2019(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato,underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike,daysToExpiration, rate, optionMktValue);
        //aqui corre runModel() de este modelo si esta definido sino corre el BS
        
    }
    @Override
    public void runModel(){
    //recalcula un modelo BS para obtener las greeks por BS
    cBlackScholes2019 optW= new cBlackScholes2019(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike, daysToExpiration, rate, 0);
        
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
    
       
    public void wWhaley(){
        pModelName="Black-Scholes ver2019";
        modelNumber=2;
        tipoEjercicio ='A';
        
        double zz;
        double b=0;
        double xx;
        //Los siguientes valores vienen heredados de BS
        //dayYear=daysToExpiration/365;
        //sqrDayYear = Math.sqrt(dayYear);
        //underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        
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
	double h = 1 - Math.exp(-rate*dayYear); //descuento para valor presente
	double alfa = 2 * rate / vlt2;
	double beta = 2 * (b-q) / vlt2;

	double lambda = (-(beta - 1) + cpFlag*Math.sqrt((beta - 1)*(beta-1) + 4 * alfa / h)) / 2;

	double eex = Math.exp(-q*dayYear);//descuento por dividendos

	double s1 = strike;
	zz = 1 / Math.sqrt(2 * Math.PI);
	double zerror = 1;
	do
	{
		double d1=(Math.log(s1 / strike) + ((rate-q) + vlt2/2)*dayYear) / VltSqrDayYear;
                
               
                xx=(1-eex*new NormalDistribution().cumulativeProbability(cpFlag*d1)); 
                
		double corr = s1 / lambda*xx;

                cBlackScholes2019 option=new cBlackScholes2019 (tipoContrato,s1,volatModel,dividendRate, callPut,strike, daysToExpiration,rate,0);
                                                                
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new cWhaley2019(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
        //impliedVol=.4444;
              
    }
    return impliedVol;
    }
}
     
    
        
    
   

