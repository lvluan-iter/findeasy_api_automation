package core.base;

import com.aventstack.extentreports.ExtentTest;
import core.report.ExtentReportManager;
import core.report.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void setupReport(ITestContext context) {
        ExtentReportManager.getInstance();
        ExtentTestManager.createSuite(context.getSuite().getName());
    }

    @BeforeClass(alwaysRun = true)
    public void setupClass(ITestContext context) {
        String className = this.getClass().getSimpleName();
        ExtentTestManager.createClassNode(className);
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest(Method method) {
        String desc = "";
        if (method.isAnnotationPresent(Test.class)) {
            desc = method.getAnnotation(Test.class).description();
        }
        ExtentTestManager.createTestNode(method.getName(), desc);
    }

    @AfterMethod(alwaysRun = true)
    public void recordResult(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("✅ Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("⚠️ Test Skipped");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        ExtentReportManager.getInstance().flush();
    }
}