/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pjsoptions2019;

import models2019.cBlackScholes2019;
import underlying.cUnderlying;

/**
 *
 * @author pseoane
 */
public class PJSOptions2019 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        cUnderlying someStock= new cUnderlying('S', 100, .30,0);
        cBlackScholes2019 bs=new cBlackScholes2019(someStock, 'C', 100,365,.1,0,0);
        bs.runModel();
        //print bs.getPrima();
        
    }
    
}
