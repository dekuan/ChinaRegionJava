package org.dekuan.ChinaRegionJava.properties;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Component
@ConfigurationProperties( "app" )
public class AppProperties
{
	@Builder.Default
	private String timezone = "Asia/Shanghai";
}