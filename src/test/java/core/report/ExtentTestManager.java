package core.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> suiteNode = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    public static void createSuite(String suiteName) {
        ExtentReports extent = ExtentReportManager.getInstance();
        suiteNode.set(extent.createTest(suiteName));
    }

    public static void createTestNode(String testName, String description) {
        ExtentTest test = suiteNode.get()
                .createNode(
                        description == null || description.isEmpty() ? testName : description
                );
        testNode.set(test);
    }

    public static ExtentTest getTest() {
        return testNode.get();
    }

    public static ExtentTest getSuitNode() {
        return suiteNode.get();
    }
}
