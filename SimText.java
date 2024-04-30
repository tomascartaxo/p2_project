import java.util.*;

public class SimText {

  private static int i = 10;                               // Default  width
  private static int j = 10;                               // Default  height
  private static int starveTime = 5;                        // Default  starvation time


  public static void main(String[] argv) throws InterruptedException {
    Grassland mea;
    Grassland.Rabbit rabbit = new Grassland.Rabbit();

    /**
     *  Read the input parameters.
     */

    if (argv.length > 0) {
      try {
        i = Integer.parseInt(argv[0]);
      }
      catch (NumberFormatException e) {
        System.out.println("First argument to Simulation is not an number.");
      }
    }

    if (argv.length > 1) {
      try {
        j = Integer.parseInt(argv[1]);
      }
      catch (NumberFormatException e) {
        System.out.println("Second argument to Simulation is not an number.");
      }
    }

    if (argv.length > 2) {
      try {
        starveTime = Integer.parseInt(argv[2]);
      }
      catch (NumberFormatException e) {
        System.out.println("Third argument to Simulation is not an number.");
      }
    }

    mea = new Grassland(i, j, starveTime);

    /**
     *  Visit each cell (in a roundabout order); randomly place a rabbit, carrot,
     *  or nothing in each.
     */

    Random random = new Random(0);      // Create a "Random" object with seed 0
    int x = 0;
    int y = 0;
    for (int xx = 0; xx < i; xx++) {
      x = (x + 78887) % i;           // This will visit every x-coordinate once
      if ((x & 8) == 0) {
        for (int yy = 0; yy < j; yy++) {
          y = (y + 78887) % j;       // This will visit every y-coordinate once
          if ((y & 8) == 0) {
            int r = random.nextInt();     // Between -2147483648 and 2147483647
            if (r < 0) {                        // 50% of cells start with carrot
              mea.addCarrot(x, y);
            } else if (r > 1500000000) {     // ~15% of cells start with rabbit
              mea.addRabbit(x, y, rabbit);
            }

          }
        }
      }
    }

    /**
     *  Perform timesteps forever.
     */

    while (true) {                                              // Loop forever
      Thread.sleep(1000);                // Wait one second (1000 milliseconds)
      mea.printMatrix();                      // Draw the current meadow
      //  For fun, you might wish to change the delay in the next line.
      //  If you make it too short, though, the graphics won't work properly.
      mea = mea.timeStep();                              // Simulate a timestep
    }
  }

}