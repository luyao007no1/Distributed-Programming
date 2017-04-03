package msg;
import java.io.Serializable;

public class Message implements Serializable{

	private int code;	// this is initator code, init first send then receive, init send the below code to nodes, one code each step
						// after each sending, the init listen nodes answer and the server assume that the node get the code from the server
						// if the server can not receive the message from node or get an unexpected code 5, restart whole processing.
						// e.g. the server send code 0 to nodes, the nodes should answer 0 which means the node got the subarry
						//****new add****
						//before the while loop, first message(send node code 0, and the subarray message) from server to node should be sent, .
			
						// this below run in a while loop
						//receive message from node
						//then analyze the code
						//****add end****
						//0: initator send subarray to nodes(send code 0 to node, next loop should receieve:node code 1) ;  
						//1: init asks pivot from nodes( send code 1. when the account of node > 0, next loop should receive: pivot and code 2, if one node does not have pivot, return code 2 and pivot -1 ) 
						//2: init send pivot to nodes and start partition cmd(then node runs partition and next loop should receive: the account, code 3);
						//3: init send keeping left, right or found k (if it is left or right message, then node answer code 1. otherwise, node exits)
						//4: some err	(if server receive code 4, it send code 4 to node) 
					
						
						//this is node code, node first receive then send, node first listen init code, after get server code, processing
						//the particular step, then answer the processing status(the below code) to server, then server reassign the next step 
						// e.g. the node receive the code 2 and true of leftRight from server, then node keep left part subarry, answer 2 to server  
						// 0:node get subarray (after get the subarray, confirm with init, send back code 1)
						// 1:one node answer a pivot(send back code 2 and pivot, if no pivot ,pivot is -1)
						// 2:after receive pivot and finish running, return count to init(send back 3, and count)
						// 3: nodes tell init that keeping L or R or found k or not, if found, node exit, otherwise send back code 1;
						// 4: some error(if the node receive code 4, then answer 0 to server, then whole process restarts)
						 
	private int[] num;	// subarray
	private int pivot;
	private int nodeAccount;		
	private int leftRight;		//true is left; false is right
	
	public Message()
	{
		this.num = null;
		this.code = 5;
		this.pivot = -1;
		this.nodeAccount = 0;
		this.leftRight = -1;
	}
	
	public void reset()
	{
		this.num = null;
		this.code = 5;
		this.pivot = -1;
		this.nodeAccount = 0;
		this.leftRight = -1;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.reset();
		this.code = code;
	}
	public int[] getNum() {
		return num;
	}
	public void setNum(int[] num) {
		this.num = num;
	}
	
	public int getPiovt() {
		return pivot;
	}
	public void setPiovt(int piovt) {
		this.pivot = piovt;
	}
	
	public int getNodeAount() {
		return nodeAccount;
	}
	public void setNodeAount(int nodeAount) {
		this.nodeAccount = nodeAount;
	}

	public int getLeftRight() {
		return leftRight;
	}

	public void setLeftRight(int leftRight) {
		this.leftRight = leftRight;
	}
	

}
