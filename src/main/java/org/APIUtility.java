package org;

import com.google.gson.*;
import io.restassured.response.Response;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIUtility extends Base{

    private static boolean flag = true;
    private static String parentMemberName;
    private static JsonObject orignalJsonObject;

    public static JsonElement getMemberValue(JsonObject jObj, String memberXPath) {

        if (memberXPath.contains("/")) {
            String[] members = memberXPath.split("/");
            int index = getJsonArrayIndex(members[0]);
            String memberName = members[0].split("\\[")[0];
            if (jObj.getAsJsonObject().get(memberName).isJsonArray()) {
                JsonArray jArray = jObj.getAsJsonObject().get(memberName).getAsJsonArray();
                JsonObject jObjTemp = jArray.get(index).getAsJsonObject();
                return getMemberValue(jObjTemp, memberXPath.replaceFirst("/*[^/]*/", ""));
            } else {
                if (jObj.get(members[0]).isJsonObject())
                    return getMemberValue(jObj.get(members[0]).getAsJsonObject(),
                            memberXPath.replaceFirst("/*[^/]*/", ""));
            }
        } else {
            if (memberXPath.contains("[")) {
                int index = getJsonArrayIndex(memberXPath);
                memberXPath = memberXPath.split("\\[")[0];
                return jObj.getAsJsonArray(memberXPath).get(index);
            }
            return jObj.get(memberXPath);
        }
        return null;
    }

    public static int getJsonArrayIndex(String value) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]+");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find())
            return Integer.parseInt(matcher.group(1));
        else
            return -1;
    }
//get value for provided argument
    public static String getMemberValueAsString(Response res, String memberName) {
        String memberValue = res.jsonPath().getString(memberName);
        if (memberValue != null) {
            memberValue = memberValue.replaceAll("\"", "");
        }
        return memberValue;
    }

    public static String getMemberValueAsString(JsonObject jObject, String memberName) {
        String memberValue = jObject.get(memberName).getAsString();
        if (memberValue != null) {
            memberValue = memberValue.replaceAll("\"", "");
        }
        return memberValue;
    }

    public static JsonElement getMemberValue(Response res, String memberXPath) {
        JsonObject jObj = getJsonObject(res);
        return getMemberValue(jObj, memberXPath);
    }

    public static JsonObject getJsonObject(Response res) {
        JsonElement jElement = new JsonParser().parse(res.asString());
        JsonObject jObject = jElement.getAsJsonObject();
        return jObject;
    }

    public static JsonObject getJsonObject(String json) {
        JsonElement jElement = new JsonParser().parse(json);
        JsonObject jObject = jElement.getAsJsonObject();
        return jObject;
    }

    private static JsonElement getJsonBody(JsonObject jObj, String memberXPath, Object value) {

        if (memberXPath.contains("/")) {
            String[] members = memberXPath.split("/");
            int index = getJsonArrayIndex(members[0]);
            String memberName = members[0].split("\\[")[0];
            if (!jObj.getAsJsonObject().keySet().contains(memberName)) {

                if (members[0].contains("[")) {
                    jObj.getAsJsonObject().add(memberName, new JsonArray());
                } else {
                    jObj.getAsJsonObject().add(memberName, new JsonObject());
                }

            }

            if (jObj.getAsJsonObject().get(memberName).isJsonArray()) {
                JsonArray jArray = jObj.getAsJsonObject().get(memberName).getAsJsonArray();


                while (index >= jArray.size() && jArray.size() > 0) {

                    JsonObject jObjTemp = jArray.get(jArray.size() - 1).getAsJsonObject();
                    JsonObject jObjCopy = jObjTemp.deepCopy();
                    jArray.add(jObjCopy);
                }

                if (jArray.size() == 0) {
                    jArray.add(new JsonObject());
                }

                JsonObject jObjTemp = jArray.get(index).getAsJsonObject();
                JsonObject updatedJobj = (JsonObject) getJsonBody(jObjTemp, memberXPath.replaceFirst("/*[^/]*/", ""), value);
                jArray.set(index, updatedJobj);
                jObj.remove(memberName);
                jObj.add(memberName, jArray);
                return jObj;

            } else {
                if (jObj.get(members[0]).isJsonObject())
                    jObj.add(members[0], getJsonBody(jObj.get(members[0]).getAsJsonObject(),
                            memberXPath.replaceFirst("/*[^/]*/", ""), value));
                return jObj;

            }
        } else {
            if (memberXPath.contains("[")) {
                int index = getJsonArrayIndex(memberXPath);
                memberXPath = memberXPath.split("\\[")[0];
                JsonArray ja = null;
                if (jObj.keySet().contains(memberXPath)) {
                    ja = jObj.getAsJsonArray(memberXPath);
                } else {
                    jObj.add(memberXPath, new JsonArray());
                    ja = jObj.getAsJsonArray(memberXPath);
                }

                if (value instanceof Integer) {
                    ja.add((Number) value);
                } else if (value instanceof String) {
                    ja.add(value.toString());
                } else if (value instanceof Boolean) {
                    ja.add((Boolean) value);
                } else if (value instanceof Character) {
                    ja.add((Character) value);
                } else if (value instanceof JsonObject) {
                    jObj.add(memberXPath, (JsonElement) value);
                } else if (value instanceof Double) {
                    jObj.addProperty(memberXPath, (Number) value);
                } else if (value == null) {
                    jObj.remove(memberXPath);
                }
                return jObj;

            }
            if (value instanceof Integer) {
                jObj.addProperty(memberXPath, (Number) value);
            } else if (value instanceof String) {
                jObj.addProperty(memberXPath, value.toString());
            } else if (value instanceof Boolean) {
                jObj.addProperty(memberXPath, (Boolean) value);
            } else if (value instanceof Character) {
                jObj.addProperty(memberXPath, (Character) value);
            } else if (value instanceof JsonObject) {
                jObj.add(memberXPath, (JsonElement) value);
            } else if (value instanceof Double) {
                jObj.addProperty(memberXPath, (Number) value);
            } else if (value == null) {
                jObj.remove(memberXPath);
            }
            return jObj;
        }

    }
//updating json body with new values
    public static String getJsonBody(JsonObject jsonObject, Object... keys) {

        for (int i = 0; i < keys.length; i++) {
            jsonObject = (JsonObject) getJsonBody(jsonObject, keys[i].toString(), (Object) keys[++i]);
        }
        return jsonObject.toString();
    }

    public static JsonObject getJsonObjectFromJsonFile(String jsonFileName) {
        FileReader file = null;

        try {
            file = new FileReader("src/main/resources/" + jsonFileName + ".json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(file, JsonObject.class);

        return jsonObject;
    }

    public static String getJsonBody(String... str) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i < str.length; i += 2) {
            m.put(str[i], str[i + 1]);
        }
        String jsonString = new GsonBuilder().disableHtmlEscaping().create().toJson(m);
        System.out.println(jsonString);
        return jsonString;
    }

    public static JsonObject removeKeysFromJsonObject(JsonObject jObj, String... memberXPaths) {

        for (String xPath : memberXPaths) {
            if (xPath.contains("/")) {
                jObj = (JsonObject) removeKeyFromJsonObject(jObj, xPath);
            } else {
                jObj.remove(xPath);
            }
        }
        return jObj;
    }

    public static JsonElement removeKeyFromJsonObject(JsonObject jObj, String memberXPath) {

        populateOnlyOnce(memberXPath.split("/")[0], jObj);
        if (memberXPath.split("/").length == 1 && flag == false) {
            flag = true;
        }

        if (memberXPath.contains("/")) {
            String[] members = memberXPath.split("/");
            int index = getJsonArrayIndex(members[0]);
            String memberName = members[0].split("\\[")[0];
            if (jObj.getAsJsonObject().get(memberName).isJsonArray()) {
                JsonArray jArray = jObj.getAsJsonObject().get(memberName).getAsJsonArray();
                JsonObject jObjTemp = jArray.get(index).getAsJsonObject();
                return removeKeyFromJsonObject(jObjTemp, memberXPath.replaceFirst("/*[^/]*/", ""));
            } else {
                if (jObj.get(members[0]).isJsonObject())
                    return removeKeyFromJsonObject(jObj.get(members[0]).getAsJsonObject(),
                            memberXPath.replaceFirst("/*[^/]*/", ""));
            }
        } else {
            if (memberXPath.contains("[")) {
                int index = getJsonArrayIndex(memberXPath);
                memberXPath = memberXPath.split("\\[")[0];
                jObj.getAsJsonArray(memberXPath).remove(index);
                orignalJsonObject.remove(parentMemberName);
                orignalJsonObject.add(parentMemberName, jObj);
                return orignalJsonObject;
            }
            jObj.remove(memberXPath);
            orignalJsonObject.remove(parentMemberName);
            orignalJsonObject.add(parentMemberName, jObj);
            return orignalJsonObject;
        }
        return null;
    }

    private static void populateOnlyOnce(String member, JsonObject jsonObject) {
        if (flag) {
            parentMemberName = member.split("\\[")[0];
            orignalJsonObject = jsonObject.deepCopy();
            flag = false;
        }
    }
}


