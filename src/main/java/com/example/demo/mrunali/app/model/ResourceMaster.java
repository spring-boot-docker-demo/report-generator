/**
 * 
 */
package com.example.demo.mrunali.app.model;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Devendra
 *
 */
public class ResourceMaster {

	private BigInteger resId;
	private String firstName;
	private String lastName;
	private String designation;

	public BigInteger getResId() {
		return resId;
	}

	public void setResId(BigInteger resId) {
		this.resId = resId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "ResourceMaster [resId=" + resId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", designation=" + designation + "]";
	}
	
	public class ResourceMasterRowMapper implements RowMapper<ResourceMaster> {
		
		private final Logger logger = LoggerFactory.getLogger(ResourceMaster.ResourceMasterRowMapper.class);
		
		@Override
		public ResourceMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
			logger.debug("Mapping Row # " + rowNum);
			ResourceMaster master = new ResourceMaster();
			master.setResId(BigInteger.valueOf(rs.getInt("res_id")));
			master.setFirstName(rs.getString("first_name"));
			master.setLastName(rs.getString("last_name"));
			master.setDesignation(rs.getString("designation"));
			return master;
		}
	}

}
