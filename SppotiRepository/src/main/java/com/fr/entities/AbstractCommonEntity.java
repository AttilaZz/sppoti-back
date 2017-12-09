package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by wdjenane on 23/02/2017.
 */

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AbstractCommonEntity implements Serializable
{
	private static final long serialVersionUID = -6893399491928930624L;
	
	/**
	 * technical id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	protected Long id;
	
	/**
	 * versionning.
	 */
	@Version
	@Column(name = "version", nullable = false)
	private Integer version = -1;
	
	/**
	 * fonctionnal id.
	 */
	@Column(nullable = false, unique = true, updatable = false)
	private String uuid = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public String toString() {
		//        return ToStringBuilder.reflectionToString(this);
		return new ToStringBuilder(this).append("id", this.id).toString();
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		final AbstractCommonEntity that = (AbstractCommonEntity) o;
		
		return Objects.equals(this.uuid, that.uuid) && this.id.equals(that.id) && this.version.equals(that.version);
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = 31;
		result = 31 * result + (getId() != null ? getId().hashCode() : 0);
		result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
		result = 31 * result + this.uuid.hashCode();
		return result;
	}
	
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Integer getVersion() {
		return this.version;
	}
	
	public void setVersion(final Integer version) {
		this.version = version;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}
}
