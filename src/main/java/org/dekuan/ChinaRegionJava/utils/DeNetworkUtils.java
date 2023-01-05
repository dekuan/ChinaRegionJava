package org.dekuan.ChinaRegionJava.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;


/**
 *	LibNetwork
 */
public class DeNetworkUtils
{
	/**
	 *	check if the sAddress is a valid IpV4 or IpV6 internet address
	 *	@param	sAddress	-
	 *	@return	boolean
	 */
	public static boolean isValidIpAddress( String sAddress )
	{
		return ! StringUtils.isBlank( sAddress ) &&
			InetAddressValidator.getInstance().isValid( sAddress );
	}

	/**
	 *	check if the sAddress is a valid IpV4 internet address
	 *	@param	sAddress	-
	 *	@return	boolean
	 */
	public static boolean isValidIpV4Address( String sAddress )
	{
		return ! StringUtils.isBlank( sAddress ) &&
			InetAddressValidator.getInstance().isValidInet4Address( sAddress );
	}

	/**
	 *	check if the sAddress is a valid IpV6 internet address
	 *	@param	sAddress	-
	 *	@return	boolean
	 */
	public static boolean isValidIpV6Address( String sAddress )
	{
		return ! StringUtils.isBlank( sAddress ) &&
			InetAddressValidator.getInstance().isValidInet6Address( sAddress );
	}
}