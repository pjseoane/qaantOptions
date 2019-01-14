/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author pseoane
 */
public class cWhaley2019 extends cBlackScholes2019 implements Optionable{
    private double drift,vlt2,VltSqrDayYear,h,alfa,beta,lambda,eex,s1,zerror,xx,corr,mBlackScholes,rhs,lhs,nd1,slope,a,vv;

    public cWhaley2019(){}
    public cWhaley2019(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato,underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike,daysToExpiration, rate, optionMktValue);
               
        buildWBA();
    
        }
        
        private void buildWBA(){
        
        runModel();
    }   
        
        
        
    
   
     public void opcionConVida(){
        /*
        cBlackScholes2019 BSOption=new cBlackScholes2019(tipoContrato, underlyingValue,histVolatility, dividendRate, callPut, strike, daysToExpiration, rate, 0);
        prima=BSOption.getPrima();
        
        this.multCallPut=BSOption.multCallPut;
        */
        prima=super.prima;
        delta=4;
        
        if(tipoContrato=='F' || callPut=='P') {
            wWhaley();
        }
       
        //greeks se devuelven de BScholes
        /*
        delta   = BSOption.getDelta();
	gamma   = BSOption.getGamma();
	vega    = BSOption.getVega();
	theta   = BSOption.getTheta();
	rho     = BSOption.getRho();
        */
    }
 private void wWhaley(){
        double zz;
        //dayYear=daysToExpiration/365;
        //sqrDayYear = Math.sqrt(dayYear);
        //underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        
        drift=(tipoContrato==STOCK) ? (rate-dividendRate):-rate; 
        
        /*
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
        */
        //zz=Math.exp((b-tasa))*dayYear; //para STOCK queda 1, 
        vlt2 = volatModel*volatModel;
        
        double d1 = (Math.log(underlyingValue / strike) + ((rate-q) + vlt2 / 2)*dayYear) / (volatModel*sqrDayYear);
        double d2 = d1 - (volatModel*sqrDayYear);
       
        VltSqrDayYear = volatModel*sqrDayYear;
	h = 1 - Math.exp(-rate*dayYear); //descuento para valor presente
	alfa = 2 * rate / vlt2;
	beta = 2 * drift / vlt2;

	lambda = (-(beta - 1) + cpFlag*Math.sqrt((beta - 1)*(beta-1) + 4 * alfa / h)) / 2;

	eex = Math.exp(-q*dayYear);//descuento por dividendos

	s1 = strike;
	zz = 1 / Math.sqrt(2 * Math.PI);
	zerror = 1;
	do
	{
		d1=(Math.log(s1 / strike) + ((rate-q) + vlt2/2)*dayYear) / VltSqrDayYear;
                
                //NormalDistribution nD=new NormalDistribution();
                //xx = (1 - eex*nD.cumulativeProbability(cpFlag*d1));
		xx=(1-eex*new NormalDistribution().cumulativeProbability(cpFlag*d1));
                
                corr = s1 / lambda*xx;

             //   Underlying Und1=new Underlying(tipoContrato,s1,histVolatility,dividendRate); 
               	cBlackScholes2019 option=new cBlackScholes2019 (tipoContrato,s1,volatModel,dividendRate, callPut,strike, daysToExpiration,rate,0);
                                                                
                mBlackScholes = option.getPrima();
                rhs = mBlackScholes + cpFlag*corr;

		lhs = cpFlag*(s1 - strike);
		zerror = lhs - rhs;
                nd1 = zz*Math.exp(-0.5*d1*d1); //standard normal prob?
		slope = cpFlag*(1 - 1 / lambda)*xx + 1 / lambda*(eex*nd1) * 1 / VltSqrDayYear;
		s1 = s1 - zerror / slope;

	} while (Math.abs(zerror)>0.000001);

	a = cpFlag*s1 / lambda*xx;
	
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
		break;
	}
    }
 }        
         

