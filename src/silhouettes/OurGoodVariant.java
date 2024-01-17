package silhouettes;

import java.awt.image.BufferedImage;

public class OurGoodVariant{
    private static final int INTENSITY = 128;

    private int[][] array;
    private boolean[][] visitedArray;
    private int silhouetteCounter = 1;
    private int counter = 0;

    protected int findSilhouettes(BufferedImage image) {
        array = new int[image.getHeight()][image.getWidth()];
        visitedArray = new boolean[image.getHeight()][image.getWidth()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int alpha = (rgb >> 24) & 0xFF;

                if (green < INTENSITY && blue < INTENSITY && red < INTENSITY && alpha > INTENSITY) {
                    array[y][x] = 1;
                } else {
                    array[y][x] = 0;
                }
                visitedArray[y][x] = false;
            }
        }
        markSilhouettes(image);
        return silhouetteCounter - 1;
    }

    public boolean backgroundIsWhite() {
        int whiteCounter = 0;
        int blackCounter = 0;
        for (int i = 0; i < array[0].length; i++) {
            if (array[0][i] == 0) {
                whiteCounter++;
            } else {
                blackCounter++;
            }
        }
        return whiteCounter > blackCounter;
    }

    private void markSilhouettes(BufferedImage image) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[0].length; x++) {
                if (array[y][x] == (backgroundIsWhite() ? 1 : 0) && !visitedArray[y][x]) {
                    dfsRecursive(y, x, image);
                    if (counter > getTheAreaOfTrash(image)) {
                        silhouetteCounter++;
                    }
                    counter = 0;
                } else {
                    visitedArray[y][x] = true;
                }
            }
        }
    }

    private void dfsRecursive(int y, int x, BufferedImage image) {
        visitedArray[y][x] = true;
        array[y][x] = silhouetteCounter;
        counter++;

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for (int[] dir : directions) {
            int newY = y + dir[0];
            int newX = x + dir[1];

            if (isValidPoint(newY, newX, image) && !visitedArray[newY][newX] && array[newY][newX] == (backgroundIsWhite() ? 1 : 0)) {
                dfsRecursive(newY, newX, image);
            }
        }
    }

    private boolean isValidPoint(int y, int x, BufferedImage image) {
        return y >= 0 && x >= 0 && y < image.getHeight() && x < image.getWidth();
    }

    private double getTheAreaOfTrash(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        double area = height * width;
        return (area / 100) * 0.1; // per cent
    }
}
