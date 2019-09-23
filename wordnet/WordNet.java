import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public final class WordNet {
    private final Map<Integer, String> dictionary;
    private final Map<String, Integer> nns;
    private final Map<Integer, String> glosses;
    private final Map<Integer, String> syn;
    private final SAP sap;
    private final Digraph graph;
        
    // constructor takes the name of the two input files public 
    public WordNet(String synsets, String hypernyms){
        
        if(synsets == null || hypernyms == null){
            throw new java.lang.IllegalArgumentException();
        }
        
        In sys = new In(synsets);
        String[] nouns = sys.readAllLines();
        //sys.readAllLines();

        dictionary = new HashMap<Integer, String>();
        nns = new HashMap<String, Integer>();
        glosses = new HashMap<Integer, String>();
        syn = new HashMap<Integer, String>();
        
        for(int i = 0; i < nouns.length; i++){
            String line = nouns[i];
            String[] list = line.split(","); // 0,1,2 indices
            String[] ns = list[1].split(" ");
            
            glosses.put(Integer.parseInt(list[0]), list[2]);
            syn.put(Integer.parseInt(list[0]), list[1]);
            dictionary.put(Integer.parseInt(list[0]), list[1]);
            
            for(int j = 0; j < ns.length; j++){
                nns.put(ns[j], Integer.parseInt(list[0]));
            }
        }

        In hyp = new In(hypernyms);
        String[] network = hyp.readAllLines();
 
        graph = new Digraph(syn.size());
        
        if(graph == null){
            throw new java.lang.IllegalArgumentException();
        }

        for(int i = 0; i < network.length; i++){

            String[] list = network[i].split(",");

            for(int j = 1; j < list.length; j++){
                graph.addEdge(Integer.parseInt(list[0]), Integer.parseInt(list[j]));

            }
        }
        sap = new SAP(graph);
    }

//   // returns all WordNet nouns
    public Iterable<String> nouns(){
        return nns.keySet();
    }
//
   // is the word a WordNet noun?
    public boolean isNoun(String word){
        if(word == null){
            throw new java.lang.IllegalArgumentException();
        }
        return nns.containsKey(word);
    }
//
//   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        
        if(nounA == null || nounB == null){
            throw new java.lang.IllegalArgumentException();
        }
        
        if(!this.isNoun(nounA) || !this.isNoun(nounB)){
            throw new java.lang.IllegalArgumentException();
        }
        //SAP sap = new SAP(graph);
        //int index = sap.length(dictionary.get(nounA), dictionary.get(nounB));
        //System.out.println(dictionary.get(nounA) + "\t" + dictionary.get(nounB));
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        
//        System.out.println(nounA + "\t" + nounB);
        
        for(int i = 0; i < dictionary.size(); i++){
//            System.out.print(dictionary.get(i));
            String[] words1 = dictionary.get(i).split(" ");
            String[] words2 = dictionary.get(i).split(" ");
            
            for(int g = 0; g < words1.length; g++){
                if(nounA.equals(words1[g])){
                    a.add(i);
                }
            }
            
            for(int h = 0; h < words2.length; h++){
                if(nounB.equals(words2[h])){
                    b.add(i);
                }
            }
            
//            }
//            if(dictionary.get(i).equals(nounB)){
//                b.add(i);
//            }
        }
        

//        System.out.print(a.size());
        return sap.length(a, b); //update function
    }
//
//   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
//   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
       
        if(nounA == null || nounB == null){
            throw new java.lang.IllegalArgumentException();
        }
        
        if(!this.isNoun(nounA) || !this.isNoun(nounB)){
            throw new java.lang.IllegalArgumentException();
        }
        //SAP sap = new SAP(graph);
//        int index = sap.ancestor(dictionary.get(nounA), dictionary.get(nounB));
        
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        
        for(int i = 0; i < dictionary.size(); i++){
            if(dictionary.get(i).equals(nounA)){
                a.add(i);
            }
            if(dictionary.get(i).equals(nounB)){
                b.add(i);
            }
        }
        
        int index = sap.ancestor(a, b);
        return syn.get(index);
    }

   // do unit testing of this class
    public static void main(String[] args){
        WordNet test = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
        System.out.println(test.isNoun("word"));
        System.out.println(test.isNoun("zephyr"));
        System.out.println(test.isNoun("xxx"));
        System.out.println(test.isNoun("a"));
        System.out.println(test.isNoun("a"));
        System.out.println(test.distance("zebra", "Hotei"));
    }
}