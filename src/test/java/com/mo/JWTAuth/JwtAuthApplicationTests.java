package com.mo.JWTAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mo.JWTAuth.web.request.CreateUserRequest;
import com.mo.JWTAuth.web.response.UserResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtAuthApplicationTests {
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ModelMapper modelMapper;

	private MockMvc mockMvc;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@BeforeAll
	void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.alwaysDo(print()).build();

		assertNotNull(mockMvc);
	}

	@Test
	void createUser() throws Exception {
		CreateUserRequest userRequest = new CreateUserRequest();
		userRequest.setFirstName("Mo");
		userRequest.setLastName("Almishkawi");
		userRequest.setEmail("mo.almishkawi@gmail.com");
		userRequest.setPassword("P@SsWORddd");

		String requestPayload = OBJECT_MAPPER.writeValueAsString(userRequest);

		MvcResult requestResult = mockMvc
				.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestPayload))
				.andExpect(status().isCreated())
				.andReturn();

		UserResponse userResponse = modelMapper.map(userRequest, UserResponse.class);
		String responseBody = OBJECT_MAPPER.writeValueAsString(userResponse);

		assertEquals(responseBody, requestResult.getResponse().getContentAsString());
		assertFalse(responseBody.contains("password"));
	}

}
