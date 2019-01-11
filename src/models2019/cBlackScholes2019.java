/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import underlying.cUnderlying;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author pseoane
 */
public class cBlackScholes2019 extends cUnderlying{
    protected char callPut,tipoEjercicio;
    protected double strike,daysToExpiration,tasa,impliedVol,optionMktValue,dayYear,sqrDayYear,underlyingNPV,q;
    protected double prima, delta, gamma, vega, theta, rho;
    protected String pModelName;
    protected int modelNumber, multCallPut;
    protected boolean opcionConVida;
    
    //enum TipoOpcion {CALL,PUT};
    public final static char CALL   ='C';
    public final static char PUT    ='P';
    
        
    public cBlackScholes2019 (){}
    public cBlackScholes2019 (cUnderlying und, char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
      super(und);
      this.callPut              =callPut;  
      this.strike               =strike;
      this.daysToExpiration     =daysToExpiration;
      this.tasa                 =tasa;
      this.impliedVol           =impliedVol;
      this.optionMktValue       =optionMktValue;
      
      buildBS();
        
        
    }
    public cBlackScholes2019 (char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double tasa,double impliedVol,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
        this.callPut              =callPut;  
        this.strike               =strike;
        this.daysToExpiration     =daysToExpiration;
        this.tasa                 =tasa;
        this.impliedVol           =impliedVol;
        this.optionMktValue       =optionMktValue;
    
        buildBS();
    }
    
    private void buildBS(){
        pModelName                      ="Black-Scholes ver2017";
        modelNumber                     =1;
        tipoEjercicio                   ='E';
        multCallPut=(callPut==CALL)?1:-1;
        // Aca verificar vida opcion <> 0
        opcionConVida=(daysToExpiration>0);
        
        /*
         if (daysToExpiration<=0){
             opcionSinVida();
         }else{
        */
            dayYear=daysToExpiration/365;
            sqrDayYear = Math.sqrt(dayYear);
            underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
       
            q=(tipoContrato==STOCK) ? dividendRate:tasa; 
            //q: si es una accion q es el dividendo, si es un futuro q se toma la tasa para descontar el valor futr a presente 
            //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
         
    }
    
    public void runModel(){
        if (opcionConVida){
            opcionConVida();
                   
        }else{
            opcionSinVida();
        }
       
    }
    
    protected void opcionConVida(){
       double z  = Math.exp(-tasa*dayYear); // e^(-r*t);
       double ww = Math.exp(-dividendRate*dayYear); // e^(-dividendRate*t) //stocks
       
       double d1 = (Math.log(underlyingValue / strike) + dayYear*(tasa-q + impliedVol*impliedVol / 2)) / (impliedVol*sqrDayYear);
       double d2 = d1 - impliedVol*sqrDayYear;
       
       double CNDFd1 =new NormalDistribution().cumulativeProbability(d1);
       double CNDFd2 =new NormalDistribution().cumulativeProbability(d2);
       double PDFd1     =new NormalDistribution().density(d1);         
       
       /*        
       CNDFd1    =DistFunctions.CNDF(d1);  //Cumulative Normal Distribution
       CNDFd2    =DistFunctions.CNDF(d2);
       PDFd1     =DistFunctions.PDF(d1);    //Probability density Function
       */
       
       //gamma y vega son iguales para call y put
       gamma     =PDFd1 *ww / (underlyingValue*impliedVol*sqrDayYear);
       vega      =underlyingNPV * sqrDayYear*PDFd1 / 100;
       
       switch (callPut)
            {
              
            case CALL: 
                                            
		prima = underlyingValue*Math.exp(-q*dayYear) * CNDFd1 - z * strike*CNDFd2;
		delta = ww*CNDFd1;
		theta   = (-(underlyingNPV*impliedVol*PDFd1 / (2 * sqrDayYear)) - strike*tasa*z*CNDFd2+dividendRate*underlyingNPV*CNDFd1) / 365;
		//rho   = strike*dayYear*z*CNDFd2 / 100;
                rho =   strike*dayYear*Math.exp(-(tasa-q)*dayYear)*CNDFd2 / 100;
		
                break;

            case PUT: 
                
                //CNDF_d1=DistFunctions.CNDF(-d1);
                //CNDF_d2=DistFunctions.CNDF(-d2);
                double CNDF_d1=new NormalDistribution().cumulativeProbability(-d1);
                double CNDF_d2=new NormalDistribution().cumulativeProbability(-d2);
                
		prima = -underlyingValue*Math.exp(-q*dayYear) * CNDF_d1 + z * strike*CNDF_d2;
		delta = ww*(CNDFd1 - 1);
		theta = (-(underlyingNPV*impliedVol*PDFd1 / (2 * sqrDayYear)) + strike*tasa*z*CNDF_d2-dividendRate*underlyingNPV*CNDF_d1) / 365;
                //rho = -strike*dayYear*z*CNDF_d2 / 100;
                rho = -strike*dayYear*Math.exp(-(tasa-q)*dayYear)*CNDF_d2 / 100;
		
                break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
    
    }
    
    protected void opcionSinVida(){
        delta=multCallPut;  
        gamma=vega=theta=rho=0;
        prima = Math.max((underlyingValue - strike)*multCallPut, 0);
        delta=(prima==0)?0:multCallPut;
       
    }
    
    public double getPrima(){return prima;}
    public double getDelta(){return delta;}
    public double getGamma(){return gamma;}
    public double getVega() {return vega;}
    public double getTheta(){return theta;}
    public double getRho()  {return rho;}
    
}