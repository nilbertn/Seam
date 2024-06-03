import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
    private Picture picture; // instance picture variable
    private int width, height; // height and width

    // creates a seam carver object from an input picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Constructor argument is null");
        }
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of each pixel (x, y)
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                    "coordinate is out of range");
        }
        // get color using based on border-non-border
        Color left = picture.get((x - 1 + width) % width, y);
        Color right = picture.get((x + 1) % width, y);
        Color up = picture.get(x, (y - 1 + height) % height);
        Color down = picture.get(x, (y + 1) % height);

        // difference in RGB values
        double rx = right.getRed() - left.getRed();
        double gx = right.getGreen() - left.getGreen();
        double bx = right.getBlue() - left.getBlue();
        double ry = down.getRed() - up.getRed();
        double gy = down.getGreen() - up.getGreen();
        double by = down.getBlue() - up.getBlue();

        // calculate squared gradient and find sum
        double deltaX2 = rx * rx + gx * gx + bx * bx;
        double deltaY2 = ry * ry + gy * gy + by * by;

        return Math.sqrt(deltaX2 + deltaY2);
    }

    // indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposePicture();
        int[] seam = findVerticalSeam();
        transposePicture();
        return seam;
    }

    // indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width][height];
        double[][] energy = new double[width][height];
        int[][] edgeTo = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energy[x][y] = energy(x, y);
            }
        }

        for (int x = 0; x < width; x++) {
            distTo[x][0] = energy[x][0];
        }

        // implementation to find the seam
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double minDist = distTo[x][y - 1];
                int minX = x;

                if (x > 0 && distTo[x - 1][y - 1] < minDist) {
                    minDist = distTo[x - 1][y - 1];
                    minX = x - 1;
                }

                if (x < width - 1 && distTo[x + 1][y - 1] < minDist) {
                    minDist = distTo[x + 1][y - 1];
                    minX = x + 1;
                }

                distTo[x][y] = energy[x][y] + minDist;
                edgeTo[x][y] = minX;
            }
        }

        // Find the bottom of the min-seam
        double minTotalEnergy = Double.POSITIVE_INFINITY;
        int minRow = 0;
        for (int x = 0; x < width; x++) {
            if (distTo[x][height - 1] < minTotalEnergy) {
                minTotalEnergy = distTo[x][height - 1];
                minRow = x;
            }
        }

        // Reconstruct the seam
        int[] seam = new int[height];
        for (int y = height - 1; y >= 0; y--) {
            seam[y] = minRow;
            minRow = edgeTo[minRow][y];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Seam cannot be null");
        }
        if (height() <= 1) {
            throw new IllegalArgumentException(
                    "Picture height is too small to remove seam");
        }
        /* if (seam.length != width) {
            throw new IllegalArgumentException(
                    "Seam length should be equal to the width");
        }

         */

        transposePicture(); // transposes the image
        removeVerticalSeam(seam); // remove vertical seam
        transposePicture(); // reset to original
    }


    // removes the vertical seam
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Seam cannot be null");
        }
        if (width() <= 1) {
            throw new IllegalArgumentException(
                    "Picture width is too small to remove seam");
        }
        if (seam.length != height) {
            throw new IllegalArgumentException(
                    "Seam length should be equal to the height");
        }
        validate(seam);

        // new picture
        Picture newPicture = new Picture(width - 1, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0, newx = 0; x < width; x++) {
                if (x != seam[y]) {
                    newPicture.set(newx++, y, picture.get(x, y));
                }
            }
        }


        // update the Picture, width, and height
        picture = newPicture;
        width = picture.width();
        height = picture.height();
    }

    // check validity of the seam
    private void validate(int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException(
                        "Invalid seams");
            }
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException("Out of bounds");
            }
        }
    }

    //  transpose the picture by swapping row and columns
    private void transposePicture() {
        int temp = width;
        int temp2 = height;
        Picture transposed = new Picture(temp2, temp);
        for (int x = 0; x < width; x++) { // copy pixels to transposed picture
            for (int y = 0; y < height; y++) {
                transposed.set(y, x, picture.get(x, y));
            }
        }
        // Update the picture, the height and the width
        picture = transposed;
        width = temp2;
        height = temp;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]); // input file
        SeamCarver sc = new SeamCarver(picture);
        sc.picture(); // show the picture
        StdOut.printf("Picture has width %d and height %d \n", sc.width(),
                      sc.height());
        // test energy by printing matrix
        StdOut.println("Energy");
        int width = sc.width();
        int height = sc.height();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                StdOut.printf("%f\n", sc.energy(x, y));
            }
        }

        // Print vertical seam
        int[] verticalSeam = sc.findVerticalSeam();
        StdOut.println("vertical seam:");
        for (int y = 0; y < sc.height(); y++) {
            StdOut.printf("(%d, %d)\n", verticalSeam[y], y);
        }

        // Remove vertical seam
        sc.removeVerticalSeam(verticalSeam);
        StdOut.printf("width of image: %d\n", sc.width());

        // Print horizontal seam
        int[] horizontalSeam = sc.findHorizontalSeam();
        StdOut.println("horizontal seam:");
        for (int x = 0; x < sc.width(); x++) {
            StdOut.printf("(%d, %d)\n", x, horizontalSeam[x]);
        }

        // Remove the horizontal seam
        sc.removeHorizontalSeam(horizontalSeam);
        StdOut.printf("horizontal seam: %d\n", sc.height());


        /*
        // Testing for Readme
        Picture picture = new Picture(args[0]); // input file
        SeamCarver sc = new SeamCarver(picture);
        Stopwatch sw = new Stopwatch();

        int removeRows = Integer.parseInt(args[1]);
        int removeColumns = Integer.parseInt(args[2]);

        for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }

        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }


        StdOut.printf("new image size is %d columns by %d rows\n",
        sc.width(), sc.height());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
    }

         */
    }
}
