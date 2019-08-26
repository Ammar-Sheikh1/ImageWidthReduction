import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Program3{

    //Input: The weights and values of each item, and the maximum allowed weight
    //Output: The indices of the items to be selected 
    //        so as to maximize value without exceeding the capacity
    public ArrayList<Integer> maximizeSentimentalValue(ArrayList<Integer> weights, ArrayList<Integer> values, int capacity){
       return maxSentimentBottomsUp(weights,values,capacity);
    }
     
    private ArrayList<Integer> maxSentimentBottomsUp(ArrayList<Integer> weights, ArrayList<Integer> values ,int capacity) {
    	int numOfItems = weights.size();
    	
    	int M[][] = new int[numOfItems+1][capacity+1];
      	
    	//Iter Optimal subproblem - Bottoms up
    	
    	for(int i=0;i<=numOfItems;i++) {
    		for(int j=0;j<=capacity;j++) {
    			if(i==0 || j==0) 
    				M[i][j] = 0;
    			else if(weights.get(i-1) > j){
    				M[i][j] = M[i-1][j];
    			}
    			else {
    				int valueOfItem = values.get(i-1);
    				int weightOfItem = weights.get(i-1);
    				M[i][j] = Math.max(M[i-1][j], valueOfItem + M[i-1][j-weightOfItem]);
    			}
    		}
    	}
    	
    	//Now we will build the solution Iteratively
    	int sentimentVal = M[numOfItems][capacity];
    	ArrayList<Integer> solution = new ArrayList<Integer>();
    	
    	/**
    	 * 		if the sentiment values is M[i-1][cap] -> Then we didnt select the item, keep going
    	 * 		else we did select the item, subtract its weights/value and add to list
    	 * 
    	 */
    	int remainingWeight = capacity;
    	for(int i=numOfItems; i>0 && sentimentVal > 0;i--){
    		
    		if(sentimentVal == M[i-1][remainingWeight]) {}
  
    		else {
    			solution.add(i-1);
    			sentimentVal = sentimentVal - values.get(i-1);
    			remainingWeight = remainingWeight - weights.get(i-1);
    		}	
    	}
    	return solution;
    }
    
    //Input: An image and its corresponding content matrix stored in an imageObject,
    //       a goal width
    //Result: The original image reduced to the desired width,
    //        by successive removals of the minimum vertical path.
    public void cleverWidthReduction(MyImage imageObject,int desiredWidth){
    	int currentWidth = imageObject.getWidth();
    	int widthToReduceBy = currentWidth - desiredWidth;
    	
    	for(int i=0;i<widthToReduceBy;i++) {
    		int[] path = cleverWidthReductionPath(imageObject);
    		imageObject.deleteVerticalPath(path);
    	}
    	
        return;
    }
   
    public int[] cleverWidthReductionPath(MyImage imageObject) {
    	int Width = imageObject.getWidth();
    	int Height = imageObject.getHeight();
    	
    	Double M[][] = new Double[Height][Width];
    	Double[][] contents = imageObject.getContentMatrix();
    	
    	for(int h=0;h<Height;h++) {
    		for(int w=0;w<Width;w++) {
    			if(h == 0) {
    				M[h][w] = contents[h][w];
    			}
    			else if(w == 0) {
    				M[h][w] = contents[h][w] + Math.min(M[h-1][w], M[h-1][w+1]);
    			}
    			else if(w == Width-1) {
    				M[h][w] = contents[h][w] + Math.min(M[h-1][w], M[h-1][w-1]);
    			}
    			else {
    				M[h][w] = contents[h][w] + Math.min(M[h-1][w+1], Math.min(M[h-1][w-1],M[h-1][w]));
    			}
    		}
    	}
    	
    	// Find the minimum pixel in the last row
    	double min = M[Height-1][0];
    	int width = 0;
    	for(int w=0;w<Width;w++) {
    		if(M[Height-1][w] < min) {
    			min = M[Height-1][w];
    			width = w;
    		}
    	}
    	//Now i will have to build the solution-> which is the pixel path
    	
    	/**
    	 * 1. M[h-1][w] // if its this one, it means its the pixel below us
    	 * 2 M[h-1][w-1] //if its this one then its the pixel to the left
    	 * 3. M[h-1[w+1] //if its this one then its the pixel to the right
    	 * 
    	 */
    	
    	int[] path = new int[Height];
    	
    	for(int h=Height-1;h>=0;h--) {
    		path[h] = width; 				//Note the current row we are in
    		min = min - contents[h][width];	//remove the content value from the current row
    		if(h == 0)
    			break;
    	
    		//width decreases by 1
    		if(width > 0 && min == M[h-1][width-1]) {
    			width = width-1;
    		}
    		
    		//width stays the same do nothing
    		else if(min == M[h-1][width]) {} 
    		
    		//width increases by 1
    		else if(width < Width-1 && min == M[h-1][width+1]) {
    			width = width+1;
    		}    		
    	}	
    	return path;
    }
}