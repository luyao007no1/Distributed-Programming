package node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import msg.Message;

public class Node {
	
	private static ServerSocket listener;
	private static Socket sin;
	private static Socket sout;
	private static String host;
	private static int portin;
	private static int portout;
	
	public Node(String host, int portin, int portout)
	{
		this.host = host;
		this.portin = portin;
		this.portout = portout;
	}
	
	public int connection() 
	{
		try {
			this.sout = new Socket(this.host, this.portout );
			this.listener = new ServerSocket(this.portin);
			this.sin = this.listener.accept();
		} catch (IOException e) {
			
			e.printStackTrace();
			return 0;
		}
		
		System.out.println("The Sever " + this.host + " is connected");
		
		return 1;
	}

	public Message nodeReceive()
	{
		ObjectInputStream ois = null;
		Message msg = null;
		
		try {
			
			ois = new ObjectInputStream(sin.getInputStream());
			msg = (Message)ois.readObject();
			
		} catch (ClassNotFoundException | IOException e) {

			e.printStackTrace();
			return null;
		}
		
		return msg;
	}
	
	public int nodeSend(Message msg)
	{
		if(msg == null)
		{
			return 0;
		}
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(sout.getOutputStream());
			oos.writeObject(msg);
		} catch (IOException e) {
			
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
	
	public void close()
	{
		try {
			this.sin.close();
			this.listener.close();
			this.sout.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		// connect server
		NodeQuickSelect nqs = new NodeQuickSelect();
		Message msg = null;
		boolean moreMsg = true;
		
		Node n = new Node("10.16.0.39", 1756, 1757);
		n.connection();
		// loop to recieve cmd from server
	
		while(moreMsg)
		{
			msg = n.nodeReceive();
			// processing
			
			System.out.println("Node recevied " + msg.getCode());
			
			switch(msg.getCode())
			{
			case 0:
				if(msg.getNum() == null)
				{
					System.err.println("Node recieve a null array, report error");
					msg.setCode(4);
					break;
				}
				
				nqs.setA(msg.getNum());
				nqs.setStartEnd(0, msg.getNum().length-1);
				
				System.out.println("Node received the array" + msg.getNum().toString());
				
				msg.setCode(1);
				
				break;
			case 1:			// Changeed: the server after send the array, first ask one node for a pivot
				// 			because the node need to know the pivot in which subarray. 
				//			after server ask the node for a pivot, the node mark itself as a pivot node
				msg.setCode(2);
				msg.setPiovt(nqs.choosePivot());
				break;
			case 2:
				if(msg.getPiovt() < 0)
				{
					System.err.println("Node recieves a invaild pivot, report error");
					msg.setCode(4);
					break;
				}
				
				int count = -1;
				int p = msg.getPiovt();
								
				nqs.setPiovt(p);
				count = nqs.partition();
				msg.setCode(3);
				msg.setNodeAount(count);
				
				System.out.println("Node recieved pivot " + p + ". send back count " + count);
				
				break;
			case 3:
				if(msg.getLeftRight() == 2)
				{
					moreMsg = false;				
					System.err.println("Whole job finished.");
					msg.setCode(5);
					break;
				}
				else
				{
					nqs.setLeftRight(msg.getLeftRight());
					nqs.setStartEnd();
					nqs.setPivotNode(false);
				}
						
				msg.setCode(1);
			
				break;
			case 4:
				nqs.reset();
				
				System.err.println("Node recieves Server error code, reset all, readly to restart");
				
				msg.setCode(0);
				
				break;
			default:
				moreMsg = false;
				break;
			}
			
			System.out.println("Node asnwer " + msg.getCode());
			
			n.nodeSend(msg);
			// send back the result
		}
		
		n.close();
		
	}
	
}
