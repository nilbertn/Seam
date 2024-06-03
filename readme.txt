Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
Our algorithm uses dynamic programming and image transposition in order to
find the "minimum cumulative energy" path through each pixel. Our dist to
iterates and stores the shortest energy path.For vertical seams, our algorithm
initializes energy paths and backtracks from the least cumulative energy in the
last row to trace the seam from the bottom up. Then, our horizontal seams are
found by transposing the image and using vertical seam detection, looking at
the transpose with respect to our original orientation. Our distto and relax
methods essentially help update the path if a lower energy route is found via
the current pixel. Then, after processing, it identifies the end of the minimum
energy seam at the bottom row and traces back the optimal path using edgeTo.


/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
In general, seam carving relies strongly on the distribution of pixel energies.
It works best with images containing large areas of low pixel energy, where color
gradients are minimal because the adjacent pixel colors are similar. So,
if an image has a lot of noise between pixels, where there are intricate
patterns related to colors, then it may be hard to preserve the original image,
and our algorithm may disrupt essential features.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
250         0.153
500         0.199               1.301       0.380
1000        0.355               1.784       0.835
2000        0.663               1.868       0.901
4000        1.36                2.051       1.036
8000        2.932               2.156       1.108

Average of the log ratio is = 0.852 which is our b factor

(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
250         0.152
500         0.194               1.276       0.352
1000        0.349               1.799       0.847
2000        0.698               2.000       1.000
4000        1.324               1.897       0.924
8000        2.843               2.147       1.103

Average of the log ratio is = 0.845 which is our b factor

/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */
T = a*n^b. So, for height constant, 2.843 = a*8000^0.845.
a is approximately 0.001431
T = a*n^b. So, for width constant, 2.932 = a*8000^0.852.
a is approximately 0.001386
Multiplying these two together, we get 1.983 * 10^-6

Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~
       1.983*10^-6 * W^0.845 * H^0.852

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */




/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
We encountered a problem whereby we kept validating both the horizontal and the
vertical instead of just the vertical as the horizontal uses the vertical method
therefore that bug took us very long to spot and limited us.



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
