package com.example.game.levelsAndServices;

public class GameState {

    private long cycle;
    private double lastTime;
    private double time;
    private int currentLevel;
    private int score;
    private int otherscore;


    private String othername;
    private String username;

    public String getUsername() {
        return username;
    }

    public String getOtherName() {
        return othername;
    }

    public void setOtherName(String othername) {
        this.othername = othername;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getOtherscore() {
        return otherscore;
    }
    private int health;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public long getCycle() {
        return cycle;
    }

    public void setCycle(long cycle) {
        this.cycle = cycle;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getTime() {
        return time;
    }

    public GameState(){
        this.lastTime = System.nanoTime();
        this.cycle= 0;
        this.score= 0;
        this.otherscore = 0;
        this.othername = "";
        this.currentLevel = 0;
    }
    public void setTime(double time) {
    this.time= time;
    }

    public double getLastTime() {
    return this.lastTime;
    }

    public void restartCounter() {
        this.cycle = 0;
    }

    public void setLastTime(double d) {
        this.lastTime= d;
    }

    public void incrementCycle() {
    this.cycle++;
    }

    public int getScore() {
        return this.score;
    }

    public void updateScore(int reward) { this.score += reward;}

    public void updateOtherScore(int reward) {
        this.otherscore += reward;
    }

    public void setOtherScore(int score) {
        this.otherscore = score;
    }
}
