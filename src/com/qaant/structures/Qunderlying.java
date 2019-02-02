/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.structures;

/**
 *
 * @author Paulino
 */
public class Qunderlying {
   
    enum tipoContrato {STOCK,FUTURES};
    public final static char STOCK='S';
    public final static char FUTURES='F';
    
    protected char  tipoContrato; //'S': Stock 'F':Futuro
    protected String ticker,ISIN;;
    protected String underlyingName;
    protected double underlyingValue;
    protected double underlyingHistVolatility;
    protected double dividendRate;
    
    protected int nodes=1;
    protected double[][] undPriceRange;//   = new double [1][step+1];
    protected double ratioLog,max, min,coeficiente, center,Dstd=3;
    protected double daysToProject=30;
    
    
    public Qunderlying(){buildPriceRange();}   
    public Qunderlying(Qunderlying und){
        this.tipoContrato               =und.tipoContrato;
        this.underlyingValue            =und.underlyingValue;
        this.underlyingHistVolatility   =und.underlyingHistVolatility;
        this.dividendRate               =und.dividendRate;
        this.ticker                     =und.ticker;
        buildPriceRange();
    }
    public Qunderlying(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate)
    {
        this.tipoContrato               =tipoContrato;
        this.underlyingValue            =underlyingValue;
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.dividendRate               =dividendRate;
        ticker                          ="TICKER";
        buildPriceRange();
    }
    public Qunderlying(char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate, int nodes)
    {
        this.tipoContrato               =tipoContrato;
        this.underlyingValue            =underlyingValue;
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.dividendRate               =dividendRate;
        this.nodes                      =nodes;
        ticker                          ="TICKER";
        
        buildPriceRange();
    }
    
        
    public Qunderlying(String ticker,char tipoContrato, double underlyingValue,double underlyingHistVolatility,double dividendRate){
        this.ticker                     =ticker;
        this.tipoContrato               =tipoContrato;
        this.underlyingValue            =underlyingValue;
        this.underlyingHistVolatility   =underlyingHistVolatility;
        this.dividendRate               =dividendRate;
        buildPriceRange();
    }
    private void buildPriceRange(){
        undPriceRange   = new double [1][nodes+1];
        
        center       = this.underlyingValue;
        coeficiente  = Math.sqrt(daysToProject/365.0)*this.underlyingHistVolatility;
        min          = center*Math.exp(coeficiente *-Dstd);
        max          = center*Math.exp(coeficiente *Dstd);
        ratioLog     = Math.exp(Math.log(max/min)/nodes);
        for (int i=0;i<nodes+1;i++){undPriceRange[0][i]=min*Math.pow(ratioLog,i);}
    }
    
    public double[][] getUnderlyingPriceRange(){
        return undPriceRange;
    }
    
    
    //getters
    public char getTipoContrato(){return tipoContrato;}
    public double getUnderlyingValue(){return underlyingValue;}
    public double getUnderlyingHistVlt(){return underlyingHistVolatility;}
    public double getDividendRate(){return dividendRate;}
    public String getTicker(){return ticker;}
    
    //setters
    public void setTipoContrato(char TipoContrato){this.tipoContrato=TipoContrato;}
    public void setUnderlyingValue(double UnderlyingValue){underlyingValue=UnderlyingValue;}
    public void setUnderlyingHistVlt(double Volatility){underlyingHistVolatility=Volatility;}
    public void setDividendRate(double DividendRate){this.dividendRate=DividendRate;}
    public void setTicker(String ticker){this.ticker =ticker;}
    public void setDstd(double x){
        this.Dstd=x;
        buildPriceRange();
    }
    public void setNumberOfNodes(int nodes){
        this.nodes=nodes;
        buildPriceRange();
    }
    
    public String getUnderlyingString(){
    StringBuilder builder =new StringBuilder();
    builder.append("Ticker ");
    builder.append(ticker);
    builder.append(" Tipo Contrato ");
    builder.append(tipoContrato);
    builder.append(" Value: ");
    builder.append(underlyingValue);
    builder.append(" Historical Vlt: ");
    builder.append(underlyingHistVolatility);
    builder.append(" Dividend Rate: ");
    builder.append(dividendRate);
    builder.append("-");
    return builder.toString();
    }
}
