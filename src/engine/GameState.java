package engine;

import engine.enums.Color;
import engine.helper.Location;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class GameState {

    public static boolean gameStart = true;

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

        while (true) {
            placeSettlement();
//            placeSettlement();

            gameStart = false;

            placeRoad();
//            placeRoad();

            int diceRoll = Dice.roll();
            System.out.println("Dice roll: " + diceRoll);
            board.produceResources(diceRoll);

            for (Player player: players) {
                System.out.println(player + " - " + player.getStockpile());
            }
        }
    }

    public void placeSettlement() {
        Scanner sc = GameRunner.sc;

        System.out.println("Available settlement placements: " + board.availableSettlementPlacements(gameStart));
        System.out.println("Enter location to place settlement: ");
        String[] input = sc.nextLine().split(" ");

        if (input[0].equals("skip"))
            return;

        boolean flag = board.placeSettlement(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2])));

        while (!flag) {
            System.out.println("Available settlement placements: " + board.availableSettlementPlacements(gameStart));
            System.out.println("Invalid settlement placement, try again: ");
            input = sc.nextLine().split(" ");
            flag = board.placeSettlement(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2])));
        }
    }

    public void placeRoad() {
        Scanner sc = GameRunner.sc;

        System.out.println("Available road placements: " + board.availableRoadPlacements());
        System.out.println("Enter location to place road: ");
        String[] input = sc.nextLine().split(" ");

        if (input[0].equals("skip"))
            return;

        boolean flag = board.placeRoad(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2])));

        while (!flag) {
            System.out.println("Available road placements: " + board.availableRoadPlacements());
            System.out.println("Invalid road placement, try again: ");
            input = sc.nextLine().split(" ");
            flag = board.placeRoad(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2])));
        }
    }

    public static void nextTurn() {
        currentPlayerIndex++;
        currentPlayer = players[currentPlayerIndex];
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

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}
