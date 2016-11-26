package u5OrderProcessor;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import u5BookingsProcessor.Booking;
import u5NewTripsProcessor.Trip;
import u5OrderClient.Order;
import weblogic.wtc.jatmi.tpusrAppKey;

public class OrderProcessor implements MessageListener {
	
	private JMSProducer producer = new JMSProducer();
	 
    // connection factory
    private QueueConnectionFactory qconFactory;
 
    // connection to a queue
    private QueueConnection qcon;
 
    // session within a connection
    private QueueSession qsession;
 
    // queue receiver that receives a message to the queue
    private QueueReceiver qreceiver;
 
    // queue where the message will be sent to
    private Queue queue;
    
    private Trip parseTrip(String s){
    	String[] sp = s.split(",");
    	return new Trip(Integer.parseInt(sp[0].trim()), sp[1].trim(), sp[2].trim());    	
    }
    private Booking parseBooking(String s) throws NumberFormatException, ParseException{
    	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    	String[] sp = s.split(",");
    	return new Booking(format.parse(sp[0].trim()), sp[1].trim(), Integer.parseInt(sp[2].trim()), Integer.parseInt(sp[3].trim()));
    }
    
    
    public void sendMessage(String queueName, Serializable msg){
    	try {
			producer.send(queueName, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    // callback when the message exist in the queue
    public void onMessage(Message msg) {
        try {
            Order order = null;
            if (msg instanceof ObjectMessage) {
                order = (Order)((ObjectMessage) msg).getObject();
            } else {
                System.out.println("Received wrong kind of message!");
            }
//            System.out.println("Message Received: " + msgText);
            if (order == null) {
            	System.out.println(String.format("Received faulty message"));
            } else {
            	if (order.getType().toLowerCase().equals("booking")){
            		Booking booking = parseBooking(order.getData());
            		System.out.println(String.format("Received an Order of type booking sending to booking processor"));
            		sendMessage("jms/u5_booking_processor", booking);
            	}
            	if (order.getType().toLowerCase().equals("trip")){
            		Trip trip = parseTrip(order.getData());
            		System.out.println("Received Order of type trip, sending to Trip processsor");
            		sendMessage("jms/u5_trip_processor", trip);
            	}
            	
            	
            }
        } catch (JMSException jmse) {
            System.err.println("An exception occurred: " + jmse.getMessage());
            jmse.printStackTrace();
        } catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    // create a connection to the WLS using a JNDI context
    public void init(Context ctx, String queueName)
            throws NamingException, JMSException {
 
        qconFactory = (QueueConnectionFactory) ctx.lookup(Config.JMS_FACTORY);
        qcon = qconFactory.createQueueConnection();
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = (Queue) ctx.lookup(queueName);
 
        qreceiver = qsession.createReceiver(queue);
        qreceiver.setMessageListener(this);
 
        qcon.start();
    }
 
    // close sender, connection and the session
    public void close() throws JMSException {
        qreceiver.close();
        qsession.close();
        qcon.close();
    }
 
    // start receiving messages from the queue
    public void receive(String queueName) throws Exception {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, Config.JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, Config.PROVIDER_URL);
 
        InitialContext ic = new InitialContext(env);
 
        init(ic, queueName);
 
        System.out.println("Connected to " + queue.toString() + ", receiving messages...");
        try {
            synchronized (this) {
                while (true) {
                    this.wait();
                }
            }
        } finally {
            close();
            System.out.println("Finished.");
        }
    }
 
    public static void main(String[] args) throws Exception {
        // input arguments
        String queueName = "jms/u5_order_processor" ;
 
        // create the producer object and receive the message
        OrderProcessor consumer = new OrderProcessor();
        consumer.receive(queueName);        
        
    }
}
