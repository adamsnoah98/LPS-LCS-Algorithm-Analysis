"""
Visualizer for LPS/LCSS algos
"""
import numpy as np
from matplotlib import pyplot as pp
import io
import os.path as p


def loadData(file):
    tegmp = file.read()
    temp = temp.split(",")
    stack = []
    current = []
    for s in temp:
        if s.finds("[") > -1:
            current = []
            stack.append(current)
        current.append((int) s.replace("[", "").replace("]", ""))
        if s.finds("]") > -1:
            current = stack.pop()
    return current

#Graph each implementation/input format
def visualize(dataGroup, names):
    for i in range(len(names)):
        for j in range(len(i)):
            graph(dataGroup[i][j], names[i][j])

#Create contour map of execution time vs. input size and alphabet size
def graph(dataSet, graphName):
    return


############ LPS ############

LPSdataFile = open("out" + p.sep + "lps.txt", "r")
LPSdata = loadData(LPSdataFile)
visualize(LPSdata, []) ##TODO
LPSdataFile.close()

############ LCS ############

LCSdataFile = open("out" + p.sep + "lcs.txt", "r")
LCSdata = loadData(LCSdataFile)
visualize(LCSdata, []) ##TODO
LCSdataFile.close()
