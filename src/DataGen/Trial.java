package DataGen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**For running Algos.LPS/Algos.LCS trials*/
public class Trial {

    //constructor fields
    private String name;
    private int maxAlphabet, minAlphabet, minLen, maxLen;
    private int maxTime = 10000, trials = 10;
    private Method[] funcs;
    private int argCount;

    //inner fields
    private boolean[] timedOut; //tracks timedOut trials each loop
    /* Record average computation time data for
     [implementation][structured vs. random][alphabet size (2-52)][string len 2^3-2^16]
     Each is an average of 10 rounds
     Alphabet size is the character set for unstructured strings, and is proportional to
     dictionary size in the structured case */
    private long[][][][] data;
    private SentenceGenerator sg;

    /**
     *
     * @param name txt file name to save trials as
     * @param funcs implementations to compare
     * @param minAlpha min Alphabet size (chars set/word dictionary size)
     * @param maxAlpha ^
     * @param minLen tries Strings greater than 2^minLen string
     * @param maxLen ^
     */
    public Trial(String name, Method[] funcs, int minAlpha, int maxAlpha,
                 int minLen, int maxLen) {
        this.name = name;
        this.funcs = funcs;
        this.minAlphabet = minAlpha;
        this.maxAlphabet = maxAlpha;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.argCount = funcs[0].getParameterCount();
    }

    /**
     * Records to file average execution time of the various implementations
     * on random Strings of varying alphabets, string formats, and lengths.
     * These arguments are bounded by a 10 second clock, once a trial exceeds this,
     * all harder trials receive an 'empty' (-1 second) trial time.
     *
     * times are averaged over 10 trials each.
     */
    public void run() {
        data = new long[funcs.length][2][maxAlphabet][maxLen];
        long[] result;
        printImplementations();
        for(int format = 0; format < 2; format++) {
            for (int i = 0; i < maxAlphabet - minAlphabet; i++) {
                timedOut = new boolean[funcs.length];
                sg = new SentenceGenerator(i+2);
                int l = (int) Math.pow(2,minLen-1);
                for (int j = 0; j < maxLen - minLen; j++) {
                    l = l << 1;
                    System.out.printf("Trial %s %d %d\n", format == 0 ?
                            "random" : "article", i + minAlphabet, l);
                    result = runTrial(format, i + minAlphabet, l);
                    System.out.println(Arrays.toString(result));
                    for(int k = 0; k < result.length; k++)
                        data[k][format][i][j] = result[k];
                }
            }
        }
        File f = new File("out" + File.separator + name + ".txt");
        write(f);
    }

    private long[] runTrial(int format, int alphabet, int len) {
        long[] times = new long[funcs.length];
        long time;
        for(int count = 0; count < trials; count++) {
            String[] args = new String[argCount];
            for(int argNum = 0; argNum < args.length; argNum++)
                args[argNum] = generate(format, alphabet, len);
            for(int i = 0; i < times.length; i++) {
                if(timedOut[i]) continue;
                time = -System.currentTimeMillis();
                try { funcs[i].invoke(null, (Object[]) args); }
                catch (Exception e) { System.out.println(e.toString());System.exit(1); }
                time += System.currentTimeMillis();
                if(time > maxTime)
                    timedOut[i] = true;
                times[i] = time;
            }
        } for(int i = 0; i < times.length; i++) //avg
            times[i] = timedOut[i] ? -1 : times[i]/trials;
        return times;
    }

    private String generate(int format, int alphabet, int len) {
        if(format == 0) {
            char[] arr = new char[len];
            for(int i = 0; i < arr.length; i++)
                arr[i] = (char) (65 + (int) (Math.random()*alphabet));
            return new String(arr);
        } else
            return sg.article(len);
    }

    private void write(File f) {
        BufferedWriter dataOut;
        try {
            dataOut = new BufferedWriter(new FileWriter(f));
            for(Method m: funcs)
                dataOut.write(m.getName() + "\n");
            dataOut.write(Arrays.deepToString(data));
            dataOut.write("\n");
            dataOut.close();
        } catch (IOException e) {
            System.out.println("Failed to open data.txt for output");
            System.exit(1);
        }
    }

    private void printImplementations() {
        StringBuilder sb = new StringBuilder();
        for(Method m: funcs) {
            sb.append(m.getName());
            sb.append(" ");
        }
        System.out.println(sb.toString());
    }

}
