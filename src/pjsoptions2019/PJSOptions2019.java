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
import com.qaant.optionModels.QEFHull;
import com.qaant.optionModels.QEFWilmott;


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
        
       
        char   contrato     ='S';
        char   option       ='P';
        double undValue     =100;
        double X            =100;
        double days         =30;
        double vh30Und      =0.30;
        double riskFreeRate =.10;
        double divYield     =0;
        double mktValue     =4;
        int steps           =1000;
        
        
        System.out.println("TEST EUROPEAN :\n");
        
        Qunderlying someStock   = new Qunderlying(contrato, undValue, vh30Und, divYield);
        
       
        QBlackScholes       op1 =new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        QBinomialJRudd  opJReur =new QBinomialJRudd('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QBinomialCRR    opCRReur=new QBinomialCRR('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QEFHull         opEFHeur=new QEFHull ('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QEFWilmott      opEFWeur=new QEFWilmott ('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        
        System.out.println("Black Scholes -QAANT  :" + Arrays.toString(op1.getDerivativesArray()[0])+"Implied VLT.."+op1.getImpliedVlt());
        System.out.println("Binomial EUR JR -QAANT:" + Arrays.toString(opJReur.getDerivativesArray()[0])+"Implied VLT.."+opJReur.getImpliedVlt());
        System.out.println("Binomial EUR CRR-QAANT:" + Arrays.toString(opCRReur.getDerivativesArray()[0])+"Implied VLT.."+opCRReur.getImpliedVlt());
        System.out.println("EF Hulll EUR CRR-QAANT:" + Arrays.toString(opEFHeur.getDerivativesArray()[0])+"Implied VLT.."+opEFHeur.getImpliedVlt());
        System.out.println("EF Wilmott EUR CRR-QAANT:" + Arrays.toString(opEFWeur.getDerivativesArray()[0])+"Implied VLT.."+opEFWeur.getImpliedVlt());
        
        
        System.out.println("\nTEST AMERICAN :\n");
        
        QBinomialJRudd opJRamer     =new QBinomialJRudd('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QBinomialCRR opCRRamer      =new QBinomialCRR('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QBinomialCV opCVamer        =new QBinomialCV(someStock,option, X,days,riskFreeRate,mktValue,steps);
        QWhaley opW2                =new QWhaley (someStock, option, X,days,riskFreeRate,mktValue);
        QEFHull         opEFHamer   =new QEFHull ('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QEFWilmott      opEFWamer   =new QEFWilmott ('A',someStock,option, X,days,riskFreeRate,mktValue,steps);
        
        
        System.out.println("Binomial AMER JR-QAANT:" + Arrays.toString(opJRamer.getDerivativesArray()[0])+"Implied VLT.."+opJRamer.getImpliedVlt());
        System.out.println("Binomial AMER CRR-QAANT:" + Arrays.toString(opCRRamer.getDerivativesArray()[0])+"Implied VLT.."+opCRRamer.getImpliedVlt());
        System.out.println("Binomial AMER CV-QAANT:" + Arrays.toString(opCVamer.getDerivativesArray()[0])+"Implied VLT.."+opCVamer.getImpliedVlt());
        System.out.println("Whaley -QAANT  :" + Arrays.toString(opW2.getDerivativesArray()[0])+"Implied VLT.."+opW2.getImpliedVlt());
        System.out.println("EF Hull -QAANT  :" + Arrays.toString(opEFHamer.getDerivativesArray()[0])+"Implied VLT.."+opW2.getImpliedVlt());
        System.out.println("EF Wilmott -QAANT  :" + Arrays.toString(opEFWamer.getDerivativesArray()[0])+"Implied VLT.."+opW2.getImpliedVlt());
        
        
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
        
        opEFHamer=new QEFHull ('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("EF Hull -QAANT  :" + Arrays.toString(opEFHamer.getDerivativesArray()[0])+"Implied VLT.."+opEFHamer.getImpliedVlt());
        
        opEFHamer=new QEFHull ('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("EF Hull -QAANT  :" + Arrays.toString(opEFHamer.getDerivativesArray()[0])+"Implied VLT.."+opEFHamer.getImpliedVlt());
        
        
        opEFWamer=new QEFWilmott ('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        System.out.println("EF Wilmott -QAANT  :" + Arrays.toString(opEFWamer.getDerivativesArray()[0])+"Implied VLT.."+opEFWamer.getImpliedVlt());
        
        opEFWamer=new QEFWilmott ('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        System.out.println("EF Wilmott -QAANT  :" + Arrays.toString(opEFWamer.getDerivativesArray()[0])+"Implied VLT.."+opEFWamer.getImpliedVlt());
        
        
        
        //********************************************************************
        System.out.println ("\nCRR Prima...:"+ opJRamer.getPrima());
        
        opJRamer.setOptionUndValue(undValue*.99);
       
        System.out.println ("CRR Caida 1%...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        
        opJRamer.setOptionUndValue(undValue*1.01);
        System.out.println ("CRR Suba 1%...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        System.out.println ("und value...:"+ opJRamer.getUnderlyingValue());
        
        opJRamer.setDaysToExpiration(0);
        opJRamer.runModel();
        
        System.out.println ("Days to zero JR...:"+opJRamer.getDaysToExpiration()+" "+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        System.out.println (QAbstractModel.modelChooser());
        
        opJRamer.setDaysToExpiration(365);
        opJRamer.setVolatModel(0.5);
        System.out.println ("Chg Volat...:"+ Arrays.toString(opJRamer.getDerivativesArray()[0]));
        
        opJRamer.setUnderlyingValue(98.40);
        System.out.println ("Intrinsic Value...:"+ opJRamer.getIntrinsicValue());
        System.out.println ("Time      Value...:"+ opJRamer.getTimeValue());
        System.out.println ("Hash Map Models...:"+ QAbstractModel.modelMap);
        System.out.println ("Hash Map Get (4)...:"+ QAbstractModel.modelMap.get(4));
        
        System.out.println ("******************************* TICKETS:\n");
        //System.out.println ("Price Range....:"+Arrays.toString(opW2.getUnderlyingPriceRange()[0]));
        //opW2.setNumberOfNodes(6);
        //System.out.println ("Price Range....:"+Arrays.toString(opW2.getUnderlyingPriceRange()[0]));
        
        contrato     ='F';
        option       ='C';
        undValue     =45400;
        X            =0;
        days         =56;
        vh30Und      =0.30;
        riskFreeRate =.39;
        divYield     =0;
        mktValue     =41000;
     //   steps        =1000;
        
        Qunderlying RFX20Mar    = new Qunderlying(contrato, undValue, vh30Und, divYield);
        QWhaley       opw       = new QWhaley (RFX20Mar, option, X,days,riskFreeRate,mktValue);
        
        Qticket     ticket1     = new Qticket (opw,1,mktValue,1,10);// +/-lots,price,multiplier,nodes
        
        System.out.println ("Price Range....:"+Arrays.toString(ticket1.getUnderlyingPriceRange()[0]));
        System.out.println ("PL Output.....:"+Arrays.toString(ticket1.getPLOutput()[0]));
        
    }
}
