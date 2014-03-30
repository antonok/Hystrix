package com.netflix.hystrix.contrib.metrics.eventpublisher;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HystrixMetricsStreamPublisher extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1008400643678968734L;

	
	private static final String POLLER_DELAY_PARAM = "pollerDelay";
	private static final String PUBLISH_URL_PARAM = "publishUrl";
	private static final int DEFAULT_POLLER_DELAY = 500;
	private static final String DEFAULT_PUBLISH_URL = "http://localhost:9090/hystrix.publish";
	
	private static final Logger logger = LoggerFactory
			.getLogger(HystrixMetricsStreamPublisher.class);

	private HystrixMetricsPusher publisher = null;

	@Override
	public void destroy() {
		super.destroy();
		publisher.stop();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("init(" + config + ")");
		int pollerDelay = getPollerDelay(config);
		String publishUrl = getPublishUrl(config);
		
		System.out.println("HystrixMetricsStreamPublisher.init(config)");
		System.out.println("\t pollerDelay: " + pollerDelay);
		System.out.println("\t publishUrl: " + publishUrl);
		publisher = new HystrixMetricsPusher(pollerDelay, publishUrl);
		publisher.start();
	}

	private int getPollerDelay(ServletConfig config) {
		String pollerDelayParam = config.getInitParameter(POLLER_DELAY_PARAM);
		if (pollerDelayParam != null) {
			try {
				return Integer.parseInt(pollerDelayParam);
			} catch (NumberFormatException ex) {
				logger.error(
						"pollerDelay must be specified as number, {} while parsing {}",
						ex.getMessage(), pollerDelayParam);
			}
		}
		return DEFAULT_POLLER_DELAY;
	}
	
	private String getPublishUrl(ServletConfig config) {
		String publishUrlParam = config.getInitParameter(PUBLISH_URL_PARAM);
		if (publishUrlParam != null) {
			try {
				URL url = new URL(publishUrlParam);
				return url.toString();
			} catch (MalformedURLException ex) {
				logger.error(
						"pollerDelay must be specified as a valid url, {} while parsing {}",
						ex.getMessage(), publishUrlParam);
			}
		}
		return DEFAULT_PUBLISH_URL;
	}


	@Override
	public void init() throws ServletException {
		super.init();
	}

}
