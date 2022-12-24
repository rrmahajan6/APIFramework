package forPracticePurposeONLY;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.APIUtility.getJsonObjectFromJsonFile;

public class mockAPI {
    public static void main(String[] args) {
        String courses=getJsonObjectFromJsonFile("courses").toString();
        /*
        1. Print No of courses returned by API
        2.Print Purchase Amount
        3. Print Title of the first course
        4. Print All course titles and their respective Prices
        5. Print no of copies sold by RPA Course
        6. Verify if Sum of all Course prices matches with Purchase Amount
         */
        JsonPath jsonPath=new JsonPath(courses);
        //1. Print No of courses returned by API
        System.out.println(jsonPath.getInt("courses.size()"));
        //2.Print Purchase Amount
        System.out.println(jsonPath.getInt("dashboard.purchaseAmount"));
        //3. Print Title of the first course
        System.out.println(jsonPath.getString("courses[0].title"));
        //4. Print All course titles and their respective Prices
        int totalCourses=jsonPath.getInt("courses.size()");
        for(int i=0;i<totalCourses;i++){
            System.out.println(jsonPath.get("courses["+i+"].title"));
            System.out.println(jsonPath.get("courses["+i+"].price"));
            System.out.println(jsonPath.get("courses["+i+"].copies"));
        }
        // 5. Print no of copies sold by RPA Course
        for(int i=0;i<totalCourses;i++){
           if(jsonPath.getString("courses["+i+"].title").equalsIgnoreCase("RPA")){
               System.out.println(jsonPath.getInt("courses["+i+"].copies"));
               break;
           }
        }
        //6. Verify if Sum of all Course prices matches with Purchase Amount
        int sum=0;
        for(int i=0;i<totalCourses;i++){
            sum+=jsonPath.getInt("courses["+i+"].price")*jsonPath.getInt("courses["+i+"].copies");
        }
        if(jsonPath.getInt("dashboard.purchaseAmount")==sum){
            System.out.println("purchase amount is correct "+jsonPath.getInt("dashboard.purchaseAmount")+" "+sum);
        }
        else System.out.println("purchase amount is not correct"+jsonPath.getInt("dashboard.purchaseAmount")+" "+sum);
    }

    //parameterization using dataprovider
    @Test(dataProvider="BooksData")
    public void addBook(String isbn,String aisle)
    {
        RestAssured.baseURI="http://216.10.245.166";
        Response resp=given().
                header("Content-Type","application/json").
                body(Addbook(isbn,aisle)).
                when().
                post("/Library/Addbook.php").
                then().assertThat().statusCode(200).
                extract().response();
        JsonPath js= rawToJson(resp.toString());
        String id=js.get("ID");
        System.out.println(id);
    }
    @DataProvider(name="BooksData")
    public Object[][]  getData()
    {
        return new Object[][] {{"ojfwty","9363"},{"cwetee","4253"}, {"okmfet","533"} };
    }
    public static JsonPath rawToJson(String response){
        JsonPath jsonPath=new JsonPath(response);
        return jsonPath;
    }
    public static String Addbook(String str,String str2){
        return str;
    }


    @Test
    public void addBook()
    {
        RestAssured.baseURI="http://216.10.245.166";
        Response resp=given().
                header("Content-Type","application/json").
                body(GenerateStringFromResource("C:\\Users\\rahul\\Documents\\Addbookdetails.json")).
                when().
                post("/Library/Addbook.php").
                then().assertThat().statusCode(200).
                extract().response();
        JsonPath js= rawToJson(resp.toString());
        String id=js.get("ID");
        System.out.println(id);
    }
    public static String GenerateStringFromResource(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
