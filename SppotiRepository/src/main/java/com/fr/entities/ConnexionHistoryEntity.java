package com.fr.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by djenanewail on 6/11/17.
 */
@Entity
@Table(name = "connexion_history")
public class ConnexionHistoryEntity extends AbstractCommonEntity
{
	@Column(name = "ip")
	private String ip;
	@Column(name = "country_code")
	private String countryCode;
	@Column(name = "country_name;")
	private String countryName;
	@Column(name = "region_code")
	private String regionCode;
	@Column(name = "region_name")
	private String regionName;
	@Column(name = "city")
	private String city;
	@Column(name = "zipCode")
	private String zipCode;
	@Column(name = "time_zone")
	private String timeZone;
	@Column(name = "latitude")
	private String latitude;
	@Column(name = "longitude")
	private String longitude;
	@Column(name = "metro_code")
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
