package org.dekuan.ChinaRegionJava.services;

import org.dekuan.ChinaRegionJava.exceptions.HttpExceptions;
import org.dekuan.ChinaRegionJava.models.RestChinaRegionCityListResponse;
import org.dekuan.ChinaRegionJava.models.RestChinaRegionDistrictListResponse;
import org.dekuan.ChinaRegionJava.models.RestChinaRegionProvinceListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *	国家统计局
 *	全国统计用区划代码和城乡划分代码
 * 	<a href="http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/index.html">...</a>
 *
 * 	开源项目
 * 	<a href="https://github.com/dekuan/ChinaRegionJava">...</a>
 */
@Getter
@Setter
@NoArgsConstructor
@Service
@Slf4j
public class ChinaRegionService
{
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	public static final class RegionNode
	{
		@Builder.Default
		String province = "";

		@Builder.Default
		String city = "";

		@Builder.Default
		String name = "";

		@Builder.Default
		String id = "";
	}

	private JsonNode jsonProvince = null;
	private JsonNode jsonCity = null;
	private JsonNode jsonDistrict = null;

	private List<RegionNode> cacheProvinceList = null;
	private HashMap<String,List<RegionNode>> cacheCityMap = new HashMap<>();
	private HashMap<String,List<RegionNode>> cacheDistrictMap = new HashMap<>();



	@PostConstruct
	public void init()
	{
		this.jsonProvince = this._loadJsonFromResource( "chinaRegions/province.json" );
		this.jsonCity     = this._loadJsonFromResource( "chinaRegions/city.json" );
		this.jsonDistrict = this._loadJsonFromResource( "chinaRegions/district.json" );


		List<RegionNode> provinceList = this.getProvinceList();
	}



	public boolean isProvinceExists( String provinceId )
	{
		if ( Strings.isBlank( provinceId ) )
		{
			return false;
		}

		//	...
		List<RegionNode> provinceList = this.getProvinceList();
		if ( null == provinceList || 0 == provinceList.size() )
		{
			return false;
		}
		for ( RegionNode node : provinceList )
		{
			if ( node.id.equalsIgnoreCase( provinceId ) )
			{
				return true;
			}
		}

		return false;
	}
	public RestChinaRegionProvinceListResponse getRestProvinceList()
	{
		return RestChinaRegionProvinceListResponse.builder()
			.status( HttpStatus.OK.value() )
			.body( RestChinaRegionProvinceListResponse.Body.builder()
				.provinceList( this.getProvinceList() )
				.build() )
			.build();
	}
	public List<RegionNode> getProvinceList()
	{
		if ( null != cacheProvinceList && cacheProvinceList.size() > 0 )
		{
			return cacheProvinceList;
		}
		if ( null == jsonProvince || ! jsonProvince.isArray() )
		{
			throw new HttpExceptions.InternalServerError( "省份列表数据未装载" );
		}

		//	...
		this.cacheProvinceList = new ArrayList<>();
		ArrayNode arrayFields = (ArrayNode )this.jsonProvince;
		arrayFields.forEach( node -> {
			if ( node.isObject() )
			{
				String name = node.get( "name" ).asText( "" );
				String id = node.get( "id" ).asText( "" );
				this.cacheProvinceList.add( RegionNode.builder()
						.province( name )
						.name( name )
						.id( id ).build() );
			}
		});

		return this.cacheProvinceList;
	}


	public boolean isCityExists( String provinceId, String cityId )
	{
		if ( Strings.isBlank( provinceId ) )
		{
			return false;
		}
		if ( Strings.isBlank( cityId ) )
		{
			return false;
		}

		List<RegionNode> cityList = this.getCityList( provinceId );
		if ( null == cityList || 0 == cityList.size() )
		{
			return false;
		}
		for ( RegionNode node : cityList )
		{
			if ( node.id.equalsIgnoreCase( cityId ) )
			{
				return true;
			}
		}

		return false;
	}
	public RestChinaRegionCityListResponse getRestCityList( String provinceId )
	{
		return RestChinaRegionCityListResponse.builder()
			.status( HttpStatus.OK.value() )
			.body( RestChinaRegionCityListResponse.Body.builder()
				.cityList( this.getCityList( provinceId ) )
				.build() )
			.build();
	}
	public List<RegionNode> getCityList( String provinceId )
	{
		if ( null != cacheCityMap &&
			cacheCityMap.containsKey( provinceId ) &&
			null != cacheCityMap.get( provinceId ) &&
			cacheCityMap.get( provinceId ).size() > 0 )
		{
			return cacheCityMap.get( provinceId );
		}
		if ( null == this.jsonCity || 0 == this.jsonCity.size() )
		{
			throw new HttpExceptions.InternalServerError( "城市列表数据未装载" );
		}
		if ( ! this.jsonCity.has( provinceId ) ||
			! this.jsonCity.get( provinceId ).isArray() )
		{
			throw new HttpExceptions.BadRequest( "指定省份不存在" );
		}

		this.cacheCityMap.put( provinceId, new ArrayList<>() );
		ArrayNode arrayCities = (ArrayNode )this.jsonCity.get( provinceId );
		arrayCities.forEach( node -> {
			if ( node.isObject() )
			{
				String province = node.get( "province" ).asText( "" );
				String name = node.get( "name" ).asText( "" );
				String id = node.get( "id" ).asText( "" );
				this.cacheCityMap.get( provinceId ).add( RegionNode.builder()
					.province( province )
					.name( name )
					.id( id ).build() );
			}
		});

		return this.cacheCityMap.get( provinceId );
	}


	public boolean isDistrictExists( String cityId, String districtId )
	{
		if ( Strings.isBlank( cityId ) )
		{
			return false;
		}
		if ( Strings.isBlank( districtId ) )
		{
			return false;
		}

		List<RegionNode> districtList = this.getDistrictList( cityId );
		if ( null == districtList || 0 == districtList.size() )
		{
			return false;
		}
		for ( RegionNode node : districtList )
		{
			if ( node.id.equalsIgnoreCase( districtId ) )
			{
				return true;
			}
		}

		return false;
	}
	public RestChinaRegionDistrictListResponse getRestDistrictList( String cityId )
	{
		return RestChinaRegionDistrictListResponse.builder()
			.status( HttpStatus.OK.value() )
			.body( RestChinaRegionDistrictListResponse.Body.builder()
				.districtList( this.getDistrictList( cityId ) )
				.build() )
			.build();
	}
	public List<RegionNode> getDistrictList( String cityId )
	{
		if ( null != cacheDistrictMap &&
			cacheDistrictMap.containsKey( cityId ) &&
			null != cacheDistrictMap.get( cityId ) &&
			cacheDistrictMap.get( cityId ).size() > 0 )
		{
			return cacheDistrictMap.get( cityId );
		}

		if ( null == this.jsonDistrict || 0 == this.jsonDistrict.size() )
		{
			throw new HttpExceptions.InternalServerError( "城市列表数据未装载" );
		}
		if ( ! this.jsonDistrict.has( cityId ) ||
			! this.jsonDistrict.get( cityId ).isArray() )
		{
			throw new HttpExceptions.BadRequest( "指定城市不存在" );
		}

		this.cacheDistrictMap.put( cityId, new ArrayList<>() );
		ArrayNode arrayDistricts = (ArrayNode )this.jsonDistrict.get( cityId );
		arrayDistricts.forEach( node -> {
			if ( node.isObject() )
			{
				String city = node.get( "city" ).asText( "" );
				String name = node.get( "name" ).asText( "" );
				String id = node.get( "id" ).asText( "" );
				this.cacheDistrictMap.get( cityId ).add( RegionNode.builder()
					.province( "" )
					.city( city )
					.name( name )
					.id( id ).build() );
			}
		});

		return this.cacheDistrictMap.get( cityId );
	}



	private JsonNode _loadJsonFromResource( String filename )
	{
		log.info( "))) will load json from resource: {}", filename );
		try ( InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream( filename ) )
		{
			//	pass InputStream to JSON-Library, e.g. using Jackson
			ObjectMapper mapper     = new ObjectMapper();
			JsonNode     jsonNode   = mapper.readValue( in, JsonNode.class );
			//String       jsonString = mapper.writeValueAsString( jsonNode );
			//System.out.println( jsonString );

			//
			//	InputStream in
			//	The close() call should be automatically called when the try block is exited.
			//
			log.info( "))) load json from resource successfully: {}", filename );
			return jsonNode;
		}
		catch ( Exception e )
		{
			log.error( "))) failed to load json from resource: {}", filename );
			e.printStackTrace();
			throw new HttpExceptions.InternalServerError( e.getMessage() );
		}
	}
}
