/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import static models2019.cBlackScholes2019.CALL;
import static models2019.cBlackScholes2019.PUT;
import org.apache.commons.math3.distribution.NormalDistribution;
import static underlying.cUnderlying.STOCK;

/**
 *
 * @author pauli
 */
public class cTestWhaley extends cBlackScholes2019 implements Optionable{
   
    
    public cTestWhaley(){}
    
    public cTestWhaley(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato,underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike,daysToExpiration, rate, optionMktValue);
        //aqui corre runModel() de este modelo si esta definido sino corre el BS
        //luego corre fillderivativesArray() de BS
        runWhaley();
    }
    private void runWhaley(){
     if(tipoContrato=='F' || callPut=='P') {
         System.out.println("Salio por wWhaley:"+callPut);
         wWhaley();
            
        }
    }    
    
    
    private void wWhaley(){
        double zz;
        double drift=(tipoContrato==STOCK) ? (rate-dividendRate):-rate; 
        double vlt2 = volatModel*volatModel;
        
        double d1 = (Math.log(underlyingValue / strike) + ((rate-q) + vlt2 / 2)*dayYear) / (volatModel*sqrDayYear);
        double d2 = d1 - (volatModel*sqrDayYear);
       
        double VltSqrDayYear = volatModel*sqrDayYear;
	double h = 1 - Math.exp(-rate*dayYear); //descuento para valor presente
	double alfa = 2 * rate / vlt2;
	double beta = 2 * drift / vlt2;

	double lambda = (-(beta - 1) + cpFlag*Math.sqrt((beta - 1)*(beta-1) + 4 * alfa / h)) / 2;

	double eex = Math.exp(-q*dayYear);//descuento por dividendos

	double s1 = strike;
	zz = 1 / Math.sqrt(2 * Math.PI);
	double zerror = 1;
	double xx;
        do
	{
		d1=(Math.log(s1 / strike) + ((rate-q) + vlt2/2)*dayYear) / VltSqrDayYear;
                
                //NormalDistribution nD=new NormalDistribution();
                //xx = (1 - eex*nD.cumulativeProbability(cpFlag*d1));
		xx =(1-eex*new NormalDistribution().cumulativeProbability(cpFlag*d1));
                
                double corr = s1 / lambda*xx;

             //   Underlying Und1=new Underlying(tipoContrato,s1,histVolatility,dividendRate); 
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
               //prima=9;
		break;
	}
        
    }
     
    
        
    
    /*
    @Override
    public void runModel(){
        pModelName                      ="Whaley-Barone-Adesi.ver2019";
        modelNumber                     =2;
        tipoEjercicio                   ='A';
    
        
    }
  */
}
