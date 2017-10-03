/**
 * 
 */
package com.techm.poc;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.preemptive;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import com.github.fge.jsonschema.SchemaVersion;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import static org.hamcrest.MatcherAssert.*;
import org.junit.BeforeClass; 
import static io.restassured.path.xml.XmlPath.from;
import static io.restassured.path.json.JsonPath.from;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.instanceOf; 
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat; 
import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.annotation.Report;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;


/**
 * @author ad707g
 *
 */
	 @RunWith(DataDrivenTestRunner.class)
	 @DataLoader(filePaths={"testData.xml"},loaderType = LoaderType.XML)
	 //@Report(outputLocation = "file:TestReports")
	 public class DCAEUnitTest{
		    public static Response response;
		    public static String jsonAsString;
	@Test 
	public void TestStatusCodeWithAuthentication(@Param(name="baseURI")String baseURI , @Param(name="basePath")String basePath ,@Param(name="searchPath")String searchPath ,@Param(name="authenticationUser")String authenticationUser, @Param(name="authenticationPwd")String authenticationPwd){
 			RestAssured.baseURI  = baseURI;
 			RestAssured.basePath = basePath;
 			RestAssured.authentication = basic(authenticationUser, authenticationPwd);
		given().
			get(searchPath).
		then().  
		  statusCode(200).log().all();
		}
	@Test 
	public void TestStatusCodeWithoutAuthentication(@Param(name="baseURI")String baseURI , @Param(name="basePath")String basePath,@Param(name="searchPath")String searchPath){
 			RestAssured.baseURI  = baseURI;
 			RestAssured.basePath = basePath;
 			given().
			get(searchPath).
		then().  
		  statusCode(200).log().all();
		}	
	 @Test
	/**
	 * Verify JSON Schema without restAssured 
	 * 
	 */
	public void testSchemaGivenJson(@Param(name="JSONFilePath")String JSONFilePath,@Param(name="SchemaFileName")String SchemaFileName)throws Exception{
		  DataInputStream dis;
		try {
			System.out.println(this.getClass().getResource("/").getPath());  
			dis = new DataInputStream (
				  new FileInputStream (JSONFilePath));
				  byte[] datainBytes = new byte[dis.available()];
				 dis.readFully(datainBytes);
				 dis.close();
				 String content = new String(datainBytes, 0, datainBytes.length);
				 //String content1 = SimpleFileReader.getFileAsString();
				 //System.out.println(content);
				 assertThat(content, matchesJsonSchemaInClasspath(SchemaFileName));
				 System.out.println("JSON Schema validated successfully for given: "+JSONFilePath);
		} catch (Exception e) {
			System.out.println("JSON Schema validation failed for given: "+JSONFilePath);
			System.out.println(e.getMessage());
		}
	       
	    }	
	@Test
	public void postJSONwithHeader(@Param(name="authenticationUser")String authenticationUser, @Param(name="authenticationPwd")String authenticationPwd, @Param(name="dataPostUrl")String dataPostUrl , @Param(name="jsonFile")String jsonFile) {
	    URL file = Resources.getResource(jsonFile);
	    String myJson;
		try {
			myJson = Resources.toString(file, Charsets.UTF_8);
		    Response responsedata = given().auth().preemptive().basic(authenticationUser,authenticationPwd)
			        .contentType("application/vnd.api+json")
			        .body(myJson)
			        .with()
			        .expect()
			        .statusCode(200)
			        .log().ifError()
			        .when()
			        .post(dataPostUrl);
			//String body = responsedata.getBody().asString();
		    //System.out.println(responsedata.prettyPrint());
			System.out.println("POST Successful! for topic: "+dataPostUrl);
		} catch (IOException e) {
			System.out.println("POST Failed! for topic: "+dataPostUrl+ " with exception as "+e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	@Test
	public void postJSONwithoutHeader(@Param(name="dataPostUrl")String dataPostUrl , @Param(name="jsonFile")String jsonFile){
	    URL file = Resources.getResource(jsonFile);
	    String myJson;
		try {
			myJson = Resources.toString(file, Charsets.UTF_8);
		    Response responsedata = given()
			        .contentType("application/vnd.api+json")
			        .body(myJson)
			        .with()
			        .expect()
			        .statusCode(200)
			        .when()
			        .post(dataPostUrl);
			//String body = responsedata.getBody().asString();
			System.out.println("POST Successful! for topic: "+dataPostUrl);
		} catch (IOException e) {
			System.out.println("POST Failed! for topic: "+dataPostUrl+ " with exception as "+e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}	
	//@Test
	/**
	 * Verify JSON Schema for given response JSON 
	 */
	public void testJsonSchema(@Param(name="baseURI")String baseURI , @Param(name="basePath")String basePath , @Param(name="authenticationUser")String authenticationUser, @Param(name="authenticationPwd")String authenticationPwd){
		RestAssured.baseURI  = baseURI;
		RestAssured.basePath = basePath;
		RestAssured.authentication = basic(authenticationUser, authenticationPwd);
		//given().when().get("/ad707g/c1?1imit=1").then().statusCode(200).log().all();
		given().when().get("/ad707g/c1?1imit=1").then().assertThat().body(matchesJsonSchemaInClasspath("Schema_CommonEventFormat_28.1.json")).log().all();
	}
	//@Test 
	@DataLoader(filePaths={"testData.csv"},loaderType = LoaderType.CSV)
	public void TestStatusCodeCSV(@Param(name="baseURI")String baseURI , @Param(name="basePath")String basePath ,@Param(name="searchPath")String searchPath, @Param(name="authenticationUser")String authenticationUser, @Param(name="authenticationPwd")String authenticationPwd){
 			RestAssured.baseURI  = baseURI;
 			RestAssured.basePath = basePath;
 			RestAssured.authentication = basic(authenticationUser, authenticationPwd);
		given().
			get("/ad707g/c1?1imit=1").
		then().  
		  statusCode(200).log().all();
		}	
    

}
