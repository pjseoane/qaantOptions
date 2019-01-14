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
    protected char callPut;
    protected double strike,daysToExpiration,rate,optionMktValue,dayYear,sqrDayYear,underlyingNPV,q,impliedVol;
    protected double prima, delta, gamma, vega, theta, rho;
    //protected 
    protected int  cpFlag;
    protected boolean opcionConVida;
    protected double elapsedTime;
    protected double volatModel;
    //Greeks
    public final static int PRIMA=0,DELTA=1,GAMMA=2,VEGA=3,THETA=4,RHO=5,IV=6;
    protected double[][] derivativesArray = new double[1][10];
    
    //enum TipoOpcion {CALL,PUT};
    public final static char CALL   ='C';
    public final static char PUT    ='P';
    protected String pModelName="Black-Scholes ver2019";
    protected int modelNumber=1;
    protected static char tipoEjercicio ='E';
    
    
        
    public cBlackScholes2019 (){}
    public cBlackScholes2019 (cUnderlying und, char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(und);
     
        this.callPut              =callPut;  
        this.strike               =strike;
        this.daysToExpiration     =daysToExpiration;
        this.rate                 =rate;
        this.optionMktValue       =optionMktValue;
        buildBS();
    }
    
    public cBlackScholes2019 (char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate,char callPut, double strike,double daysToExpiration,double rate,double optionMktValue){
        super(tipoContrato, underlyingValue, underlyingHistVolatility, dividendRate);
       
        this.callPut              =callPut;  
        this.strike               =strike;
        this.daysToExpiration     =daysToExpiration;
        this.rate                 =rate;
        this.optionMktValue       =optionMktValue;
        buildBS();
    }
    
    private void buildBS(){
        double startTime=System.currentTimeMillis();
        
        cpFlag=(callPut==CALL)?1:-1;
        // Aca verificar vida opcion <> 0
        opcionConVida=(daysToExpiration>0);
        
        if (opcionConVida){
            volatModel = underlyingHistVolatility;
            dayYear=daysToExpiration/365;
            sqrDayYear = Math.sqrt(dayYear);
            underlyingNPV=underlyingValue*Math.exp(-dividendRate*dayYear);
            q=(tipoContrato==STOCK) ? dividendRate:rate; 
            //q: si es una accion q es el dividendo, si es un futuro q se toma la rate para descontar el valor futr a presente 
            //Se hace este reemplazo para poder usar la misma form en STOCK y FUTURO
                      
            runModel();
        }else{
            opcionSinVida();
        } 
        
        elapsedTime = System.currentTimeMillis() - startTime;
                
        impliedVol=getImpliedVlt();
        fillDerivativesArray();
    
    }
    
    @Override
    public void runModel(){
       
         
       double z  = Math.exp(-rate*dayYear); // e^(-r*t);
       //double ww = Math.exp(-dividendRate*dayYear); // e^(-dividendRate*t) //stocks
       
       double d1 = (Math.log(underlyingValue / strike) + dayYear*(rate-q + volatModel*volatModel / 2)) / (volatModel*sqrDayYear);
       double d2 = d1 - volatModel*sqrDayYear;
       
       double CNDFd1 =new NormalDistribution().cumulativeProbability(d1);
       double CNDFd2 =new NormalDistribution().cumulativeProbability(d2);
       double PDFd1  =new NormalDistribution().density(d1);         
       
       //gamma y vega son iguales para call y put
       gamma     =PDFd1 *z / (underlyingNPV*volatModel*sqrDayYear);
       vega      =underlyingNPV * sqrDayYear*PDFd1 / 100;
       
       
       switch (callPut)
            {
              
            case CALL: 
                                            
		prima = underlyingValue*Math.exp(-q*dayYear) * CNDFd1 - z * strike*CNDFd2;
		delta = Math.exp(-q*dayYear)*CNDFd1;
                theta   = (-(underlyingNPV*volatModel*PDFd1 / (2 * sqrDayYear)) - strike*rate*z*CNDFd2+dividendRate*underlyingNPV*CNDFd1) / 365;
		rho =   strike*dayYear*Math.exp(-(rate-q)*dayYear)*CNDFd2 / 100;
		
                break;

            case PUT: 
                
                
                double CNDF_d1=new NormalDistribution().cumulativeProbability(-d1);
                double CNDF_d2=new NormalDistribution().cumulativeProbability(-d2);
                
		prima = -underlyingValue*Math.exp(-q*dayYear) * CNDF_d1 + z * strike*CNDF_d2;
		delta = Math.exp(-q*dayYear)*(CNDFd1 - 1);
		theta = (-(underlyingNPV*volatModel*PDFd1 / (2 * sqrDayYear)) + strike*rate*z*CNDF_d2-dividendRate*underlyingNPV*CNDF_d1) / 365;
                rho = -strike*dayYear*Math.exp(-(rate-q)*dayYear)*CNDF_d2 / 100;
		
                break;
            
            default:
                prima=delta=gamma=theta=rho=0;
                break;
                
        }//end switch
       
        //Aca calcula IV si se informa algun mktValue
        
    }
    
    public void opcionSinVida(){
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
   
    public double getTasa(){return rate;}
    public double getValueToFind(int i){
     
        if (i>9){i=0;}
        return derivativesArray[0][i];
    }    
        
    //setters
    public void setCallPut(char callPut){this.callPut=callPut;}
    public void setStrike (double strike){this.strike=strike;}
    public void setDaysToExp(double daysToExpiration){this.daysToExpiration=daysToExpiration;}
    public void setRiskFreeRate(double rf){rate=rf;}
    public void setOptionVlt(double vlt){this.volatModel=vlt;}
   
    
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
        
        DoubleUnaryOperator opt1 = x-> optionMktValue-new cBlackScholes2019(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0).getPrima();
               
        impliedVol= ImpliedVolCalc.bisection(opt1, min, max, iter, precision);
        //***********************************
       
    }
    return impliedVol;
    }
    
    public double getIV2(){
        impliedVol=volatModel;
        if(optionMktValue>0 && daysToExpiration>0){
        DoubleUnaryOperator opt1 = x-> optionMktValue-new cBlackScholes2019(tipoContrato, underlyingValue, x,dividendRate, callPut, strike, daysToExpiration,rate,0).getPrima();
        impliedVol= ImpliedVolCalc.ivVega(opt1, volatModel, vega, 20, 0.00001);    
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
        derivativesArray[0][6]=optionMktValue;
        derivativesArray[0][7]=impliedVol;
        derivativesArray[0][8]=elapsedTime;
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
        builder.append(volatModel);
        builder.append("z");
        return builder.toString();
    }//end getString
}


    