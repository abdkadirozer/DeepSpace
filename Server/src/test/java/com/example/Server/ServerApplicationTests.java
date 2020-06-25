package com.example.Server;

import com.example.Server.model.Score;
import com.example.Server.model.User;
import com.example.Server.service.ScoreService;
import com.example.Server.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
class ServerApplicationTests {

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private UserService userService;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	ObjectMapper objectMapper;

	private User user;
	private User user1;
	private User user2;
	private User user3;
	private User user4;

	private Score score;
	private Score score1;
	private Score score2;
	private Score score3;

	@BeforeEach
	public void setUp(){
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		user = new User("ali","pass");
		user1 = new User("veli","pass1");
		user2 = new User("firat","2222");
		user3 = new User("ece","pass3");
		user4 = new User("ayse","pass4");

		score = new Score(user,100);
		score1 = new Score(user1,60);
		score2 = new Score(user,20);
		score3 = new Score(user4,10);
	}
	@Test
	public void sign_upScenario() throws Exception {
		int old_size = userService.getAllUsers().size();
		this.mockMvc.perform(post("/user/signup").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)));
		int new_size = userService.getAllUsers().size();
		// True ,if user list size is increased after signing up.
		assertEquals(old_size+1,new_size);
	}
	// Scenarion : username field is empty
	@Test
	public void invalid_sign_upScenario() throws Exception {
		User invalid_user = new User("","12345");
		int old_size = userService.getAllUsers().size();
		this.mockMvc.perform(post("/user/signup").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalid_user)));
		int new_size = userService.getAllUsers().size();
		// True ,if user list size is not increased trying signing up.
		assertEquals(old_size,new_size);
	}

	/// Scenario : username has already taken
	@Test
	public void invalid_sign_upScenario2() throws Exception {
		//username is the same with user
		User invalid_user = new User("ali","12345");

		int old_size = userService.getAllUsers().size();
		this.mockMvc.perform(post("/user/signup").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalid_user)));
		int new_size = userService.getAllUsers().size();
		// True ,if user list size is not increased trying signing up.
		String session = userService.login(user);
		userService.deleteUser(user.getUserName(),session);
		assertEquals(old_size,new_size);
	}

	@Test
	public void loginScenario() throws Exception {
		String input  = objectMapper.writeValueAsString(user3);
		this.userService.addUser(user3);
		MvcResult mvcResult = this.mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
				.content(input)).andReturn();
		userService.deleteUser(user3.getUserName(),mvcResult.getResponse().getContentAsString());
		assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
	}

	@Test
	public void invalid_loginScenario() throws Exception {
		//user1 is in user list, but user2 is not.
		MvcResult mvcResult = this.mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\": \"" + user2.getUserName() + "\" , \"password\": \"" + user2.getPassword() + "\"}")).andReturn();

		assertNotEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK);
	}

}
