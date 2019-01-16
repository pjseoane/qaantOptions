/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;

import java.util.Arrays;
import models2019.*;
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
        double vh30Und      =0.3;
        double riskFreeRate =.10;
        double divYield     =0;
        double mktValue     =9.0;
        int steps           =200;
        
        cUnderlying someStock   = new cUnderlying(contrato, undValue, vh30Und, divYield);
        BlackScholes2019 bs    = new BlackScholes2019(someStock, option, X,days,riskFreeRate,mktValue);
        
        
        double iv=bs.getImpliedVlt();
        BlackScholes2019 bs1   = new BlackScholes2019(contrato, undValue, iv,divYield,option, X,days,riskFreeRate,mktValue);
        
        
        
        System.out.println("Model Name:"+ bs.getModelName());
        System.out.println("Prima bs  :" + Arrays.toString(bs.getDerivativesArray()[0]));
        System.out.println("Prima bs  :"+bs.getPrima());
        System.out.println("Prima mkt  :"+bs.getOptionMktValue());
        System.out.println("Prima IV :" + bs.getImpliedVlt());
        System.out.println("Prima IV2 :" + bs.getIV2());
       
        System.out.println("Prima bs1 :" + Arrays.toString(bs1.getDerivativesArray()[0]));
        
               
        Whaley2019 opW= new Whaley2019(contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue);
        System.out.println("Tst W:" + Arrays.toString(opW.getDerivativesArray()[0]));
        System.out.println("prima W "+opW.getPrima());
        System.out.println("Prima IV W: " + opW.getImpliedVlt());
        
        BinomialCRR2019 optJR=new BinomialCRR2019('E',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,0,steps);
        System.out.println("Tst CRR:" + Arrays.toString(optJR.getDerivativesArray()[0]));
        
        BinomialJarrowRudd optCRR = new BinomialJarrowRudd('E',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,0,steps);
        System.out.println("Tst JR:" + Arrays.toString(optCRR.getDerivativesArray()[0]));
        
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
