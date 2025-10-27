package core.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import core.constants.FrameworkConstants;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class BaseTest {
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter(FrameworkConstants.REPORT);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeMethod
    public void start(Method method) {
        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void end(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) test.fail(result.getThrowable());
        else if (result.getStatus() == ITestResult.SUCCESS) test.pass("PASSED");
    }

    @AfterSuite
    public void flush() {
        extent.flush();
    }
}
