/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;

import com.qaant.optionModels.ImpliedVolCalc;
import java.util.function.DoubleUnaryOperator;
import org.apache.commons.math3.distribution.NormalDistribution;
import underlying.cUnderlying;

/**
 *
 * @author pseoane
 */
public class QBlackScholes extends QAbstractModel {
    
    public QBlackScholes(){super();}
    public QBlackScholes(cUnderlying und,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und, callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    public QBlackScholes(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate,callPut, strike, daysToExpiration, rate, optionMktValue);
    }
    
    
    @Override
    public void runModel(){
       
        
        pModelName="Black-Scholes QAANT";
        modelNumber=1;
        tipoEjercicio =EUROPEAN;
        
        double q=(tipoContrato==STOCK) ? dividendRate:rate; 
            //q: si es una accion q es el dividendo, si es un futuro q se toma la rate para descontar el valor futr a presente 
            //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
        
        double d1 = (Math.log(underlyingValue / strike) + dayYear*(rate-q + volatModel*volatModel / 2)) / (volatModel*sqrDayYear);
        double d2 = d1 - volatModel*sqrDayYear;
       
        double CNDFd1 =new NormalDistribution().cumulativeProbability(d1);
        double CNDFd2 =new NormalDistribution().cumulativeProbability(d2);
        double PDFd1  =new NormalDistribution().density(d1); 
        
        //gamma y vega son iguales para call y put
       gamma     =PDFd1 *z / (underlyingNPV*volatModel*sqrDayYear);
       vega      =underlyingNPV * sqrDayYear*PDFd1 / 100;
       
       switch (callPut)
            {
              
            case CALL: 
                prima = underlyingValue*Math.exp(-q*dayYear) * CNDFd1 - z * strike*CNDFd2;
		delta = Math.exp(-q*dayYear)*CNDFd1;
                theta   = (-(underlyingNPV*volatModel*PDFd1 / (2 * sqrDayYear)) - strike*rate*z*CNDFd2+dividendRate*underlyingNPV*CNDFd1) / 365;
		rho =   strike*dayYear*Math.exp(-(rate-q)*dayYear)*CNDFd2 / 100;
                break;

            case PUT: 
                double CNDF_d1=new NormalDistribution().cumulativeProbability(-d1);
                double CNDF_d2=new NormalDistribution().cumulativeProbability(-d2);
                
		prima = -underlyingValue*Math.exp(-q*dayYear) * CNDF_d1 + z * strike*CNDF_d2;
		delta = Math.exp(-q*dayYear)*(CNDFd1 - 1);
		theta = (-(underlyingNPV*volatModel*PDFd1 / (2 * sqrDayYear)) + strike*rate*z*CNDF_d2-dividendRate*underlyingNPV*CNDF_d1) / 365;
                rho = -strike*dayYear*Math.exp(-(rate-q)*dayYear)*CNDF_d2 / 100;
                break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
   
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new QBlackScholes(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
              
        }
    return impliedVol;
    }
}
