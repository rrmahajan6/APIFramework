package utilities;

import org.Base;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestResult;
import org.testng.Reporter;
import java.util.HashMap;
import java.util.Map;

public class APIReports extends Base {
    public static ExtentSparkReporter sparkReporter;
    public static ExtentReports extent;
    protected static Map<String, ExtentTest> extentTestMap;
    protected static Map<String, ExtentTest> extentNodeMap;

    public static ExtentReports getExtentReportInstance() {
        if (extent == null) {
            extent = new ExtentReports();
            sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/Reports");
            extentTestMap = new HashMap<>();
            extentNodeMap = new HashMap<>();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "environment");
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            sparkReporter.config().setReportName("Dolphin Automation Report");
            sparkReporter.config().setDocumentTitle("Dolphin Automation Report");
            return extent;
        }
        return extent;
    }

    synchronized public void getResult(ITestResult result) {
        String testClass = result.getTestClass().getName();
        testClass = testClass.substring(testClass.lastIndexOf('.') + 1);
        String testCase = result.getName();
        ExtentTest test;
        ExtentTest node;

        if (!extentTestMap.containsKey(testClass)) {
            test = extent.createTest(testClass);
            extentTestMap.put(testClass, test);
        }
        if (!extentNodeMap.containsKey(testClass + testCase)) {
            test = extentTestMap.get(testClass);
            node = test.createNode(testCase);
            extentNodeMap.put(testClass + testCase, node);
        }

        node = extentNodeMap.get(testClass + testCase);
        for (String message : Reporter.getOutput(result)) {
            node.log(Status.INFO, message);
        }

        if (result.getStatus() == ITestResult.FAILURE) {
            node.log(Status.INFO, MarkupHelper.createLabel("Test Case Failed : " + result.getName(), ExtentColor.PURPLE));
            node.log(Status.INFO, MarkupHelper.createLabel("Reason : " + result.getThrowable(), ExtentColor.PURPLE));
            node.assignCategory(testClass);
        } else if (result.getStatus() == ITestResult.SKIP && result.getThrowable() instanceof Exception) {
            node.log(Status.INFO, MarkupHelper.createLabel("Test Case Skipped : " + result.getName(), ExtentColor.PURPLE));
            node.log(Status.INFO, MarkupHelper.createLabel("Reason : " + result.getThrowable(), ExtentColor.PURPLE));
            node.assignCategory(testClass);
        } else if (result.getStatus() == ITestResult.SKIP) {
            node.log(Status.INFO, MarkupHelper.createLabel("Test Case Skipped : " + result.getName(), ExtentColor.PURPLE));
            node.log(Status.INFO, MarkupHelper.createLabel("Reason : " + result.getThrowable(), ExtentColor.PURPLE));
            node.assignCategory(testClass);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            node.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
            node.assignCategory(testClass);
        }
    }
    synchronized public void saveReport() {
        extent.flush();
    }
}
