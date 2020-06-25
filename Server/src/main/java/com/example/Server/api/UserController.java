package com.example.Server.api;


import com.example.Server.model.User;
import com.example.Server.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"User Operations"})
@SwaggerDefinition(tags = {
        @Tag(name = "User Operations", description = "Operations pertaining to users in our Game")})
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;


    @ApiOperation(value = "Signup operation for users.", response = User.class)
    @PostMapping(value = "/signup")
    public ResponseEntity<User> addUser(@RequestBody User user){
        if(user.getUserName().equals(""))
            return ResponseEntity.badRequest().body(null);
        else if(user.getPassword().equals(""))
            return ResponseEntity.badRequest().body(null);

        User tmp = this.userService.addUser(user);
        if(tmp == null)
            return ResponseEntity.badRequest().body(null);
        else
            return ResponseEntity.ok().body(tmp);
    }


    @ApiOperation(value = "Update user information.", response = User.class)
    @ApiResponses( value =  {
            @ApiResponse(code = 200, message = "User is successfully updated."),
            @ApiResponse(code = 401, message = "You are not authorized to update the user.")})
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody Map<String,String> input){

        User u = this.userService.updateUser(input);
        if(u != null)
            return ResponseEntity.ok().body(u);
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //Debug purposes
    @ApiOperation(value = "Find user information corresponding to the username.", response = User.class)
    @ApiResponses( value =  {
            @ApiResponse(code = 200, message = "Find operation is done."),
            @ApiResponse(code = 404, message = "User you were trying to reach is not found.")})
    @GetMapping("/find/{username}")
    public ResponseEntity<User> findUser(@PathVariable("username") String username){
        User user = this.userService.findUser(username);
        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else
            return ResponseEntity.ok().body(user);
    }


    @ApiOperation(value = "Login operation for the users.", response = String.class)
    @ApiResponses( value =  {
            @ApiResponse(code = 200, message = "User is logged in successfully."),
            @ApiResponse(code = 400, message = "Wrong username or password.")})
    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody User user)
    {
        String session = this.userService.login(user);
        if(session != null) {
            return ResponseEntity.ok().body(session);
        }
        else
            return ResponseEntity.badRequest().build();
    }

    //Debug purposes
    @ApiOperation(value = "Lists all of the users.", response = List.class)
    @ApiResponse(code = 200, message = "All users are listed.")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(this.userService.getAllUsers());
    }

    @ApiOperation(value = "Delete user.", response = User.class)
    @ApiResponses( value =  {
            @ApiResponse(code = 200, message = "User and the scores are successfully deleted."),
            @ApiResponse(code = 401, message = "You are not authorized to delete the user.")})
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestBody Map<String,String> input){
        User u = this.userService.deleteUser(input.get("username"),input.get("session"));
        if(u != null)
            return ResponseEntity.ok().body(u);
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
