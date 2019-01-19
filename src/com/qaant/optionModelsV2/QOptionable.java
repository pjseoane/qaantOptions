/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;



/**
 *
 * @author Paulino qOOnt
 */
public interface QOptionable {
    /*
    public final static int PRIMA=0,DELTA=1,GAMMA=2,VEGA=3,THETA=4,RHO=5,IV=6;
    public final static char CALL       ='C';
    public final static char PUT        ='P';
    public final static char EUROPEAN   ='E';
    public final static char AMERICAN   ='A';
    */
    
    void runModel();
    void fillDerivativesArray();
    String getModelName();
    double getPrima();
    double getDelta();
    double getGamma();
    double getVega();
    double getTheta();
    double getRho();
   // double getImpliedVlt();

}
