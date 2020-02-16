"""
Visualizer for LPS/LCSS algos
"""
import matplotlib.colors as c
import matplotlib.pyplot as pp
import matplotlib.ticker as ticker
import numpy as np
import os.path as p
import re

MAXDATAPOINT = 10000
formats = ["Random", "Article"]

colors = pp.get_cmap('coolwarm')
loc = ticker.FixedLocator([(1.5 if i % 2 else 1)*(2**int(i/2)) for i in range(0, 40)])
bar_formatter = ticker.FixedFormatter([2**i for i in range(13)])
norm = c.LogNorm(vmin=1, vmax=MAXDATAPOINT)


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
            if stack[-1][-1] <= 0:
                stack[-1][-1] = 1
            if stack[-1] == 0:
                stack[-1] = 1
    # next few lines of code work around an old bug from when the Data files were built
    # its not needed nor problematic if data is recompiled
    for i in range(len(data)):
        for j in range(len(data[i])):
            data[i][j] = data[i][j][:domain[1] - domain[0]]
            for k in range(len(data[i][j])):
                data[i][j][k] = data[i][j][k][:domain[3]-domain[2]]
    return dict(zip(funcs, data)), domain


# Graph each implementation/input format
def visualize(data_group, domain, problem_name, alt_param="alphabet"):
    for k in data_group.keys():
        make_figure(data_group[k], domain, k, problem_name)


# Create contour map of execution time vs. input size and alphabet size
def make_figure(dataSet, domain, graphName, problemName):
    param = "ratio" if problemName == "Ratio-LCS" else "alphabet"
    rows = 1 if problemName == "Ratio-LCS" else 2
    fig, axes = pp.subplots(nrows=rows)
    if not hasattr(axes, "__iter__"):
        axes = [axes]
    for i in range(len(axes)):
        vis = graph(axes[i], dataSet[i], domain, formats[i], alt_param=param)
        fig.colorbar(vis, ax=axes[i], format=bar_formatter)

    fig.suptitle(problemName + ": Average Execution Time (ms)")
    fig.tight_layout(rect=[0, 0.03, 1, 0.95])
    pp.savefig(".." + p.sep + "Graphs" + p.sep + problemName+"_"+graphName)


# Handles all graph plotting reation cases
def graph(axes, dataSet, domain, graph_name, alt_param="alphabet"):
    xs = np.array([2**i for i in range(domain[2], domain[3])])
    ys = range(domain[0], domain[1])
    if alt_param == "alphabet":
        ys = np.array(ys if graph_name == "Random" else [int(13*i/4)+1 for i in ys])
    else:
        ys = np.array([2**-r for r in ys])
    zs = np.array(dataSet)
    zs = np.ma.masked_where(zs <= 0, zs)

    axes.set_xscale('log', basex=2)
    if not alt_param == "alphabet":
        axes.set_yscale('log', basey=2)
    x_label = "String Length" if alt_param == "alphabet" else "String 1 Length"
    axes.set_xlabel(x_label)
    y_label = "Alphabet Size" if graph_name == "Random" else "Dictionary Size"
    if alt_param != "alphabet":
        y_label = "String Length Ratio (s2/s1)"
    axes.set_ylabel(y_label)
    axes.set_title(graph_name)

    vis = axes.contourf(xs, ys, zs, locator=loc, cmap=colors, norm=norm)
    return vis


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


"""================= LCS3 ==============="""

LCS3_data_file = open(".." + p.sep + "Data" + p.sep + "lcs3.txt", "r")
LCS3_data, domain = load_data(LCS3_data_file)
visualize(LCS3_data, domain, "3-LCS")
LCS_data_file.close()

"""=== LCS Ratio (differing str lens)===="""

LCSR_data_file = open(".." + p.sep + "Data" + p.sep + "lcsRatio.txt", "r")
LCSR_data, domain = load_data(LCSR_data_file)
visualize(LCSR_data, domain, "Ratio-LCS")
LCSR_data_file.close()
