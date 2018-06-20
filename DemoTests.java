package com.test.api.discovery.overstock;

import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DemoTests {

	
	@Test
	public void putAnything() throws IOException{
			
			String jsonString = "{\"name\":\"Tom Brady\",\"post-season\":[{\"superbowl\":[{\"year\":2001,\"outcome\":\"W\"},{\"year\":2003,\"outcome\":\"W\"},{\"year\":2004,\"outcome\":\"W\"},{\"year\":2007,\"outcome\":\"L\"},{\"year\":2011,\"outcome\":\"L\"},{\"year\":2014,\"outcome\":\"W\"},{\"year\":2016,\"outcome\":\"W\"},{\"year\":2017,\"outcome\":\"L\"}]}]}";
			
			// Send PUT
			System.out.println("Return all W's");
			Response response =
			given()
			.body(jsonString)
			.when()
			.put( "http://httpbin.org/anything")
			.then()
			.statusCode(200)
			.extract()
			.response();
	
			Pattern patternW = Pattern.compile("\"outcome\": \"W\"");
			Matcher matcherW = patternW.matcher(response.body().prettyPrint());
			
			// Count Wins
			int countW = 0;
			while (matcherW.find())
				countW++;
			
			System.out.println("WINS :: " + countW);
			
			Pattern patternL = Pattern.compile("\"outcome\": \"L\"");
			Matcher matcherL = patternL.matcher(response.body().prettyPrint());
			
			// Count Loses
			int countL = 0;
			while (matcherL.find())
				countL++;
			
			System.out.println("LOSES :: " + countL);
		
			// Convert int to string
			String countWasString = new StringBuffer().append(countW).toString();
			String countLasString = new StringBuffer().append(countL).toString();
			
			// Verify
			assertNotNull(response.body().jsonPath().get("data"));
			assert countWasString.equals("5");
			assert countLasString.equals("3");
			
	}

	@Test
	public void deleteAnything() throws IOException{
			
			// Send Delete
			System.out.println("Delete");
			Response response =
			given()
			.when()
			.delete( "http://httpbin.org/anything")
			.then()
			.statusCode(200)
			.extract()
			.response();
	
			Pattern patternOrig = Pattern.compile("\"origin\": \"192.5.106.1\"");
			Matcher matcherOrig = patternOrig.matcher(response.body().prettyPrint());
			
			Pattern patternDel = Pattern.compile("\"method\": \"DELETE\"");
			Matcher matcherDel = patternDel.matcher(response.body().prettyPrint());
			
			Pattern patternNull = Pattern.compile("\"json\": null");
			Matcher matcherNull = patternNull.matcher(response.body().prettyPrint());
			
			Pattern patternUrl = Pattern.compile("\"url\": \"http://httpbin.org/anything\"");
			Matcher matcherUrl = patternUrl.matcher(response.body().prettyPrint());
			
			// Verify
			assert matcherOrig.find();
			assert matcherDel.find();
			assert matcherNull.find();
			assert matcherUrl.find();
			
			
	}

	

}
