package com.project.ai.api.performance;

import io.restassured.response.Response;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * AI-Powered Performance Anomaly Detector for API Testing
 * Uses statistical analysis and machine learning principles to:
 * - Establish baseline performance metrics
 * - Detect response time anomalies
 * - Identify performance degradation trends
 * - Alert on sudden performance spikes
 * - Track performance over time
 */
public class PerformanceAnomalyDetector {
    
    private static final Logger logger = LogManager.getLogger(PerformanceAnomalyDetector.class);
    private static final String METRICS_PATH = "target/performance-metrics/";
    private static final Map<String, PerformanceMetrics> endpointMetrics = new HashMap<>();
    
    // Anomaly detection thresholds
    private static final double OUTLIER_THRESHOLD = 3.0; // 3 standard deviations
    private static final double DEGRADATION_THRESHOLD = 1.5; // 50% slower than average
    private static final int MINIMUM_SAMPLES = 5; // Minimum samples before anomaly detection
    
    static {
        try {
            Files.createDirectories(Paths.get(METRICS_PATH));
        } catch (IOException e) {
            logger.error("Failed to create metrics directory: " + e.getMessage());
        }
    }
    
    /**
     * Track API call and detect anomalies
     */
    @SuppressWarnings("unchecked")
    public static <T> T track(String endpointName, Callable<Response> apiCall) {
        long startTime = System.currentTimeMillis();
        Response response = null;
        
        try {
            // Execute API call
            response = apiCall.call();
            long responseTime = System.currentTimeMillis() - startTime;
            
            // Record metrics
            recordMetrics(endpointName, responseTime, response.getStatusCode());
            
            // Detect anomalies
            detectAnomalies(endpointName, responseTime);
            
            return (T) response;
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            logger.error("API call failed for " + endpointName + " after " + responseTime + "ms");
            recordMetrics(endpointName, responseTime, -1);
            throw new RuntimeException("API call failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Track response performance
     */
    public static void trackResponse(String endpointName, Response response) {
        long responseTime = response.getTime();
        int statusCode = response.getStatusCode();
        
        recordMetrics(endpointName, responseTime, statusCode);
        detectAnomalies(endpointName, responseTime);
    }
    
    /**
     * Record performance metrics
     */
    private static void recordMetrics(String endpointName, long responseTime, int statusCode) {
        PerformanceMetrics metrics = endpointMetrics.computeIfAbsent(
            endpointName, k -> new PerformanceMetrics(endpointName)
        );
        
        metrics.addMeasurement(responseTime, statusCode);
        
        // Log metrics
        logger.info(String.format("API Performance [%s]: %dms (Status: %d)", 
            endpointName, responseTime, statusCode));
        
        // Save to file periodically
        if (metrics.getMeasurementCount() % 10 == 0) {
            saveMetricsToFile(endpointName, metrics);
        }
    }
    
    /**
     * Detect performance anomalies using statistical analysis
     */
    private static void detectAnomalies(String endpointName, long responseTime) {
        PerformanceMetrics metrics = endpointMetrics.get(endpointName);
        
        if (metrics == null || metrics.getMeasurementCount() < MINIMUM_SAMPLES) {
            // Not enough data for anomaly detection
            return;
        }
        
        DescriptiveStatistics stats = metrics.getStatistics();
        double mean = stats.getMean();
        double stdDev = stats.getStandardDeviation();
        double zScore = (responseTime - mean) / stdDev;
        
        // Check for outliers (Z-score > threshold)
        if (Math.abs(zScore) > OUTLIER_THRESHOLD) {
            logger.warn(String.format(
                "⚠ PERFORMANCE ANOMALY DETECTED [%s]: %dms (Z-score: %.2f, Mean: %.0fms, StdDev: %.0fms)",
                endpointName, responseTime, zScore, mean, stdDev
            ));
            
            // Record anomaly
            metrics.recordAnomaly(responseTime, zScore);
            
            // Alert if configured
            alertOnAnomaly(endpointName, responseTime, mean, zScore);
        }
        
        // Check for degradation (significantly slower than baseline)
        if (responseTime > mean * DEGRADATION_THRESHOLD) {
            logger.warn(String.format(
                "⚠ PERFORMANCE DEGRADATION [%s]: %dms (%.0f%% slower than average of %.0fms)",
                endpointName, responseTime, 
                ((responseTime - mean) / mean * 100), mean
            ));
        }
        
        // Check for consistent improvement or degradation
        detectTrend(endpointName, metrics);
    }
    
    /**
     * Detect performance trends over time
     */
    private static void detectTrend(String endpointName, PerformanceMetrics metrics) {
        List<Long> recentMeasurements = metrics.getRecentMeasurements(10);
        
        if (recentMeasurements.size() < 10) {
            return;
        }
        
        // Calculate trend (simple moving average comparison)
        double firstHalfAvg = recentMeasurements.subList(0, 5).stream()
            .mapToLong(Long::longValue).average().orElse(0);
        double secondHalfAvg = recentMeasurements.subList(5, 10).stream()
            .mapToLong(Long::longValue).average().orElse(0);
        
        double changePercent = ((secondHalfAvg - firstHalfAvg) / firstHalfAvg) * 100;
        
        if (Math.abs(changePercent) > 20) {
            if (changePercent > 0) {
                logger.warn(String.format(
                    "⚠ PERFORMANCE DEGRADATION TREND [%s]: %.1f%% slower in recent calls",
                    endpointName, changePercent
                ));
            } else {
                logger.info(String.format(
                    "✓ PERFORMANCE IMPROVEMENT TREND [%s]: %.1f%% faster in recent calls",
                    endpointName, Math.abs(changePercent)
                ));
            }
        }
    }
    
    /**
     * Assert response time is within baseline
     */
    public static void assertWithinBaseline(String endpointName, long responseTime) {
        PerformanceMetrics metrics = endpointMetrics.get(endpointName);
        
        if (metrics == null || metrics.getMeasurementCount() < MINIMUM_SAMPLES) {
            logger.info("Not enough baseline data for " + endpointName);
            return;
        }
        
        DescriptiveStatistics stats = metrics.getStatistics();
        double mean = stats.getMean();
        double upperBound = mean + (stats.getStandardDeviation() * 2); // 95% confidence
        
        if (responseTime > upperBound) {
            String message = String.format(
                "Response time exceeded baseline for %s: %dms > %.0fms (mean: %.0fms)",
                endpointName, responseTime, upperBound, mean
            );
            
            boolean failOnAnomaly = Boolean.parseBoolean(
                System.getProperty("api.fail.on.anomaly", "false")
            );
            
            if (failOnAnomaly) {
                throw new AssertionError(message);
            } else {
                logger.warn("⚠ " + message);
            }
        }
    }
    
    /**
     * Get baseline metrics for endpoint
     */
    public static BaselineMetrics getBaseline(String endpointName) {
        PerformanceMetrics metrics = endpointMetrics.get(endpointName);
        
        if (metrics == null) {
            return null;
        }
        
        DescriptiveStatistics stats = metrics.getStatistics();
        
        return new BaselineMetrics(
            endpointName,
            (long) stats.getMean(),
            (long) stats.getPercentile(50),  // Median
            (long) stats.getPercentile(95),  // 95th percentile
            (long) stats.getPercentile(99),  // 99th percentile
            (long) stats.getMin(),
            (long) stats.getMax(),
            metrics.getMeasurementCount()
        );
    }
    
    /**
     * Print performance report
     */
    public static void printPerformanceReport() {
        String separator = repeatString("=", 80);
        logger.info("\n" + separator);
        logger.info("API PERFORMANCE REPORT");
        logger.info(separator);
        
        endpointMetrics.forEach((endpoint, metrics) -> {
            BaselineMetrics baseline = getBaseline(endpoint);
            if (baseline != null) {
                logger.info(String.format(
                    "\n[%s]\n" +
                    "  Samples: %d\n" +
                    "  Mean: %dms\n" +
                    "  Median: %dms\n" +
                    "  95th percentile: %dms\n" +
                    "  99th percentile: %dms\n" +
                    "  Min: %dms\n" +
                    "  Max: %dms\n" +
                    "  Anomalies: %d",
                    metrics.getEndpointName(),
                    baseline.sampleCount,
                    baseline.mean,
                    baseline.median,
                    baseline.p95,
                    baseline.p99,
                    baseline.min,
                    baseline.max,
                    metrics.getAnomalyCount()
                ));
            }
        });
        
        logger.info("\n" + repeatString("=", 80));
    }
    
    /**
     * Helper method for Java 8 compatibility (String.repeat added in Java 11)
     */
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * Save metrics to file
     */
    private static void saveMetricsToFile(String endpointName, PerformanceMetrics metrics) {
        try {
            String fileName = METRICS_PATH + endpointName + "_metrics.csv";
            boolean isNewFile = !Files.exists(Paths.get(fileName));
            
            try (FileWriter writer = new FileWriter(fileName, true)) {
                // Write header if new file
                if (isNewFile) {
                    writer.write("Timestamp,ResponseTime,StatusCode\n");
                }
                
                // Write recent measurements
                List<Long> recent = metrics.getRecentMeasurements(1);
                if (!recent.isEmpty()) {
                    String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    writer.write(String.format("%s,%d,%d\n", 
                        timestamp, recent.get(0), metrics.getLastStatusCode()));
                }
            }
        } catch (IOException e) {
            logger.error("Failed to save metrics: " + e.getMessage());
        }
    }
    
    /**
     * Alert on anomaly (extensible for external alerting)
     */
    private static void alertOnAnomaly(String endpointName, long responseTime, 
                                       double baseline, double zScore) {
        // This can be extended to send alerts via email, Slack, PagerDuty, etc.
        String alertMessage = String.format(
            "Performance anomaly detected!\n" +
            "Endpoint: %s\n" +
            "Response Time: %dms\n" +
            "Baseline: %.0fms\n" +
            "Z-Score: %.2f\n" +
            "Severity: %s",
            endpointName, responseTime, baseline, zScore,
            Math.abs(zScore) > 5 ? "CRITICAL" : "WARNING"
        );
        
        // For now, just log
        logger.error("🚨 ALERT: " + alertMessage);
        
        // Future: Send to alerting system
        // SlackNotifier.send(alertMessage);
        // EmailNotifier.send(alertMessage);
    }
    
    /**
     * Reset metrics for endpoint
     */
    public static void resetMetrics(String endpointName) {
        endpointMetrics.remove(endpointName);
        logger.info("Metrics reset for: " + endpointName);
    }
    
    /**
     * Reset all metrics
     */
    public static void resetAllMetrics() {
        endpointMetrics.clear();
        logger.info("All metrics reset");
    }
    
    // ========== Inner Classes ==========
    
    /**
     * Performance metrics tracker
     */
    private static class PerformanceMetrics {
        private final String endpointName;
        private final DescriptiveStatistics statistics;
        private final List<Long> measurements;
        private int anomalyCount;
        private int lastStatusCode;
        
        public PerformanceMetrics(String endpointName) {
            this.endpointName = endpointName;
            this.statistics = new DescriptiveStatistics();
            this.measurements = Collections.synchronizedList(new ArrayList<>());
            this.anomalyCount = 0;
            this.statistics.setWindowSize(100); // Keep last 100 measurements
        }
        
        public void addMeasurement(long responseTime, int statusCode) {
            statistics.addValue(responseTime);
            measurements.add(responseTime);
            this.lastStatusCode = statusCode;
        }
        
        public String getEndpointName() {
            return endpointName;
        }
        
        public void recordAnomaly(long responseTime, double zScore) {
            anomalyCount++;
        }
        
        public DescriptiveStatistics getStatistics() {
            return statistics;
        }
        
        public int getMeasurementCount() {
            return measurements.size();
        }
        
        public int getAnomalyCount() {
            return anomalyCount;
        }
        
        public int getLastStatusCode() {
            return lastStatusCode;
        }
        
        public List<Long> getRecentMeasurements(int count) {
            int size = measurements.size();
            int fromIndex = Math.max(0, size - count);
            return new ArrayList<>(measurements.subList(fromIndex, size));
        }
    }
    
    /**
     * Baseline metrics model
     */
    public static class BaselineMetrics {
        public final String endpointName;
        public final long mean;
        public final long median;
        public final long p95;
        public final long p99;
        public final long min;
        public final long max;
        public final int sampleCount;
        
        public BaselineMetrics(String endpointName, long mean, long median, 
                             long p95, long p99, long min, long max, int sampleCount) {
            this.endpointName = endpointName;
            this.mean = mean;
            this.median = median;
            this.p95 = p95;
            this.p99 = p99;
            this.min = min;
            this.max = max;
            this.sampleCount = sampleCount;
        }
        
        @Override
        public String toString() {
            return String.format(
                "BaselineMetrics[%s: mean=%dms, p95=%dms, samples=%d]",
                endpointName, mean, p95, sampleCount
            );
        }
    }
}
