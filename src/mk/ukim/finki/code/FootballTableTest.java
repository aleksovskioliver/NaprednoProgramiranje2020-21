package mk.ukim.finki.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Team implements Comparable<Team>{
    String name;
    int wins;
    int draws;
    int loses;
    int dadeniGolovi;
    int primeniGolovi;

    public Team(String name){
        this.name = name;
        this.wins=0;
        this.draws = 0;
        this.loses = 0;
        this.dadeniGolovi= 0;
        this.primeniGolovi = 0;
    }
    public int getPoints(){
        return wins*3 + draws;
    }
    public int goalRazlika(){
        return dadeniGolovi-primeniGolovi;
    }
    public int vkupnoNatprevari(){
        return wins+draws+loses;
    }

    public String getName() {
        return name;
    }

    public void setWins() {
        this.wins++;
    }

    public void setDraws() {
        this.draws++;
    }

    public void setLoses() {
        this.loses++;
    }

    public void setDadeniGolovi(int dadeniGolovi) {
        this.dadeniGolovi += dadeniGolovi;
    }

    public void setPrimeniGolovi(int primeniGolovi) {
        this.primeniGolovi += primeniGolovi;
    }

    @Override
    public int compareTo(Team o) {
        return Comparator.comparing(Team::getPoints)
                .thenComparing(Team::goalRazlika).reversed()
                .thenComparing(Team::getName)
                .compare(o,this);
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d",
                name,vkupnoNatprevari(),wins,draws,loses,getPoints());
    }
}
class FootballTable{
    Map<String, Team> teams;

    public FootballTable(){
        this.teams = new HashMap<>();
    }
    private Team checkInTable(String teamName){
        if (teams.containsKey(teamName))
            return this.teams.get(teamName);
        Team t = new Team(teamName);
        this.teams.put(teamName,t);
        return t;
    }

    public void addGame(String homeTeam,String awayTeam,int homeGoals,int awayGoals){
        Team domaci = checkInTable(homeTeam);
        Team gostici = checkInTable(awayTeam);

        if (homeGoals>awayGoals){
            domaci.setWins();
            gostici.setLoses();
        }else if(homeGoals == awayGoals){
            domaci.setDraws();
            gostici.setDraws();
        }else{
            domaci.setLoses();
            gostici.setWins();
        }
        setGoals(domaci,homeGoals, awayGoals);
        setGoals(gostici,awayGoals,homeGoals);
    }
    private void setGoals(Team t,int dadeni,int primeni){
        t.setPrimeniGolovi(primeni);
        t.setDadeniGolovi(dadeni);
    }
    public void printTable(){
        List<Team> table = new ArrayList<>(this.teams.values());
        Collections.sort(table);
        for (int i=0;i<table.size();i++){
            System.out.printf("%2d. %s\n",i+1,table.get(i));
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

