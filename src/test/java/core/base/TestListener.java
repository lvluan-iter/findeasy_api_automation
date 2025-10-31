package core.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import core.report.ExtentReportManager;
import org.testng.*;

public class TestListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> methodTest = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
        extent = ExtentReportManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest testNode = extent.createTest(result.getMethod().getMethodName());

        testNode.assignCategory(result.getTestClass().getName());

        methodTest.set(testNode);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        methodTest.get().pass("PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        methodTest.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        methodTest.get().skip("SKIPPED");
    }

    @Override
    public void onStart(ITestContext context) {
        methodTest.remove();
    }
}
