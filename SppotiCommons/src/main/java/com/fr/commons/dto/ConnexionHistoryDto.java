package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

;

/**
 * Created by djenanewail on 6/11/17.
 */
public class ConnexionHistoryDto extends AbstractCommonDTO
{
	
	@JsonProperty("ip")
	private String ip;
	
	@JsonProperty("country_code")
	private String countryCode;
	
	@JsonProperty("country_name")
	private String countryName;
	
	@JsonProperty("region_code")
	private String regionCode;
	
	@JsonProperty("region_name")
	private String regionName;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("zip_code")
	private String zipCode;
	
	@JsonProperty("time_zone")
	private String timeZone;
	
	@JsonProperty("latitude")
	private String latitude;
	
	@JsonProperty("longitude")
	private String longitude;
	
	@JsonProperty("metro_code")
	private String metroCode;
	
	private Long userId;
	
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
	
	public Long getUserId() {
		return this.userId;
	}
	
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
}
