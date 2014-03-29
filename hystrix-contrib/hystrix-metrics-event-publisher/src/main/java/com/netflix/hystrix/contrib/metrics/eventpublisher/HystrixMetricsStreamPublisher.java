package com.netflix.hystrix.contrib.metrics.eventpublisher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.contrib.metrics.eventpublisher.obsolete.HystrixMetricsStreamServlet;

public class HystrixMetricsStreamPublisher extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1008400643678968734L;

	private static final Logger logger = LoggerFactory.getLogger(HystrixMetricsStreamServlet.class);

	@Override
	public void destroy() {
		
		super.destroy();
		HystrixMetricsPusher.getInstance().stop();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.info("init(" + config+ ")");
		System.out.println("HystrixMetricsStreamPublisher.init(config)");
		System.out.println("\t delay: " + config.getInitParameter("delay"));
		HystrixMetricsPusher.getInstance().start();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		logger.info("init()");

		System.out.println("HystrixMetricsStreamPublisher.init()");
	}
	
	
}
