package engine;

import engine.enums.Color;
import engine.helper.Location;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class GameState {

    private static Player currentPlayer = null;
    private static int currentPlayerIndex = 0;

    private static Player[] players;
    private final Board board;

    private Player longestRoadHolder = null;
    private Player largestArmyHolder = null;

    public GameState(int numPlayers, int seed) {
        Dice.init(seed);
        board = new Board();
        players = new Player[numPlayers];

        initializePlayers(numPlayers);

        Scanner sc = GameRunner.sc;
        System.out.println("Enter location to place settlement: ");

        while (sc.hasNextLine()) {
            String[] input = sc.nextLine().split(" ");

            board.placeSettlement(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2])));

            System.out.println("Enter location to place road: ");

            input = sc.nextLine().split(" ");
            board.placeRoad(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2])));

            nextTurn();
            System.out.println("Enter location to place settlement: ");
        }
    }

    public static void nextTurn() {
        currentPlayerIndex++;
        currentPlayer = players[currentPlayerIndex];
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void initializePlayers(int numPlayers) {
        TreeMap<Integer, Player> playerMap = new TreeMap<>();

        for (int i = 0; i < numPlayers; i++) {
            int roll = Dice.roll();

            while (playerMap.containsKey(roll)) {
                roll = Dice.roll();
            }

            playerMap.put(roll, new Player(Color.values()[i]));
        }

        Set<Integer> keys = playerMap.keySet();

        int index = numPlayers - 1;

        for (Integer key: keys) {
            players[index] = playerMap.get(key);
            index--;
        }

        currentPlayer = players[0];
    }


}
