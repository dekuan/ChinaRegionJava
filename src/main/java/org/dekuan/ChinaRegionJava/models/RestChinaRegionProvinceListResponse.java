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
public class RestChinaRegionProvinceListResponse extends RestHeader
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
		List<ChinaRegionService.RegionNode> provinceList = null;

		public long getCount()
		{
			return null != provinceList ? provinceList.size() : 0;
		}
	}
}