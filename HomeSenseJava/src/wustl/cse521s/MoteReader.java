

/*									tab:4
 * Copyright (c) 2005 The Regents of the University  of California.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the
 *   distribution.
 * - Neither the name of the copyright holders nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 * Java-side application for testing serial port communication.
 * 
 *
 * @author Phil Levis <pal@cs.berkeley.edu>
 * @date August 12 2005
 */

package wustl.cse521s;
import java.io.IOException;
import java.util.HashMap;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

public class MoteReader implements MessageListener {

	private MoteIF moteIF;
	  
	  private HashMap<Integer, Integer> statusMap = new HashMap<Integer,Integer>();
	  
	  private MoteReader(){
		  
	  }
	  private static MoteReader instance;
	  public static MoteReader getInstance(){
		  
		  if(instance == null)
			  initialize();
		  return instance;
	  }
	  public static HashMap<Integer,String> moteMap = new HashMap<Integer,String>();
	  private static void initialize() {
		// instance = new TestSerial();
		 PhoenixSource phoenix;
		    String source ="serial@/dev/ttyUSB0:telosb";
		    if (source == null) {
		      phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
		    }
		    else {
		      phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
		    }

		    MoteIF mif = new MoteIF(phoenix);
		    instance = new MoteReader(mif); 
		    moteMap.put(1, "ENABLE");
		    moteMap.put(2, "ENABLE");
		 
	  }

	public MoteReader(MoteIF moteIF) {
	    this.moteIF = moteIF;
	    this.moteIF.registerListener(new MoteStatusMsg(), this);
	  }
	  public void sendPacket(int status){
		  MoteStatusMsg payload = new MoteStatusMsg();
		  payload.set_status(status);    
		  try {
			  moteIF.send(0, payload);  
		  }
		  catch (IOException exception) {
		      System.err.println("Exception thrown when sending packets. Exiting.");
		      System.err.println(exception);
		   }
	  }

	  public void sendPackets() {
	    int counter = 0;
	    MoteStatusMsg payload = new MoteStatusMsg();
	    
	    try {
	      while (true) {
		System.out.println("Sending packet " + counter);
		//payload.set_counter(counter);
		moteIF.send(0, payload);
		counter++;
		try {Thread.sleep(3000);}
		catch (InterruptedException exception) {exception.printStackTrace();}
	      }
	    }
	    catch (IOException exception) {
	      System.err.println("Exception thrown when sending packets. Exiting.");
	      System.err.println(exception);
	    }
	  }

	  public void messageReceived(int to, Message message) {
		  MoteStatusMsg msg = (MoteStatusMsg)message;
		  System.out.println("Got -" +msg.get_status());
		  if(msg.get_status() < 99){
			  statusMap.put(msg.get_status()/10, msg.get_status()%10);
			  System.out.println(msg.get_status()/10 +  " -- " + msg.get_status()%10);
			  if(moteMap.get(msg.get_status()).equals("ENABLE"))
			  PublishSubscribeSample.publishToAmazon(msg.get_status());
		  }		  
		  else 
			  System.out.println("Ignoring sequence number " + msg.get_status());
	  }
	  
	  public HashMap<Integer, Integer> getStatusMap() {
		return statusMap;
	}

	private static void usage() {
	    System.err.println("usage: TestSerial [-comm <source>]");
	  }
	  
//	  public static void main(String[] args) throws Exception {
//	    String source = null;
//	    if (args.length == 2) {
//	      if (!args[0].equals("-comm")) {
//		usage();
//		System.exit(1);
//	      }
//	      source = args[1];
//	    }
//	    else if (args.length != 0) {
//	      usage();
//	      System.exit(1);
//	    }
//	    
//	    PhoenixSource phoenix;
//	    
//	    if (source == null) {
//	      phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
//	    }
//	    else {
//	      phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
//	    }
//
//	    MoteIF mif = new MoteIF(phoenix);
//	    TestSerial serial = new TestSerial(mif);
//	    //serial.sendPackets();
//	  }


}