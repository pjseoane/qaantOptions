/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;

import com.qaant.structures.Qoption;

/**
 *
 * @author pauli
 */
public abstract class QbasicModel extends Qoption{
    public QbasicModel(){};
    
    public QbasicModel(Qoption option){
      build();
      
    }
    private void build(){};
    
}
