/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models2019;

import java.util.function.DoubleUnaryOperator;
import underlying.cUnderlying;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author pseoane
 */
public class cBlackScholes2019 extends cUnderlying implements Optionable{
    protected char callPut,tipoEjercicio;
    protected double strike,daysToExpiration,tasa,impliedVol,optionMktValue,dayYear,sqrDayYear,underlyingNPV,q;
    protected double prima, delta, gamma, vega, theta, rho;
    protected String pModelName;
    protected int modelNumber, cpFlag;
    protected boolean opcionConVida;
    protected double elapsedTime;
    //Greeks
    public final static int PRIMA=0,DELTA=1,GAMMA=2,VEGA=3,THETA=4,RHO=5,IV=6;
    protected double[][] derivativesArray = new double[1][10];
    
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
        pModelName                      ="Black-Scholes ver2019";
        modelNumber                     =1;
        tipoEjercicio                   ='E';
        runModel();
    }
    
    @Override
    public void runModel(){
        double startTime=System.currentTimeMillis();
        
         cpFlag=(callPut==CALL)?1:-1;
        // Aca verificar vida opcion <> 0
        opcionConVida=(daysToExpiration>0);
        
        if (opcionConVida){
            opcionConVida();
        }else{
            opcionSinVida();
        } 
        
        elapsedTime = System.currentTimeMillis() - startTime;
        fillDerivativesArray();
    }
    
    private void opcionConVida(){
       
        
        dayYear=daysToExpiration/365;
        sqrDayYear = Math.sqrt(dayYear);
        underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
       
        q=(tipoContrato==STOCK) ? dividendRate:tasa; 
        //q: si es una accion q es el dividendo, si es un futuro q se toma la tasa para descontar el valor futr a presente 
        //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
         
       double z  = Math.exp(-tasa*dayYear); // e^(-r*t);
       //double ww = Math.exp(-dividendRate*dayYear); // e^(-dividendRate*t) //stocks
       
       double d1 = (Math.log(underlyingValue / strike) + dayYear*(tasa-q + impliedVol*impliedVol / 2)) / (impliedVol*sqrDayYear);
       double d2 = d1 - impliedVol*sqrDayYear;
       
       double CNDFd1 =new NormalDistribution().cumulativeProbability(d1);
       double CNDFd2 =new NormalDistribution().cumulativeProbability(d2);
       double PDFd1  =new NormalDistribution().density(d1);         
       
       //gamma y vega son iguales para call y put
       gamma     =PDFd1 *z / (underlyingNPV*impliedVol*sqrDayYear);
       vega      =underlyingNPV * sqrDayYear*PDFd1 / 100;
       
       
       switch (callPut)
            {
              
            case CALL: 
                                            
		prima = underlyingValue*Math.exp(-q*dayYear) * CNDFd1 - z * strike*CNDFd2;
		delta = Math.exp(-q*dayYear)*CNDFd1;
                theta   = (-(underlyingNPV*impliedVol*PDFd1 / (2 * sqrDayYear)) - strike*tasa*z*CNDFd2+dividendRate*underlyingNPV*CNDFd1) / 365;
		rho =   strike*dayYear*Math.exp(-(tasa-q)*dayYear)*CNDFd2 / 100;
		
                break;

            case PUT: 
                
                
                double CNDF_d1=new NormalDistribution().cumulativeProbability(-d1);
                double CNDF_d2=new NormalDistribution().cumulativeProbability(-d2);
                
		prima = -underlyingValue*Math.exp(-q*dayYear) * CNDF_d1 + z * strike*CNDF_d2;
		delta = Math.exp(-q*dayYear)*(CNDFd1 - 1);
		theta = (-(underlyingNPV*impliedVol*PDFd1 / (2 * sqrDayYear)) + strike*tasa*z*CNDF_d2-dividendRate*underlyingNPV*CNDF_d1) / 365;
                rho = -strike*dayYear*Math.exp(-(tasa-q)*dayYear)*CNDF_d2 / 100;
		
                break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
    
    }
    
    private void opcionSinVida(){
        delta=cpFlag;  
        gamma=vega=theta=rho=0;
        prima = Math.max((underlyingValue - strike)*cpFlag, 0);
        delta=(prima==0)?0:cpFlag;
       
    }
    
    //getters

    /**
     *
     * @return
     */
    @Override
    public String getModelName(){return pModelName;}
    @Override
    public double getPrima(){return prima;}
    @Override
    public double getDelta(){return delta;}
    @Override
    public double getGamma(){return gamma;}
    @Override
    public double getVega() {return vega;}
    @Override
    public double getTheta(){return theta;}
    @Override
    public double getRho()  {return rho;}
    
    
    
    public double getOptionMktValue(){return optionMktValue;}
    public char getTipoEjercicio(){return tipoEjercicio;}
    public char getCallPut(){return callPut;}
    public double getStrike(){return strike;}
    public double getDaysToExpiration(){return daysToExpiration;}
   
    public double getTasa(){return tasa;}
    public double getValueToFind(int i){
     
        if (i>9){i=0;}
        return derivativesArray[0][i];
    }    
        
    //setters
    public void setCallPut(char callPut){this.callPut=callPut;}
    public void setStrike (double strike){this.strike=strike;}
    public void setDaysToExp(double daysToExpiration){this.daysToExpiration=daysToExpiration;}
    public void setRiskFreeRate(double rf){tasa=rf;}
    public void setOptionVlt(double vlt){this.impliedVol=vlt;}
   
    
    @Override
    public double getImpliedVlt() {
    if(optionMktValue>0 && daysToExpiration>0){
        double min;
        double max;
        int iter=50;
        double precision=0.00001;
    
        if(prima<=optionMktValue){
            min=0; //impliedVol;
            max=2;//min*4;
        }else{
            min=0;// impliedVol/3;
            max=2;//impliedVol;
        }
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new cBlackScholes2019(tipoContrato, underlyingValue, underlyingHistVolatility,dividendRate, callPut, strike, daysToExpiration,tasa,x,0).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
        //***********************************
       
    }
    return impliedVol;
    }
    
    @Override
    public void fillDerivativesArray(){
        derivativesArray[0][0]=prima;
        derivativesArray[0][1]=delta;
        derivativesArray[0][2]=gamma;
        derivativesArray[0][3]=vega;
        derivativesArray[0][4]=theta;
        derivativesArray[0][5]=rho;
        derivativesArray[0][6]=impliedVol;
        derivativesArray[0][7]=elapsedTime;
        derivativesArray[0][8]=optionMktValue;
        derivativesArray[0][9]=modelNumber;
      
    }
    
    public double[][] getDerivativesArray(){return derivativesArray;}
    
    public String getOptionString(){
        StringBuilder builder =new StringBuilder();
        // builder.append("Ticker-");
        //builder.append(anUnderlying.getTicker());
        builder.append(modelNumber);
        builder.append("-");
        builder.append(pModelName);
        builder.append("/Option->");
        builder.append(callPut);
        builder.append("/strike->");
        builder.append(strike);
        builder.append("/prima->");
        builder.append(prima);
        builder.append("/delta->");
        builder.append(delta);
        builder.append("/gamma->");
        builder.append(gamma);
        builder.append("/vega->");
        builder.append(vega);
        builder.append("/theta->");
        builder.append(theta);
        builder.append("/rho->");
        builder.append(rho);
        builder.append("/impVlt->");
        builder.append(impliedVol);
        builder.append("z");
        return builder.toString();
    }//end getString
}


    