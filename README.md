# LPS-LCS-Algorithm-Analysis

# Setup 

Java source code requires Java 8 or better; furthermore, test code uses JUnit 4.12, so this must be added to the project using Maven 
or another preferred method. Python visualization code is included and requires MatPlotLib. Download this to your class path
with your preferred package manager if needed.

# Longest Common Substring (LCS)
### Naive Implementation

### Dynamic Programming

### Suffix Trees

# Longest Palindromic Substring (LPS)
### Naive Implementation

### Dynamic Programming

### Suffix Trees

## Further Notes

The generalized LCS problem with more strings truly displays the strength of utilizing GSTs, as the given algorithms practically solve this already.  Simply passing the additional strings to the GST is all that's required for refactoring, and the complexity of adding strings scales additively with their lengths (maintaining linear time), whereas the naive and dynamic programming solutions scale multiplicatively, and require more refactoring.  I conducted a breif DP vs. suffix tree test on the case of 3 strings, and on inputs of only 2^12 characaters dynamic programming execution times already exceeded 1200ms, compared to suffix trees avg 0ms execution time.  The GST's required inputs of 2^20 or more to see the same sloggy 1200ms.

Possible improvements for these implementations include:
* Loops on naive and DP solutions could be optimized for locality.
* Using primitive node representations in the GST can reduce indirection for speed at the cost of readability.
* GST construction could skip constructing deeper than the existing shared inner nodes, as LCS solutions only care
about fully shared nodes in the general case. This would trim the time and space complexity from `O(n+m)` to `O(min(n,m))` 
in the standard case (assuming prior knowledge of string lengths). 
