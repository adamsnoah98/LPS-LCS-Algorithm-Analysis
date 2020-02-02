package DataGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Used in Trials class for structured strings, generates random sentence-like strings
 * from a small set of base words
 *
 * The sentences follow a basic WCFG, and aren't necessarily coherent,
 * all words are lower case, and omit all punctuation excluding commas
 */
public class SentenceGenerator {

    final String[] nouns, verbs, adj, adv, preps, arts, conj;

    public SentenceGenerator(int dictionarySize) {
        nouns = new String[dictionarySize];
        loadWords(nouns, new File("dictionary" + File.separator + "nouns.txt"));
        verbs = new String[dictionarySize];
        loadWords(nouns, new File("dictionary" + File.separator + "verbs.txt"));
        adj = new String[dictionarySize/2+1];
        loadWords(nouns, new File("dictionary" + File.separator + "adj.txt"));
        adv = new String[dictionarySize/4+1];
        loadWords(nouns, new File("dictionary" + File.separator + "adv.txt"));
        preps = new String[dictionarySize/4+1];
        loadWords(nouns, new File("dictionary" + File.separator + "preps.txt"));
        arts = new String[dictionarySize/8+1];
        loadWords(nouns, new File("dictionary" + File.separator + "arts.txt"));
        conj = new String[dictionarySize/8+1];
        loadWords(nouns, new File("dictionary" + File.separator + "conj.txt"));
    }

    private void loadWords(String[] to, File from) {
        try {
            Scanner s = new Scanner(from);
            s.useDelimiter("(, |,\n)");
            for (int i = 0; i < to.length && s.hasNext(); i++)
                to[i] = s.next();
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load dict file");
            System.exit(1);
        }
    }

    public String sentence() {

        return ""; //TODO WCFG
    }

    public String article(int len) {
        StringBuilder sb = new StringBuilder();
        while(sb.length() < len)
            sb.append(sentence());
        return sb.substring(0, len);
    }

    /** Recursive Data Structure for representing basic
     * weighted context free grammars.
     */
    class WCFGRule {


    }
}
