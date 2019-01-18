/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

/**
 *
 * @author pseoane
 */
public class BinomialControlVariate2019 extends BinomialJarrowRudd implements Optionable{
    /*
Solo Para American
Modelo Con tecnica de estabilizacioon Control Variate
binomial american+bseuropean-binomialeuropean.
Hull pag 333 y 351
*/
    public BinomialControlVariate2019(){}
    public BinomialControlVariate2019(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
    
    }
    
    
}
