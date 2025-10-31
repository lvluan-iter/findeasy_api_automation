package core.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import core.report.ExtentReportManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        extent = ExtentReportManager.getInstance();
    }

    @BeforeMethod
    public void start(ITestResult result) {
        String desc = result.getMethod().getDescription();

        if (desc == null || desc.isEmpty()) {
            desc = result.getMethod().getMethodName();
        }

        test = extent.createTest(desc);
    }

    @AfterMethod
    public void end(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("PASSED");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("SKIPPED");
        }
    }

    @AfterSuite
    public void flush() {
        extent.flush();
    }
}
