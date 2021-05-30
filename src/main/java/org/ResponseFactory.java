package org;
import java.util.Map;
import io.restassured.response.Response;


public class ResponseFactory extends ResponseService {
    private Map<String, Object> commonHeader = Base.getCommonHeaders();
    private Map<String, Object> commonQeury;

    public Response addPlace(String jsonBody) {
        commonQeury= Base.getCommonQueryParameters();
        return getResponse("/maps/api/place/add/json", jsonBody, commonQeury, commonHeader, RequestMethodType.POST);
    }
    public Response getPlace(String placeID) {
        commonQeury= Base.getCommonQueryParameters();
        commonQeury.put("place_id",placeID);
        return getResponse("/maps/api/place/get/json", null, commonQeury, commonHeader, RequestMethodType.GET);
    }
    public Response updatePlace(String jsonBody,String placeID) {
        commonQeury= Base.getCommonQueryParameters();
        commonQeury.put("place_id",placeID);
        return getResponse("/maps/api/place/add/json", jsonBody, commonQeury, commonHeader, RequestMethodType.PUT);
    }
    public Response deletePlace(String jsonBody) {
        commonQeury= Base.getCommonQueryParameters();
        return getResponse("/maps/api/place/delete/json", jsonBody, commonQeury, commonHeader, RequestMethodType.DELETE);
    }
}
