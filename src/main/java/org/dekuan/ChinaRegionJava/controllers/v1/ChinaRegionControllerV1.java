package org.dekuan.ChinaRegionJava.controllers.v1;

import org.dekuan.ChinaRegionJava.models.RestChinaRegionCityListResponse;
import org.dekuan.ChinaRegionJava.models.RestChinaRegionDistrictListResponse;
import org.dekuan.ChinaRegionJava.models.RestChinaRegionProvinceListResponse;
import org.dekuan.ChinaRegionJava.services.ChinaRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api( tags = "中国行政区域" )
@RequestMapping( "/api/v1/chinaRegion/" )
@Slf4j
public class ChinaRegionControllerV1
{
	@Autowired
	private ChinaRegionService chinaRegionService;


	@ApiOperation( value = "获取省份列表", notes = "-" )
	@RequestMapping( value = "/province/list", method = { RequestMethod.GET } )
	@ResponseBody
	public RestChinaRegionProvinceListResponse getProvinceList
		(
			HttpServletRequest request,
			HttpServletResponse response
		)
	{
		return chinaRegionService.getRestProvinceList();
	}

	@ApiOperation( value = "获取城市列表", notes = "-" )
	@RequestMapping( value = "/city/list/{provinceId}", method = { RequestMethod.GET } )
	@ResponseBody
	public RestChinaRegionCityListResponse getCityList
		(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String provinceId
		)
	{
		return chinaRegionService.getRestCityList( provinceId );
	}


	@ApiOperation( value = "获取区域列表", notes = "-" )
	@RequestMapping( value = "/district/list/{cityId}", method = { RequestMethod.GET } )
	@ResponseBody
	public RestChinaRegionDistrictListResponse getDistrictList
		(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String cityId
		)
	{
		return chinaRegionService.getRestDistrictList( cityId );
	}
}