package org;
import org.testng.annotations.BeforeSuite;
import java.util.HashMap;
import java.util.Map;
public class Base {
    public static ResponseFactory responseFactory;
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        responseFactory=new ResponseFactory();
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
}
