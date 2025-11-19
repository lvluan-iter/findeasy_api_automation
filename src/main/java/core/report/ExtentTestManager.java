package core.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    private static final Map<String, ExtentTest> CLASS_NODE_MAP = new HashMap<>();
    private static final ThreadLocal<ExtentTest> TEST_NODE = new ThreadLocal<>();

    public static ExtentTest getOrCreateClassNode(String className) {
        if (!CLASS_NODE_MAP.containsKey(className)) {
            ExtentReports extent = ExtentReportManager.getInstance();
            ExtentTest classNode = extent.createTest(className);
            CLASS_NODE_MAP.put(className, classNode);
        }
        return CLASS_NODE_MAP.get(className);
    }

    public static void createTestNode(String testName, String description, ExtentTest classNode) {
        ExtentTest test = classNode.createNode(
                (description == null || description.isBlank()) ? testName : description
        );
        TEST_NODE.set(test);
    }

    public static ExtentTest getTest() {
        return TEST_NODE.get();
    }
}

