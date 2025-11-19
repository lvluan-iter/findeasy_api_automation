package core.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.constants.PathConstants;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance(PathConstants.REPORT);
        }
        return extent;
    }

    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter spark = new ExtentSparkReporter(fileName);

        spark.config()
                .setTheme(Theme.STANDARD);
        String reportFileName = "ExecutionReport.html";
        spark.config()
                .setDocumentTitle(reportFileName);
        spark.config()
                .setReportName(reportFileName);
        spark.config()
                .setEncoding("UTF-8");
        spark.config()
                .setTimelineEnabled(true);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Luân");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        return extent;
    }
}