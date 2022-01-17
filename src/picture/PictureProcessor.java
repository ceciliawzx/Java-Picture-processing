package picture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PictureProcessor {

  public static void main(String[] args) {

    String type = args[0];
    if (type == "invert") {
      String input = args[1];
      String output = args[2];
      Picture picture = new Picture(input);
      picture.invert();
      picture.saveAs(output);
    }

    if (type == "grayscale") {
      String input = args[1];
      String output = args[2];
      Picture picture = new Picture(input);
      picture.grayscale();
      picture.saveAs(output);
    }

    if (type == "rotate") {
      double degree = Integer.parseInt(args[1]);
      String input = args[2];
      String output = args[3];
      Picture picture = new Picture(input);
      if (degree == 90) {
        picture.rotate(90).saveAs(output);
      }
      else if (degree == 180) {
        picture.rotate(90).rotate(90).saveAs(output);
      }
      else if (degree == 270) {
        picture.rotate(90).rotate(90).rotate(90).saveAs(output);
      } else {
        System.out.println("cannot rotate with this angle.");
      }
    }

    if (type == "flip") {
      String direction = args[1];
      String input = args[2];
      String output = args[3];
      Picture picture = new Picture(input);
      if (direction == "H") {
        picture.flipHorizontal().saveAs(output);
      } else {
        picture.flipVertical().saveAs(output);
      }
    }

    if (type == "blend") {
      int n = args.length;
      List<Picture> pictures = new ArrayList<>(List.of());
      Picture picture = new Picture(args[1]);
      String output = args[n-1];
      for (int i=1; i<n-1; i++) {
        pictures.add(new Picture(args[i]));
      }
      picture.blend(pictures).saveAs(output);
    }

    if (type == "blur") {
      String input = args[1];
      String output = args[2];
      Picture picture = new Picture(input);
      picture.blur().saveAs(output);
    }
  }
}