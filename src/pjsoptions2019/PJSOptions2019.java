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
import com.qaant.optionModels.QOptionable;


import java.util.Arrays;
import com.qaant.structures.Qunderlying;
import com.qaant.threadModels.TBinomialCRR;
import com.qaant.threadModels.TBinomialCV;
import com.qaant.threadModels.TBinomialJR;
import com.qaant.threadModels.TBlackScholes;
import com.qaant.threadModels.TGenericModel;
import com.qaant.threadModels.TWhaley;
import java.util.ArrayList;
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
        
       
        char   contrato     ='F';
        char   option       ='P';
        double undValue     =100;
        double X            =100;
        double days         =10;
        double vh30Und      =0.30;
        double riskFreeRate =.10;
        double divYield     =0;
        double mktValue     =3;
        int steps           =1000;
        
        
        System.out.println("TEST EUROPEAN :\n");
        
        Qunderlying someStock   = new Qunderlying(contrato, undValue, vh30Und, divYield);
        
       
        
        /*
        //QBlackScholes       op1 =new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        QAbstractModel       op1 =new QBlackScholes (someStock, option, X,days,riskFreeRate,mktValue);
        QAbstractModel   opJReur =new QBinomialJRudd('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QAbstractModel   opCRReur=new QBinomialCRR('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QAbstractModel   opEFHeur=new QEFHull ('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        QAbstractModel   opEFWeur=new QEFWilmott ('E',someStock,option, X,days,riskFreeRate,mktValue,steps);
        
       
         
         
        
        
       // Thread t1   =new Thread(op1);
        //t1.start();
        
        
        System.out.println("Black Scholes -QAANT  :" + Arrays.toString(op1.getDerivativesArray()[0])+"Implied VLT.."+op1.getImpliedVlt());
        System.out.println("Binomial EUR JR -QAANT:" + Arrays.toString(opJReur.getDerivativesArray()[0])+"Implied VLT.."+opJReur.getImpliedVlt());
        System.out.println("Binomial EUR CRR-QAANT:" + Arrays.toString(opCRReur.getDerivativesArray()[0])+"Implied VLT.."+opCRReur.getImpliedVlt());
        System.out.println("EF Hulll EUR CRR-QAANT:" + Arrays.toString(opEFHeur.getDerivativesArray()[0])+"Implied VLT.."+opEFHeur.getImpliedVlt());
        System.out.println("EF Wilmott EUR CRR-QAANT:" + Arrays.toString(opEFWeur.getDerivativesArray()[0])+"Implied VLT.."+opEFWeur.getImpliedVlt());
        */
        
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
       // 
       opJRamer.run();
        
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
        
        //********************************** Estudio Threads
        ArrayList<TBinomialJR> arrayListOptions =new ArrayList <>();
        
        
        contrato     ='F';
        option       ='P';
        undValue     =100;
        X            =100;
        days         =10;
        vh30Und      =0.30;
        riskFreeRate =.10;
        divYield     =0;
        mktValue     =3;
        steps        =1000;
        
        someStock   = new Qunderlying(contrato, undValue, vh30Und, divYield);
        
        int processors= Runtime.getRuntime().availableProcessors();
        
        System.out.print("\nCantidad procesadores......: "+processors);
        
        long startTime=System.currentTimeMillis();
        
        TBinomialJR JRAC =new TBinomialJR('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        TBinomialJR JRAP =new TBinomialJR('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        TBinomialJR JREP =new TBinomialJR('E',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        
        arrayListOptions.add(JRAC);
        arrayListOptions.add(JRAP);
        arrayListOptions.add(JREP);
        //arrayListOptions.add(opEFHeur);
        //arrayListOptions.add(opEFWeur);
        
        Thread[] worker = new Thread[arrayListOptions.size()];
        
        
        System.out.println("\nNumero elementos: "+arrayListOptions.size());
       
        for (int i=0 ;i<arrayListOptions.size();i++){
           worker[i] = new Thread(arrayListOptions.get(i));
           worker[i].start();
        }
       
        for (int i=0 ;i<arrayListOptions.size();i++){
            try{
                worker[i].join();
            }
            catch (InterruptedException e){
            }
        }
         for (int i=0 ;i<arrayListOptions.size();i++){
             System.out.println("Binomial JR-Thread:" + Arrays.toString(arrayListOptions.get(i).getDerivativesArray()[0]));//+"Implied VLT.."+tOpt1.getImpliedVlt());
        
         }
         System.out.println("\nElapsed Time Total           :" + (System.currentTimeMillis()-startTime));
           
        
        //*****************************************************************************************
        // Estudio CRR
        startTime= System.currentTimeMillis();
        TBinomialCRR tOpt4 =new TBinomialCRR('A',someStock,'C', X,days,riskFreeRate,mktValue,steps);
        TBinomialCRR tOpt5 =new TBinomialCRR('A',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        TBinomialCRR tOpt6 =new TBinomialCRR('E',someStock,'P', X,days,riskFreeRate,mktValue,steps);
        
        Thread worker4= new Thread(tOpt4);
        Thread worker5= new Thread(tOpt5);
        Thread worker6= new Thread(tOpt6);
        
        worker4.start();
        worker5.start();
        worker6.start();
           
        try{
            worker4.join();
            worker5.join();
            worker6.join();
        }
        catch (InterruptedException e){
        }
        System.out.println("\nBinomial AMER CRR-Thread Call:" + Arrays.toString(tOpt4.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt1.getImpliedVlt());
        System.out.println("Binomial AMER CRR-Thread Put :" + Arrays.toString(tOpt5.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt2.getImpliedVlt());
        System.out.println("Binomial EUR  CRR-Thread Put :" + Arrays.toString(tOpt6.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt3.getImpliedVlt());
        
        System.out.println("\nElapsed Time Total           :" + (System.currentTimeMillis()-startTime));
        
        //*****************************************************************************************
        // Estudio BS
        startTime= System.currentTimeMillis();
        TBlackScholes tOpt7 =new TBlackScholes(someStock,'C', X,days,riskFreeRate,mktValue);
        TBlackScholes tOpt8 =new TBlackScholes(someStock,'P', X,days,riskFreeRate,mktValue);
       
        Thread worker7= new Thread(tOpt7);
        Thread worker8= new Thread(tOpt8);
        
        worker7.start();
        worker8.start();
                
        try{
            worker7.join();
            worker8.join();
            }
        catch (InterruptedException e){
        }
        
        System.out.println("\nBlack Scholes-Thread Call:" + Arrays.toString(tOpt7.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt1.getImpliedVlt());
        System.out.println("Black Scholes-Thread Put :" + Arrays.toString(tOpt8.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt2.getImpliedVlt());
        
        System.out.println("\nElapsed Time Total           :" + (System.currentTimeMillis()-startTime));
        
        //*****************************************************************************************
        // Estudio CV
        startTime= System.currentTimeMillis();
        
        TBinomialCV tOpt9 =new TBinomialCV(someStock,'C', X,days,riskFreeRate,mktValue,steps);
        TBinomialCV tOpt10 =new TBinomialCV(someStock,'P', X,days,riskFreeRate,mktValue,steps);
       
        Thread worker9= new Thread(tOpt9);
        Thread worker10= new Thread(tOpt10);
        
        worker9.start();
        worker10.start();
        
        
        try{
            worker9.join();
            worker10.join();
            }
        catch (InterruptedException e){
        }
        
        System.out.println("\nBinomial CV Call:" + Arrays.toString(tOpt9.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt1.getImpliedVlt());
        System.out.println("Binomial CV Put :" + Arrays.toString(tOpt10.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt2.getImpliedVlt());
        
        System.out.println("\nElapsed Time Total           :" + (System.currentTimeMillis()-startTime));
        
        //***********************************************************
        // Estudio Whaley Thread
        startTime       = System.currentTimeMillis();
        TWhaley tOptWC  =new TWhaley(someStock,'C', X,days,riskFreeRate,mktValue);
        TWhaley tOptWP  =new TWhaley(someStock,'P', X,days,riskFreeRate,mktValue);
       
        Thread worker11 = new Thread(tOptWC);
        Thread worker12 = new Thread(tOptWP);
        
        worker11.start();
        worker12.start();
                
        try{
            worker11.join();
            worker12.join();
            }
        catch (InterruptedException e){
        }
        
        System.out.println("\nWhaley-Thread Call:" + Arrays.toString(tOptWC.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt1.getImpliedVlt());
        System.out.println("Whaley-Thread Put :" + Arrays.toString(tOptWP.getDerivativesArray()[0]));//+"Implied VLT.."+tOpt2.getImpliedVlt());
        System.out.println("\nElapsed Time Total           :" + (System.currentTimeMillis()-startTime));
        
        //***********************************
        
        JRAC.setOptionUndValue(undValue*.99);
        JRAC.run();
        
        
        System.out.println ("CRR Caida 1%...:"+ Arrays.toString(JRAC.getDerivativesArray()[0]));
        
        //***
        //El .run() podria incluirse en el method en TGenericModel, pero de esa forma no se maneja el Thread
        //Aqui se puede poner .run() o mandar a un thread
        JRAC.setOptionUndValue(undValue*1.01);
        JRAC.run();
        
        System.out.println ("CRR Suba 1%...:"+ Arrays.toString(JRAC.getDerivativesArray()[0]));
        System.out.println ("und value...:"+ JRAC.getUnderlyingValue());
        
        
    }
}
