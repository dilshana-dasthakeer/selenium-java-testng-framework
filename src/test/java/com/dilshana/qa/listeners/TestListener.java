package com.dilshana.qa.listeners;

import com.dilshana.qa.driver.DriverManager;
import com.dilshana.qa.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Logs the test lifecycle and attaches a screenshot and the page URL to the Allure report on failure. */
public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("START  {}", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("PASS   {}", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIP   {} (retrying or skipped)", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("FAIL   {} : {}", result.getMethod().getQualifiedName(), result.getThrowable().getMessage());
        if (!DriverManager.hasDriver()) {
            return;
        }
        try {
            byte[] png = ScreenshotUtil.capture(DriverManager.get());
            Allure.addAttachment("Screenshot at failure", "image/png", new ByteArrayInputStream(png), "png");
            Allure.addAttachment("Page URL at failure", "text/plain", DriverManager.get().getCurrentUrl());
        } catch (RuntimeException e) {
            LOG.warn("Could not capture failure evidence: {}", e.getMessage());
        }
    }
}
