package com.codecool.reshuffle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reshuffle {

    private final List<Team> oldTeams = new ArrayList<>();
    private final List<Team> newTeams = new ArrayList<>();
    private List<Integer> randomNums = new ArrayList<>();
    private int listSize;

    public void letsRoll() {
        System.out.println("Some magic happens here");
        fillUpRandomNumsList();

        for (int row = 0; row < listSize; row++) {
            addNewTeamToNewTeams();
            fillNewTeamInNewTeams(row);
        }
    }

    private void addNewTeamToNewTeams() {
        Team team = new Team();
        newTeams.add(team);
    }

    private void fillNewTeamInNewTeams(int row) {
        int innerListSize = oldTeams.get(row).getMembers().size();

        for (int column = 0; column < innerListSize; column++) {
            String member = getMember(row, column);
            newTeams.get(row).addMember(member);
        }
    }

    private String getMember(int row, int column) {
        int index = getRandomOtherTeamIndex(row, column);
        return oldTeams.get(index).getMembers().get(column);
    }

    private int getRandomOtherTeamIndex(int row, int column) {
        int rotation = randomNums.get(rotate(column));
        return rotate(row + rotation);
    }

    private int rotate(int index) {
        return index % listSize;
    }

    private void fillUpRandomNumsList() {
        randomNums = IntStream.range(0, listSize).boxed().collect(Collectors.toList());
        Collections.shuffle(randomNums);
    }

    public void loadOldTeams(String fileName) {
        oldTeams.clear();
        try {
            Scanner s = new Scanner(new File(fileName));
            Team team = null;
            while (s.hasNext()) {
                String nextLine = s.next();
                if (nextLine.contentEquals("#")) {
                    team = null;
                } else {
                    if (team == null) {
                        team = new Team();
                        oldTeams.add(team);
                    }
                    team.addMember(nextLine);
                }
            }
            listSize = oldTeams.size();
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printOldTeams() {
        System.out.println("Old: " + oldTeams);
    }

    public void printNewTeams() {
        System.out.println("New: " + newTeams);
    }

    public static void main(String[] args) {
        Reshuffle reshuffle = new Reshuffle();
        reshuffle.loadOldTeams("resources/teams_sample.txt");
        reshuffle.printOldTeams();
        reshuffle.letsRoll();
        reshuffle.printNewTeams();
    }

}
