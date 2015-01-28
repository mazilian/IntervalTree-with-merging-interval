
public class IntervalTree
{    
	IntervalNode root = null;
	
	public IntervalTree()
	{
		
	}
	
	public IntervalNode getTree()
	{
		return this.root;
	}

    public void AddRange(int lower, int upper)
    {
    	this.root = IntervalNode.addInterval(this.root, new Interval(lower,upper));
    }


    public boolean QueryRange(int lower, int upper)
    {
    	return IntervalNode.queryInterval(this.root, new Interval(lower,upper));
    }


    public void DeleteRange(int lower, int upper)
    {
    	IntervalNode.removeInterval(this.root, new Interval(lower,upper));
    }


};

//relation of two intervals
enum Position
{
	LeftOutSide,
	RightOutSide,
	Overlap,
	Include,
	LeftCross,
	RightCross
}

//interval class
class Interval
{
	int start;
	int end;
	public Interval(int start,int end){
		this.start=start;
		this.end=end;
	}
	
	public int getStart()
	{
		return this.start;
	}
	public int getEnd()
	{
		return this.end;
	}
	
	//relation of two intervals
	public Position getPosition(Interval oi)
	{
		Position p;
		
		int oe = oi.getEnd();
		int os = oi.getStart();
		
		if(oe < this.start)
		{
			p = Position.LeftOutSide;
		}
		else if(os > this.end)
		{
			p = Position.RightOutSide;
		}
		else if (oe <= this.end && os < this.start)
		{
			p = Position.LeftCross;
		}
		else if(os >= this.start && oe > this.end)
		{
			p = Position.RightCross;
		}
		else if(os>=this.start && oe<=this.end)
			p = Position.Overlap;
		else
		{
			p = Position.Include;
		}
		return p;
	}
}


//a interval tree node
class IntervalNode {
    IntervalNode left; 
    Interval section;
    int maxEnd;
    IntervalNode right;
    boolean validate;

    public IntervalNode()
    {
    	
    }
    
    public IntervalNode(IntervalNode left, int start, int end, int maxEnd, IntervalNode right,boolean ifVal) {
        this.left = left;
        this.section = new Interval(start,end);
        this.maxEnd = maxEnd;
        this.right = right;
        this.validate = ifVal; 
    }
    
    public Interval getInterval()
    {
    	return this.section;
    }
    
    public IntervalNode getLeft()
    {
    	return this.left;
    }
    
    public IntervalNode getRight()
    {
    	return this.right;
    }
    
    public int getMaxEnd()
    {
    	return this.maxEnd;
    }
    
    public boolean getValidate()
    {
    	return this.validate;
    }
    
    public void setValidate(boolean v)
    {
    	this.validate = v;
    }
    
    public void setLeft(IntervalNode n)
    {
    	this.left = n;
    }
    
    public void setRight(IntervalNode n)
    {
    	this.right = n;
    }
    
    public void setInterval(Interval it)
    {
    	this.section = it;
    }
    
    
    //to add interval to the current interval tree
    public static IntervalNode addInterval(IntervalNode root, Interval newInterval)
    {
    	if(root == null)
    	{
    		return new IntervalNode(
    								null,
    								newInterval.getStart(),
    								newInterval.getEnd(),
    								newInterval.getEnd(),
    								null,
    								true
    								);
    	}
    	
    	Position p = root.getInterval().getPosition(newInterval);
    	
    	switch(p)
    	{
    	case Overlap:		//if the new interval is overlaped by current node, when current node is not removed node we take current node, else we take the new node
    		if(root.getValidate())
    			return root;
    		else
    			return  new IntervalNode(
						root.getLeft(),
						newInterval.getStart(),
						newInterval.getEnd(),
						newInterval.getEnd(),
						root.getRight(),
						true
						);
    	case LeftOutSide:		//if the new interval is outside the current node, we recruit to the left or right sub nodes
    		root.setLeft(addInterval(root.left,newInterval));
    		break;
    	case RightOutSide:
    		root.setRight(addInterval(root.right,newInterval));
    		break;
    	case LeftCross:			   							
    	case RightCross:
    	case Include:
    		//if the new interval cross or includes the current node:
    		//1. we create a new root node with new interval (to pay attention to the condition that current node is a removed node) 
    		//2. we recuit the left and right node of current node to add them to the new root node
    		Interval newRootInterval = new Interval
    										(
    												root.getValidate() ? Math.min(root.getInterval().getStart(), newInterval.getStart()) : newInterval.getStart(),
    												root.getValidate() ? Math.max(root.getInterval().getEnd(), newInterval.getEnd()) : newInterval.getEnd()
    										);
    		
    		int newMaxEnd = Math.max(root.getMaxEnd(), newRootInterval.getEnd());
    		
    		IntervalNode newRoot = new IntervalNode(
    												null,
    												newRootInterval.getStart(),
    												newRootInterval.getEnd(),
    												newMaxEnd,
    												null,
    												true
    												);
    		newRoot = addIntervalNode(newRoot,root.getLeft());
    		newRoot = addIntervalNode(newRoot,root.getRight());
    		
    		root =  newRoot;
    		break;
    	}
    	
    	return root;
    }
    
    //to recruit a node and its sub nodes and add them to a root node
	public static IntervalNode addIntervalNode(IntervalNode root, IntervalNode newNode)
	{
		if(newNode == null)
			return root;
		
		root = addInterval(root,newNode.getInterval());
		root = addIntervalNode(root,newNode.getLeft());
		root = addIntervalNode(root, newNode.getRight());
		
		return root;
	}
    
	//to query interval and check if it is on tracking
	public static boolean queryInterval(IntervalNode root, Interval intv)
	{
		 if (root == null) return false;
		 
		 Position p = root.getInterval().getPosition(intv);

		 //if the interval is overlaped by current node and current node is not a removed node then tracking
		 if (p == Position.Overlap)
			 return root.getValidate() && true;
		 
		 //else recruit search in the left and right sub nodes
		 if (root.getLeft() != null && root.getLeft().getMaxEnd() >= intv.getStart())
		         return queryInterval(root.getLeft(), intv);
		 return queryInterval(root.getRight(), intv);

	}
	
	//to remove an interval from the interval tree
	//to keep the logical simple we do not remove the node from the tree
	//instead we make the node validate as false which should be deleted  
	public static IntervalNode removeInterval(IntervalNode root, Interval delInterval)
	{
		if(root == null)
			return null;
		
		Position p = root.getInterval().getPosition(delInterval);
		
		switch(p)
		{
		case Include:		//if current node is included in the delInterval, then set current node validate as false
			root.setValidate(false);
			removeInterval(root.getLeft(),delInterval);
			removeInterval(root.getRight(),delInterval);
			return root;
		case LeftOutSide:	//if delete interval is outside the current node then to recruit the left or right node
			root.setLeft(removeInterval(root.getLeft(),delInterval));
			break;
		case RightOutSide:
			root.setRight(removeInterval(root.getRight(),delInterval));
			break;
		case LeftCross:	//if delete interval is cross the current node then reduce the current node interval and then recruit the left or right node
			Interval newRootInterval = new Interval(delInterval.getEnd(),root.getInterval().getEnd());
			root.setInterval(newRootInterval);
			root.setLeft(removeInterval(root.getLeft(),delInterval));
			break;
		case RightCross:
			Interval newRootInterval1 = new Interval(root.getInterval().getStart(),delInterval.getStart());
			root.setInterval(newRootInterval1);
			root.setRight(removeInterval(root.getRight(),delInterval));
			break;
		case Overlap:	
			//if the delete node is including in the current node:
			//1. to split the current node into two nodes
			//2. set the right node of the original root to be the right node of the second part of new node
			//3. keep the first part as root.
			//4. set the second part as the right node of root
			IntervalNode newRightNode = new IntervalNode(null,delInterval.getEnd(),root.getInterval().getEnd(),root.getMaxEnd(),root.getRight(),true);
			root.setInterval(new Interval(root.getInterval().getStart(),delInterval.getStart()));
			root.setRight(newRightNode);
			break;
		}
		
		return root;
	}
	
	//For test: recruit to print the intervals of the interval tree
	public static void printTree(IntervalNode root)
	{
		if(root == null) return;
		if(root.getValidate())
		{
			System.out.println(root.getInterval().getStart());
			System.out.println(root.getInterval().getEnd());
		}
		printTree(root.getLeft());
		printTree(root.getRight());
	}
}



