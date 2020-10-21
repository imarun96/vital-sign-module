package com.hospitalrecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.hospitalrecord.dao.HospitalDao;
import com.hospitalrecord.model.HospitalRecord;
import com.hospitalrecord.service.HospitalService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VitalSignModuleApplicationTests {
	@Autowired
	private HospitalService service;

	@MockBean
	private HospitalDao dao;

	private Calendar cal = Calendar.getInstance();

	@Test
	public void getUserTest() {
		when(dao.getRecordWithDate()).thenReturn(Stream
				.of(new HospitalRecord(2, 14, "99 °C", "2020-10-29", "High", "Elevated", "High", "admin", "admin",
						new Timestamp(cal.getTimeInMillis()), new Timestamp(cal.getTimeInMillis())))
				.collect(Collectors.toList()));
		assertEquals(1, service.getRecordsWithDate().size());
	}

	@Test
	public void addUserTest() {
		HospitalRecord record = new HospitalRecord(2, 14, "99 °C", "2020-10-29", "High", "Elevated", "High", "admin",
				"admin", new Timestamp(cal.getTimeInMillis()), new Timestamp(cal.getTimeInMillis()));
		when(dao.insertDetailsIntoDb(record)).thenReturn(record);
		assertEquals(record, service.insertDetails(record));
	}

	@Test
	public void deleteUserTest() {
		HospitalRecord record = new HospitalRecord(2, 14, "99 °C", "2020-10-29", "High", "Elevated", "High", "admin",
				"admin", new Timestamp(cal.getTimeInMillis()), new Timestamp(cal.getTimeInMillis()));
		when(dao.insertDetailsIntoDb(record)).thenReturn(record);
		service.deleteById(2L);
		verify(dao, times(1)).deleteById(2L);
	}
}