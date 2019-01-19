/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;

import com.qaant.optionModels.Whaley2019;
import com.qaant.optionModels.BlackScholes2019;
import com.qaant.optionModels.BinomialJarrowRudd;
import com.qaant.optionModels.BinomialCRR2019;
import java.util.Arrays;
import underlying.cUnderlying;
import com.qaant.optionModelsV2.*;

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
        
        /*
        Alguna conclusiones:
        American 
        Futuro y Call conviene JR (itera mas rapido el calculo de IV) 
        Futuro y Put idem
        */
        
        char   contrato     ='F';
        char   option       ='P';
        double undValue     =100;
        double X            =100;
        double days         =365;
        double vh30Und      =0.30;
        double riskFreeRate =.10;
        double divYield     =0;
        double mktValue     =11.1;
        int steps           =1000;
        
        
        System.out.println("TEST EUROPEAN :\n");
        
        cUnderlying someStock   = new cUnderlying(contrato, undValue, vh30Und, divYield);
        BlackScholes2019 bs    = new BlackScholes2019(someStock, option, X,days,riskFreeRate,mktValue);
        System.out.println("Balck Scholes bs  :" + Arrays.toString(bs.getDerivativesArray()[0]));
        
        BinomialCRR2019 optCRR=new BinomialCRR2019('E',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial CRR      :" + Arrays.toString(optCRR.getDerivativesArray()[0]));
        
        BinomialJarrowRudd optJR = new BinomialJarrowRudd('E',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial  JR      :" + Arrays.toString(optJR.getDerivativesArray()[0]));
        
        System.out.println("\nTEST AMERICAN :\n");
        Whaley2019 opW= new Whaley2019(contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue);
        System.out.println("Whaley            :" + Arrays.toString(opW.getDerivativesArray()[0]));
        BinomialCRR2019 optCRRa=new BinomialCRR2019('A',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial CRR      :" + Arrays.toString(optCRRa.getDerivativesArray()[0]));
        
        BinomialJarrowRudd optJRa = new BinomialJarrowRudd('A',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial  JR      :" + Arrays.toString(optJRa.getDerivativesArray()[0]));
        
        System.out.println("\nTEST QAANT Models :\n");
        QBlackScholes op1=new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        System.out.println("Balck Scholes -QAANT  :" + Arrays.toString(op1.getDerivativesArray()[0])+"Prima.."+op1.getPrima());
        
        QBinomialJarrowRudd opEUR=new QBinomialJarrowRudd('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial EUR JR -QAANT:" + Arrays.toString(opEUR.getDerivativesArray()[0])+"Prima.."+opEUR.getPrima());
        
        QBinomialJarrowRudd opAMER=new QBinomialJarrowRudd('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opAMER.getDerivativesArray()[0])+"Prima.."+opAMER.getPrima());
        
        QBinomialControlVariate opAMER2=new QBinomialControlVariate(someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opAMER2.getDerivativesArray()[0])+"Prima.."+opAMER2.getPrima());
        
        
        /*
        double iv=bs.getImpliedVlt();
        BlackScholes2019 bs1   = new BlackScholes2019(contrato, undValue, iv,divYield,option, X,days,riskFreeRate,mktValue);
        
        
        
        System.out.println("Model Name:"+ bs.getModelName());
        
        System.out.println("Prima bs  :"+bs.getPrima());
        System.out.println("Prima mkt  :"+bs.getOptionMktValue());
        System.out.println("Prima IV :" + bs.getImpliedVlt());
        System.out.println("Prima IV2 :" + bs.getIV2());
       
        System.out.println("Prima bs1 :" + Arrays.toString(bs1.getDerivativesArray()[0]));
        
               
        Whaley2019 opW= new Whaley2019(contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue);
        System.out.println("Tst W:" + Arrays.toString(opW.getDerivativesArray()[0]));
        System.out.println("prima W "+opW.getPrima());
        System.out.println("Prima IV W: " + opW.getImpliedVlt());
        
        BinomialCRR2019 optCRR1=new BinomialCRR2019('E',contrato, undValue, vh30Und,divYield,'C', X,days,riskFreeRate,mktValue,500);
        System.out.println("Tst CRR:" + Arrays.toString(optCRR.getDerivativesArray()[0]));
        
        BinomialCRR2019 optCRR2=new BinomialCRR2019('E',contrato, undValue, vh30Und,divYield,'P', X,days,riskFreeRate,mktValue,500);
        System.out.println("Tst CRR:" + Arrays.toString(optCRR2.getDerivativesArray()[0]));
        
        BinomialJarrowRudd optJR = new BinomialJarrowRudd('E',contrato, undValue, vh30Und,divYield,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Tst JR:" + Arrays.toString(optJR.getDerivativesArray()[0]));
        
       System.out.println("\nJR Put Call Parity European Futures:\n");
       optJR = new BinomialJarrowRudd('E',contrato, undValue, vh30Und,divYield,'C', X,days,riskFreeRate,0,steps);
       System.out.println("Call JR:\n" + Arrays.toString(optJR.getDerivativesArray()[0]));
       optJR = new BinomialJarrowRudd('E',contrato, undValue, vh30Und,divYield,'P', X,days,riskFreeRate,0,steps);
       System.out.println("Put JR:\n" + Arrays.toString(optJR.getDerivativesArray()[0]));
       
       System.out.println("\nJR Put Call Parity American Futures:\n");
       optJR = new BinomialJarrowRudd('A',contrato, undValue, vh30Und,divYield,'C', X,days,riskFreeRate,0,steps);
       System.out.println("Call JR:\n" + Arrays.toString(optJR.getDerivativesArray()[0]));
       optJR = new BinomialJarrowRudd('A',contrato, undValue, vh30Und,divYield,'P', X,days,riskFreeRate,0,steps);
       System.out.println("Put JR:\n" + Arrays.toString(optJR.getDerivativesArray()[0]));
       
       
       
        
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
