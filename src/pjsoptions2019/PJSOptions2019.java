/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;


import com.qaant.optionModels.QAbstractModel;
import com.qaant.optionModels.QWhaley;
import com.qaant.optionModels.QBinomialJRudd;
import com.qaant.optionModels.QBinomialCV;
import com.qaant.optionModels.QBlackScholes;
import com.qaant.optionModels.QBinomialCRR;
import com.qaant.optionModelsV2.QBlackScholesV2;
import com.qaant.structures.Qoption;

import java.util.Arrays;
import com.qaant.structures.Qunderlying;
//import java.util.function.Consumer;
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
        
        Qunderlying someStock   = new Qunderlying(contrato, undValue, vh30Und, divYield);
        /*
       
        QBlackScholes op1=new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        System.out.println("Balck Scholes -QAANT  :" + Arrays.toString(op1.getDerivativesArray()[0])+"Implied VLT.."+op1.getImpliedVlt());
        
        QBinomialJRudd opJReur=new QBinomialJRudd('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial EUR JR -QAANT:" + Arrays.toString(opJReur.getDerivativesArray()[0])+"Implied VLT.."+opJReur.getImpliedVlt());
        
        QBinomialCRR opCRReur= new QBinomialCRR('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial EUR CRR-QAANT:" + Arrays.toString(opCRReur.getDerivativesArray()[0])+"Implied VLT.."+opCRReur.getImpliedVlt());
        
        System.out.println("\nTEST AMERICAN :\n");
        QBinomialJRudd opJRamer=new QBinomialJRudd('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Implied VLT.."+opJRamer.getImpliedVlt());
        
        QBinomialCRR opCRRamer= new QBinomialCRR('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Implied VLT.."+opCRRamer.getImpliedVlt());
        
        QBinomialCV opCVamer=new QBinomialCV(someStock,option, X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Implied VLT.."+opCVamer.getImpliedVlt());
        
        QWhaley opW2=new QWhaley (someStock, option, X,days,riskFreeRate,mktValue);
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Implied VLT.."+opW2.getImpliedVlt());
        
        System.out.println("\nTEST Put/Call Parity :\n");
        
        opJRamer=new QBinomialJRudd('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Implied VLT.."+opJRamer.getImpliedVlt());
        opJRamer=new QBinomialJRudd('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Implied VLT.."+opJRamer.getImpliedVlt());
        
        System.out.println("\n");
        opCRRamer= new QBinomialCRR('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Implied VLT.."+opCRRamer.getImpliedVlt());
        opCRRamer= new QBinomialCRR('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Implied VLT.."+opCRRamer.getImpliedVlt());
        
        System.out.println("\n");
        opCVamer=new QBinomialCV(someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Implied VLT.."+opCVamer.getImpliedVlt());
        opCVamer=new QBinomialCV(someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Implied VLT.."+opCVamer.getImpliedVlt());
        
        System.out.println("\n");
        opW2=new QWhaley (someStock, 'C', X,days,riskFreeRate,mktValue);
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Implied VLT.."+opW2.getImpliedVlt());
        opW2=new QWhaley (someStock, 'P', X,days,riskFreeRate,mktValue);
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Implied VLT.."+opW2.getImpliedVlt());
        
        
        //********************************************************************
        System.out.println ("\nCRR Prima...:"+ opJRamer.getPrima());
        
        opJRamer.setUnderlyingValue(undValue*.99);
        opJRamer.runModel();
        System.out.println ("CRR Caida 1%...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        
        opJRamer.setUnderlyingValue(undValue*1.01);
        opJRamer.runModel();
        System.out.println ("CRR Suba 1%...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        System.out.println ("und value...:"+ opJRamer.getUnderlyingValue());
        
        opJRamer.setDaysToExpiration(0);
        opJRamer.runModel();
        System.out.println ("Days to zero...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        System.out.println (QAbstractModel.modelChooser());
        */
        
        option='P';
        Qunderlying someStock1  = new Qunderlying(contrato,undValue, vh30Und, divYield);
        Qoption     opt1        = new Qoption(someStock, option,X,days,riskFreeRate,mktValue);
        Qoption     opt2        = new Qoption(contrato,undValue+1.3, vh30Und, divYield,option,X,days,riskFreeRate,mktValue);
        
        
        System.out.println ("------------------------\n tipoContrato "+opt1.getTipoContrato());
        System.out.println ("------------------------\nPayoff ..."+opt2.getPayoff());
        QBlackScholesV2 opt3   =new QBlackScholesV2(someStock, option,X,days,riskFreeRate,mktValue);
        QBlackScholesV2 opt4   =new QBlackScholesV2(opt1);
        QBlackScholesV2 opt5   =new QBlackScholesV2(contrato,undValue, vh30Und, divYield,option,X,days,riskFreeRate,mktValue);
          
        System.out.println ("opt3 ...:"+ Arrays.toString(opt3.getDerivativesArray()[0]));
        System.out.println ("opt4 ...:"+ Arrays.toString(opt4.getDerivativesArray()[0]));
        System.out.println ("opt5 ...:"+ Arrays.toString(opt5.getDerivativesArray()[0]));
        
        opt3.setDaysToExpiration(0);
        opt4.setOptionMktValue(12);
        opt5.setRiskFreeRate(.30);
        opt3.runModel();
        opt4.runModel();
        opt5.runModel();
        
        System.out.println ("opt3 ...:"+ Arrays.toString(opt3.getDerivativesArray()[0]));
        System.out.println ("opt4 ...:"+ Arrays.toString(opt4.getDerivativesArray()[0]));
        System.out.println ("opt5 ...:"+ Arrays.toString(opt5.getDerivativesArray()[0]));
       
        
        
        
        
        
        
        
        
    }
}