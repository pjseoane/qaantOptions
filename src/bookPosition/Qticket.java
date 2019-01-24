/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookPosition;

import com.qaant.optionModels.QAbstractModel;
import com.qaant.structures.Qoption;
import com.qaant.structures.Qunderlying;

/**
 *
 * @author pauli
 */
public class Qticket extends Qoption {
    protected double lots,precio;
     protected double[][] lot = new double[1][20];
    
    public Qticket(){}
    public Qticket(Qunderlying und ,Qoption option, double lots, double precio){
        super(und,option);
        this.lots   =lots;
        this.precio =precio;
        buildLot();
        
    }
    private void buildLot(){
        lot[0][0] =0 ;
        lot[0][1] = this.tipoEjercicio;
        lot[0][2] = this.tipoContrato;
        lot[0][3] = this.cpFlag;
        lot[0][4] = this.underlyingValue;
        lot[0][5] = this.dividendRate;
        lot[0][6] = this.strike;
        lot[0][7] = this.daysToExpiration;
        lot[0][8] = this.underlyingHistVolatility;
        lot[0][9] = this.rate;
        lot[0][10] = this.optionMktValue;
        
    }
    public double[][] getLotArray(){return lot;}
}
