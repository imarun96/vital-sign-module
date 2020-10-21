package com.hospitalrecord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospitalrecord.dao.HospitalDao;
import com.hospitalrecord.model.HospitalRecord;

@Service
public class HospitalServiceImpl implements HospitalService {
	@Autowired
	HospitalDao hospitalDao;

	@Override
	public HospitalRecord insertDetails(HospitalRecord details) {
		return hospitalDao.insertDetailsIntoDb(details);
	}

	@Override
	public List<HospitalRecord> getRecordsWithDate() {
		return hospitalDao.getRecordWithDate();
	}

	@Override
	public String deleteById(Long id) {
		return hospitalDao.deleteById(id);
	}

	@Override
	public HospitalRecord getSingleVitalRecord(Long id) {
		return hospitalDao.getSingleRecord(id);
	}

	@Override
	public String getLastRecord(Long id) {
		return hospitalDao.getLastServicedRecord(id);
	}
}