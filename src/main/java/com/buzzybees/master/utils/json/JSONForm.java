package com.buzzybees.master.utils.json;

import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class JSONForm {


    ArrayList<JSONParam> params = new ArrayList<>();

    public JSONForm(ArrayList<JSONParam> params) {
        this.params = params;
    }

    public JSONForm() {

    }


    /**
     * Adds a {@code JSONParam} to the form.
     *
     * @param param The {@code JSONParam} to add.
     * @return The updated {@code JSONForm} instance.
     */
    public JSONForm addParam(JSONParam param) {
        params.add(param);
        return this;
    }

    /**
     * Removes the last added {@code JSONParam} from the form.
     *
     * @return The updated {@code JSONForm} instance.
     */
    public JSONForm popParam() {
        if (!params.isEmpty()) {
            params.remove(params.size() - 1);
        }
        return this;
    }

    /**
     * Removes the specified {@code JSONParam} from the form.
     *
     * @param param The {@code JSONParam} to remove.
     * @return The updated {@code JSONForm} instance.
     */
    public JSONForm removeParam(JSONParam param) {
        params.remove(param);
        return this;
    }

    public ArrayList<JSONParam> getParams() {
        return params;
    }


    /**
     * Retrieves a list of {@code JSONParam} objects containing only the required parameters.
     *
     * @return A {@code List} of {@code JSONParam} objects that are marked as required.
     */
    public List<JSONParam> getRequiredParams() {
        return params.stream()
                .filter(JSONParam::isRequired)
                .collect(Collectors.toList());
    }


    /**
     * Checks if the provided JSON object is in a valid form by verifying the presence
     * of all required parameters.
     *
     * @param jsonObject The JSON object to check.
     * @return {@code true} if the JSON object is in a valid form, {@code false} otherwise.
     */
    public boolean isInValidForm(JSONObject jsonObject) {
        List<JSONParam> requiredParams = getRequiredParams();


        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (params.stream().noneMatch(param -> param.getName().equals(key))) {
                return false; // Extra parameter found
            }
        }

        for (JSONParam requiredParam : requiredParams) {
            String paramName = requiredParam.getName();

            // Check if the required parameter is present in the JSON object
            if (!jsonObject.has(paramName)) {
                return false;
            }

            // checks validity of param value
            Object paramValue = jsonObject.get(paramName);
            if (!requiredParam.isValidType(paramValue)) {
                return false;
            }
        }

        // All required parameters are present in the JSON object and no extra params were found
        return true;
    }


    public HashMap<String, String> getParamTypesMap() {
        HashMap<String, String> paramTypesMap = new HashMap<>();

        for (JSONParam param : params) {
            paramTypesMap.put(param.getName(), param.getParamType().name());
        }

        return paramTypesMap;
    }

}
