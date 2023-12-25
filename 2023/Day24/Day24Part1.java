package AdventOfCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day24Final {
	
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
        Scanner sc = new Scanner(file);
        List<double[]> hailstones = new ArrayList<>();
        double min = 200000000000000D;
        double max = 400000000000000D;
        while(sc.hasNext()) {
        	String line = sc.nextLine();
        	String[] components = line.replace("@", ",").split(",");
        	double[] hailstone = new double[components.length];
            for (int i = 0; i < components.length; i++) {
                hailstone[i] = Double.parseDouble(components[i]);
            }
            hailstones.add(hailstone);
        }
 
        
        int count = 0;
        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                if (check(hailstones.get(i), hailstones.get(j), min, max)){
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    private static boolean check(double[] hailstone1, double[] hailstone2, double min, double max) {
        double x1 = hailstone1[0];
        double y1 = hailstone1[1];
        double x2 = hailstone1[3];
        double y2 = hailstone1[4];
        
        double x3 = hailstone2[0];
        double y3 = hailstone2[1];
        double x4 = hailstone2[3];
        double y4 = hailstone2[4];

        if (y2 * x4 == y4 * x2) {
            return false;
        }

        double time1 = (y4 * (x1 - x3) - x4 * (y1 - y3)) / (y2 * x4 - x2 * y4);
        double time2 = (y2 * (x3 - x1) - x2 * (y3 - y1)) / (y4 * x2 - x4 * y2);
        
        
        
        return (time1 > 0 && min < x1 + time1 * x2 && x1 + time1 * x2 < max
                && time2 > 0 && min < y1 + time1 * y2 && y1 + time1 * y2 < max);
    }
}

