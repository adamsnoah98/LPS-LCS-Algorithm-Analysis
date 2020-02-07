# LPS-LCS-Algorithm-Analysis

# Setup 

Java source code requires Java 8 or better; furthermore, test code uses JUnit 4.12, so this must be added to the project using Maven 
or another preferred method. Python visualization code is included and requires MatPlotLib. Download this to your class path
with your preferred package manager if needed.

# Longest Common Substring (LCS)
### Naive Implementation
This basic solution involves checking all possible pairs of indices between the input strings, and computing the length shared starting there. While having the benefit of being in-place, the runtime approaches cubic with `O(nmL)` - `L` being the optimal solution length.

TODO GRAPH

### Dynamic Programming
TODO first INTRO

TODO first GRAPH

TODO second INTRO

TODO second GRAPH

### Suffix Trees
TODO INTRO

TODO GRAPH

# Longest Palindromic Substring (LPS)
LPS is a special case of LCS, where the two input strings eachothers' reverse. Interestingly, we will see this gives dynamic programming much better execution times for large but limited (<2^24 character) strings.
### Naive Implementation
While this is not a truly naive solution, it is certainly the simplist of these three. The algorithm runs several iterations over the string, each time searching for a longer palindrome of length up to the optimal `L`.  Since palindromes always have at least n/2 inner palindromes the algorithm can terminate after `L<n` iterations, giving a runtime of `O(nL^2)`.

TODO GRAPH

### Dynamic Programming
The best performing algorithm for DPS (for strings under 2^24 characters), was a DP implementation, which runs in `O(P)` time, where `P` is the number of palindromic substrings.  Typically this number is `O(n)`, and the algorithm is very basic so there is little overhead. It iterates through the string, for each iteration `i` finding the longest possible odd-length palindrome centered on character `i`, and the longest even-length palindrome centered between characters `i` and `i+1`.

TODO GRAPH


### Suffix Trees
Despite having the best asympotic runtime, suffix trees performed the worst for my given sample space due to their large overhead and the absraction heavy implementation I used. The algorithm simply finds the deepest shared node in a linear generalized sized suffix tree (GST) constructed in linear time implementing Ukkonen's algorithm.

TODO GRAPH

## Further Notes

The generalized LCS problem with more strings truly displays the strength of utilizing GSTs, as the given algorithms practically solve this already.  Simply passing the additional strings to the GST is all that's required for refactoring, and the complexity of adding strings scales additively with their lengths (maintaining linear time) whereas the naive and dynamic programming solutions scale multiplicatively, and require more refactoring.  I conducted a brief DP vs. suffix tree test on the case of 3 strings, and on inputs of only 2^12 characaters dynamic programming execution times already exceeded 10s, compared to suffix trees avg sub-1ms execution time.  The GST's required inputs of 2^20 or more to see sloggy >500ms runs.

Possible improvements for these implementations include:
* Loops on naive and DP solutions could be optimized for locality.
* Using primitive node representations in the GST can reduce indirection for speed at the cost of readability.
* GST construction could skip constructing deeper than the existing shared inner nodes, and skip retracing over known shared subtrees, as LCS solutions only care about fully shared nodes in the general case. This would trim the worst-case space complexity from `O(n+m)` to `O(min(n,m))` in the standard case, and `O(n_1 + ... + n_k)` space to `O(min(n_1, ..., n_k))` space time in general.  In reality this could be a significant improvement, as the optimal solution is usually small compared to the input size. 

(All timed trials were run on a measely 16GB of 1800MHz DDR3 with a 2.7GHz intel i5 processor)
