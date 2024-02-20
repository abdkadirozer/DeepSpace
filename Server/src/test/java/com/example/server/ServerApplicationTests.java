package com.example.server;

import com.example.server.model.Score;
import com.example.server.model.User;
import com.example.server.model.request.UserAddRequest;
import com.example.server.service.ScoreService;
import com.example.server.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

import static com.example.server.model.User.UserBuilder.anUser;
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
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        user = anUser()
                .setUsername("ali")
                .setPassword("pass")
                .build();
        user1 = anUser()
                .setUsername("veli")
                .setPassword("pass1")
                .build();
        user2 = anUser()
                .setUsername("firat")
                .setPassword("2222")
                .build();
        user3 = anUser()
                .setUsername("ece")
                .setPassword("pass3")
                .build();
        user4 = anUser()
                .setUsername("ayse")
                .setPassword("pass4")
                .build();

        score = new Score(user, 100);
        score1 = new Score(user1, 60);
        score2 = new Score(user, 20);
        score3 = new Score(user4, 10);
    }

    @Test
    public void sign_upScenario() throws Exception {
        int old_size = userService.getAllUsers().size();
        UserAddRequest request = new UserAddRequest("ali", "12345");
        this.mockMvc.perform(post("/user/signup").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        int new_size = userService.getAllUsers().size();
        // True ,if user list size is increased after signing up.
        assertEquals(old_size + 1, new_size);
    }

    /// Scenario : username has already taken
    @Test
    public void invalid_sign_upScenario2() throws Exception {
        //username is the same with user
        UserAddRequest invalid_request = new UserAddRequest("ali", "12345");

        int old_size = userService.getAllUsers().size();
        this.mockMvc.perform(post("/user/signup").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid_request)));
        int new_size = userService.getAllUsers().size();
        // True ,if user list size is not increased trying signing up.
        String session = userService.login(invalid_request);
        userService.deleteUser(invalid_request.getUsername(), session);
        assertEquals(old_size, new_size);
    }

    @Test
    public void loginScenario() throws Exception {
        String input = objectMapper.writeValueAsString(new UserAddRequest(user3.getUsername(), user3.getPassword()));
        this.userService.addUser(user3);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
                .content(input)).andReturn();
        userService.deleteUser(user3.getUsername(), mvcResult.getResponse().getContentAsString());
        assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    public void invalid_loginScenario() throws Exception {
        //user1 is in user list, but user2 is not.
        MvcResult mvcResult = this.mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + user2.getUsername() + "\" , \"password\": \"" + user2.getPassword() + "\"}")).andReturn();

        assertNotEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK);
    }

}
