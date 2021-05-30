package org;
import com.aventstack.extentreports.ExtentReports;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import utilities.APIListeners;
import utilities.APIReports;
import utilities.PriorityInterceptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Listeners({APIListeners.class, PriorityInterceptor.class})
public class Base {
    public static ResponseFactory responseFactory;
    public static Properties properties;
    public static ExtentReports extent;
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        loadPropertiesFiles(System.getProperty("user.dir")+"/src/main/resources/configuration.properties");
        responseFactory=new ResponseFactory();
        extent= APIReports.getExtentReportInstance();
    }
    public static Map getCommonHeaders(){
        HashMap<String,Object> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        return headers;
    }
    public static Map getCommonQueryParameters(){
        HashMap<String,Object> queryParams=new HashMap<>();
        queryParams.put("key","qaclick123");
        return queryParams;
    }
    public void loadPropertiesFiles(String filepath) {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            try {
                properties.load(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
