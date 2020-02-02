import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
//import Percolation.java;

public class PercolationStats {
    private double[] results;
    private int T;
    
    public PercolationStats(int n, int trials){
        // perform trials independent experiments on an n-by-n grid
        // Percolation trial = new Percolation(n);
        results = new double[trials];
        T = trials;
        //StdRandom ran = new StdRandom();
        
        for(int i = 0; i < trials; i++){
            Percolation trial = new Percolation(n);
            
            while(!trial.percolates()){
                int a = StdRandom.uniform(n);
                int b = StdRandom.uniform(n);
                trial.open(a, b);
            }
            
            double calc = trial.numberOfOpenSites()/((double)n*n);
            results[i] = calc;
            
//            trial.pprint();
        }
    }
    public double mean(){
        // sample mean of percolation threshold
        return StdStats.mean(results);
    }                         
    public double stddev(){
        // sample standard deviation of percolation threshold
        return StdStats.stddev(results);
    }                        
    public double confidenceLo(){
        // low  endpoint of 95% confidence interval
        return this.mean() - (Math.sqrt(StdStats.var(results))/Math.sqrt(T));
    }                  
    public double confidenceHi(){
        // high endpoint of 95% confidence interval
        //this.mean(); StdStats.var(results)
        return this.mean() + (Math.sqrt(StdStats.var(results))/Math.sqrt(T));
    }                  
    public static void main(String[] args){
        // test client (described below)
        PercolationStats test = new PercolationStats(100,5);
        System.out.println("mean:\t\t\t" + test.mean());
        System.out.println("stddev:\t\t\t" + test.stddev());
        System.out.println("95% confidence interval:\t[" + test.confidenceLo() + ", " + test.confidenceHi() + "]");
    }        
}