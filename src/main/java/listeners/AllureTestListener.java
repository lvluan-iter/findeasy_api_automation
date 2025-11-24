package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.EnvReader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class AllureTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START: " + result.getMethod()
                .getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASS : " + result.getMethod()
                .getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAIL : " + result.getMethod()
                .getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIP : " + result.getMethod()
                .getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("==> TEST SUITE START: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("<== TEST SUITE END : " + context.getName());

        try {
            Properties props = new Properties();
            props.setProperty("Env", System.getProperty("env", "Qa"));
            props.setProperty("BaseUrl", EnvReader.getBaseUrl());
            props.setProperty("OS", System.getProperty("os.name"));
            props.setProperty("Java Version", System.getProperty("java.version"));
            props.setProperty("Tester", System.getProperty("user.name"));

            File file = new File("target/allure-results/environment.properties");
            file.getParentFile()
                    .mkdirs();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, null);
            }

        } catch (Exception e) {
            System.err.println("⚠ Could not write environment.properties: " + e.getMessage());

        }
    }
}