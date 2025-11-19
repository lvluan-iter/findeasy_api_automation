package listeners;

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
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.getInstance()
                .flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();

        ExtentTest classNode = ExtentTestManager.getOrCreateClassNode(className);

        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        String desc = method.isAnnotationPresent(Test.class)
                ? method.getAnnotation(Test.class).description()
                : method.getName();

        ExtentTestManager.createTestNode(method.getName(), desc, classNode);
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