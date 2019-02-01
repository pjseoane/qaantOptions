/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookPosition;

import com.qaant.optionModels.QAbstractModel;


/**
 *
 * @author pauli
 */
public class Qticket {
    protected QAbstractModel option;
    protected double lots;
    protected double price;
    
    public Qticket(){};
    public Qticket(QAbstractModel option,double lots, double price){
        this.option =option;
        this.lots   =lots;
        this.price  =price;
     
    
    }
}
