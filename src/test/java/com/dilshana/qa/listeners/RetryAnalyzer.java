package com.dilshana.qa.listeners;

import com.dilshana.qa.config.Settings;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed test up to retry.max times. Attempts are counted per method and per
 * data-provider row. Retries are logged so flaky tests stay visible and can be fixed.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(RetryAnalyzer.class);
    private static final Map<String, Integer> ATTEMPTS = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        String key = result.getMethod().getQualifiedName() + Arrays.toString(result.getParameters());
        int attempt = ATTEMPTS.merge(key, 1, Integer::sum);
        if (attempt <= Settings.maxRetries()) {
            LOG.warn("Retrying {} (retry {} of {})", key, attempt, Settings.maxRetries());
            return true;
        }
        return false;
    }
}
