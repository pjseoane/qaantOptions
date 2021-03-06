/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qaant.optionModels;
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
     * and f(b) have opposite signs.The algorithm returns a solution
 x if either f(x) == 0.0 or x differs from a root by less than
 the specified tolerance.//@param df implements the function f for the equation to solve//
     * @param func the function to be integrated
     * @param left
     * @param right
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
                               int maxIterations, double tolerance){
        int    iterations       = 0;  
        double solution         = (a+b)/2.0;
        double funcAtLeft,funcAtRight   ;
        double difFunc=1;
        
        while (Math.abs(b-a) > tolerance && iterations <= maxIterations && Math.abs(difFunc)> tolerance){
       //while (Math.abs(solution)>tolerance && iterations <= maxIterations ){
            funcAtLeft  =func.applyAsDouble(a);
            funcAtRight =func.applyAsDouble(solution);
            difFunc     =funcAtLeft-funcAtRight;
            
            if (funcAtLeft*funcAtRight>0){
                a=solution;
            }else{
                b=solution;
            }
            solution=(a+b)/2.0;
            iterations++;
           // System.out.print("\nIterations: "+iterations);
        }
        return solution;
    }
    
    public static double bisectionPRO(DoubleUnaryOperator func, double a, double b,
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
              //  System.out.print("\nIterations: "+iterations);
              }
          }

        throw new ArithmeticException("Bisection Algorithm failed to converge");
      }
    
    
  public static double ivNewton(DoubleUnaryOperator func, double vol, double vega,
                               int maxIterations, double tolerance){
        //throws IllegalArgumentException, ArithmeticException{
      
    int c=0;
    double iv=vol;
    double dif=func.applyAsDouble(iv);
    
    while (Math.abs(dif)> tolerance && c < maxIterations ){  
            //System.out.print("\nNewton count: "+c);
            iv +=(dif/vega/100);
            dif=func.applyAsDouble(iv);    
            
            c++;
    }
    return iv; 
    //  throw new ArithmeticException("IV Vega Algorithm failed to converge");          
  }
  
  public static double turboNewton(DoubleUnaryOperator func, double vol, double vega,
                               int maxIterations, double tolerance){
      double iv=vol;
      double ivAnt=vol;
      double dif=func.applyAsDouble(iv);
      double a,b;
      //loopea 3 veces suficiente para aproximar la IV y luego hace biseccion, por si eventualmente se va a la mierda con vega
      for(int i=0;i<3;i++){
          ivAnt=iv;
          iv +=(dif/vega/100);
          dif=func.applyAsDouble(iv);    
      }
      
      if (iv<=ivAnt){
          a=iv;
          b=ivAnt;
      }else{
          a=ivAnt;
          b=iv;
      }
     // System.out.println("a y b antes de entrar a biseccion "+" "+a+" "+b);
      return (bisection(func, a, b,maxIterations, tolerance));
    }
}