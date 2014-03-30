package com.netflix.hystrix.contrib.metrics.eventpublisher;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HystrixMetricsPublisherClient {
	
	private static final Logger logger = LoggerFactory
			.getLogger(HystrixMetricsPublisherClient.class);
	
	private final static HttpClient httpClient = new DefaultHttpClient();
	private String url = null;
	
	public HystrixMetricsPublisherClient(String url) {
		this.url = url;
		
	}
	
	public void publishEvents(List<String> events) {
		HttpPost post = new HttpPost(url);
		StringBuilder sb = new StringBuilder();
		
		for (String event : events) {
			sb.append("data: ")
				.append(event)
				.append("\n");
		}
		
		try {
			StringEntity entity = new StringEntity(sb.toString());
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				logger.info("published events, yey");
			} else {
				logger.error("failed to publish events, :(");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
