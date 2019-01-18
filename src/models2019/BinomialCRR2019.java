/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author pauli
 */
public class BinomialCRR2019 extends BlackScholes2019 implements Optionable{
    protected int steps;
    
    public BinomialCRR2019(){}
    public BinomialCRR2019(char tipoEjercicio,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue,int steps){
        //super(tipoContrato,underlyingValue, underlyingHistVolatility, dividendRate, callPut, strike,daysToExpiration, rate, optionMktValue);
        // puede servir en este modelp construir un BS para aproximar algun valor??
        
        this.tipoEjercicio        =tipoEjercicio;
        this.tipoContrato         =tipoContrato;
        this.underlyingValue      =underlyingValue;
        volatModel                =underlyingHistVolatility;
        this.dividendRate         =dividendRate;
        this.callPut              =callPut;  
        this.strike               =strike;
        this.daysToExpiration     =daysToExpiration;
        this.rate                 =rate;
        this.optionMktValue       =optionMktValue;
        this.steps                = steps;
        
        buildCRR();
    }
    
    
     private void buildCRR(){
        pModelName="Binomial Cox-Ross-Rubinstein ver2019";
        modelNumber=3;
        cpFlag=cpFlag();        
        build();  //definido en BS
     }
        
    
    
    @Override
     public void runModel(){
       // System.out.println("Run Model CRR..."+tipoEjercicio);
       /**
        *Nueva version 02/may/2014	
        *tipoEjercicio: E: European, A:American
        *tipoContrato: S:Acciones, F:Futuros
        *Copiado de la version C++
        *Conviene que _steps sea par para ver despues como chequear _steps=integer(x);
        *double Btree(_steps+2);
        *Trabajamos con un arbol 4dt mayor al numero de _steps para mejorar la precision de las Greeks
        *tamaño razonable de _steps >=160
        *Basado en el Modelo Binomial de Modelos 2008.xla
        *Dividendos: Pasar un Stock Adjusted y procesar modelo normal.
        */   
        
        //volatModel = underlyingHistVolatility;
       // dayYear=daysToExpiration/365;
        //int UpBound =steps+1;
        double [] vPrices=new double[steps+1];
        dayYear=daysToExpiration/365;
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
        //q=(tipoContrato==STOCK) ? 0:rate;
        
        double h = dayYear / steps;
        double u = Math.exp(volatModel*Math.sqrt(h));
        double d = Math.exp(-volatModel*Math.sqrt(h));
        double z = Math.exp(-rate*h); 
        double drift=(tipoContrato=='F')? 1: Math.exp(rate*h);
        q=(drift-d)/(u-d);
        double qx=1-q;
	
        double assetAtNode;
        //Boundary Conditions
	for (int i = 0; i <steps+1; i++){
		assetAtNode = underlyingNPV*Math.pow(u, (steps - i))*Math.pow(d, i);
		//vPrices[i] = Math.max((AssetAtNode-strike)*cpFlag, 0);
                vPrices[i] = payoff(assetAtNode,strike,cpFlag);
	}
	//Fin Boundary Conditions 
        
        //Resolving Tree Backward
      

        //Resolving Tree Backward
	double a=0;
        double b=0;
        double c=0;
        for (int i = 0; i <steps; i++){
            if(i==steps-2){
                a = vPrices[0];
                b = vPrices[1];
                c = vPrices[2];
            }
            for (int j=0;j<steps-i;j++){
                double optionAtNode=(q*vPrices[j]+qx*vPrices[j+1])*z;
                vPrices[j]=optionAtNode;
                
                if(tipoEjercicio=='A'){
                    assetAtNode=underlyingNPV * Math.pow(u,steps-i-j-1)*Math.pow(d,j);
                    vPrices[j]=Math.max(payoff(assetAtNode,strike,cpFlag), optionAtNode);
                }
            }
        }
        prima   =vPrices[0];
        delta   =(a-c)/ (underlyingNPV*u*u - underlyingNPV * d * d);
        double delta1=(a-b)/(underlyingNPV*u*u-underlyingNPV*u*d);
        double delta2=(b-c)/(underlyingNPV*u*d-underlyingNPV*d*d);
        gamma=(delta1-delta2)/((underlyingNPV*u*u-underlyingNPV*d*d)/2);
        theta=(b-vPrices[0])/(2*h*365);
        
        if(optionMktValue>-1){
            BinomialCRR2019 optCRR=new BinomialCRR2019(tipoEjercicio,tipoContrato, underlyingValue, volatModel+0.01, dividendRate,callPut,  strike, daysToExpiration, rate, -1, steps);
            //System.out.print("Volat ..."+(volatModel));
            vega=optCRR.getPrima()-prima;
            
            optCRR=new BinomialCRR2019(tipoEjercicio,tipoContrato, underlyingValue, volatModel, dividendRate,callPut,  strike, daysToExpiration, rate+0.01, -1, steps);
            //System.out.print("Volat ..."+(volatModel));
            rho=optCRR.getPrima()-prima;
                    
        }
        
               
     }
    
   
    @Override
    public double getImpliedVlt() {
        impliedVol=volatModel;
        
        if(optionMktValue>0 && daysToExpiration>0){
            double min;
            double max;
            int iter=50;
            double precision=0.00001;
    
        if(prima<=optionMktValue){
            min=volatModel;
            max=min*3;
            }else{
                min=0;// impliedVol/3;
                max=volatModel;
            }
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new BinomialCRR2019(tipoEjercicio, tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0,steps).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
                     
    }
    return impliedVol;
    }
}
