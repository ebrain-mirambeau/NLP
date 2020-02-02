import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;


public final class SAP {
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(final Digraph G) {
        this.graph = G;
        
        if (this.graph == null) {
            throw new java.lang.IllegalArgumentException();
        }

    }
   // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        
        if (v < 0 || v > graph.V()) {
            throw new java.lang.IllegalArgumentException();
        }
        if (w < 0 || w > graph.V()) {
            throw new java.lang.IllegalArgumentException();
        }
        int ancestor = this.ancestor(v, w);
        
        if (ancestor < 0) {
            return -1;
        }
        else {
            BreadthFirstDirectedPaths x = new BreadthFirstDirectedPaths(graph, v);
            BreadthFirstDirectedPaths y = new BreadthFirstDirectedPaths(graph, w);
            
            return x.distTo(ancestor) + y.distTo(ancestor);
        }
    }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        
        if (v < 0 || v > graph.V()) {
            throw new java.lang.IllegalArgumentException();
        }
        if (w < 0 || w > graph.V()) {
            throw new java.lang.IllegalArgumentException();
        }

         BreadthFirstDirectedPaths x = new BreadthFirstDirectedPaths(graph, v);
         BreadthFirstDirectedPaths y = new BreadthFirstDirectedPaths(graph, w);
         Map<Integer, Integer> common = new HashMap<Integer, Integer>();
         
         for (int i = 0; i < graph.V(); i++) {
             if (x.hasPathTo(i) && y.hasPathTo(i)) {
                 common.put(x.distTo(i) + y.distTo(i), i);
             }
         }
         
         if (common.isEmpty()) {
             return -1;
         }
         else {
             Set<Integer> cm = common.keySet();
             
             Object[] cms = cm.toArray();
             
             Arrays.sort(cms);
             int index = common.get(cms[0]);
             
             return index;
         }
    }


   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }
        
        ArrayList<Integer> a = new ArrayList<Integer>();
        
        Iterator<Integer> itr1 = v.iterator();
        Iterator<Integer> itr2 = w.iterator();
        
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        
        while (itr1.hasNext()) {
            int v_temp = itr1.next();
            x.add(v_temp);
        }
            
        while (itr2.hasNext()) {
            int w_temp = itr2.next();
            y.add(w_temp);
        }
        
        for (int i = 0; i < x.size(); i++) {
            for (int j = 0; j < y.size(); j++) {
                int l = length(x.get(i), y.get(j));
                a.add(l);
            }
        }
        
        if (a.size() == 0) {
            return -1;
        }
        else {
            Object[] ancestor = a.toArray();
            Arrays.sort(ancestor);
            
            int i = ((Integer) ancestor[0]).intValue();
            return i;
        }
    }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }
         ArrayList<Integer> a = new ArrayList<Integer>();
         Map<Integer, Integer> finlen = new HashMap<Integer, Integer>();

         
         Iterator<Integer> itr1 = v.iterator();
         Iterator<Integer> itr2 = w.iterator();
         
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        
        while (itr1.hasNext()) {
            int v_temp = itr1.next();
            x.add(v_temp);
        }
            
        while (itr2.hasNext()) {
            int w_temp = itr2.next();
            y.add(w_temp);
        }
        
        for (int i = 0; i < x.size(); i++) {
            for (int j = 0; j < y.size(); j++) {
                int anc = this.ancestor(x.get(i), y.get(j));
//                int anc = this.ancestor(v_temp, nxt);
                int len = this.length(x.get(i), y.get(j));
                finlen.put(len, anc);
//                a.add(l);
            }
        }
        
//         while (itr1.hasNext()) {
//             int v_temp = itr1.next();
//                 while (itr2.hasNext()) {
//                     int nxt = itr2.next();
//                     int anc = this.ancestor(v_temp, nxt);
//                     int len = this.length(v_temp, nxt);
//                     finlen.put(len, anc);
//                     a.add(anc);
//                 }
//         }
         
         if (finlen.size() == 0) {
             return -1;
         }
         else {
             Set<Integer> d = finlen.keySet();
             //Object[] dist = d.toArray();
             Object[] ancestor = d.toArray();
             Arrays.sort(ancestor);
             
             return finlen.get(ancestor[0]);
             
//             int i = ((Integer) ancestor[ancestor.length-1]).intValue();
//             return i;
         }
         
    }

   // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);//In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}