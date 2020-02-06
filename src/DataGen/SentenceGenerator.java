package DataGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Extremely bare bones class to aid testing
 *
 * Used in Trials class for structured strings, generates random sentence-like strings
 * from a small set of base words
 *
 * The sentences follow a basic WCFG, and aren't necessarily coherent,
 * all words are lower case, and omit all punctuation excluding periods.
 */
public class SentenceGenerator {

    final String[] nouns, verbs, adj, adv, preps, arts, conj;
    private final String[][] dictionary;
    private StringBuilder sb;

    public SentenceGenerator(int dictionarySize) {
        nouns = new String[dictionarySize];
        loadWords(nouns, new File("dictionary" + File.separator + "nouns.txt"));
        verbs = new String[dictionarySize];
        loadWords(verbs, new File("dictionary" + File.separator + "verbs.txt"));
        adj = new String[dictionarySize/2+1];
        loadWords(adj, new File("dictionary" + File.separator + "adj.txt"));
        adv = new String[dictionarySize/4+1];
        loadWords(adv, new File("dictionary" + File.separator + "adv.txt"));
        preps = new String[dictionarySize/4+1];
        loadWords(preps, new File("dictionary" + File.separator + "preps.txt"));
        arts = new String[dictionarySize/8+1];
        loadWords(arts, new File("dictionary" + File.separator + "arts.txt"));
        conj = new String[dictionarySize/8+1];
        loadWords(conj, new File("dictionary" + File.separator + "conj.txt"));
        dictionary = new String[][] {nouns, verbs, adj, adv, preps, arts, conj};
    }

    private void loadWords(String[] to, File from) {
        try {
            Scanner s = new Scanner(from);
            s.useDelimiter("\\s");
            for (int i = 0; i < to.length && s.hasNext(); i++)
                to[i] = s.next();
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load dict file");
            System.exit(1);
        }
    }

    /**
     * @param len article length
     * @return A series of sentences appended together.
     */
    public String article(int len) {
        StringBuilder sb = new StringBuilder();
        while(sb.length() < len) {
            sb.append(sentence());
        }
        return sb.substring(0, sb.lastIndexOf(".") + 1);
    }

    /**
     * Generate a random psuedo sentence using weighted rules for the following:
     *      clause
     *      noun phrase
     *      verb phrase
     *      prepositional phrase
     *      noun descriptor
     *      verb descriptor
     * @return aforementioned sentence
     */
    public String sentence() {
        sb = new StringBuilder();
        clause();
        if(Math.random() > 0.75) {
            addPOS(6); //conjunction
            clause();
        }
        return sb.substring(0, sb.length()-1) + ". ";
    }

    private void clause() {
        np(); //subject
        vp(); //predicate
        if(Math.random() > 0.50) //direct object
            np();
    }

    private void np() {
        nd();
        while(Math.random() > 0.75) {
            addPOS(6); //conjunction
            nd();
        }
    }

    private void vp() {
        vd();
        while(Math.random() > 0.75) {
            addPOS(6); //conjunction
            vd();
        }
    }

    private void pp() {
        addPOS(4);
        nd();
    }

    private void nd() {
        if(Math.random() > 0.5)
            addPOS(5);
        while(Math.random() > 0.75) {
            if(Math.random() > 0.85)
                addPOS(3); //adverb
            addPOS(2); //adjective
        }
        addPOS(0); //noun
        while (Math.random() > 0.8)
            pp(); //prep phrase
    }

    private void vd() {
        while(Math.random() > 0.75)
            addPOS(3); //adverb
        addPOS(1); //noun
        while (Math.random() > 0.8)
            pp(); //prep phrase
    }

    /** returns a random word from the specified POS, includes space */
    private void addPOS(int index) {
        String[] pos = dictionary[index];
        sb.append(pos[(int) (Math.random()*pos.length)]);
        sb.append(" ");
    }

    /**Test program prints a series of random articles*/
    public static void main(String[] args) {
        System.out.println("Small:\n");
        SentenceGenerator sg = new SentenceGenerator(5);
        for(int i = 0; i < 5; i++)
            System.out.printf("%s\n\n", sg.article(50+10*i));

        System.out.println("Large:\n");
        sg = new SentenceGenerator(40);
        for(int i = 0; i < 5; i++)
            System.out.printf("%s\n\n", sg.article(50+10*i));
    }
}
