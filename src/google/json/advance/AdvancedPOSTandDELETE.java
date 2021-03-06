package google.json.advance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import commonmethods.ReusebleMethods;
import google.json.advance.datafiles.PayLoad;
import google.json.advance.datafiles.Resources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AdvancedPOSTandDELETE {
	
	Properties prop = new Properties();
	
	@Before
	public void getData() throws IOException {
		
		FileInputStream fileloca = new FileInputStream("/Users/work/Desktop/Jawad/DemoProject/src/evn.properties");  // in windows it's //Users//work//..
		prop.load(fileloca);
//		prop.getProperty("HOST");  Base URL from the properties
	}
	
	@Test
	public  void testingPostAndDeleteRequests() {
		
		// Task 1, getting a return response and storing it in a string
		// Base URL
		RestAssured.baseURI = prop.getProperty("HostURL");
		
		Response response = given().log().all().
				queryParam("key",prop.getProperty("GoogleKey")).
				body(PayLoad.getPostData()).   // getting the post data from PayLoad class 
		when().
				post(Resources.placePostData()).
		
		then().
				assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
					body("status", equalTo("OK")).and().extract().response();
		
		
		// Task 2, extracting the place_id from the response
		JsonPath jsonObj =  ReusebleMethods.rawToJSON(response);
		System.out.println((String)jsonObj.get("place_id"));
					
		
		// put the place_id this in delete request
		given().
				queryParam("key",prop.getProperty("GoogleKey")). // importing the key from evn.properties
				body("{" + 
						"  \"place_id\": \"" + jsonObj.get("place_id") + "\"" + 
						"}").
		when().
				post(Resources.placeDeleteData()).
		then().
				assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
					body("status", equalTo("OK"));
	}

}
