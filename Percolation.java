//import edu.princeton.cs.algs4.StdRandom;
//import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import java.util.Random;

public class Percolation {
    private boolean[][] open;
    private int[][] id;
    private WeightedQuickUnionUF comp;
//    private boolean[][] full;
    private int dimension;
    
    public Percolation(int n){
        // create n-by-n grid, with all open blocked
        comp = new WeightedQuickUnionUF(n*n+2); // n*n+2 = number of cells
        id = new int[n][n];    
        open = new boolean[n][n];
//        full = new boolean[n][n];
        dimension = n;
        
        int counter = 1;
        
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                open[i][j] = false;
                id[i][j] = counter;
//                full[i][j] = false;
                counter++;
            }
        }
        // unionize top and bottom cells with top and bottom pointers
        // 0 -> top; n+1 -> bottom
        for(int i = 1; i <= n; i++){
            comp.union(0, i);
            comp.union(n*n+1-i, n*n+1);
        }
        
    }               
    public void open(int row, int col){
        
        if(row < 0 || row >= dimension){
            throw new java.lang.IllegalArgumentException();
        }
        
        if(col < 0 || col >= dimension){
            throw new java.lang.IllegalArgumentException();
        }
        
        // open site (row, col) if it is not open already
        open[row][col] = true;
    
        // check if adjacent sites are open so that 
        // they could be joined together 
                
//        if(comp.connected(0, id[row][col])){
//            full[row][col] = true;
//        }
        
        if(row+1 < dimension){
            if(isOpen(row+1, col)){
                comp.union(id[row][col], id[row+1][col]);
//                full[row+1][col] = comp.connected(0, id[row+1][col]);
                
//                if(comp.connected(0, id[row+1][col])){
//                    full[row+1][col] = true;
//                }
            }
        }
        if(row-1 >= 0){
            if(isOpen(row-1, col)){
                comp.union(id[row][col], id[row-1][col]);
//                full[row-1][col] = comp.connected(0, id[row-1][col]);
                
//                if(comp.connected(0, id[row-1][col])){
//                    full[row-1][col] = true;
//                }
            }
            
        }
        if(col+1 < dimension){
            if(isOpen(row, col+1)){
                comp.union(id[row][col], id[row][col+1]);
//                full[row][col+1] = comp.connected(0, id[row][col+1]);
                
//                if(comp.connected(0, id[row][col+1])){
//                    full[row][col+1] = true;
//                }
            }
        }
        if(col-1 >= 0){
            if(isOpen(row, col-1)){
                comp.union(id[row][col], id[row][col-1]);
//                full[row][col-1] = comp.connected(0, id[row][col-1]);
                
//                if(comp.connected(0, id[row][col-1])){
//                    full[row][col-1] = true;
//                }
            }
        }
        
//        full[row][col-1] = comp.connected(0, id[row][col-1]);
//        full[row][col+1] = comp.connected(0, id[row][col+1]);
//        full[row-1][col] = comp.connected(0, id[row-1][col]);
//        full[row+1][col] = comp.connected(0, id[row+1][col]);
    }    
    public boolean isOpen(int row, int col){
        // is site (row, col) open?
        if(row < 0 || row >= dimension){
            throw new java.lang.IllegalArgumentException();
        }
        
        if(col < 0 || col >= dimension){
            throw new java.lang.IllegalArgumentException();
        }        
        
        return open[row][col];
    }  
    public boolean isFull(int row, int col){
        // is site (row, col) full?
        //return full[row][col];
        if(row < 0 || row >= dimension){
            throw new java.lang.IllegalArgumentException();
        }
        
        if(col < 0 || col >= dimension){
            throw new java.lang.IllegalArgumentException();
        }        
        return comp.connected(0, id[row][col]);
    }  
    public int numberOfOpenSites(){
        // number of open open
        int counter = 0;
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(isOpen(i,j)){
                    counter++;
                }
            }
        }
        
        return counter;
    }       
    public boolean percolates(){
        // does the system percolate?
        return comp.connected(0, dimension*dimension+1);
        
    }  
//    public void pprint(){
//        System.out.print("%%%%%%%%%%%%%PERCOLATION%%%%%%%%%%%%%\n");
////        for(int i = 0; i < dimension; i++){
////            for(int j = 0; j < dimension; j++){
////                System.out.print(open[i][j] + "\t");
////                if(full[i][j]){
////                    System.out.print("F" + "\t");
////                }
////                else if(open[i][j]){
////                    System.out.print("O" + "\t");
////                }
////                else{
////                    System.out.print("B"  + "\t");
////                }
////            }
////            System.out.print("\n");
////        }
////       System.out.print("%%%%%%%%%%%%%FULL%%%%%%%%%%%%%\n");
//         for(int i = 0; i < dimension; i++){
//            for(int j = 0; j < dimension; j++){
////                if(comp.connected(0, id[i][j])){
////                    System.out.print("F" + "\t");
////                }
//                if(isOpen(i,j) && !(comp.connected(0, id[i][j]))){
//                    System.out.print(" ");
//                }
//                else if(isOpen(i,j) && comp.connected(0, id[i][j])){
//                    System.out.print("*");
//                }
//                else{
//                    System.out.print("@");
//                }
//            }
//                
//                       
////                System.out.print(full[i][j] + "\t");
////                if(full[i][j]){
////                    System.out.print("F" + "\t");
////                }
////                else if(open[i][j]){
////                    System.out.print("O" + "\t");
////                }
////                else{
////                    System.out.print("B"  + "\t");
////                }
////            }
//            System.out.print("\n");
//        }
//    }

    public static void main(String[] args){ // optional
//        Percolation test = new Percolation(5);
//        System.out.println(test.percolates());
//        System.out.println(test.numberOfOpenSites());
//        test.open(0,0);
//        test.open(1,0);
//        test.open(2,0);
//        test.open(3,0);
//        test.open(4,0);
//        System.out.println(test.numberOfOpenSites());
//        System.out.println(test.percolates());
//        System.out.println(test.isOpen(0,4));
//        System.out.println(test.isFull(0,4));
//        System.out.println(test.isFull(0,0));
//        System.out.println(test.isFull(0,1));
//        System.out.println(test.isFull(0,2));
//        System.out.println(test.isFull(0,3));
//        System.out.println(test.isFull(0,4));
//        System.out.println(test.isOpen(2,2));
//        System.out.println(test.isFull(2,2));
//        test.open(2,2);
//        System.out.println(test.isFull(2,2));
//        System.out.println(test.percolates());
//        System.out.println(test.isFull(4,0));
//        System.out.println(test.isFull(3,0));
//        System.out.println(test.isFull(2,0));
//        System.out.println(test.isFull(1,0));
//        System.out.println(test.isFull(0,0));
//        System.out.println(test.percolates());
//        test.pprint();
        
//        Random ran = new Random();
//        //ran.nextInt(10);
//        
//        int dimension = 25;
//        
//        Percolation ten = new Percolation(dimension);
//        
//        int counter = 0;
//        
//        while(!ten.percolates()){
//            int a = ran.nextInt(dimension);
//            int b = ran.nextInt(dimension);
//            ten.open(a, b);
//            
//            if(ten.isFull(a,b)){counter++;}
////            System.out.println(ten.isFull(a,b) + "\t" + counter );
//            
//        }
//        
//        ten.pprint();
//        double calc = ten.numberOfOpenSites()/((double)dimension*dimension);
//        System.out.print(calc + "\n");
        
    }
}