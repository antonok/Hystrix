package com.netflix.hystrix.contrib.metrics.eventpublisher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HystrixMetricsPusher {

	private static Boolean isRunning = false;
	private MetricJsonListener listener = null;
	private HystrixMetricsPoller poller = null;
	private PublisherThread publisher = null;

	private ExecutorService executor = null;
	private HystrixMetricsPublisherClient publisherClient = null;

	
	public HystrixMetricsPusher(int pollerDelay, String url) {
		System.out.println("HystrixMetricsPusher.new()");
		
		listener = new MetricJsonListener();
		poller = new HystrixMetricsPoller(listener, pollerDelay);
		executor = Executors.newFixedThreadPool(1);
		publisher = new PublisherThread();
		publisherClient = new HystrixMetricsPublisherClient(url);
	}
	

	public void start() {
		System.out.println("HystrixMetricsPusher.start()");
		if (!isRunning) {
			synchronized (isRunning) {
				System.out
						.println("HystrixMetricsPusher.start() starting poller...");
				poller.start();
				System.out
						.println("HystrixMetricsPusher.start() executing publisher...");
				executor.execute(publisher);
			}
		}
		System.out.println("HystrixMetricsPusher.start() -done");
	}

	public void stop() {

	}

	private class PublisherThread implements Runnable {

		@Override
		public void run() {
			System.out.println("PublisherThread.run()");
			while (poller.isRunning()) {
				List<String> jsonMessages = listener.getJsonMetrics();
				if (jsonMessages.isEmpty()) {
					continue;
				}
				
				publisherClient.publishEvents(jsonMessages);
				// call client and send messages
//				for (String jsonMessage : jsonMessages) {
//					System.out.println("data: " + jsonMessage + "\n");
//				}
			}

		}

	}

}
