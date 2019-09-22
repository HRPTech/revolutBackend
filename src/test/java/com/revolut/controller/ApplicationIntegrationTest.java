package com.revolut.controller;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;

import org.jooby.test.JoobyRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.revolut.Application;
import com.revolut.model.Transaction;

import io.restassured.http.ContentType;

public class ApplicationIntegrationTest {
	@ClassRule
	public static JoobyRule app = new JoobyRule(new Application());
	private	Integer account1 = 1;
    private Integer account2 = 2;
    private	Integer account3 = 3;
    private Integer account4 = 4;
    private Integer unknownAccount = 100;
	
    @BeforeClass
    public static void setup(){
    	//setup user 1
     	given()
     	.param("balance", "1000")
     	.header("Accept", ContentType.JSON.getAcceptHeader())
     	.post("/account/create").then().statusCode(200).body(equalTo("1"));
	
    	//setup user 2	 
	 	given()
		.param("balance", "5000")
		.header("Accept", ContentType.JSON.getAcceptHeader())
		.post("/account/create").then().statusCode(200).body(equalTo("2"));
	 	
	 	//setup user 3
     	given()
     	.param("balance", "1000")
     	.header("Accept", ContentType.JSON.getAcceptHeader())
     	.post("/account/create").then().statusCode(200).body(equalTo("3"));
	
    	//setup user 4 
	 	given()
		.param("balance", "5000")
		.header("Accept", ContentType.JSON.getAcceptHeader())
		.post("/account/create").then().statusCode(200).body(equalTo("4"));
    }
    
	@Test
	public void testBalanceTransfer()  {     
 
	 	 get("/balance/" + account1).then().assertThat().body(equalTo("1000")).statusCode(200);	
	 	 get("/balance/" + account2).then().assertThat().body(equalTo("5000")).statusCode(200);
		 		
		 given().header("Accept", ContentType.JSON.getAcceptHeader())
		 .contentType("application/json")
		 .body(new Transaction(account1,account2,BigDecimal.valueOf(100)))
		 .post("/transaction").then().statusCode(200);
		 
		 get("/balance/" + account1).then().assertThat().body(equalTo("900")).statusCode(200);			 
		 get("/balance/" + account2).then().assertThat().body(equalTo("5100")).statusCode(200);		 
	}
	
	@Test
	public void testAccountNotExist() {		
		 given().header("Accept", ContentType.JSON.getAcceptHeader())
		 .contentType("application/json")
		 .body(new Transaction(unknownAccount,account1,BigDecimal.valueOf(100)))
		 .post("/transaction").then().statusCode(400);
	}
	
	
	@Test
	public void testInsufficientFunds()  {		
		 get("/balance/" + account3).then().assertThat().body(equalTo("1000")).statusCode(200);	
	 	 get("/balance/" + account4).then().assertThat().body(equalTo("5000")).statusCode(200);
		 		
		 given().header("Accept", ContentType.JSON.getAcceptHeader())
		 .contentType("application/json")
		 .body(new Transaction(account3,account4,BigDecimal.valueOf(100000)))
		 .post("/transaction").then().statusCode(400);
		 
		 get("/balance/" + account3).then().assertThat().body(equalTo("1000")).statusCode(200);			 
		 get("/balance/" + account4).then().assertThat().body(equalTo("5000")).statusCode(200);	
	}
	
}
