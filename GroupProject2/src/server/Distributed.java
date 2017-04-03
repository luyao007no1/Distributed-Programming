package server;

import msg.Message;

/**
 *
 * @author
 */
public class Distributed {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws Exception
	 */

	static ServerConnection connection;
	public static int startPort = 1755;
	public static int nodeNum = 3;
	public static int k = 4;

	public static void main(String[] args) throws Exception {
		// TODO code application logic here
		
		if(nodeNum <=0)
		{
			return;
		}
				
		int a[] = { 7, 3, 2, 7, 9, 2, 76, 72, 9, 4, 65, 82, 1 };
		int res = a.length%nodeNum;
		int ares[] = null;
		int subLength = a.length/nodeNum;
		int istart;
		
		int[][] aa = new int[nodeNum][subLength];
		
		for(int i=0; i<nodeNum; i++)
		{
			for(int j=i*subLength; j<(i+1)*subLength; j++)
			{
				aa[i][j-i*subLength] = a[j];	
			}
			if(i == nodeNum-1 && res != 0)
			{
				ares = new int[res];
				System.arraycopy(a, a.length-1-res, ares, 0, res);			
			}
		}
		System.out.println("Arrays are readly");
		connection = new ServerConnection(nodeNum, startPort);
		
		connection.sendArray(aa, ares);
		connection.receiveMsg();
		
		int kValue = findk(k);
		System.out.println("The value of the k-th number is " + kValue);
	}

	public static int findk(int k) throws Exception {
		int pivot=-1;
		int kIndex = k;
		Message msg = new Message();
		int count = -1;
		
		while (true) {
			//ask each node pivot first msg code set 1
			for(int i=0; i<nodeNum; i++)
			{
				msg.setCode(1);
				connection.sendMsg(i, msg);
				Message responce = connection.receiveMsg(connection.inFromNode[i]);
			
				//then get piovt
				pivot = responce.getPiovt();
				if(responce.getCode()!=2)
				{
					return -1;
				}
				if(pivot!=-1)
				{
					break;
				}
				else
				{
					continue;
				}
			}
			
			if(pivot == -1)
			{
				return -1;
			}			
			
			//tell each node start partition code set 2
			
			msg.setCode(2);
			msg.setPiovt(pivot);
			
			connection.sendMsg(msg);
			connection.receiveMsg();

			//receive code 3 and check count
			for(int i=0; i<nodeNum; i++ )
			{
				count += connection.msg[i].getNodeAount();				
			}

			//decide left or right, set code 3
			msg.setCode(3);

			//if == k then finish or continue
			if(k == count)
			{	
				msg.setLeftRight(2);
				connection.sendMsg(msg);
				break;
			}else if(k < count)		//keep left part
			{
				msg.setLeftRight(3);
				System.out.println("count " + count + " > " + " k " + k + " leftRight 3");
			}else if(k > count)		//keep right part
			{
				msg.setLeftRight(1);		//
				System.out.println("count " + count + " < " + " k " + k + " leftRight 1");
			}
			connection.sendMsg(msg);			
			
		}
		return pivot;
	}

}
