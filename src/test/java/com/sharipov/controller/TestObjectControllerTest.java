package com.sharipov.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharipov.model.TestObject;
import com.sharipov.service.TestObjectService;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(TestObjectController.class)
public class TestObjectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private TestObjectService testObjectService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.defaultRequest(get("/").with(user("root").password("root").roles("USER"))).build();
	}

	@Test
	public void findTObjects_StorageIsEmpty_NoTObjectsAreReturned() throws Exception {
		mockMvc.perform(get("/objects")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	public void findTObjects_StorageIsNotEmpty_OneTObjectIsReturned() throws Exception {
		given(testObjectService.findAll()).willReturn(Arrays.asList(new TestObject()));
		mockMvc.perform(get("/objects")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	public void saveTObject_validTObject_TObjectIsReturned() throws Exception {
		TestObject testObject = new TestObject();
		testObject.setValue(100);
		testObject.setTitle("Java 8");
		mockMvc.perform(post("/object").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(testObject))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.title", Matchers.equalTo("Java 8")));

	}

	@Test
	public void saveTObject_NotValidTObject_BadRequest() throws Exception {
		TestObject testObject = new TestObject();

		testObject.setTitle("Java 8");
		mockMvc.perform(post("/object").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(testObject))).andExpect(status().isBadRequest());
	}

	@Test
	public void saveTObject_NotValidId_NotFound() throws Exception {
		TestObject testObject = new TestObject(2, "test post", 20);
		given(testObjectService.contains(2)).willReturn(false);
		mockMvc.perform(post("/object/2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(testObject))).andExpect(status().isNotFound());
	}

	@Test
	public void updateTObject_validTObject_TObjectIsReturned() throws Exception {
		TestObject testObject = new TestObject(2, "Java 8", 100);
		mockMvc.perform(post("/object").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(testObject)));
		TestObject testObject2 = new TestObject(2, "Java 9", 200);
		given(testObjectService.contains(2)).willReturn(true);
		mockMvc.perform(put("/object/2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(testObject2))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.title", Matchers.equalTo("Java 9")));

	}

	@Test
	public void findTObjectByID_ValidId_OneObjectIsReturned() throws Exception {
		TestObject testObject = new TestObject(1, "Test TestObject", 100);
		given(testObjectService.findOne(1)).willReturn(testObject);
		given(testObjectService.contains(1)).willReturn(true);
		mockMvc.perform(get("/object/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", Matchers.equalTo(1)));
	}

	@Test
	public void findTObjectByID_NotValidId_NotFound() throws Exception {
		given(testObjectService.contains(1)).willReturn(false);
		mockMvc.perform(get("/object/1")).andExpect(status().isNotFound());
	}

	@Test
	public void deleteTObjectById_ValidId_OneObjectIsDeleted() throws Exception {
		given(testObjectService.contains(1)).willReturn(true);
		doNothing().when(testObjectService).delete(1);
		mockMvc.perform(delete("/object/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	public void deleteTObjectById_NotValidId_NotFound() throws Exception {
		given(testObjectService.contains(1)).willReturn(false);
		mockMvc.perform(delete("/object/1")).andExpect(status().isNotFound());
	}

}
