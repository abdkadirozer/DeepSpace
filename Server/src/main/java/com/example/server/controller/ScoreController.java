package com.example.server.controller;

import com.example.server.model.Score;
import com.example.server.model.User;
import com.example.server.model.projection.ScoreBoardDto;
import com.example.server.model.projection.ScoreDto;
import com.example.server.service.ScoreService;
import com.example.server.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"Score Operations"})
@SwaggerDefinition(tags = {
        @Tag(name = "Score Operations", description = "Operations pertaining to scores in our Game")})
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @Autowired
    UserService userService;

    @SuppressWarnings("unused")
    @ApiOperation(value = "Lists all of the scores.", response = List.class)
    @ApiResponse(code = 200, message = "All of the scores are listed successfully.")
    @GetMapping(value = "/all")
    public ResponseEntity<List<ScoreDto>> getAll(){
        return ResponseEntity.ok().body(this.scoreService.getAll());
    }

    @ApiOperation(value = "Lists max scores of all users.", response = List.class)
    @ApiResponse(code = 200, message = "All of the max scores are listed successfully.")
    @GetMapping(value = "/leaderboard/all_time")
    public ResponseEntity<List<ScoreBoardDto>> getBoard(){
        return ResponseEntity.ok().body(this.scoreService.getBoard());
    }

    @ApiOperation(value = "Lists max scores of all users weekly.", response = List.class)
    @ApiResponse(code = 200, message = "All of the weekly max scores are listed successfully.")
    @GetMapping(value = "/leaderboard/week")
    public ResponseEntity<List<ScoreBoardDto>> getWeekBoard(){
        return ResponseEntity.ok().body(this.scoreService.getWeekBoard());
    }

    @ApiOperation(value = "Lists max scores of all users monthly.", response = List.class)
    @ApiResponse(code = 200, message = "All of the monthly max scores are listed successfully.")
    @GetMapping(value = "/leaderboard/month")
    public ResponseEntity<List<ScoreBoardDto>> getMonthBoard(){
        return ResponseEntity.ok().body(this.scoreService.getMonthBoard());
    }

    @ApiOperation(value = "Gets the max score of the requested user.", response = Map.class)
    @ApiResponse(code = 200, message = "Max score is found correctly.")
    @GetMapping(value =  "/max/{username}")
    public ResponseEntity<ScoreDto> maxScore(@PathVariable  String username){
        return ResponseEntity.ok().body(this.scoreService.findMaxByName(username));
    }

    @ApiOperation(value = "Adding new score operation.", response = Score.class)
    @ApiResponse(code = 200, message = "Score is added successfully.")
    @PostMapping(value = "/add")
    public ResponseEntity<Score> addScore(@RequestBody Map<String ,String > input){
        User u = this.userService.findUser(input.get("username"));
        Score s = new Score(u,Integer.parseInt(input.get("point")));
        this.scoreService.addScore(s);
        return ResponseEntity.ok().body(s);
    }
}