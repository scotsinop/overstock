package com.test.api.discovery.overstock;

import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.*;     
import org.jsoup.nodes.*;
import  org.jsoup.parser.*;


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
			
			
			String data = response.body().jsonPath().get("data");
			System.out.println("DATA: " + data);
			boolean nameExists = data.matches("(?i).*Tom Brady.*");
			System.out.println("NameExists: " + nameExists);
			
			// Verify
			assert (nameExists);
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
	
			Pattern patternOrig = Pattern.compile("\"origin\": \"75.68.48.182\"");
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

	@Test
	public void getReviewsByISBN() throws IOException{
			
			String key = "Xz3J1TuhfmgVlWUY71C0g";
			BigInteger isbn = new BigInteger("9780385508452");
			
			System.out.println("Get Reviews");
			Response response =
			given()
			.when()
			.get( "https://www.goodreads.com/book/isbn/"+ isbn + "?format=xml&key=" + key)
			.then()
			.statusCode(200)
			.extract()
			.response();
	

			String resp = response.body().prettyPrint();
			Document doc = Jsoup.parse(resp, "", Parser.xmlParser());
			String authName = doc.select("author").select("name").text();
			System.out.println("Display Author Object :: " + authName);
			String isbnNum = doc.select("book").select("isbn13").text();
			System.out.println("Display ISBN :: " + isbnNum);
			String pubYear = doc.select("book").select("publication_year").text();
			System.out.println("Display Publication Year :: " + pubYear);
			
					
			assert authName.contains("Sarah Helm");
			assert isbnNum.contains("9780385508452");
			assert pubYear.contains("2006");
			
			
	}
	
	 
		@Test
		public void getAuthPaginate() throws IOException{
				
				String key = "Xz3J1TuhfmgVlWUY71C0g";
				BigInteger id = new BigInteger("68");
				int page1 = 1;
				int page2 = 2;
				
				System.out.println("Get ID Page 1");
				Response response1 =
				given()
				.when()
				.get( "https://www.goodreads.com/author/list.xml" + "?id=" + id + "&page=" + page1 + "&key=" + key)
				.then()
				.statusCode(200)
				.extract()
				.response();
		

				System.out.println("Page1 :: " + response1.body().prettyPrint());
				String resp1 = response1.body().prettyPrint();
				Document doc1 = Jsoup.parse(resp1, "", Parser.xmlParser());
				String isbnNum1 = doc1.select("book").select("isbn13").text();
				
				
				System.out.println("Get ID Page 2");
				Response response2 =
				given()
				.when()
				.get( "https://www.goodreads.com/author/list.xml" + "?id=" + id + "&page=" + page2 + "&key=" + key)
				.then()
				.statusCode(200)
				.extract()
				.response();
		

				System.out.println("Page :: " + response2.body().prettyPrint());
				String resp2 = response2.body().prettyPrint();
				Document doc2 = Jsoup.parse(resp2, "", Parser.xmlParser());
				String isbnNum2 = doc2.select("book").select("isbn13").text();
				System.out.println("Display ISBN1 :: " + isbnNum1);
				System.out.println("Display ISBN2 :: " + isbnNum2);

				String[] array1 = isbnNum1.split(" ");
				int array1Len = array1.length;
				System.out.println("Array1 length :: " + array1Len);
				String array1Last = array1[array1Len -1];
				System.out.println("Array1 last :: " + array1Last);
				
				String[] array2 = isbnNum2.split(" ");
				String array2First = array2[0];
				System.out.println("Array2 first :: " + array2First);
				

				assert !(array2First.equals(array1Last));
				
				
		}
		

}
