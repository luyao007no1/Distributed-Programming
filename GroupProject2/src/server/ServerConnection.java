package server;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;

import msg.Message;

/**
 *
 * @author 
 */
public class ServerConnection {

	ObjectOutputStream outToNode[];
	ObjectInputStream inFromNode[];
	private static int nodeNum;
	private static int startInPort;
	private static int startOutPort;
	ServerSocket nodeSocket[];
	Socket inFromNodeSocket[];
	Socket outToNodeSocket[];
	Message[] msg = null;
	
	public ServerConnection(int nodeNum, int startPort) throws Exception {
		
		nodeSocket = new ServerSocket[nodeNum];
		inFromNodeSocket = new Socket[nodeNum];
		outToNodeSocket = new Socket[nodeNum];
		outToNode = new ObjectOutputStream[nodeNum];
		inFromNode = new ObjectInputStream[nodeNum];
		
		this.nodeNum = nodeNum;
		//this start port shoud be 1755, then next port is 1757, 1759
		this.startInPort = startPort;
		//this start port shoud be 1754, then next port is 1756, 1758
		this.startOutPort = startPort-1;
		
		for(int i=0; i<nodeNum; i++)
		{
			System.out.println("Waiting for Node " + i +" Connection");
			this.nodeSocket[i] = new ServerSocket(this.startInPort);
			this.startInPort += 2;
			
			this.inFromNodeSocket[i] = nodeSocket[i].accept();
			this.outToNodeSocket[i] = new Socket(this.inFromNodeSocket[i].getInetAddress().getHostAddress(), this.startOutPort);
			this.startOutPort +=2;
			
	
			System.out.println("Node " + i +" Connected");
		}
	}

	public void sendArray(int[][] a, int[] ares) throws Exception {

		Message msg = new Message();
		msg.setCode(0);

		for(int i=0; i<this.nodeNum; i++)
		{
			if(i==this.nodeNum-1 && ares != null)
			{
				int[] aux = new int[a[i].length+ares.length];
				System.arraycopy(a[i], 0, aux, 0, a[i].length);
				System.arraycopy(ares, 0, aux, a[i].length, ares.length);
				msg.setNum(aux);
				outToNode[i].writeObject(msg);
				break;
			}

			msg.setNum(a[i]);
			outToNode[i].writeObject(msg);
		}
		
	}

	public Message receiveMsg(ObjectInputStream in) throws Exception {
		return (Message) in.readObject();
	}
	
	public Message[] receiveMsg() throws ClassNotFoundException, IOException
	{
		
		for(int i=0; i<this.nodeNum; i++)
		{
			this.inFromNode[i] = new ObjectInputStream(inFromNodeSocket[i].getInputStream());
			msg[i] = (Message)this.inFromNode[i].readObject();			
		}

		return msg;
	}

	public Message receiveMsg(int node) throws ClassNotFoundException, IOException
	{
		if(node>this.nodeNum || node<0)
		{
			return null;
		}
		this.inFromNode[node] = new ObjectInputStream(inFromNodeSocket[node].getInputStream());
		return (Message)this.inFromNode[node].readObject();
	}
	
	public void sendMsg(ObjectOutputStream out, Message msg) throws Exception {
		out.writeObject(msg);
	}
	
	public void sendMsg(int node, Message msg) throws Exception {
		if(node>this.nodeNum || node<0)
		{
			return;
		}
		
		this.outToNode[node] = new ObjectOutputStream(outToNodeSocket[node].getOutputStream());
		this.outToNode[node].writeObject(msg);
	}
	
	public void sendMsg(Message msg) throws IOException
	{
		for(int i=0; i<nodeNum; i++)
		{
			this.outToNode[i] = new ObjectOutputStream(outToNodeSocket[i].getOutputStream());
			this.outToNode[i].writeObject(msg);
		}
	}
	
}
