/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelsV2;

/**
 *
 * @author pseoane
 */
public interface OptionModel {
    public final static int PRIMA=0,DELTA=1,GAMMA=2,VEGA=3,THETA=4,RHO=5,IV=6;
    public final static char CALL       ='C';
    public final static char PUT        ='P';
    public final static char EUROPEAN   ='E';
    public final static char AMERICAN   ='A';
    public final static char STOCK      ='S';
    public final static char FUTURE     ='F';
    
}
