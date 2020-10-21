package com.hospitalrecord.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospitalrecord.model.HospitalRecord;

@Service
public interface HospitalService {
	public HospitalRecord insertDetails(HospitalRecord details);

	public List<HospitalRecord> getRecordsWithDate();

	public String deleteById(Long id);

	public HospitalRecord getSingleVitalRecord(Long id);

	public String getLastRecord(Long id);
}