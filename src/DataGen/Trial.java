package DataGen;

import Algos.LCS;
import Algos.LPS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**For running Algos.LPS/Algos.LCS trials*/
public class Trial {

    private boolean isLPS;
    private boolean[] timedOut; //tracks timedOut trials each loop
    /* Record average computation time data for
     [implementation][structured vs. random][alphabet size (2-52)][string len 2^3-2^16]
     Each is an average of 10 rounds
     Alphabet size is the character set for unstructured strings, and is proportional to
     dictionary size in the structured case */
    private long[][][][] data;
    private SentenceGenerator sg;

    public Trial(boolean LPSTrial) {
        isLPS = LPSTrial;
    }

    /**
     * Records to file average execution time of the various implementations
     * on random Strings of varying alphabets, string formats, and lengths.
     * These arguments are bounded by a 10 second clock, once a trial exceeds this,
     * all harder trials receive an 'empty' (-1 second) trial time.
     *
     * In the case of Algos.LCS the second string's parameters are upper-bounded by the first string
     *
     * times are averaged over 10 trials each.
     */
    public void run() {
        data = new long[isLPS ? 3 : 4][2][51][14];
        long[] result;
        for(int format = 0; format < 2; format++) {
            for (int i = 0; i < 51; i++) {
                timedOut = new boolean[isLPS ? 3 : 4];
                sg = new SentenceGenerator(i+2);
                for (int j = 0; j < 14; j++) { //TODO avg over 10 times
                    if(isLPS)
                        result = runLPSTrial(format, i + 2, (int) Math.pow(2, j + 3));
                    else
                        result = runLCSTrial(format, i + 2, (int) Math.pow(2, j + 3));
                    for(int k = 0; k < result.length; k++)
                        data[k][format][i][j] = result[k];
                }
            }
        }
        File f = new File("out" + File.separator + (isLPS ? "lps.txt" : "lcs.txt"));
        write(f);
    }

    private long[] runLPSTrial( int format, int alphabet, int len) {
        long[] times = new long[3];
        String s = generate(format, alphabet, len);
        for(int i = 0; i < times.length; i++) {
            if(timedOut[i]) {
                times[i] = -1; continue;
            }
            times[i] = -System.currentTimeMillis();
            switch (i) {
                case 0: LPS.naive(s); break;
                case 1: LPS.dp(s); break;
                case 2: LPS.st(s); break;
            }
            times[i] += System.currentTimeMillis();
            if(times[i] > 10000) {
                times[i] = -1;
                timedOut[i] = true;
            }
        }
        return times;
    }

    private long[] runLCSTrial(int format, int alphabet, int len) {
        long[] times = new long[4];
        String s1 = generate(format, alphabet, len);
        String s2 = generate(format, (int) (Math.random()*alphabet), (int) (Math.random()*len));
        for(int i = 0; i < times.length; i++) {
            if(timedOut[i]) {
                times[i] = -1; continue;
            }
            times[i] = -System.currentTimeMillis();
            switch (i) {
                case 0: LCS.naive(s1, s2); break;
                case 1: LCS.dpStd(s1, s2); break;
                case 2: LCS.dpSkip(s1, s2); break;
                case 3: LCS.st(s1, s2); break;
            }
            times[i] += System.currentTimeMillis();
            if(times[i] > 10000) {
                times[i] = -1;
                timedOut[i] = true;
            }
        }
        return times;
    }

    private String generate(int format, int alphabet, int len) {
        if(format == 0) {
            char[] arr = new char[len];
            for(int i = 0; i < arr.length; i++)
                arr[i] = (char) (65 + (int) (Math.random()*alphabet));
            return new String(arr);
        } else {
            return sg.article(len);
        }
    }

    private void write(File f) {
        ObjectOutputStream dataOut;
        try {
            dataOut = new ObjectOutputStream(new FileOutputStream(f));
            dataOut.writeObject(data);
            dataOut.close();
        } catch (IOException e) {
            System.out.println("Failed to open data.txt for output");
            System.exit(1);
        }
    }

}