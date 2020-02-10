# LPS-LCS-Algorithm-Analysis

The longest common substring (LCS) problem is a classic computational problem used all the time.  This project explores LCS, a special case of LCS, the longest palindromic substring (LPS) problem, and its generalizations, alongside how different problem domains can affect algorithm performance.  I implement a variety of solutions to these problems, discuss their theorectical performance, and then quantitatively examine their capabilities.  

Algorithms and data generation code is written in Java, and visualized with PyPlot.  The domain for these visualizations evaluates performance on string length (2<sup>7</sup>-2<sup>24</sup>), comparing random data with structured data in the form of Articles written as a random series of psuedo-sentences via a weighted context free grammar and small dictionary.  Additionally, the alphabet (character set size), and dictionary (word set size) are varied to test any possible performance effect.

# Setup 

Java source code requires Java 8 or better; furthermore, test code uses JUnit 4.12, so this must be added to the project using Maven 
or another preferred method. Python visualization code is included and requires MatPlotLib. Download this to your class path
with your preferred package manager if needed.

# Longest Common Substring (LCS)
### Naive Implementation
This basic solution involves checking all possible pairs of indices between the input strings, and computing the length shared starting there. While having the benefit of being in-place, the runtime approaches cubic with *O(nmL)* - *L* being the optimal solution length, and unsurprisngly, is the worst of the 4 LCS solutions here.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LCS_naive.png" width="500" height="300" />

Unsurprisngly, most of our domain is not tractable with a naive solution.  Additionally, its sensible alhpabet size would not affect performance as this impelementation leveages no aspects of its findings or input format.

### Dynamic Programming
A classic dynamic programming solution is building, bottom up, a *n x m* matrix with each entry being the length in a common substring ending at these respective indices. This is a well known *O(nm)* time and space solution; however, since only the previous row is needed to fill the next, this first implementation improves the space to *O(min(n,m))*.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LCS_dpStd.png" width="500" height="300" />

At first glance it may be surprising to see how similar this looks to our naive performance; however, recalling the similar runtimes *O(nmL)* vs *O(nm)* validates this finding as typically *L <<< n + m*, and the vast majority of naive iterations end extremely quick.

Improving upon this classic algorithm, notice that if you know two indices *i*, *j*, don't match, then any optimal common substring must include *i+l* and *j+l* respectively - if *l* is the current best length - if the optimal solution lies between these bounds. This is called skipping, and we can use it to reduce the number of locations we check.  In the best case, the optimal solution is found immediately and the runtime is *O(nm/L)*, while the worst case remains *O(nm)*.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LCS_dpSkip.png" width="500" height="300" />

My standout favorite result is this graph. First it shows a vast performance boost over the standard dynamic programming approach, as only one long substring needs to be found to take advantage of sizeable skips.  Second, it confirmed my suspicion that these implementations may have varying results with different types of strings.  The performance different is noticable, and sensible, as smaller alphabets lend themselves to more shared strings simply by chance.

### Suffix Trees
Suffix trees provide quick solutions to many string based operations, and the discovery of linear time construction in the 90s, such as [Ukkonen's Algorithm](https://www.cs.helsinki.fi/u/ukkonen/SuffixT1withFigs.pdf) - upon which this solution is derived from, allows LCS solutions by finding the deepest shared node in the suffix tree.

Below we see a solidly linear time complexity, with the even scaling on the graph below.  Besides the obvious comparisons to the other implementations, note the dependence on alphabet size. This is a likely result of two forces. The GST node implementation using HashMaps, which need to be resized, and more characters imply more nodes with shorter edges, increasing indirection within the algorithm. This effect is less pronounced on the structured data as the edges are longer (often words or fragments), and even the smallest dictionary size has a sizeable 15-20 character alphabet.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LCS_st.png" width="500" height="300" />

# Longest Palindromic Substring (LPS)
LPS is a special case of LCS, where the two input strings eachothers' reverse. Interestingly, we will see this gives dynamic programming much better execution times for large but limited (<2^24 character) strings.
### Naive Implementation
While this is not a truly naive solution, it is certainly the simplist of these three. The algorithm runs several iterations over the string, each time searching for a longer palindrome of length up to the optimal *L*.  Since palindromes always have at least *n / 2* inner palindromes the algorithm can terminate after *L < n* iterations, giving a runtime of *O(nL^2)*.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LPS_naive.png" width="500" height="300" />

### Dynamic Programming
The best performing algorithm for DPS (for strings under 2<sup>24</sup> characters), was a DP implementation, which runs in *O(P)* time, where *P* is the number of palindromic substrings.  Typically this number is *O(n)*, and the algorithm is very basic so there is little overhead. It iterates through the string, for each iteration *i* finding the longest possible odd-length palindrome centered on character *i*, and the longest even-length palindrome centered between characters *i* and *i+1*.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LPS_dp.png" width="500" height="300" />


### Suffix Trees
Despite having the best asympotic runtime, suffix trees performed the worst for my given sample space due to their large overhead and the absraction heavy implementation I used. The algorithm utilizes the LCS solution, feeding in the target string and its reverse.

<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/LPS_st.png" width="500" height="300" />

## Further Notes

The generalized k-Common Substring problem with truly displays the strength of utilizing GSTs, as the given algorithm requires no refactoring t and the complexity of adding strings scales additively with their lengths (maintaining linear time) whereas the naive and dynamic programming solutions scale multiplicatively, and require more refactoring.  I conducted a brief DP vs. suffix tree test on the case of 3 strings, and on inputs of only 2<sup>12</sup> characaters dynamic programming execution times already exceeded 10s, compared to suffix trees avg sub-1ms execution time.  The GST's required inputs of 2<sup>20</sup> or more to see sloggy >500ms runs.

### Extended Dynamic Programming Solution
<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/3-LCS_dp.png" width="425" height="225" />

### Extended Suffix Tree Solution
<img src="https://github.com/adamsnoah98/LPS-LCS-Algorithm-Analysis/blob/master/Graphs/3-LCS_st.png" width="425" height="225" />

## Possible improvements and extensions for these implementations include:
* Loops on naive and DP solutions could be optimized for locality.
* Using primitive node representations in the GST can reduce indirection for speed at the cost of readability.
* All of solutions excluding suffix trees have *straight forward* parallelizable variants that give *scalable speedups* in the base case.
* Suffix trees in the general case can be built in parallel with nodes as critical sections.
* A dedicated LPS GST constructor could elimate the need to copy and reverse the target string.
* GST construction could skip constructing deeper than the existing shared inner nodes, and skip retracing over known shared subtrees, as LCS solutions only care about fully shared nodes in the general case. This would improve the algorithm's performance in multiple ways:
    1. Space worst - *O(n<sub>1</sub>, ..., n<sub>k</sub>)* to *O(min(n<sub>1</sub>, ..., n<sub>k</sub>))*. 
    2. Time best - *O(n<sub>1</sub>, ..., n<sub>k</sub>)* to *O(k\*min(n<sub>1</sub>, ..., n<sub>k</sub>))*.
    3. Execution keeping a smaller tree reduces memory allocations, map sizes, and indirection.
    4. A simple parallel algorithm variant with best case *O(L lg k + median(n<sub>1</sub>, ..., n<sub>k</sub>))*, excluding load balancing.

  In practice this could be a significant improvement, as the optimal solution is usually small compared to the input size. 

(All timed trials were run on a measely 16GB of 1800MHz DDR3 with a 2.7GHz intel i5 processor)
