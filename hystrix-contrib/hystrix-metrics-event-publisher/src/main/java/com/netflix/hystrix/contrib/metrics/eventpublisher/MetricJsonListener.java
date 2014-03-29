package com.netflix.hystrix.contrib.metrics.eventpublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.concurrent.ThreadSafe;

/**
 * This will be called from another thread so needs to be thread-safe.
 */
@ThreadSafe 
public class MetricJsonListener implements HystrixMetricsPoller.MetricsAsJsonPollerListener {

    /**
     * Setting limit to 1000. In a healthy system there isn't any reason to hit this limit so if we do it will throw an exception which causes the poller to stop.
     * <p>
     * This is a safety check against a runaway poller causing memory leaks.
     */
    private final LinkedBlockingQueue<String> jsonMetrics = new LinkedBlockingQueue<String>(1000);

    /**
     * Store JSON messages in a queue.
     */
    @Override
    public void handleJsonMetric(String json) {
        jsonMetrics.add(json);
    }

    /**
     * Get all JSON messages in the queue.
     * 
     * @return
     */
    public List<String> getJsonMetrics() {
        ArrayList<String> metrics = new ArrayList<String>();
        jsonMetrics.drainTo(metrics);
        return metrics;
    }
}