package org.dekuan.ChinaRegionJava.models;

import org.dekuan.ChinaRegionJava.services.ChinaRegionService;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RestChinaRegionDistrictListResponse extends RestHeader
{
	@Builder.Default
	public final Body body = new Body();

	/**
	 *	Body
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@SuperBuilder
	public static class Body
	{
		@Builder.Default
		List<ChinaRegionService.RegionNode> districtList = null;

		public long getCount()
		{
			return null != districtList ? districtList.size() : 0;
		}
	}
}