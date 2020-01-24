package com.digitalx.event.server.emitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventEmitter {
	
	private static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();
	private static final int PORT = 9999;
	private static final String DELIMITER = ":";
	private static final long EVENT_PERIOD_SECONDS = 1;
	private static final Random random = new Random();

	public static void main(String[] args) throws IOException, InterruptedException {
		BlockingQueue<String> eventQueue = new ArrayBlockingQueue<String>(100);
		SERVER_EXECUTOR.execute(new SteamingServer(eventQueue));
		while (true) {
			eventQueue.put(generateEvent());
			Thread.sleep(TimeUnit.SECONDS.toMillis(EVENT_PERIOD_SECONDS));
		}
	}

	private static String generateEvent() {
		// int userNumber = random.nextInt(10);
		// String event = random.nextBoolean() ? "login" : "purchase";
		// In production use a real schema like JSON or protocol buffers
		//String json = "{\"type\":\"facebook\",\"id\":\"302769499797134_1465265340214205\",\"timestamp\":1493046720,\"url\":\"http://twitter.com/302769499797134_1465265340214205\",\"content\":\"In this upcoming webinar with Hortonworks, we'll take you through how Hadoop and cognitive technologies are being used to gain competitive advantage in financial services plus show you how the right hardware platform can accelerate your benefits.\"}";
		
		//String jsonlike = "1565025892,event,{\"type\":\"event\",\"url\":\"https:\\/\\/www.starfriends.lk\\/\",\"pageIdAction\":\"6\",\"idpageview\":\"\",\"serverTimePretty\":\"Aug 5, 2019 17:24:52\",\"pageId\":\"72\",\"eventCategory\":\"zmEvent\",\"eventAction\":\"click\",\"interactionPosition\":null,\"timestamp\":1565025892,\"icon\":\"plugins\\/Morpheus\\/images\\/event.png\",\"iconSVG\":\"plugins\\/Morpheus\\/images\\/event.svg\",\"title\":\"Event\",\"subtitle\":\"Event_Category: \\\"zmEvent', Action: \\\"click\\\"\",\"eventName\":\"A - MOVIES...\",\"idSite\":\"2\",\"idVisit\":\"9\",\"userId\":null,\"visitorId\":\"9a774036a03c3499\"}";
		String jsonlike = "1564994250,click,{\"idSite\":2,\"idVisit\":\"31\",\"userId\":\"\",\"visitorId\":\"12f4ebbc1a381548\",\"pageIdAction\":1,\"idpageview\":\"\",\"visitCount\":3,\"visitIp\":\"103.21.0.0\",\"resolution\":\"320x570\",\"type\":\"event\",\"url\":\"https:\\/\\/www.starfriends.lk\\/video\\/call-a-friend-with-anuj\\/NydzY5aTE6bvf1uC8wdAaSazSChZUT9E\",\"idAction\":1,\"eventCategory\":\"zmEvent\",\"eventAction\":\"click\",\"eventName\":\"path\",\"tag\":\"path\",\"att\":{\"d\":\"M424.4 214.7L72.4 6.6C43.8-10.3 0 6.1 0 47.9V464c0 37.5 40.7 60.1 72.4 41.3l352-208c31.4-18.5 31.5-64.1 0-82.6z\",\"fill\":\"currentColor\"}}";
		return jsonlike;
		// return String.format("user-%s", userNumber) + DELIMITER + event;
	}

	private static class SteamingServer implements Runnable {
		private final BlockingQueue<String> eventQueue;

		public SteamingServer(BlockingQueue<String> eventQueue) {
			this.eventQueue = eventQueue;
		}

		public void run() {
			try (ServerSocket serverSocket = new ServerSocket(PORT);
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {
				while (true) {
					String event = eventQueue.take();
					//System.out.println(String.format("Writing \"%s\" to the socket.", event));
					System.out.println(event);
					//out.println(event);
				}
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException("Server error", e);
			}
		}
	}

}
