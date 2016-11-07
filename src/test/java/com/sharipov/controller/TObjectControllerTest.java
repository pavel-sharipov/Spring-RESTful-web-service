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
import com.sharipov.model.TObject;
import com.sharipov.service.TObjectService;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(TObjectController.class)
public class TObjectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private TObjectService tObjectService;

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
		given(tObjectService.findAll()).willReturn(Arrays.asList(new TObject()));
		mockMvc.perform(get("/objects")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	public void saveTObject_validTObject_TObjectIsReturned() throws Exception {
		TObject tObject = new TObject();
		tObject.setValue(100);
		tObject.setTitle("Java 8");
		mockMvc.perform(post("/object").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(tObject))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.title", Matchers.equalTo("Java 8")));

	}

	@Test
	public void saveTObject_NotValidTObject_BadRequest() throws Exception {
		TObject tObject = new TObject();

		tObject.setTitle("Java 8");
		mockMvc.perform(post("/object").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(tObject))).andExpect(status().isBadRequest());
	}
	@Test
	public void saveTObject_NotValidId_NotFound() throws Exception {
		TObject tObject = new TObject(2, "test post", 20);
		given(tObjectService.contains(2)).willReturn(false);
		mockMvc.perform(post("/object/2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(tObject))).andExpect(status().isNotFound());
	}

	@Test
	public void updateTObject_validTObject_TObjectIsReturned() throws Exception {
		TObject tObject = new TObject(2, "Java 8", 100);
		mockMvc.perform(post("/object").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(tObject)));
		TObject tObject2 = new TObject(2, "Java 9", 200);
		given(tObjectService.contains(2)).willReturn(true);
		mockMvc.perform(put("/object/2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(MAPPER.writeValueAsString(tObject2))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.title", Matchers.equalTo("Java 9")));

	}

	@Test
	public void findTObjectByID_StorageIsNotEmpty_OneObjectIsReturned() throws Exception {
		TObject tObject = new TObject(1, "Test TObject", 100);
		given(tObjectService.findOne(1)).willReturn(tObject);
		given(tObjectService.contains(1)).willReturn(true);
		mockMvc.perform(get("/object/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", Matchers.equalTo(1)));
	}

	@Test
	public void findTObjectByID_NonCorrectId_NotFound() throws Exception {
		given(tObjectService.contains(1)).willReturn(false);
		mockMvc.perform(get("/object/1")).andExpect(status().isNotFound());
	}

	@Test
	public void deleteTObjectById_CorrectId_OneObjectIsDeleted() throws Exception {
		given(tObjectService.contains(1)).willReturn(true);
		doNothing().when(tObjectService).delete(1);
		mockMvc.perform(delete("/object/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	public void deleteTObjectById_NonCorrectId_NotFound() throws Exception {
		given(tObjectService.contains(1)).willReturn(false);
		mockMvc.perform(delete("/object/1")).andExpect(status().isNotFound());
	}

}
