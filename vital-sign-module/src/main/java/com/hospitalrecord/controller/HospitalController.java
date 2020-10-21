package com.hospitalrecord.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hospitalrecord.config.VitalMessagingConfig;
import com.hospitalrecord.model.HospitalRecord;
import com.hospitalrecord.model.LoggingSaver;
import com.hospitalrecord.repository.LoggingSaverRepository;
import com.hospitalrecord.service.HospitalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ExampleProperty;

@RestController
@RequestMapping("/vital")
@CrossOrigin("*")
@Api(value = "Patient Record History Service", description = "Vital Sign Module")
public class HospitalController {
	/* The Hospital Service */
	private HospitalService hospitalService;
	/* RabbitTemplate for publising message to RabbitMQ */
	private RabbitTemplate template;

	private DiscoveryClient client;
	private LoggingSaverRepository repository;

	public HospitalController(HospitalService hospitalService, RabbitTemplate template,
			LoggingSaverRepository repository, DiscoveryClient client, RestTemplate restTemplate) {
		this.hospitalService = hospitalService;
		this.template = template;
		this.repository = repository;
		this.client = client;
	}

	/*
	 * Param - Hospital Record Creates a new entry of Patient History in DB
	 */

	@RequestMapping(path = "/checkup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "snapshot", dataType = "DesktopSnapshot", value = "{\"check_up_date\":\"2020-10-08\",\"patient_id\":1,\"temperature\":\"26F\"}", examples = @io.swagger.annotations.Example(value = {
					@ExampleProperty(value = "{\"check_up_date\":\"2020-10-08\",\"patient_id\":1,\"temperature\":\"26F\"}", mediaType = "application/json") })) })
	@ApiOperation(value = "To insert a Patient History Entry in Database")
	public HospitalRecord insertHospitalRecord(@RequestBody HospitalRecord hospitalRecord) {
		HospitalRecord record = new HospitalRecord();
		record = hospitalService.insertDetails(hospitalRecord);
		template.convertAndSend(VitalMessagingConfig.EXCHANGE, VitalMessagingConfig.ROUTING_KEY, record);
		return record;
	}

	@GetMapping("/getInstances")
	private String getPatientHistory() {
		URI uri = URI.create(StringUtils.EMPTY);
		Optional<URI> value = client.getInstances("Book-Appointment-Service").stream().map(ServiceInstance::getUri)
				.findAny();
		if (value.isPresent()) {
			Optional<URI> uriValue = value.map(mapper -> mapper.resolve("/book/appointment"));
			if (uriValue.isPresent()) {
				uri = uriValue.get();
			}
		}
		System.out.println(uri);
		return "Hello arun";
	}

	/*
	 * Lists the patient history in DB
	 */

	@GetMapping("/checkup")
	@ApiOperation(value = "To get existing Patient History Entries from Database")
	@Cacheable(value = "getRecordWithDat")
	public List<HospitalRecord> getRecordWithDate() {
		return hospitalService.getRecordsWithDate();
	}

	@GetMapping("/samples")
	public List<String> sample() {
		return Stream.of("Arun", "Balaji").collect(Collectors.toList());
	}

	@GetMapping("/checkup/{id}")
	@ApiOperation(value = "To get specific Patient History Entry from Database")
	public HospitalRecord getSingleRecord(@PathVariable(name = "id") Long id) {
		return hospitalService.getSingleVitalRecord(id);
	}

	@GetMapping("/lastVisited/{id}")
	@ApiOperation(value = "To get the recent history from Database")
	public String getlastVisitedRecord(@PathVariable(name = "id") Long id) {
		return hospitalService.getLastRecord(id);
	}

	/*
	 * @param - Record ID Deletes an entry from DB
	 */

	@DeleteMapping("/checkup/{id}")
	@CacheEvict(value = "getRecordWithDat", allEntries = true)
	@ApiOperation(value = "To delete existing Patient History Entry in Database")
	public String deletePatientId(@PathVariable(name = "id") Long id) {
		return hospitalService.deleteById(id);
	}

	@GetMapping("/logs")
	@ApiOperation(value = "Gives the CRUD logs from DynamoDB")
	public List<LoggingSaver> aopLogs() {
		return repository.getAllLogs();
	}
}