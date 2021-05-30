package utilities;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.*;

public class APIListeners extends APIReports implements ITestListener {
    APIReports report = new APIReports();
    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() instanceof Exception) {
            Reporter.log("Test is skipped due to failed configuration : " + result.getName(), true);
            report.getResult(result);
        } else if (result.getThrowable().getMessage().contains("depends on not successfully finished methods")) {
            Reporter.log("Test is skipped : " + result.getName(), true);
            report.getResult(result);
        } else {
            Reporter.log("Test is skipped with exception : " + result.getName(), true);
            report.getResult(result);
        }
    }
    @Override
    public void onTestFailure(ITestResult tr) {
        report.getResult(tr);
        Reporter.log("Test Case Ended " + tr.getName(), true);
    }
    @Override
    public void onTestSuccess(ITestResult tr) {
        report.getResult(tr);
        Reporter.log("All Steps of Test are completed", true);
        Reporter.log("Test Case Ended " + tr.getName(), true);
    }
    @Override()
    public void onTestStart(ITestResult tr) {
        report.getResult(tr);
        Reporter.log("Test Case started " + tr.getName(), true);
    }
    @Override
    public void onFinish(ITestContext context) {
        report.saveReport();
    }
}