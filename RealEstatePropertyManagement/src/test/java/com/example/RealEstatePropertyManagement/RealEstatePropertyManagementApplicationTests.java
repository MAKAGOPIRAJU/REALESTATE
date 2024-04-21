package com.example.RealEstatePropertyManagement;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.HashMap;


import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = { "/schema.sql", "/data.sql" })
class RealEstatePropertyManagementApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	HashMap<Integer, String[]> owners = new HashMap<>();

	{

		// Initially has two owners

		owners.put(1, new String[]{"John Doe"});
		owners.put(2, new String[]{"Jane Smith"});
		owners.put(3,new String[]{"Alice Johnson"}); // post
		owners.put(4,new String[]{"Bob Smith"}); // post
		owners.put(5,new String[]{"Jack Sparrow"}); // post
		owners.put(6,new String[]{"Alice Smith"});// put for 1
	}

	HashMap<Integer, String[]> properties = new HashMap<>();

	{
		properties.put(1, new String[]{"1234 Elm St", "Not-Available", "1200"});
		properties.put(2, new String[]{"5678 Oak St", "Not-Available", "15000"});
		properties.put(3, new String[]{"123 North St", "Available", "10000"});// post the new properties
		properties.put(4, new String[]{"1234 Elm St", "Not-Available", "15000"});// put the new properties for id = 1
	}
	HashMap<Integer, String[]> leases = new HashMap<>();

	{
		leases.put(1, new String[]{"2022-01-01", "2022-12-31", "1", "1"});
		leases.put(2, new String[]{"2022-02-01", "2023-01-31", "2", "2"});
	}

	@BeforeAll
	public void start() {}

	@Test
	@Order(1)
	public void testGetOwners() throws Exception {

		mockMvc.perform(get("/owners"))
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].ownerid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].name",Matchers.equalTo(owners.get(1)[0])))
				.andExpect(jsonPath("$[1].ownerid",Matchers.equalTo(2)))
				.andExpect(jsonPath("$[1].name",Matchers.equalTo(owners.get(2)[0])));
	}

	@Test
	@Order(2)
	public void testgetOwnerById() throws Exception{

		mockMvc.perform(get("/owners/1"))
				.andExpect(jsonPath("$.ownerid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.name", Matchers.equalTo("John Doe")))
				.andExpect(jsonPath("$.properties[0].leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.properties[0].startdate", Matchers.equalTo("2022-01-01")))
				.andExpect(jsonPath("$.properties[0].enddate", Matchers.equalTo("2022-12-31")));
	}

	@Test
	@Order(3)
	public void testAddOwner() throws Exception{
		String ownerJson = "{\"name\": \"Alice Johnson\"}";
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/owners")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(ownerJson);
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.equalTo("Alice Johnson")))
				.andExpect(jsonPath("$.ownerid",Matchers.equalTo(3)))
				.andExpect(jsonPath("$.properties").value(Matchers.nullValue()));

		String ownerJson2 = "{\"name\": \"Bob Smith\"}";

		MockHttpServletRequestBuilder mockRequest2 = MockMvcRequestBuilders.post("/owners")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(ownerJson2);

		mockMvc.perform(mockRequest2)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.equalTo("Bob Smith")))
				.andExpect(jsonPath("$.ownerid",Matchers.equalTo(4)))
				.andExpect(jsonPath("$.properties").value(Matchers.nullValue()));

		String ownerJson3 = "{\"name\": \"Jack Sparrow\"}";

		MockHttpServletRequestBuilder mockRequest3 = MockMvcRequestBuilders.post("/owners")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(ownerJson3);

		mockMvc.perform(mockRequest3)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.equalTo("Jack Sparrow")))
				.andExpect(jsonPath("$.ownerid",Matchers.equalTo(5)))
				.andExpect(jsonPath("$.properties").value(Matchers.nullValue()));
	}

	@Test
	@Order(4)
	public void testUpdateOwners() throws Exception {

		String ownerJson = "{\"name\": \"Alice Smith\"}";

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/owners/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(ownerJson);

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.equalTo("Alice Smith")))
				.andExpect(jsonPath("$.ownerid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.properties[0].leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.properties[0].startdate", Matchers.equalTo("2022-01-01")))
				.andExpect(jsonPath("$.properties[0].enddate", Matchers.equalTo("2022-12-31")));

	}

	@Test
	@Order(5)
	public void testDeleteOwnerValidId() throws Exception {
		int ownerId = 4;

		mockMvc.perform(delete("/owners/4", ownerId))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(6)
	public void testDeleteOwnerInvalidId() throws Exception {
		int invalidOwnerId = 999;

		mockMvc.perform(delete("/owners/{ownerId}", invalidOwnerId))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(7)
	public void testGetProperties() throws  Exception{

		mockMvc.perform(MockMvcRequestBuilders.get("/properties")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].propertyid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].address", Matchers.equalTo("1234 Elm St")))
				.andExpect(jsonPath("$[0].status", Matchers.equalTo("Not-Available")))
				.andExpect(jsonPath("$[0].price", Matchers.equalTo(1200)))
				.andExpect(jsonPath("$[0].leases[0].leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].leases[0].startdate", Matchers.equalTo("2022-01-01")))
				.andExpect(jsonPath("$[0].leases[0].enddate", Matchers.equalTo("2022-12-31")))
				.andExpect(jsonPath("$[1].propertyid", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[1].address", Matchers.equalTo("5678 Oak St")))
				.andExpect(jsonPath("$[1].status", Matchers.equalTo("Not-Available")))
				.andExpect(jsonPath("$[1].price", Matchers.equalTo(15000)))
				.andExpect(jsonPath("$[1].leases[0].leaseid", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[1].leases[0].startdate", Matchers.equalTo("2022-02-01")))
				.andExpect(jsonPath("$[1].leases[0].enddate", Matchers.equalTo("2023-01-31")));
	}


	@Test
	@Order(8)
	public void testGetPropertyById() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/properties/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.propertyid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.address", Matchers.equalTo("1234 Elm St")))
				.andExpect(jsonPath("$.status", Matchers.equalTo("Not-Available")))
				.andExpect(jsonPath("$.price", Matchers.equalTo(1200)))
				.andExpect(jsonPath("$.leases[0].leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.leases[0].startdate", Matchers.equalTo("2022-01-01")))
				.andExpect(jsonPath("$.leases[0].enddate", Matchers.equalTo("2022-12-31")));
	}

	@Test
	@Order(9)
	public void testAddProperty() throws Exception {
		String propertyJson = "{\"address\":\"123 North St\",\"status\":\"Available\",\"price\":10000}";

		mockMvc.perform(MockMvcRequestBuilders.post("/properties")
						.contentType(MediaType.APPLICATION_JSON)
						.content(propertyJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.propertyid", Matchers.equalTo(4)))
				.andExpect(jsonPath("$.address", Matchers.equalTo("123 North St")))
				.andExpect(jsonPath("$.status", Matchers.equalTo("Available")))
				.andExpect(jsonPath("$.price", Matchers.equalTo(10000)))
				.andExpect(jsonPath("$.leases", Matchers.hasSize(0)));
	}

	@Test
	@Order(10)
	public void testUpdateProperty_ValidPropertyId() throws Exception {
		String propertyJson = "{\"price\":\"15000\"}";

		mockMvc.perform(MockMvcRequestBuilders.put("/properties/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(propertyJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.propertyid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.address", Matchers.equalTo("1234 Elm St")))
				.andExpect(jsonPath("$.status", Matchers.equalTo("Not-Available")))
				.andExpect(jsonPath("$.price", Matchers.equalTo(15000)))
				.andExpect(jsonPath("$.leases", Matchers.hasSize(1)))
				.andExpect(jsonPath("$.leases[0].leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.leases[0].startdate", Matchers.equalTo("2022-01-01")))
				.andExpect(jsonPath("$.leases[0].enddate", Matchers.equalTo("2022-12-31")));
	}

	@Test
	@Order(11)
	public void testUpdateProperty_InvalidPropertyId() throws Exception {
		String propertyJson = "{\"price\":\"15000\"}";

		mockMvc.perform(MockMvcRequestBuilders.put("/properties/100")
						.contentType(MediaType.APPLICATION_JSON)
						.content(propertyJson))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(12)
	public void testDeleteProperty_ValidPropertyId() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/properties/3"))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(13)
	void testGetLeases() throws Exception {
		mockMvc.perform(get("/leases"))
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].startdate", Matchers.equalTo("2022-01-01")))
				.andExpect(jsonPath("$[0].enddate", Matchers.equalTo("2022-12-31")))
				.andExpect(jsonPath("$[1].leaseid", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[1].startdate", Matchers.equalTo("2022-02-01")))
				.andExpect(jsonPath("$[1].enddate", Matchers.equalTo("2023-01-31")));
	}

	@Test
	@Order(14)
	void testAddLease() throws Exception {

		String requestBody = "{\"startdate\":\"12-09-2023\",\"enddate\":\"12-12-2024\"}";

		mockMvc.perform(post("/leases/4/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.leaseid", Matchers.equalTo(3)))
				.andExpect(jsonPath("$.startdate", Matchers.equalTo("12-09-2023")))
				.andExpect(jsonPath("$.enddate", Matchers.equalTo("12-12-2024")));
	}


	@Test
	@Order(15)
	void testGetLeaseById() throws Exception {

		mockMvc.perform(get("/leases/3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.leaseid", Matchers.equalTo(3)))
				.andExpect(jsonPath("$.startdate", Matchers.equalTo("12-09-2023")))
				.andExpect(jsonPath("$.enddate", Matchers.equalTo("12-12-2024")));
	}

	@Test
	@Order(16)
	void testAddLeaseWithInvalidPropertyId() throws Exception {
		String requestBody = "{\"startdate\":\"12-09-2023\",\"enddate\":\"12-12-2024\"}";

		mockMvc.perform(post("/leases/2/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(17)
	void testUpdateLease() throws Exception {

		String requestBody = "{\"startdate\":\"12-09-2023\",\"enddate\":\"12-12-2024\"}";

		mockMvc.perform(put("/leases/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.leaseid", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.startdate", Matchers.equalTo("12-09-2023")))
				.andExpect(jsonPath("$.enddate", Matchers.equalTo("12-12-2024")));
	}

	@Test
	@Order(18)
	void testDeleteLeaseValidId() throws Exception {
		mockMvc.perform(delete("/leases/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(19)
	void testDeleteLeaseInvalidId() throws Exception {
		mockMvc.perform(delete("/leases/1"))
				.andExpect(status().isNotFound());
	}



	@AfterAll
	public void cleanUp(){
		jdbcTemplate.execute("drop table lease");
		jdbcTemplate.execute("drop table property");
		jdbcTemplate.execute("drop table owner");
	}

}
