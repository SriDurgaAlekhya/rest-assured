package com.myrest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;


public class AllRestApiMethodsTest {

    String BASE_URL = "http://dummy.restapiexample.com/";

    @Test
    public void employee() {
        String employeeId = createEmployee();
        validateEmployye(employeeId, "Alekhya", "28", "1000");
        updateEmployee(employeeId);
        validateEmployye(employeeId, "Akshan", "2", "1000");
        deleteEmployee(employeeId);
    }

    private void deleteEmployee(String employeeId) {
        String deleteUrl = BASE_URL + "api/v1/delete/" + employeeId;
        Response response = when().delete(deleteUrl);
        assertEquals(200, response.statusCode());
        String message = response.jsonPath().getString("success.text");
        assertEquals("successfully! deleted Records", message);

    }

    private void updateEmployee(String employeeId) {
        String updateurl = BASE_URL + "api/v1/update/" + employeeId;

        Response response = given()
                                .body(" {\"name\":\"Akshan\",\"salary\":\"1000\",\"age\":\"2\"} ").
                            when()
                                .put(updateurl);

        assertEquals(200, response.statusCode());

        String name = response.jsonPath().getString("name");

        assertEquals("Akshan", name);
    }

    private void validateEmployye(String employeeId, String name, String age, String salary) {
        String idUrl = BASE_URL + "api/v1/employee/" + employeeId;
        Response response = when().get(idUrl);
        assertEquals(200, response.statusCode());
        JsonPath employeeresponse = response.jsonPath();
        String employee_name = employeeresponse.getString("employee_name");
        String employee_salary = employeeresponse.getString("employee_salary");
        String employee_age = employeeresponse.getString("employee_age");
        assertEquals(name, employee_name);
        assertEquals(age, employee_age);
        assertEquals(salary, employee_salary);
    }

    private String createEmployee() {
        Response response = RestAssured.given().
                                            body("{\"name\":\"Alekhya\",\"salary\":\"1000\",\"age\":\"28\"}").
                                        when().
                                            post(BASE_URL + "api/v1/create").
                                        then().
                                            statusCode(200).
                                        extract().response();
        String employeeId = response.jsonPath().getString("id");
        return employeeId;
    }
}
