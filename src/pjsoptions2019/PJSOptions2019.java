/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;

import java.util.Arrays;
import models2019.cBlackScholes2019;
import underlying.cUnderlying;

/**
 *
 * @author pseoane
 */
public class PJSOptions2019 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        char   contrato     ='S';
        char   option       ='P';
        double undValue     =100;
        double X            =100;
        double days         =365;
        
        
        double vh30Und      =.30;
        double viOption     =.30;
        double riskFreeRate =.10;
        double divYield     =0;
        double mktValue     =7.90;
        
        cUnderlying someStock   = new cUnderlying(contrato, undValue, vh30Und, divYield);
        cBlackScholes2019 bs    = new cBlackScholes2019(someStock, option, X,days,riskFreeRate,viOption,mktValue);
        
        double iv=bs.getImpliedVlt();
        cBlackScholes2019 bs1   = new cBlackScholes2019(contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,iv,mktValue);
        
        
        
        System.out.println("Model Name:"+ bs.getModelName());
        System.out.println("Prima bs  :" + Arrays.toString(bs.getDerivativesArray()[0]));
        System.out.println("Prima bs  :"+bs.getPrima());
        System.out.println("Prima mkt  :"+bs.getOptionMktValue());
        System.out.println("Prima IV :" + bs.getImpliedVlt());
       // bs1.setOptionVlt(iv);
        //bs1.runModel();
        System.out.println("Prima bs1 :" + Arrays.toString(bs1.getDerivativesArray()[0]));
        
        /*
        bs.setUnderlyingValue(102);
        bs.setStrike(105);
        bs.setCallPut('P');
        bs.setDaysToExp(29);
       
        
        bs.setOptionVlt(.31);
        bs.runModel();
        
        System.out.println("Days to exp :" + bs.getDaysToExpiration());
        System.out.println("Prima bs  :" + Arrays.toString(bs.getDerivativesArray()[0]));
         System.out.println("Prima bs  :"+bs.getPrima());
        System.out.println("Prima bs :" + bs.getOptionString());
        System.out.println("Prima IV :" + bs.getImpliedVlt());
        */
    }
    
}
