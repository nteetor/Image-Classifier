/**
 * Returns the average pixel colors of sections of an image as a 2 dimensional
 * array in the order r, g, b.
 */
public class ImageParsing {

	/**
	 * Only takes cubes (numbers that are a square of something) returns a two
	 * dimensional array of ints. The first array shows which section of the
	 * image. i.e. if we want 9 sections [0][1][2] [3][4][5] [7][8][9] The
	 * second array shows the average R,G,B values of each section of picture.
	 */
	public static double[][] pixelColor1(String image, int section) {
		String input = image;
		Picture canvas = new Picture(input);
		int h = canvas.getHeight();
		int w = canvas.getWidth();
		int count = 0;
		int numSections = section;
		section = (int) Math.sqrt(section);
		double[][] perceptronInput = new double[4][numSections];
		int xBorder = w / section;
		int yBorder = h / section;
		int subTotal = xBorder * yBorder;
		int xCutoff = w % section;
		int yCutoff = h % section;
		for (int xStart = 0; xStart < w - xCutoff; xStart += (w / section)) {
			for (int yStart = 0; yStart < h - yCutoff; yStart += (h / section)) {
				int rSum = 0, gSum = 0, bSum = 0;
				for (int x = xStart; x < xStart + xBorder; x++) {
					for (int y = yStart; y < yStart + yBorder; y++) {
						int R = canvas.getPixelRed(x, y);
						int G = canvas.getPixelGreen(x, y);
						int B = canvas.getPixelBlue(x, y);
						rSum += R;
						gSum += G;
						bSum += B;
					}
				}
				perceptronInput[0][count] = rSum / subTotal;
				perceptronInput[1][count] = gSum / subTotal;
				perceptronInput[2][count] = bSum / subTotal;
				count++;
			}
		}
		return perceptronInput;
	}

	/**
	 * returns a 4 dimensional array. first array contains shows which section
	 * of the image. second and third arrays show x,y coordinates respectively.
	 * fourth array shows RGB values of the pixel.
	 */
	public static int[][][][] pixelColor2(String image, int section) {
		String input = image;
		Picture canvas = new Picture(input);
		int h = canvas.getHeight();
		int w = canvas.getWidth();
		int count = 0;
		int numSections = (int) Math.pow(section, 2);
		int xBorder = w / section;
		int yBorder = h / section;
		int[][][][] perceptronInput = new int[numSections][xBorder][yBorder][3];
		int xCutoff = w % section;
		int yCutoff = h % section;
		for (int xStart = 0; xStart < w - xCutoff; xStart += (w / section)) {
			for (int yStart = 0; yStart < h - yCutoff; yStart += (h / section)) {
				int x1 = 0;
				for (int x = xStart; x < (xStart + xBorder); x++) {
					int y1 = 0;
					for (int y = yStart; y < (yStart + yBorder); y++) {
						int R = canvas.getPixelRed(x, y);
						int G = canvas.getPixelGreen(x, y);
						int B = canvas.getPixelBlue(x, y);
						perceptronInput[count][x1][y1][0] = R;
						perceptronInput[count][x1][y1][1] = G;
						perceptronInput[count][x1][y1][2] = B;
						y1++;
					}
					x1++;
				}
				count++;
			}
		}
		return perceptronInput;
	}
}
