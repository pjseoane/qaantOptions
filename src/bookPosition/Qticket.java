/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookPosition;

import com.qaant.optionModels.QAbstractModel;
import com.qaant.structures.Qoption;


/**
 *
 * @author pauli
 */
public class Qticket extends Qoption{
    protected QAbstractModel option;
    protected double lots;
    protected double price;
    protected double multiplier;
    protected int step=60;
    protected double[][] PriceRange   = new double [1][step+1];
    protected double[][] PLOutput = new double [1][step+1];
    protected double ratioLog,max, min,coeficiente, center, Dstd=3;
    
    
    public Qticket(){};
    public Qticket(QAbstractModel option,double lots, double price, double multiplier){
       // super (option.tipoContrato,undValue,underlyingHistVolatility,undDivRate);
        this.option =option;
        this.lots   =lots;
        this.price  =price;
        this.multiplier=multiplier;
        build();
    
    }
    private void build(){
        center       = option.getUnderlyingValue();
        coeficiente  = Math.sqrt(30.0/365.0)*option.getUnderlyingHistVlt();
        min          = center*Math.exp(coeficiente *-Dstd);
        max          = center*Math.exp(coeficiente *Dstd);
        ratioLog     = Math.exp(Math.log(max/min)/step);
        
        for (int i=0;i<step+1;i++){PriceRange[0][i]=min*Math.pow(ratioLog,i);}
    }
    
    public void setDstd(double x){Dstd=x; }
    public double[][] getPriceRange(){return PriceRange;}
    
    public double[][] getPLOutput(){
               
       // double check=0;
        for (int i=0;i<step+1;i++){
            option.setOptionUndValue(PriceRange[0][i]);
            PLOutput[0][i]=(option.getPrima()-price)*lots*multiplier;
        }
       
    return PLOutput;
    }
    
}
