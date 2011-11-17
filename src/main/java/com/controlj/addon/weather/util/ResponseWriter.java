package com.controlj.addon.weather.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ResponseWriter {
    private static final String ERROR_LIST = "errors";
    private HttpServletResponse resp;
    private JSONObject jsonRoot;

    public ResponseWriter(HttpServletResponse resp) {
        this.resp = resp;
        this.jsonRoot = new JSONObject();
    }

    public void putInteger(String fieldName, int value) {
        try {
            jsonRoot.put(fieldName, value);
        } catch (JSONException e) {
            Logging.println("Error encoding json int value", e);
        }
    }

    public void putString(String fieldName, String value) {
        try {
            jsonRoot.put(fieldName, value);
        } catch (JSONException e) {
            Logging.println("Error encoding json string value of '"+value+"'", e);
        }
    }

    public void appendToArray(String arrayName, Map<String, Object> values) {
        try {
            JSONArray array = getOrCreateArray(arrayName);
            JSONObject next = new JSONObject(values);
            array.put(next);
        } catch (JSONException e) {
            Logging.println("Error adding to JSON array", e);
        }

    }

    public boolean hasErrors() {
        return jsonRoot.has(ERROR_LIST);
    }

    public void addValidationError(String fieldName, String message) {
        try {
            JSONArray errors = getOrCreateArray(ERROR_LIST);
            JSONObject error = new JSONObject();
            error.put("errortype","validation");
            error.put("field", fieldName);
            error.put("message", message);
            errors.put(error);
        } catch (JSONException e) {
            Logging.println("Error trying to write out errors with JSON", e);
        }
    }

    public void addError(String message) {
        try {
            JSONArray errors = getOrCreateArray(ERROR_LIST);
            JSONObject error = new JSONObject();
            error.put("errortype","general");
            error.put("message", message);
            errors.put(error);
        } catch (JSONException e) {
            Logging.println("Error trying to write out errors with JSON", e);
        }
    }

    public void write() throws IOException {
        resp.setContentType("application/json");
        try {
            jsonRoot.write(resp.getWriter());
        } catch (JSONException e) {
            throw new IOException("Error writing JSON", e);
        }
    }

    private JSONArray getOrCreateArray(String arrayName) throws JSONException {
        JSONArray result;
        if (jsonRoot.has(arrayName)) {
            result = (JSONArray) jsonRoot.get(arrayName);
        } else {
            result = new JSONArray();
            jsonRoot.put(arrayName, result);
        }
        return result;
    }

}
