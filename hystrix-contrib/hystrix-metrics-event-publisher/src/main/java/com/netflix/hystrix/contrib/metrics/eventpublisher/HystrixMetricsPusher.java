package com.netflix.hystrix.contrib.metrics.eventpublisher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HystrixMetricsPusher {

	private static final int DEFAULT_DELAY = 500;
	private static HystrixMetricsPusher INSTANCE = new HystrixMetricsPusher();
	private static Boolean isRunning = false;
	private MetricJsonListener listener = null;
	private HystrixMetricsPoller poller = null;
	private PublisherThread publisher = null;

	private ExecutorService executor = null;

	private HystrixMetricsPusher() {
		System.out.println("HystrixMetricsPusher.new()");
		listener = new MetricJsonListener();
		poller = new HystrixMetricsPoller(listener, DEFAULT_DELAY);
		executor = Executors.newFixedThreadPool(1);
		publisher = new PublisherThread();
	}

	public static HystrixMetricsPusher getInstance() {
		return INSTANCE;
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
				// call client and send messages
				for (String jsonMessage : jsonMessages) {
					System.out.println("data: " + jsonMessage + "\n");
				}
			}

		}

	}

}
