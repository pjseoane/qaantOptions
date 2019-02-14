/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 */
package com.qaant.optionModels;


import org.apache.commons.math3.distribution.NormalDistribution;
import com.qaant.structures.Qunderlying;

/**
 *
 * @author pseoane Modelo OK, chequeado valores aca: http://www.math.drexel.edu/~pg/fin/VanillaCalculator.html
 */
public class QBlackScholes extends QAbstractModel implements QOptionable{
    
    
    static {modelMap.put(1,"Black Scholes -QAANT");}
    
    public QBlackScholes(){super();}
    public QBlackScholes(Qunderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und, callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    public QBlackScholes(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    
    @Override
    //public void runModel(){
            public void run() {
       
        pModelName="Black-Scholes QAANT";
        modelNumber=1;
        tipoEjercicio =EUROPEAN;
        //
        
        double q=(tipoContrato==STOCK) ? dividendRate:rate; 
            //q: si es una accion q es el dividendo, si es un futuro q se toma la rate para descontar el valor futr a presente 
            //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
        
        //drift=Math.exp((q-rate)*dayYear);
        double drift=(tipoContrato=='F')? z: 1;
        double x = (tipoContrato=='F')? 1:0;
        
        double d1 = (Math.log(underlyingNPV / strike) + dayYear*(rate-q + volatModel*volatModel / 2)) / (volatModel*sqrDayYear);
        double d2 = d1 - volatModel*sqrDayYear;
       
        double CNDFd1 =new NormalDistribution().cumulativeProbability(d1);
        double CNDFd2 =new NormalDistribution().cumulativeProbability(d2);
        double PDFd1  =new NormalDistribution().density(d1); 
        
        //gamma y vega son iguales para call y put
       
       gamma     =PDFd1 *drift / (underlyingNPV*volatModel*sqrDayYear);
       vega      =underlyingNPV *drift* sqrDayYear*PDFd1 / 100;
       
       switch (callPut)
            {
              
            case CALL: 
                prima   = underlyingValue*Math.exp(-q*dayYear) * CNDFd1 - z * strike*CNDFd2;
		delta   = Math.exp(-q*dayYear)*CNDFd1;
                theta   = (-(underlyingNPV*drift*volatModel*PDFd1 / (2 * sqrDayYear)) - strike*1*rate*CNDFd2)/(365);
               
                rho     = z*dayYear*(strike*CNDFd2-x*underlyingNPV*CNDFd1) / 100;
                break;

            case PUT: 
                double CNDF_d1=new NormalDistribution().cumulativeProbability(-d1);
                double CNDF_d2=new NormalDistribution().cumulativeProbability(-d2);
                
		prima = -underlyingValue*Math.exp(-q*dayYear) * CNDF_d1 + z * strike*CNDF_d2;
		delta = Math.exp(-q*dayYear)*(CNDFd1 - 1);
		theta = (-(underlyingNPV*drift*volatModel*PDFd1 / (2 * sqrDayYear)) + strike*1*rate*CNDF_d2)/365;
                rho   = -z*dayYear*(strike*CNDF_d2-x*underlyingNPV*CNDF_d1) / 100;
                break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
   
    }
    
     @Override
    protected double modelGetPrima(double volForLambda){
        return new QBlackScholes(tipoContrato, underlyingValue, volForLambda,dividendRate, callPut, strike, daysToExpiration,rate,-1).getPrima();
    }
}
