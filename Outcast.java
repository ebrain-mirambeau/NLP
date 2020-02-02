import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public final class Outcast {
    private final WordNet wnet;
    
    public Outcast(final WordNet wordnet) {         // constructor takes a WordNet object
        this.wnet = wordnet;
       
    }
    public String outcast(String[] nouns) {   // given an array of WordNet nouns, return an outcast
        String[] ns1 = nouns;
        //String[] ns2 = nouns;
        
        Map<Integer, String> wl = new HashMap<Integer, String>();
        
        for (int i = 0; i < ns1.length; i++) {
            int len = 0;
            for (int j = 0; j < ns1.length; j++) {
                if (i != j) {
                    String word1 = ns1[i].replace("\n", "").replace("\r", "");
                    String word2 = ns1[j].replace("\n", "").replace("\r", "");

                    len += wnet.distance(word1, word2);
                }
            }
//            System.out.println(len + "\t" + ns1[i]);
            wl.put(len, ns1[i]);

        }
        
        Set<Integer> d = wl.keySet();
        Object[] dist = d.toArray();
        
        Arrays.sort(dist);
        
        return wl.get(dist[dist.length-1]);
    }
    public static void main(String[] args) {  // see test client below
            WordNet wordnet = new WordNet(args[0], args[1]);
            Outcast outcast = new Outcast(wordnet);
            for (int t = 2; t < args.length; t++) {
                In in = new In(args[t]);
                String[] nouns = in.readAllStrings();
                StdOut.println(args[t] + ": " + outcast.outcast(nouns));
            }
    }
}