/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package wustl.cse521s;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tinyos.packet.PacketSource;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.AWSIotTopic;
import wustl.cse521s.CommandArguments;
import wustl.cse521s.SampleUtil;
import wustl.cse521s.SampleUtil.KeyStorePasswordPair;

/**
 * This is an example that uses {@link AWSIotMqttClient} to subscribe to a topic and
 * publish messages to it. Both blocking and non-blocking publishing are
 * demonstrated in this example.
 */
public class PublishSubscribeSample {

	private static final String armDisarmTopic = "sdk/test/armdisam";
    private static final String TestTopic = "sdk/test/Python";
    private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;

    private static AWSIotMqttClient awsIotClient;

    public static void setClient(AWSIotMqttClient client) {
        awsIotClient = client;
    }

    public static class BlockingPublisher implements Runnable {
        private final AWSIotMqttClient awsIotClient;

        public BlockingPublisher(AWSIotMqttClient awsIotClient) {
            this.awsIotClient = awsIotClient;
        }

        @Override
        public void run() {
            long counter = 1;

            while (true) {
                String payload = "hello from blocking publisher - " + (counter++);
                try {
                    awsIotClient.publish(TestTopic, payload);
                } catch (AWSIotException e) {
                    System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
                }
                System.out.println(System.currentTimeMillis() + ": >>> " + payload);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(System.currentTimeMillis() + ": BlockingPublisher was interrupted");
                    return;
                }
            }
        }
    }

    public static class NonBlockingPublisher implements Runnable {
        private final AWSIotMqttClient awsIotClient;

        public NonBlockingPublisher(AWSIotMqttClient awsIotClient) {
            this.awsIotClient = awsIotClient;
        }

        @Override
        public void run() {
            long counter = 1;

            while (true) {
                String payload = "hello from non-blocking publisher - " + (counter++);
                AWSIotMessage message = new NonBlockingPublishListener(TestTopic, TestTopicQos, payload);
                try {
                    awsIotClient.publish(message);
                } catch (AWSIotException e) {
                    System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(System.currentTimeMillis() + ": NonBlockingPublisher was interrupted");
                    return;
                }
            }
        }
    }

    private static void initClient(CommandArguments arguments) {
        String clientEndpoint = arguments.getNotNull("clientEndpoint", SampleUtil.getConfig("clientEndpoint"));
        String clientId = arguments.getNotNull("clientId", SampleUtil.getConfig("clientId"));

        String certificateFile = arguments.get("certificateFile", SampleUtil.getConfig("certificateFile"));
        String privateKeyFile = arguments.get("privateKeyFile", SampleUtil.getConfig("privateKeyFile"));
        if (awsIotClient == null && certificateFile != null && privateKeyFile != null) {
            String algorithm = arguments.get("keyAlgorithm", SampleUtil.getConfig("keyAlgorithm"));
            KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);

            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }

        if (awsIotClient == null) {
            String awsAccessKeyId = arguments.get("awsAccessKeyId", SampleUtil.getConfig("awsAccessKeyId"));
            String awsSecretAccessKey = arguments.get("awsSecretAccessKey", SampleUtil.getConfig("awsSecretAccessKey"));
            String sessionToken = arguments.get("sessionToken", SampleUtil.getConfig("sessionToken"));

            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
                        sessionToken);
            }
        }

        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    }
    private static String insertSQL = "INSERT INTO ALERT (SOURCE_MOTE, TIME_STAMP) VALUES(?,?)";
    
    public static void main(String args[]) throws InterruptedException, AWSIotException, AWSIotTimeoutException {
        CommandArguments arguments = CommandArguments.parse(args);
        initClient(arguments);
        try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        initDB();
        awsIotClient.connect();

        AWSIotTopic topic = new TestTopicListener(armDisarmTopic, TestTopicQos);
        awsIotClient.subscribe(topic, true);
//
//       Thread blockingPublishThread = new Thread(new BlockingPublisher(awsIotClient));
//        Thread nonBlockingPublishThread = new Thread(new NonBlockingPublisher(awsIotClient));
//
//     blockingPublishThread.start();
//        nonBlockingPublishThread.start();
//
//        blockingPublishThread.join();
//        nonBlockingPublishThread.join();
 //       PacketSource reader;
       

        MoteReader telosb=MoteReader.getInstance();
      //  telosb.sendPackets();
      
    }
    private static Connection con;
	private static void initDB() {
		// TODO Auto-generated method stub
		try {
			con = DriverManager.getConnection("jdbc:mariadb://homesense.cdngwxg80nn5.us-west-2.rds.amazonaws.com:3306/homesense","homesense","homesense!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

	public static void publishToAmazon(int status) {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        Date date = new Date();
	        String payload = "Motion detected - at " + dateFormat.format(date) + " from Mote " + status;
	        AWSIotMessage message = new NonBlockingPublishListener(TestTopic, TestTopicQos, payload);
	        try {
	            awsIotClient.publish(message);
	        } catch (AWSIotException e) {
	            System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
	        }
	        try {
				PreparedStatement pstmt =  con.prepareStatement(insertSQL);
				pstmt.setInt(1, status);
				//pstmt.setInt(2, 1);
				pstmt.setTimestamp(2, getCurrentTimeStamp());
				pstmt.executeUpdate();
				System.out.println("Inserted in DB");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
