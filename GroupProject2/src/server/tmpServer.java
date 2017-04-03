package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import msg.Message;

public class tmpServer {
	public static void main(String args[]) throws IOException
	{
		System.out.println("Server started");
		
		ServerSocket ss1 = new ServerSocket(1755);
	
		Socket sss1 = ss1.accept();
		
		Socket snode1 = new Socket(sss1.getInetAddress().getHostAddress(), 1754);
		ObjectOutputStream ops11 = new ObjectOutputStream(snode1.getOutputStream());
		
		int[] a = {34, 13, 1, 2, 44, 123, 4, 5, 18 ,6 ,7 , 9};
		
		Message msg = new Message();
		
		msg.setCode(0);
		msg.setNum(a);
		
		ops11.writeObject(msg);
		ops11.flush();
		
		int k = 3;		
		int code = -1;
		int pivot = -1;
		boolean moreMsg = true;
		boolean doSendMsg = true;
		
		while(moreMsg)
		{
			ObjectInputStream ois1 = new ObjectInputStream(sss1.getInputStream());
			try {
				msg =(Message)ois1.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			code = msg.getCode();
			System.out.println("Server received: " + code);
			
			switch(code)
			{
			case 0:
				msg.setCode(0);
				msg.setNum(a);
				break;
			case 1:
				msg.setCode(1);
				break;
			case 2:
				pivot = msg.getPiovt();
				System.out.println("Server get pivot " + pivot);
				msg.setCode(2);
				msg.setPiovt(pivot);
				break;
			case 3:
				int count = msg.getNodeAount();
				System.out.println("Server got count of node " + count);
				
				int leftRight = -1;
				if(k == count)
				{	
					leftRight = 2;
					System.out.println("count " + count + " == " + " k " + k + " leftRight " + leftRight);
					System.err.println("Server got No. " + k + " smallest number: " + pivot + "\n");
					
				}else if(k < count)		//keep left part
				{
					leftRight = 3;
					System.out.println("count " + count + " > " + " k " + k + " leftRight " + leftRight);
				}else if(k > count)		//keep right part
				{
					leftRight = 1;		//
					System.out.println("count " + count + " < " + " k " + k + " leftRight " + leftRight);
				}
				
				msg.setCode(3);
				msg.setLeftRight(leftRight);
				break;
			case 4:
				msg.setCode(4);
				break;
			case 5:
				moreMsg = false;
				doSendMsg = false;
				System.out.println("Server Shutting Down");
				break;				
			default:
				msg.setCode(4);
				break;
			}
			
			if(doSendMsg)
			{
				System.out.println("Server answer: " + msg.getCode());	
				ObjectOutputStream ops1 = new ObjectOutputStream(snode1.getOutputStream());
				ops1.writeObject(msg);				
			}
		}
		
		ss1.close();
		sss1.close();
		snode1.close();
	}
}
