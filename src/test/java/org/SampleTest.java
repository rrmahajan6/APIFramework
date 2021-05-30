package org;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.APIUtility.*;

public class SampleTest extends Base {
    public static String placeID;
    @Test(priority = 0)
    public void verifyAddPlaceAPI(){
        JsonObject jsonObject = getJsonObjectFromJsonFile("sample");
        String jsonBody = getJsonBody(jsonObject,"types[2]","type1","types[3]","type2");
        Response addPlaceResult = responseFactory.addPlace(jsonBody);
        placeID=getMemberValueAsString(addPlaceResult,"place_id");
        Assert.assertEquals(getMemberValueAsString(addPlaceResult,"scope"),"APP","Add Place is failed");
        Assert.assertEquals(getMemberValueAsString(addPlaceResult,"status"),"OK","Add Place is failed");
        Assert.assertEquals(addPlaceResult.statusCode(),200,"Add operation is failed");
//        System.out.println(addPlaceResult.asString());
    }
    @Test
    public void verifyGetPlaceAPI(){
        Response getPlaceResult = responseFactory.getPlace(placeID);
        Assert.assertEquals(getPlaceResult.statusCode(),200,"get operation is failed");
     //   System.out.print(getPlaceResult.asString());
    }
    @Test
    public void verifyUpdatePlaceAPI(){
        JsonObject jsonObject = getJsonObjectFromJsonFile("sample");
        String jsonBody = getJsonBody(jsonObject);
        Response updatePlaceResult = responseFactory.updatePlace(jsonBody,"f4512a2362b2c4fd5b028bf245910bb5");
        Assert.assertEquals(updatePlaceResult.statusCode(),200,"update operation is failed");
     //   System.out.print(updatePlaceResult.asString());
    }
    @Test
    public void verifyDeletePlaceAPI(){
        String jsonBody=APIUtility.getJsonBody("place_id/fd/dy[0]",placeID);
        Response deletePlaceResult = responseFactory.deletePlace(jsonBody);
        Assert.assertEquals(deletePlaceResult.statusCode(),200,"delete operation is failed");
//        System.out.print(deletePlaceResult.asString());
    }
}
