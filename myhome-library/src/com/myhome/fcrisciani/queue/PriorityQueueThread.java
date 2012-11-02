/**
 * This thread extract new command from the priority queue and send them on the Plant.
 * Between each one it put a 300ms of delay to assure the correct execution on the plant.
 * If there is no available command it suspends on the priority queue semaphore.
 */

package com.myhome.fcrisciani.queue;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.myhome.fcrisciani.connector.MyHomeJavaConnector;
import com.myhome.fcrisciani.connector.MyHomeSocketFactory;

/**
 * @author Flavio Crisciani
 *
 */
public class PriorityQueueThread implements Runnable{
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //
	MyHomeJavaConnector myConnector = null;
	PriorityCommandQueue list = null;

	// ---- METHODS ---- //
	/**
	 * Create the Priority Queue Thread giving the reference to the MyHome connector and the Priority queue
	 * @param myConnector myhome connector used only for IP, port read
	 * @param list priority queue to handle
	 */
	public PriorityQueueThread(MyHomeJavaConnector myConnector, PriorityCommandQueue list) {
		this.myConnector = myConnector;
		this.list = list;
	}


	@Override
	public void run() {
		Socket sk = null;
		String tosend = null;
		PrintWriter output = null;
		do{
			try{
				tosend = list.getCommand();
				if(sk == null){                  // Create a new command session
					try{
						sk = MyHomeSocketFactory.openCommandSession(myConnector.ip, myConnector.port);
					}catch(IOException e){
						System.err.println("PriorityQueueThread: Problem during socket monitor opening - " + e.toString());
						sk = null;
						continue;
					}
				}
				try{
					if (output == null) {
						output = new PrintWriter(sk.getOutputStream());
					}
					output.write(tosend);
					output.flush();
				}catch(IOException e){
					System.err.println("PriorityQueueThread: Problem during command sending - " + e.toString());
					continue;
				}
				try{
					Thread.sleep(300);				// Wait 300ms to be sure that command sent had been executed
				}catch(InterruptedException e){
					System.err.println("PriorityQueueThread: Problem during suspension - " + e.toString());
					continue;
				}
				if(list.numCommands() == 0){        // There are no more message to handle close command session
					try{
						output.close();
						output = null;
						MyHomeSocketFactory.disconnect(sk);
						sk = null;
					}catch(IOException e){
						System.err.println("PriorityQueueThread: Problem during connection closure - " + e.toString());
					}
				}
			}catch (Exception e) {
				System.err.println("PriorityQueueThread: Not handled exception - " + e.toString());
				output.close();
				output = null;
			}
		}while(true);
	}

}
