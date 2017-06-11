package com.fr.commons.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by djenanewail on 6/11/17.
 */
public class ConnexionHistoryDto extends AbstractCommonDTO
{
	@NotEmpty
	private String ip;
	@NotEmpty
	private String countryCode;
	@NotEmpty
	private String countryName;
	@NotEmpty
	private String regionCode;
	@NotEmpty
	private String regionName;
	@NotEmpty
	private String city;
	@NotEmpty
	private String zipCode;
	@NotEmpty
	private String timeZone;
	@NotEmpty
	private String latitude;
	@NotEmpty
	private String longitude;
	@NotEmpty
	private String metroCode;
	
	public String getIp() {
		return this.ip;
	}
	
	public void setIp(final String ip) {
		this.ip = ip;
	}
	
	public String getCountryCode() {
		return this.countryCode;
	}
	
	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return this.countryName;
	}
	
	public void setCountryName(final String countryName) {
		this.countryName = countryName;
	}
	
	public String getRegionCode() {
		return this.regionCode;
	}
	
	public void setRegionCode(final String regionCode) {
		this.regionCode = regionCode;
	}
	
	public String getRegionName() {
		return this.regionName;
	}
	
	public void setRegionName(final String regionName) {
		this.regionName = regionName;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(final String city) {
		this.city = city;
	}
	
	public String getZipCode() {
		return this.zipCode;
	}
	
	public void setZipCode(final String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(final String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(final String longitude) {
		this.longitude = longitude;
	}
	
	public String getMetroCode() {
		return this.metroCode;
	}
	
	public void setMetroCode(final String metroCode) {
		this.metroCode = metroCode;
	}
}
