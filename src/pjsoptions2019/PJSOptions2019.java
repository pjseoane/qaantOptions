/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;


import bookPosition.Qticket;
import com.qaant.optionModels.QAbstractModel;
import com.qaant.optionModels.QWhaley;
import com.qaant.optionModels.QBinomialJRudd;
import com.qaant.optionModels.QBinomialCV;
import com.qaant.optionModels.QBlackScholes;
import com.qaant.optionModels.QBinomialCRR;


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
        char   option       ='C';
        double undValue     =45400;
        double X            =48000;
        double days         =57;
        double vh30Und      =0.30;
        double riskFreeRate =.39;
        double divYield     =0;
        double mktValue     =1120;
        int steps           =1000;
        
        
        System.out.println("TEST EUROPEAN :\n");
        
        Qunderlying someStock   = new Qunderlying(contrato, undValue, vh30Und, divYield);
        
       
        QBlackScholes       op1 =new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        QBinomialJRudd  opJReur =new QBinomialJRudd('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QBinomialCRR    opCRReur=new QBinomialCRR('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
       
        
        System.out.println("Black Scholes -QAANT  :" + Arrays.toString(op1.getDerivativesArray()[0])+"Implied VLT.."+op1.getImpliedVlt());
        System.out.println("Binomial EUR JR -QAANT:" + Arrays.toString(opJReur.getDerivativesArray()[0])+"Implied VLT.."+opJReur.getImpliedVlt());
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
        
        opJRamer.setOptionUndValue(undValue*.99);
       
        System.out.println ("CRR Caida 1%...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        
        opJRamer.setOptionUndValue(undValue*1.01);
        System.out.println ("CRR Suba 1%...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        System.out.println ("und value...:"+ opJRamer.getUnderlyingValue());
        
        opJRamer.setDaysToExpiration(0);
       
        System.out.println ("Days to zero JR...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        System.out.println (QAbstractModel.modelChooser());
        
        opJRamer.setDaysToExpiration(365);
        opJRamer.setVolatModel(0.31180124331295156);
        System.out.println ("Chg Volat...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        
        opJRamer.setUnderlyingValue(98.40);
        System.out.println ("Intrinsic Value...:"+ opJRamer.getIntrinsicValue());
        System.out.println ("Time      Value...:"+ opJRamer.getTimeValue());
        System.out.println ("Hash Map Models...:"+ QAbstractModel.modelMap);
        System.out.println ("Hash Map Get (4)...:"+ QAbstractModel.modelMap.get(4));
        
         System.out.println ("******************************* TICKETS:\n");
         
         QWhaley opW3=new QWhaley (someStock, 'C', X,days,riskFreeRate,mktValue);
         QWhaley opW4=new QWhaley (someStock,'C',46000,days,riskFreeRate,0);
         Qticket ticket1 = new Qticket(opW3,-6,700,1);
         Qticket ticket2 = new Qticket(opW4,5,1000,1);
         System.out.println ("Price Range op1...:"+ Arrays.toString(ticket1.getPriceRange()[0]));
         System.out.println ("PLOutput op1......:"+ Arrays.toString(ticket1.getPLOutput()[0]));
         System.out.println ("PLOutput op2......:"+ Arrays.toString(ticket2.getPLOutput()[0]));
        
        /*
        System.out.println ("**************************************************************************************");
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
       
        Qoption     optW1       =new  Qoption(someStock, option,X,days,riskFreeRate,mktValue);
        QWhaleyV2   optW3       =new  QWhaleyV2(someStock, option,X,days,riskFreeRate,mktValue);
        QWhaleyV2   optW4       =new  QWhaleyV2(optW1);
        QWhaleyV2   optW5       =new  QWhaleyV2(contrato,undValue, vh30Und, divYield,option,X,days,riskFreeRate,mktValue);
   
        System.out.println ("optW3 ...:"+ Arrays.toString(optW3.getDerivativesArray()[0]));
        System.out.println ("optW4 ...:"+ Arrays.toString(optW4.getDerivativesArray()[0])); 
        System.out.println ("optW5 ...:"+ Arrays.toString(optW5.getDerivativesArray()[0])); 
        
        optW3.setDaysToExpiration(0);
        optW4.setOptionMktValue(12);
        optW5.setRiskFreeRate(.30);
        optW3.runModel();
        optW4.runModel();
        optW5.runModel();
        
        System.out.println ("optW3 ...:"+ Arrays.toString(optW3.getDerivativesArray()[0]));
        System.out.println ("optW4 ...:"+ Arrays.toString(optW4.getDerivativesArray()[0])); 
        System.out.println ("optW5 ...:"+ Arrays.toString(optW5.getDerivativesArray()[0])); 
        */
    }
}