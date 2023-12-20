package AdventOfCode;

import java.util.ArrayList;

public class MathHelper {
	 	     
	    public static long lcm_of_array_elements(ArrayList<Integer> element_array)
	    {
	        long lcm_of_array_elements = 1;
	        int div = 2;
	         
	        while (true) {
	            int counter = 0;
	            boolean divisible = false;
	             
	            for (int i = 0; i < element_array.size(); i++) {
	                if (element_array.get(i) == 0) {
	                    return 0;
	                }
	                else if (element_array.get(i) < 0) {
	                    element_array.set(i, element_array.get(i) * (-1));
	                }
	                if (element_array.get(i) == 1) {
	                    counter++;
	                }
	                if (element_array.get(i) % div == 0) {
	                    divisible = true;
	                    element_array.set(i,  element_array.get(i) / div);
	                }
	            }
	 
	            if (divisible) {
	                lcm_of_array_elements = lcm_of_array_elements * div;
	            }
	            else {
	                div++;
	            }
	 
	            // Check if all element_array is 1 indicate 
	            // we found all factors and terminate while loop.
	            if (counter == element_array.size()) {
	                return lcm_of_array_elements;
	            }
	        }
	    }
}
