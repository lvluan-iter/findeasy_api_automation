package core.base;

import com.aventstack.extentreports.ExtentTest;
import core.report.ExtentReportManager;
import core.report.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class TestListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        String className = context.getAllTestMethods()[0].getRealClass()
                .getSimpleName();
        ExtentTestManager.createSuite(className);
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.getInstance()
                .flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        Method method = result.getMethod()
                .getConstructorOrMethod()
                .getMethod();

        String desc = "";
        if (method.isAnnotationPresent(Test.class)) {
            desc = method.getAnnotation(Test.class)
                    .description();
        }
        ExtentTestManager.createTestNode(method.getName(), desc);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null)
            test.pass("✅ Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null)
            test.fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null)
            test.skip("⚠️ Test Skipped");
    }
}