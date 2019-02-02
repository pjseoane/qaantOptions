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
  
    protected double[][] PLOutput;
    
    public Qticket(){};
   
    public Qticket(QAbstractModel option,double lots, double price, double multiplier, int nodes){
        super(option.getTipoContrato(),option.getUnderlyingValue(),option.getUnderlyingHistVlt(),option.getDividendRate() ,option.getCallPut(), option.getStrike(),option.getDaysToExpiration(),option.getTasa(),option.getOptionMktValue(),option.getModelSteps(),nodes);
        this.option =option;
        this.lots   =lots;
        this.price  =price;
        this.multiplier=multiplier;
        build();
    
    }
   
    private void build(){
        PLOutput = new double [1][nodes+1];
        
        for (int i=0;i<nodes+1;i++){
            option.setOptionUndValue(undPriceRange[0][i]);
            PLOutput[0][i]=(option.getPrima()-price)*lots*multiplier;
        }
    }
    
    public double[][] getPLOutput(){
        return PLOutput;
    }
}
