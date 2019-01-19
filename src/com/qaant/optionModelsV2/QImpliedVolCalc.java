/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModelsV2;
import java.util.function.DoubleUnaryOperator;
/**
 *
 * @author pauli
 */
public class QImpliedVolCalc
  {
    /**
     * Apply the bisection algorithm to solve an equation of the form
     * f(x) = 0, where f is continuous on an interval [a, b], and f(a)
     * and f(b) have opposite signs.  The algorithm returns a solution
     * x if either f(x) == 0.0 or x differs from a root by less than
     * the specified tolerance.
     * 
     * //@param df implements the function f for the equation to solve//
     * @param func the function to be integrated
     * @param a  the lower bound of interval containing the solution
     * @param b  the upper bound of interval containing the solution
     * @param maxIterations the maximum number of iterations to attempt
     * @param tolerance the precision used to determine the approximate
     *     solution
     *
     * @return a value that differs from the root of f(x) = 0 by less
     *     than the specified tolerance
     *
     * @throws IllegalArgumentException if b < a or if df.f(a) and df.f(b)
     *     have the same sign (i.e., both positive or both negative)
     * @throws ArithmeticException if no solution is found after the
     *     specified maximum number of iterations
     */
    public static double bisection(DoubleUnaryOperator func, double a, double b,
                               int maxIterations, double tolerance)
        throws IllegalArgumentException, ArithmeticException
      {
        if (b < a)
          {
            String message = "a must be <= b; a =" + a + ", b = " + b;
            throw new IllegalArgumentException(message);
          }
        else if (func.applyAsDouble(a)*func.applyAsDouble(b) > 0.0)
          {
              /*
            String message = "function values at " + a + " and " + b
                +  " should have opposite signs; func.applyAsDouble(" + a + ") = " + func.applyAsDouble(a)
                + ", func.applyAsDouble(" + b + ") = " + func.applyAsDouble(b);
            throw new IllegalArgumentException(message);
              */
              return 0;
          }
          
        int    iterations = 1;
        double solution = (a + b)/2.0;

        while (iterations <= maxIterations)
          {
            if (func.applyAsDouble(solution) == 0.0 || (b - a)/2.0 < tolerance)
                return solution;      // approximate solution has been found
            else
              {                        // continue iteration
                if (func.applyAsDouble(a)*func.applyAsDouble(solution) > 0.0)
                    a = solution;
                else
                    b = solution;

                solution = (a + b) / 2.0;
                ++iterations;
              }
          }

        throw new ArithmeticException("Bisection Algorithm failed to converge");
      }
    
    /*
    *Using Vega
    *
    *
    */
  public static double ivVega(DoubleUnaryOperator func, double vol, double vega,
                               int maxIterations, double tolerance)
        throws IllegalArgumentException, ArithmeticException{
      
    int c=0;
    double iv=vol;
    double dif=func.applyAsDouble(iv);
    
    while (Math.abs(dif)> tolerance && c < maxIterations && iv >tolerance){
            {  
            dif=func.applyAsDouble(iv);    
            iv +=(dif/vega/100);
            
            c++;
            
            }
        return iv;
    }
      
      
      throw new ArithmeticException("IV Vega Algorithm failed to converge");          
    }
}