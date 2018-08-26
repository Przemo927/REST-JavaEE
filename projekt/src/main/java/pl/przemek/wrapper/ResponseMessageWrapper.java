package pl.przemek.wrapper;

import org.json.simple.JSONObject;

public class ResponseMessageWrapper {
    public static JSONObject wrappMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message", message);
        return jsonObject;
    }
}
