package org.dekuan.ChinaRegionJava;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@AutoConfigureMockMvc
@ContextConfiguration( initializers = { ConfigFileApplicationContextInitializer.class } )
@Slf4j
public class ChinaRegionControllerV1Tests
{
	protected final static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36";

	@Autowired
	protected MockMvc mockMvc;

	//	...
	protected long   tenantId = 1;



	@Before
	public void setup() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}


	@Test
	public void testQueryProvinceListCase1() throws Exception
	{
		this.queryProvinceList();
	}

	@Test
	public void testQueryCityListCase1() throws Exception
	{
		JsonArray provinceList = this.queryProvinceList();
		assertNotNull( provinceList );
		assertTrue( provinceList.size() > 0 );
		for ( int i = 0; i < provinceList.size(); i ++ )
		{
			String provinceId = provinceList.get( i ).getAsJsonObject().get( "id" ).getAsString();
			JsonArray cityList = this.queryCityList( provinceId );
			assertNotNull( cityList );
			assertTrue( cityList.size() > 0 );

			int arraySize = cityList.size();
			for ( int j = 0; j < arraySize; j ++ )
			{
				JsonObject item = cityList.get( j ).getAsJsonObject();
				assertTrue( item.get( "province" ).getAsString().length() > 0 );
				assertTrue( item.has( "city" ) );
				assertTrue( item.get( "name" ).getAsString().length() > 0 );
				assertTrue( item.get( "id" ).getAsString().length() > 0 );
			}
		}
	}

	@Test
	public void testQueryDistrictListCase1() throws Exception
	{
		//	query province list
		JsonArray provinceList = this.queryProvinceList();
		assertNotNull( provinceList );
		assertTrue( provinceList.size() > 0 );
		for ( int i = 0; i < provinceList.size(); i ++ )
		{
			//	query city list
			String provinceId = provinceList.get( i ).getAsJsonObject().get( "id" ).getAsString();
			JsonArray cityList = this.queryCityList( provinceId );
			assertNotNull( cityList );
			assertTrue( cityList.size() > 0 );

			int cityArraySize = cityList.size();
			for ( int j = 0; j < cityArraySize; j ++ )
			{
				JsonObject cityItem = cityList.get( j ).getAsJsonObject();
				assertTrue( cityItem.get( "province" ).getAsString().length() > 0 );
				assertTrue( cityItem.has( "city" ) );
				assertTrue( cityItem.get( "name" ).getAsString().length() > 0 );
				assertTrue( cityItem.get( "id" ).getAsString().length() > 0 );

				//	query district list
				String    cityId       = cityItem.get( "id" ).getAsString();
				JsonArray districtList = this.queryDistrictList( cityId );
				assertNotNull( districtList );
				assertTrue( districtList.size() > 0 );

				int districtArraySize = districtList.size();
				for ( int k = 0; k < districtArraySize; k++ )
				{
					JsonObject districtItem = districtList.get( k ).getAsJsonObject();
					assertTrue( districtItem.has( "province" ) );
					assertTrue( districtItem.get( "city" ).getAsString().length() > 0 );
					assertTrue( districtItem.get( "name" ).getAsString().length() > 0 );
					assertTrue( districtItem.get( "id" ).getAsString().length() > 0 );
				}
			}
		}
	}



	private JsonArray queryDistrictList( String cityId ) throws Exception
	{
		String url = String.format( "/api/v1/chinaRegion/district/list/%s", cityId );
		log.info( "))) will send GET request to url: {}", url );
		MvcResult oResult = mockMvc.perform(
				MockMvcRequestBuilders.get( url )
					.header( "User-Agent", userAgent )
					.header( "X-TenantId", tenantId )
					.accept( MediaType.APPLICATION_JSON ) )
			.andDo( MockMvcResultHandlers.print() )
			.andExpect( status().isOk() )
			.andReturn();

		//	convert string to json object by way 1
		String body = oResult.getResponse().getContentAsString();
		log.info( "))) response body: {}", body );
		JsonObject jsonObject = new Gson().fromJson( body, JsonObject.class );
		assertTrue( jsonObject.isJsonObject() );
		assertEquals( jsonObject.getAsJsonObject().get( "status" ).getAsInt(), 200 );
		assertTrue( jsonObject.getAsJsonObject().get( "body" ).isJsonObject() );
		assertTrue( jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "districtList" ).isJsonArray() );
		JsonArray arrayList = jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "districtList" ).getAsJsonArray();
		assertNotNull( arrayList );
		int arraySize = arrayList.size();
		assertTrue( arraySize > 0 );
		for ( int k = 0; k < arraySize; k++ )
		{
			JsonObject districtItem = arrayList.get( k ).getAsJsonObject();
			assertTrue( districtItem.has( "province" ) );
			assertTrue( districtItem.get( "city" ).getAsString().length() > 0 );
			assertTrue( districtItem.get( "name" ).getAsString().length() > 0 );
			assertTrue( districtItem.get( "id" ).getAsString().length() > 0 );
		}

		return jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "districtList" ).getAsJsonArray();
	}

	private JsonArray queryCityList( String provinceId ) throws Exception
	{
		String url = String.format( "/api/v1/chinaRegion/city/list/%s", provinceId );
		log.info( "))) will send GET request to url: {}", url );
		MvcResult oResult = mockMvc.perform(
				MockMvcRequestBuilders.get( url )
					.header( "User-Agent", userAgent )
					.header( "X-TenantId", tenantId )
					.accept( MediaType.APPLICATION_JSON ) )
			.andDo( MockMvcResultHandlers.print() )
			.andExpect( status().isOk() )
			.andReturn();

		//	convert string to json object by way 1
		String body = oResult.getResponse().getContentAsString();
		log.info( "))) response body: {}", body );
		JsonObject jsonObject = new Gson().fromJson( body, JsonObject.class );
		assertTrue( jsonObject.isJsonObject() );
		assertEquals( jsonObject.getAsJsonObject().get( "status" ).getAsInt(), 200 );
		assertTrue( jsonObject.getAsJsonObject().get( "body" ).isJsonObject() );
		assertTrue( jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "cityList" ).isJsonArray() );
		JsonArray arrayList = jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "cityList" ).getAsJsonArray();
		assertNotNull( arrayList );
		int arraySize = arrayList.size();
		assertTrue( arraySize > 0 );
		for ( int k = 0; k < arraySize; k++ )
		{
			JsonObject districtItem = arrayList.get( k ).getAsJsonObject();
			assertTrue( districtItem.get( "province" ).getAsString().length() > 0 );
			assertTrue( districtItem.has( "city" ) );
			assertTrue( districtItem.get( "name" ).getAsString().length() > 0 );
			assertTrue( districtItem.get( "id" ).getAsString().length() > 0 );
		}

		return jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "cityList" ).getAsJsonArray();
	}

	/**
	 * 	获取省份列表
	 */
	private JsonArray queryProvinceList() throws Exception
	{
		//
		//	创建后，查询
		//
		String url = "/api/v1/chinaRegion/province/list";
		log.info( "))) will send GET request to url: {}", url );
		MvcResult oResult = mockMvc.perform(
				MockMvcRequestBuilders.get( url )
					.header( "User-Agent", userAgent )
					.header( "X-TenantId", tenantId )
					.accept( MediaType.APPLICATION_JSON ) )
			.andDo( MockMvcResultHandlers.print() )
			.andExpect( status().isOk() )
			.andReturn();

		//	convert string to json object by way 1
		String body = oResult.getResponse().getContentAsString();
		log.info( "))) response body: {}", body );
		JsonObject jsonObject = new Gson().fromJson( body, JsonObject.class );
		assertTrue( jsonObject.isJsonObject() );
		assertEquals( jsonObject.getAsJsonObject().get( "status" ).getAsInt(), 200 );
		assertTrue( jsonObject.getAsJsonObject().get( "body" ).isJsonObject() );
		assertTrue( jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "provinceList" ).isJsonArray() );
		JsonArray arrayList = jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "provinceList" ).getAsJsonArray();
		assertNotNull( arrayList );
		int arraySize = arrayList.size();
		assertTrue( arraySize > 0 );
		for ( int i = 0; i < arraySize; i ++ )
		{
			JsonObject item = jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject()
				.get( "provinceList" ).getAsJsonArray().get( i ).getAsJsonObject();
			assertTrue( item.get( "province" ).getAsString().length() > 0 );
			assertTrue( item.has( "city" ) );
			assertTrue( item.get( "name" ).getAsString().length() > 0 );
			assertTrue( item.get( "id" ).getAsString().length() > 0 );
		}

		return jsonObject.getAsJsonObject().get( "body" ).getAsJsonObject().get( "provinceList" ).getAsJsonArray();
	}
}