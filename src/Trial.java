import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**For running LPS/LCSS trials*/
public class Trial {

    private boolean isLPS;
    private boolean[] timedOut; //tracks timedOut trials each loop
    //Record average computation time data for
    // [implementation][structured vs. random][alphabet size (2-52)][string len 2^3-2^16]
    // Each is an average of 10 rounds
    long[][][][] data;

    public Trial(boolean LPStrial) {
        isLPS = LPStrial;
    }

    /**
     * Records to file average execution time of the various implementations
     * on random Strings of varying alphabets, string formats, and lengths.
     * These arguments are bounded by a 10 second clock, once a trial exceeds this,
     * all harder trials receive an 'empty' (-1 second) trial time.
     *
     * In the case of LCSS the second string's parameters are upper-bounded by the first string
     *
     * times are averaged over 10 trials each.
     */
    public void run() {
        data = new long[isLPS ? 3 : 4][2][51][14];
        long[] result;
        for(int format = 0; format < 2; format++) {
            for (int i = 0; i < 51; i++) {
                timedOut = new boolean[isLPS ? 3 : 4];
                for (int j = 0; j < 14; j++) {
                    if(isLPS)
                        result = runLPSTrial(format, i + 2, (int) Math.pow(2, j + 3));
                    else
                        result = runLCSSTrial(format, i + 2, (int) Math.pow(2, j + 3));
                    for(int k = 0; k < result.length; k++)
                        data[k][format][i][j] = result[k];
                }
            }
        }
        File f = new File("out" + File.separator + (isLPS ? "lps.txt" : "lcss.txt"));
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

    private long[] runLCSSTrial(int format, int alphabet, int len) {
        long[] times = new long[4];
        String s1 = generate(format, alphabet, len);
        String s2 = generate(format, (int) (Math.random()*alphabet), (int) (Math.random()*len));
        for(int i = 0; i < times.length; i++) {
            if(timedOut[i]) {
                times[i] = -1; continue;
            }
            times[i] = -System.currentTimeMillis();
            switch (i) {
                case 0: LCSS.naive(s1, s2); break;
                case 1: LCSS.dpStd(s1, s2); break;
                case 2: LCSS.dpSkip(s1, s2); break;
                case 3: LCSS.st(s1, s2); break;
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
        return ""; //TODO
    }

    private void write(File f) {
        Writer dataOut;
        try {
            dataOut = new FileWriter(f);
        } catch (IOException e) {
            System.out.println("Failed to open data.txt for output");
            System.exit(1);
        }
        //TODO
    }

}
