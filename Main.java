public class Main {
    
    public static void main(String[] args) {
       
    	IntervalTree rm = new IntervalTree();
        /*Simple test case 1
    	rm.AddRange(5, 20);
        rm.AddRange(10, 30);
        rm.AddRange(35, 40);
        rm.AddRange(32, 38);
        IntervalNode root = rm.getTree();
        IntervalNode.printTree(root);
        
        */
    	
    	/*A little bit test case 2
    	rm.AddRange(20, 30);
    	rm.AddRange(10, 15);
    	rm.AddRange(35, 40);
    	rm.AddRange(16, 19);
    	rm.AddRange(3, 5);
    	rm.AddRange(33, 34);
    	rm.AddRange(45, 50);
    	rm.AddRange(4, 12);
    	rm.AddRange(31, 42);
    	if(rm.QueryRange(15, 20))
        	System.out.println("overlaping!");
        else
        	System.out.println("not overlaping!");
        	*/
    	

    	
  	
    	rm.AddRange(1, 50);
    	rm.DeleteRange(5, 10);
    	rm.DeleteRange(20, 30);
    	rm.AddRange(6, 15);
    	rm.AddRange(40, 55);
    	rm.DeleteRange(3, 25);
        IntervalNode root = rm.getTree();
        IntervalNode.printTree(root);
    	if(rm.QueryRange(15, 20))
        	System.out.println("overlaping!");
        else
        	System.out.println("not overlaping!");

    	

        

    }
}