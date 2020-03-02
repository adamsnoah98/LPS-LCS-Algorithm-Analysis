package SuffixTree;

import java.util.HashMap;
import java.util.Map;

/**
 * Class which represents a preprocessed Range Minimum Query (RMQ) solution for some specified tree
 *
 * Linear time construction and Constant time LCA retrieval
 *
 * Processing based on algorithm presented by Bender & Farach-Colton (2000)
 */
public class RMQ {

    //LPS preprocessing helpers
    private Node root;
    private int blkCt, blkSize, qryCt, size;
    private int[] flatTree;
    private Map<Integer, Node> flatMap;
    private int[] firsts;
    private int[][] normalBlocks;
    private int[] blockPointers;
    private int[] blockOffsets;
    private RMQ sparseTreeMins;

    public RMQ(Node root, int size) {
        this.root = root;
        this.size = size;
        blkCt = (int) Math.ceil(Math.sqrt(size))/2 + 1;
        blkSize = (int) Math.ceil(Math.log(size)/Math.log(2));
        qryCt = blkSize * (blkSize -1) / 2;
        flatten();
        initNormalBlocks();
        rmqInit();
    }

    private RMQ(int[] mins) {
        //TODO
    }

    private void flatten() {
        int lastDepth = -1, lastLevel = -1, i = 0;
        flatTree = new int[size*2 - 1];
        flatMap = new HashMap<>();
        Node n = root;
        flatTree[0] = 0;
        do {
            n.level = lastLevel + n.depth > lastDepth ? 1 : -1;
            lastDepth = n.depth;
            lastLevel = n.level;
            flatTree[i] = n.level;
            flatMap.put(i++, n);
            n = n.walk();
        } while (i < flatTree.length);
    }

    private void initNormalBlocks() {
        normalBlocks = new int[blkCt][qryCt];
        int i, vector, counter, best, bestIndex;
        for(i = 0; i < normalBlocks.length - 1; i++) { //fill in query returns
            initNormalBlock(normalBlocks[i], i, blkSize);
        }
        initNormalBlock(normalBlocks[i], i, size % blkSize);
    }

    private void initNormalBlock(int[] block, int vector, int size) { //TODO ditch size?
        int counter, best, bestIndex, tempV;
        for(int i = 0; i < blkSize; i++)
            for(int j = i + 1; j < blkSize; j++) {
                counter = 0;
                best = blkSize;
                bestIndex = 0;
                tempV = vector >> i;
                for(int k = i; k < j; k++) {
                    counter += tempV % 2 > 0 ? 1 : -1;
                    if(counter < best) {
                        best = counter;
                        bestIndex = k;
                    }
                    vector >>= 1;
                }
                block[i*blkSize + j] = bestIndex;
            }
    }

    private void rmqInit() {
        blockPointers = new int[blkCt];
        blockOffsets = new int[blkCt];
        int[] mins = new int[blkCt];
        for(int i = 0; i < size; i += blkSize) {
            int blockIndex = 0;
            mins[i/blkSize] = LCAindex(i, i + blkSize - 1);
            blockOffsets[i/blkSize] = flatTree[i];
            for(int j = 1; j < blkSize; j++) {
                blockIndex <<= 1;
                if(flatTree[j-1] < flatTree[j]) blockIndex++;
            }
            blockPointers[i/blkSize] = blockIndex;
        }
        //TODO for last block, check edge case exact multiple
        sparseTreeMins = new RMQ(mins);
    }

    private int LCAindex(int i, int j) {
        assert i >= j;
        if(i == j)
            return i;
        int iBlock = i / blkSize;
        int jBlock = j / blkSize;
        int iMin, jMin, interMin;
        if(iBlock == jBlock) //intra block
            return iBlock*blkSize + normalBlocks[blockPointers[iBlock]][i*blkSize +j];
        else {//inter block
            iMin = LCAindex(i, (iBlock+1)*blkSize - 1);
            jMin = LCAindex(j, (jBlock+1)*blkSize - 1);
            interMin = sparseTreeMins.LCAindex((iBlock+1)*blkSize, jBlock*blkSize);
            //TODO min of their actual values (NOT indices)
            return Math.min(Math.min(iMin, jMin), interMin);
        }
    }

    public Node LCA(int i, int j) {
        return flatMap.get(LCAindex(i, j));
    }
}
