package picture;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /**
   * The internal image representation of this picture.
   */
  private final BufferedImage image;

  /**
   * Construct a new (blank) Picture object with the specified width and height.
   */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Construct a new Picture from the image data in the specified file.
   */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   * otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
        || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x   the x-coordinate of the pixel to be updated
   * @param y   the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
        x,
        y,
        0xff000000
            | (((0xff & rgb.getRed()) << 16)
            | ((0xff & rgb.getGreen()) << 8)
            | (0xff & rgb.getBlue())));
  }

  /**
   * Returns a String representation of the RGB components of the picture.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  // Invert
  public void invert() {
    for (int i=0; i<image.getWidth(); i++) {
      for (int j=0; j<image.getHeight(); j++) {
        Color old_colour = getPixel(i, j);
        int red = 255 - old_colour.getRed();
        int green = 255 - old_colour.getGreen();
        int blue = 255 - old_colour.getBlue();
        Color new_colour = new Color(red, green, blue);
        setPixel(i, j, new_colour);
      }
    }
  }

  //Grayscale
  public void grayscale() {
    for (int i=0; i<image.getWidth(); i++) {
      for (int j=0; j<image.getHeight(); j++) {
        Color old_colour = getPixel(i, j);
        int old_red = old_colour.getRed();
        int old_green = old_colour.getGreen();
        int old_blue = old_colour.getBlue();
        int avg = (old_red + old_green + old_blue) / 3;
        Color new_colour = new Color(avg, avg, avg);
        setPixel(i, j, new_colour);
      }
    }
  }


  //Rotate
  public Picture rotate(double degree) {
    Picture picture = new Picture(image.getHeight(), image.getWidth());
    if (degree == 90) {
      for (int i = 0; i < image.getWidth(); i++) {
        for (int j = 0; j < image.getHeight(); j++) {
          Color colour = getPixel(i, j);
          int x = -j + image.getHeight() - 1;
          int y = i;
          picture.setPixel(x, y, colour);
        }
      }
    }
    return picture;
  }

  //Flip horizontal
  public Picture flipHorizontal() {
    Picture picture = new Picture(image.getWidth(), image.getHeight());
    for (int i=0; i<image.getWidth(); i++) {
      for (int j=0; j<image.getHeight(); j++) {
        Color colour = getPixel(i, j);
        int x = -i + image.getWidth() - 1;
        int y = j;
        picture.setPixel(x, y, colour);
      }
    }
    return picture;
  }

  //Flip vertical
  public Picture flipVertical() {
    Picture picture = new Picture(image.getWidth(), image.getHeight());
    for (int i=0; i<image.getWidth(); i++) {
      for (int j=0; j<image.getHeight(); j++) {
        Color colour = getPixel(i, j);
        int x = i;
        int y = -j + image.getHeight() - 1;
        picture.setPixel(x, y, colour);
      }
    }
    return picture;
  }

  //Blend
  public Picture blend(List<Picture> pictures) {
    int n = pictures.size();
    int height = pictures.get(0).getHeight();
    int width = pictures.get(0).getWidth();
    for (Picture p : pictures) {
      if (p.getHeight() < height) {
        height = p.getHeight();
      }
      if (p.getWidth() < width) {
        width = p.getWidth();
      }
    }
    Picture picture = new Picture(width, height);
    for (int i=0; i<width; i++) {
      for (int j=0; j<height; j++) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Picture p : pictures) {
          Color colour = p.getPixel(i, j);
          red += colour.getRed();
          green += colour.getGreen();
          blue += colour.getBlue();
        }
        int red_avg = red / n;
        int green_avg = green / n;
        int blue_avg = blue / n;
        Color new_colour = new Color(red_avg, green_avg, blue_avg);
        picture.setPixel(i, j, new_colour);
      }
    }
    return picture;
  }

  //Blur
  public Picture blur() {
    Picture picture = new Picture(image.getWidth(), image.getHeight());
    for (int i=0; i<image.getWidth(); i++) {
      for (int j=0; j<image.getHeight(); j++) {
        int red = 0;
        int green = 0;
        int blue = 0;
        boolean isAtBoundary = false;
        for (int x=-1; x<2; x++) {
          if (isAtBoundary) {
            break;
          }
          for (int y=-1; y<2; y++) {
            if (contains(i+x, j+y)) {
              red += getPixel(i+x, j+y).getRed();
              green += getPixel(i+x, j+y).getGreen();
              blue += getPixel(i+x, j+y).getBlue();
            } else {
              picture.setPixel(i,j,getPixel(i,j));
              isAtBoundary = true;
              break;
            }
          }
        }
        if (!isAtBoundary) {
          picture.setPixel(i, j, new Color(red/9, green/9, blue/9));
        }
      }
    }
    return picture;
  }

  //Mosaic
//  public Picture mosaic(List<Picture> pictures, int tileSize) {
//    int n = pictures.size();
//    int height = pictures.get(0).getHeight();
//    int width = pictures.get(0).getWidth();
//    for (Picture p : pictures) {
//      if (p.getHeight() < height) {
//        height = p.getHeight();
//      }
//      if (p.getWidth() < width) {
//        width = p.getWidth();
//      }
//    }
//    height = Math.floorMod(height, tileSize) * tileSize;
//    width = Math.floorMod(width, tileSize) * tileSize;
//    Picture picture = new Picture(height, width);
//    Color init = pictures.get(0).getPixel(0,0);
//    picture.setPixel(0, 0, init);
//    for (int i=0; i<width; i++) {
//      int m = i + 1;
//      for (int j=0; j<height; j++) {
//        int index = m % n;
//        Color col1 = pictures.get(index).getPixel(i+1, j);
//        Color col2 = pictures.get(index).getPixel(i, j+1);
//        picture.setPixel(i+1, j, col1);
//        picture.setPixel(i, j+1, col2);
//        m += 1;
//      }
//    }
//    return picture;
//  }
}
