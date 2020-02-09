"""
Visualizer for LPS/LCSS algos
"""
import numpy as np
from matplotlib.ticker import MaxNLocator
import matplotlib.pyplot as pp
import os.path as p
import re


MAXDATAPOINT = 10000
formats = ["Random", "Article"]


def load_data(file):
    domain = [int(i) for i in file.readline().split(" ")]
    contents = file.read()
    funcs = contents[:contents.find("[")].split("\n")
    atoms = re.findall(r"(\[|\]|-?[0-9]+)", contents)
    stack = []
    data = None
    for atom in atoms:
        if atom == "[":
            stack.append([])
            if len(stack) > 1:
                stack[-2].append(stack[-1])
            else:
                data = stack[0]
        elif atom == "]":
            stack.pop(-1);
        else:
            atom = int(atom)
            stack[-1].append(MAXDATAPOINT if atom == -1 else atom)
    # next few lines of code work around an old bug from when the Data files were built
    # its not needed nor problematic if data is recompiled
    for i in range(len(data)):
        for j in range(len(data[i])):
            data[i][j] = data[i][j][:domain[1] - domain[0]]
            for k in range(len(data[i][j])):
                data[i][j][k] = data[i][j][k][:domain[3]-domain[2]]
    return dict(zip(funcs, data)), domain


# Graph each implementation/input format
def visualize(dataGroup, domain, problemName):
    for k in dataGroup.keys():
        graph(dataGroup[k], domain, k, problemName)


# Create contour map of execution time vs. input size and alphabet size
def graph(dataSet, domain, graphName, problemName):
    ys = np.array(range(domain[0], domain[1]))
    xs = np.array([2**i for i in range(domain[2], domain[3])])
    z0s = np.array(dataSet[0])
    z1s = np.array(dataSet[1])

    colors = pp.get_cmap('PiYG')
    fig, (g0, g1) = pp.subplots(nrows=2)
    g0.set_xscale('log', basex=2)
    g1.set_xscale('log', basex=2)

    lvls = MaxNLocator(nbins=40).tick_values(z0s.min(), z0s.max())
    vis0 = g0.contourf(xs, ys, z0s, levels=lvls, cmap=colors)
    vis1 = g1.contourf(xs, ys, z1s, levels=lvls, cmap=colors)
    fig.colorbar(vis0, ax=g0)
    fig.colorbar(vis1, ax=g1)
    g0.set_title(graphName + ": " + formats[0])
    g1.set_title(graphName + ": " + formats[1])
    fig.tight_layout()
    pp.savefig(".." + p.sep + "Graphs" + p.sep + problemName+"_"+graphName)


"""================= LPS ==============="""


LPS_data_file = open(".." + p.sep + "Data" + p.sep + "lps.txt", "r")
LPS_data, domain = load_data(LPS_data_file)
visualize(LPS_data, domain, "LPS")
LPS_data_file.close()


"""================= LCS ==============="""

LCS_data_file = open(".." + p.sep + "Data" + p.sep + "lcs.txt", "r")
LCS_data, domain = load_data(LCS_data_file)
visualize(LCS_data, domain, "LCS")
LCS_data_file.close()
