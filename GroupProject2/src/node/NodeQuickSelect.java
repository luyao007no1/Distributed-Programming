package node;

public class NodeQuickSelect {
	private static int[] a;	// subarray
	private static int pivot;
	private static int nodeAccount;		
	private static int leftRight;		//true is left; false is right
	private static int start;
	private static int end;
	private static int pivotIndex;
	private static boolean pivotNode;
	
	public NodeQuickSelect()
	{
		this.a = null;
		this.start = 0;
		this.end = 0;
		this.pivot = -1;
		this.pivotIndex = -1;
		this.pivotNode = false;
		this.nodeAccount = 0;
		this.leftRight = -1;
	}
	
	public void setStartEnd(int start, int end)
	{
		this.start = start;
		this.end = end;
		System.out.println("Node gets array lenght " + this.end);
	}
	
	public void setStartEnd()
	{
		
		int fix = 0;
		if(this.leftRight == 3)		
		{
			// if the msg leftOrRight is true, if this node is pivot node then end = piovtIndex-1
			// if it is left but pivot node, then end = piovtindex-0
			if(this.pivotNode)
			{
				fix = 1;
			}
			
			end = pivotIndex-fix;
		}
		else if(this.leftRight == 1)
		{
			//if msg leftOrRight is false, no matter it is pivot or not, shoud +1
			start = pivotIndex+1;
		}
		
		System.out.println("Node in setStarEnd: start is " + this.start + " end is " + this.end);
	}
	
	public int choosePivot()
	{
		if((this.end-this.start)<0)
		{
			System.err.println("Can not offer pivot, because the array length is 0");
			this.pivotNode = false;
			return -1;
		}
		this.pivotNode = true;	
		System.out.println("Node picks piovt No. " + this.start + " : " + a[this.start]);		
		return a[this.start];
		
	}
	
	public int partition()
	{
		int i = this.start;
	    int j = this.end + 1;
	    int val = this.pivot;

	    if(!this.isPivotNode())
	    {
	    	i--;
	    }
	    
	    while (true) { 

		     // find item on start to swap
	    	while (a[++i]<val)
		    {
		    	if (i == end)
		    		break;
		    }

		     // find item on end to swap
	        while (val<a[--j])
	        	if (j == start) 
	        		break;      // redundant since a[start] acts as sentinel

	            // check if pointers cross
	         	if (i >= j) 
	         		break;

	            swap(a, i, j);
	        }
	     
	    	// now, a[start .. j-1] <= a[j] <= a[j+1 .. end]
        	this.pivotIndex = j;
        	
	     	//if the node is not picked pivot, then no swap this step
	     	//if it is pivot picked node then just swap
	    	if(this.pivotNode)
	    	{
	    		// put partitioning item v at a[j]
	    		this.swap(a, start, j);	   
	    		
		        //**if the node is a picked pivot node, if j is 0
	    		//**then it means the pivot is counted a number, so return 1;
	    		/*if(j==0)
	    		{
	    			return 1;
	    		}
	    		*/	    	
	    	}
	    	//**otherwise j=0, because it means on the left side there has no number
	    	/*if(j==0)
	    	{
	    		return 0;
	    	}
	        */
	        //we return j+1 normally, because we keep one side numbers, including the pivot
	        return j;	// this is the count of how many  
	        
	}
	
	public double quickSelect(double[] a, int start, int end, int k)
	{
		if(end>a.length-1 || start<0 || k<0 || k>a.length)
		{
			System.err.println("QuickSelect Exit; End is " + end + " Start is " + start);
			return -1.0;
		}
		
		int piovtIndex;
		
		while(true)
		{
			if(start==end)
			{
				System.out.println("start and end equal");
				return a[start];
			}
			
			piovtIndex = partition();			
			
			if(k==piovtIndex)
			{
				return a[k];
			}
			//TODO if the msg leftOrRight is true, if this node is pivot node then end = piovtIndex-1
			// if it is left but pivot node, then end = piovtindex
			else if(k<piovtIndex)
			{
				end = piovtIndex-1;
			}
			//TODO if msg leftOrRight is false, no matter it is pivot or not, shoud +1
			else if(k>piovtIndex)
			{
				start = piovtIndex+1;
			}
		}
	}
	
	private void swap(int[] a2, int i, int j)
	{
		int swap = a2[i];
	    a2[i] = a2[j];
	    a2[j] = swap;
	}
	
	public void reset()
	{
		this.a = null;
		this.start = 0;
		this.end = 0;
		this.pivot = -1;
		this.pivotIndex = -1;
		this.pivotNode = false;
		this.nodeAccount = 0;
		this.leftRight = -1;
	}
	
	public int[] getA() {
		return a;
	}

	public void setA(int[] a) {
		this.a = a;
	}

	public int getPiovt() {
		return pivot;
	}

	public void setPiovt(int piovt) {
		this.pivot = piovt;
	}

	public int getNodeAccount() {
		return nodeAccount;
	}

	public void setNodeAccount(int nodeAccount) {
		this.nodeAccount = nodeAccount;
	}

	public int isLeftRight() {
		return leftRight;
	}

	public void setLeftRight(int i) {
		this.leftRight = i;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isPivotNode() {
		return pivotNode;
	}

	public void setPivotNode(boolean pivotNode) {
		this.pivotNode = pivotNode;
	}

}
