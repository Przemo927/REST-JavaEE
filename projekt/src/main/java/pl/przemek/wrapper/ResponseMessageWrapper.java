package pl.przemek.wrapper;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessageWrapper {
    public static JSONObject wrappMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message", message);
        return jsonObject;
    }
    public static JSONObject wrapMessage(HashMap<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        return jsonObject;
    }
}
