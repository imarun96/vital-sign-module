package com.hospitalrecord.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;

import com.hospitalrecord.Credential;
import com.hospitalrecord.model.HospitalRecord;

@Repository
@EnableConfigurationProperties(Credential.class)
public class HospitalDaoImpl implements HospitalDao {
	private static Credential credential;

	public HospitalDaoImpl(Credential credential) {
		HospitalDaoImpl.credential = credential;
	}

	public static SessionFactory getSessionFactory() {
		SessionFactory sessionFactory;
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(HospitalRecord.class);
		configuration.setProperty("connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/hospital");
		configuration.setProperty("hibernate.connection.username", credential.getUserName());
		configuration.setProperty("hibernate.connection.password", credential.getPassword());
		configuration.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		configuration.setProperty("show_sql", "true");
		configuration.setProperty("hibernate.connection.pool_size", "10");
		ServiceRegistry builder = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(builder);
		return sessionFactory;
	}

	@Override
	public HospitalRecord insertDetailsIntoDb(HospitalRecord details) {
		SessionFactory sessionactory = HospitalDaoImpl.getSessionFactory();
		Session session = sessionactory.openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		Calendar cal = Calendar.getInstance();
		details.setInputDateTime(new Timestamp(cal.getTimeInMillis()));
		details.setLastUpdateDateTime(new Timestamp(cal.getTimeInMillis()));
		session.save(details);
		session.getTransaction().commit();
		session.close();
		return details;
	}

	@Override
	public List<HospitalRecord> getRecordWithDate() {
		SessionFactory sessionactory = HospitalDaoImpl.getSessionFactory();
		Session session = sessionactory.openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		String hql = "FROM hospital_record";
		Query q = session.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<HospitalRecord> patientRecord = q.list();
		session.getTransaction().commit();
		session.close();
		return patientRecord;
	}

	@Override
	public String deleteById(long Id) {
		SessionFactory sessionactory = HospitalDaoImpl.getSessionFactory();
		Session session = sessionactory.openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		HospitalRecord patientRecord = (HospitalRecord) session.get(HospitalRecord.class, Id);
		session.delete(patientRecord);
		session.getTransaction().commit();
		session.close();
		return String.valueOf("Deleted");
	}

	@Override
	public HospitalRecord getSingleRecord(Long id) {
		SessionFactory sessionactory = HospitalDaoImpl.getSessionFactory();
		Session session = sessionactory.openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		HospitalRecord patientRecord = (HospitalRecord) session.get(HospitalRecord.class, id);
		session.getTransaction().commit();
		session.close();
		return patientRecord;
	}

	@Override
	public String getLastServicedRecord(Long id) {
		SessionFactory sessionactory = HospitalDaoImpl.getSessionFactory();
		Session session = sessionactory.openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		String hql = "select check_up_date FROM hospital_record u where u.patient_id=" + id
				+ " order by u.lastUpdateDateTime desc";
		System.out.println(hql);
		Query q = session.createQuery(hql);
		q.setMaxResults(1);
		String response = (String) q.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return (StringUtils.isNotBlank(response) || StringUtils.isNotEmpty(response)) ? "Last visit on " + response
				: "No previous visit to the hospital";
	}
}