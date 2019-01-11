/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import underlying.Underlying;

/**
 *
 * @author pseoane
 */
public class cBlackScholes2019 extends Underlying{
    protected char callPut;
    protected double strike,daysToExpìration,tasa,impliedVol,optionMktValue;
    
    
    
    public cBlackScholes2019(){}
    public cBlackScholes2019(Underlying und, char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
      super(und);
      this.callPut              =callPut;  
      this.strike               =strike;
      this.daysToExpìration     =daysToExpiration;
      this.tasa                 =tasa;
      this.impliedVol           =impliedVol;
      this.optionMktValue       =optionMktValue;
      
      
        
        
    }
    public cBlackScholes2019 (char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
        this.callPut              =callPut;  
        this.strike               =strike;
        this.daysToExpìration     =daysToExpiration;
        this.tasa                 =tasa;
        this.impliedVol           =impliedVol;
        this.optionMktValue       =optionMktValue;
    
    
    }
    
    
}
