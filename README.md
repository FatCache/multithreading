# Demonstration of the use of Akka in Java
## Letter & Word Count 

In a concurrent environment, multiple actors are used to estimate and refine a parameter in each iteration of data source.

The parameter is `C'(T+1)` is governed by `g` weight in `Estimator.class`. Using `U(t)` feedback from `Counter.class`, the former parameters are tweaked.

@author
Abdusamed

Resource: (http://akka.io/)[http://akka.io/]