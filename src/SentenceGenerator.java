import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Used in Trials class for structured strings, generates random sentences
 * from a small(ish) set of base words
 *
 * The sentences follow a basic WCFG, and aren't necessarily coherent
 */
public class SentenceGenerator {

    String[] nouns, verbs, adj, adv, preps, arts, conj;


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
        return ""; //TODO
    }

}
