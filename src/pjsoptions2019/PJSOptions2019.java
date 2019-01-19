/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;


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
        
       
        QBlackScholes op1=new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        System.out.println("Balck Scholes -QAANT  :" + Arrays.toString(op1.getDerivativesArray()[0])+"Prima.."+op1.getPrima());
        
        QBinomialJRudd opJReur=new QBinomialJRudd('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial EUR JR -QAANT:" + Arrays.toString(opJReur.getDerivativesArray()[0])+"Prima.."+opJReur.getPrima());
        
        QBinomialCRR opCRReur= new QBinomialCRR('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial EUR CRR-QAANT:" + Arrays.toString(opCRReur.getDerivativesArray()[0])+"Prima.."+opCRReur.getPrima());
        
        System.out.println("\nTEST AMERICAN :\n");
        QBinomialJRudd opJRamer=new QBinomialJRudd('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Prima.."+opJRamer.getPrima());
        
        QBinomialCRR opCRRamer= new QBinomialCRR('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Prima.."+opCRRamer.getPrima());
        
        QBinomialCV opCVamer=new QBinomialCV(someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Prima.."+opCVamer.getPrima());
        
        QWhaley opW2=new QWhaley (someStock, option, X,days,riskFreeRate,mktValue);
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Prima.."+opW2.getPrima());
        
        System.out.println("\nTEST Put/Call Parity :\n");
        
        opJRamer=new QBinomialJRudd('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Prima.."+opJRamer.getPrima());
        opJRamer=new QBinomialJRudd('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Prima.."+opJRamer.getPrima());
        
        System.out.println("\n");
        opCRRamer= new QBinomialCRR('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Prima.."+opCRRamer.getPrima());
        opCRRamer= new QBinomialCRR('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Prima.."+opCRRamer.getPrima());
        
        System.out.println("\n");
        opCVamer=new QBinomialCV(someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Prima.."+opCVamer.getPrima());
        opCVamer=new QBinomialCV(someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Prima.."+opCVamer.getPrima());
        
        System.out.println("\n");
        opW2=new QWhaley (someStock, 'C', X,days,riskFreeRate,mktValue);
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Prima.."+opW2.getPrima());
        opW2=new QWhaley (someStock, 'P', X,days,riskFreeRate,mktValue);
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Prima.."+opW2.getPrima());
       
        
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
