package com.hospitalrecord.dao;

import java.util.List;

import com.hospitalrecord.model.HospitalRecord;

public interface HospitalDao {
	public HospitalRecord insertDetailsIntoDb(HospitalRecord details);

	public List<HospitalRecord> getRecordWithDate();

	public String deleteById(long Id);

	public HospitalRecord getSingleRecord(Long id);

	public String getLastServicedRecord(Long id);
}