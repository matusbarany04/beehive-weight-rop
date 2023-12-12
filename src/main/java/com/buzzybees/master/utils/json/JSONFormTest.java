package com.buzzybees.master.utils.json;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSONFormTest {
    @Test
    public void testIsValidFormMissingRequiredParam() {
        // Arrange
        JSONForm jsonForm = new JSONForm(new ArrayList<>());
        jsonForm.addParam(new JSONParam("name", ParamType.STRING, true));
        jsonForm.addParam(new JSONParam("age", ParamType.NUMERIC, true));
        JSONObject jsonObject = new JSONObject().put("name", "John");

        // Act
        boolean result = jsonForm.isInValidForm(jsonObject);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidFormExtraParam() {
        // Arrange
        JSONForm jsonForm = new JSONForm(new ArrayList<>());
        jsonForm.addParam(new JSONParam("name", ParamType.STRING, true));
        jsonForm.addParam(new JSONParam("age", ParamType.NUMERIC, true));
        JSONObject jsonObject = new JSONObject()
                .put("name", "John")
                .put("age", 25)
                .put("extraParam", "extra");

        // Act
        boolean result = jsonForm.isInValidForm(jsonObject);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidFormInvalidType() {
        // Arrange
        JSONForm jsonForm = new JSONForm(new ArrayList<>());
        jsonForm.addParam(new JSONParam("name", ParamType.STRING, true));
        jsonForm.addParam(new JSONParam("age", ParamType.NUMERIC, true));
        JSONObject jsonObject = new JSONObject()
                .put("name", "John")
                .put("age", "25"); // Incorrect type for age

        // Act
        boolean result = jsonForm.isInValidForm(jsonObject);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidFormEmptyForm() {
        // Arrange
        JSONForm jsonForm = new JSONForm(new ArrayList<>());
        JSONObject jsonObject = new JSONObject();

        // Act
        boolean result = jsonForm.isInValidForm(jsonObject);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidFormValidWithExtraParam() {
        // Arrange
        JSONForm jsonForm = new JSONForm(new ArrayList<>());
        jsonForm.addParam(new JSONParam("name", ParamType.STRING, true));
        jsonForm.addParam(new JSONParam("age", ParamType.NUMERIC, true));
        jsonForm.addParam(new JSONParam("city", ParamType.STRING, false)); // Optional param

        JSONObject jsonObject = new JSONObject()
                .put("name", "John")
                .put("age", 25)
                .put("city", "New York");

        // Act
        boolean result = jsonForm.isInValidForm(jsonObject);

        // Assert
        assertTrue(result);
    }
}
