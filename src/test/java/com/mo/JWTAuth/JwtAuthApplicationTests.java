package com.mo.JWTAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mo.JWTAuth.web.request.CreateUserRequest;
import com.mo.JWTAuth.web.request.LoginUserRequest;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
				.apply(springSecurity())
				.alwaysDo(print()).build();

		assertNotNull(mockMvc);
	}

	@Test
	void functional() throws Exception {
		/* SIGNUP TEST — "POST /api/users" */
		CreateUserRequest userRequest = new CreateUserRequest();
		userRequest.setFirstName("Mo");
		userRequest.setLastName("Almishkawi");
		userRequest.setEmail("mo@testemail.me");
		userRequest.setPassword("N0TMyReAlP@SsWORddd");
		String requestPayload = OBJECT_MAPPER.writeValueAsString(userRequest);

		/* payload not valid scenario */
		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest());
		/* ... */

		/* success scenario */
		MvcResult createRequestResult = mockMvc
				.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestPayload))
				.andExpect(status().isCreated())
				.andReturn();

		/* email already exists scenario */
		mockMvc
				.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestPayload))
				.andExpect(status().isBadRequest());

		UserResponse userResponse = modelMapper.map(userRequest, UserResponse.class);
		String userResponseBody = OBJECT_MAPPER.writeValueAsString(userResponse);

		assertEquals(userResponseBody, createRequestResult.getResponse().getContentAsString());
		assertFalse(userResponseBody.contains("password"));
		/* ------------------------------- */

		/* LOGIN TEST — "POST /api/login" */
		LoginUserRequest loginUserRequest = new LoginUserRequest();
		loginUserRequest.setEmail("mo@testemail.me");
		loginUserRequest.setPassword("N0TMyReAlP@SsWORddd");
		String loginRequestPayload = OBJECT_MAPPER.writeValueAsString(loginUserRequest);

		/* success scenario */
		MvcResult loginRequestResult = mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginRequestPayload))
				.andExpect(status().isOk())
				.andExpect(header().exists("Authorization"))
				.andReturn();

		String authToken = loginRequestResult.getResponse()
				.getHeaderValue("Authorization").toString();

		assertTrue(authToken.startsWith("Bearer ey"));

		/* Invalid credentials scenario */
		loginUserRequest.setPassword("onetwothree");
		loginRequestPayload = OBJECT_MAPPER.writeValueAsString(loginUserRequest);
		mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginRequestPayload))
				.andExpect(status().isUnauthorized());
		/* ------------------------------- */

		/* AUTHENTICATED USER DETAILS TEST — "GET /api/users/authenticated" */
		MvcResult authenticatedRequestResult = mockMvc.perform(get("/api/users/authenticated")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", authToken)
				.content("{}"))
				.andExpect(status().isOk())
				.andReturn();

		assertEquals(userResponseBody, authenticatedRequestResult.getResponse().getContentAsString());
		/* AUTHENTICATED USER DETAILS TEST — END */
	}
}
