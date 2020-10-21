package com.hospitalrecord.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "hospital_record")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HospitalRecord implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "record_id")
	@ApiModelProperty(value = "Auto-Generated value. No need to enter.")
	private long record_id;
	@ApiModelProperty(value = "Patient Id")
	@Column(name = "patient_id")
	private long patient_id;
	@ApiModelProperty(value = "Body Temperature")
	@Column(name = "temperature")
	private String temperature;
	@ApiModelProperty(value = "Check Up Date")
	@Column(name = "check_up_date")
	private String check_up_date;
	@ApiModelProperty(value = "Pulse Rate")
	@Column(name = "pulseRate")
	private String pulseRate;
	@ApiModelProperty(value = "Blood Pressure")
	@Column(name = "bloodPressure")
	private String bloodPressure;
	@ApiModelProperty(value = "Respiration Rate")
	@Column(name = "respirationRate")
	private String respirationRate;
	@Column(name = "inputUserId")
	private String inputUserId;
	@Column(name = "lastUpdateUserId")
	private String lastUpdateUserId;
	@Column(name = "inputDateTime")
	private Timestamp inputDateTime;
	@Column(name = "lastUpdateDateTime")
	private Timestamp lastUpdateDateTime;
}