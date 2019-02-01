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
    protected double[][] PLOutput = new double[1][step];
    
    public Qticket(){};
    public Qticket(QAbstractModel option,double lots, double price, double multiplier){
       // super (option.tipoContrato,undValue,underlyingHistVolatility,undDivRate);
        this.option =option;
        this.lots   =lots;
        this.price  =price;
        this.multiplier=multiplier;
     
    
    }
    
    public double[][] getPLOutput(){
        double min= option.getUnderlyingValue()*.9;
        double max= option.getUnderlyingValue()*1.1;
        double stepPrice= (max-min)/step;
        double getMktValue;
        
        
        for (int i=0;i<step;i++){
            option.setOptionUndValue(min+(i*stepPrice));
            PLOutput[0][i]=option.getPrima();
        }
        
        return PLOutput;
        
    }
    
}
