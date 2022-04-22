package engine;

import engine.enums.Color;
import engine.enums.ResourceType;
import engine.helper.Location;

import java.util.*;

public class GameState {

    public static boolean gameStart = true;

    private static Player currentPlayer = null;
    private static int currentPlayerIndex = 0;

    private static Player[] players;
    private final Board board;

    private Player longestRoadHolder = null;
    private Player largestArmyHolder = null;

    private Scanner sc;

    public GameState(Scanner sc, int numPlayers, int seed) {
        this.sc = sc;
        Dice.init(seed);
        board = new Board();
        players = new Player[numPlayers];

        initializePlayers(numPlayers);

        setUpPhase();

        while (true) {
            resourceProductionPhase();
            tradePhase();
            buyPhase();

            nextTurn();
        }

    }

    public static void nextTurn() {
        currentPlayerIndex++;

        if (currentPlayerIndex >= players.length)
            currentPlayerIndex = 0;

        currentPlayer = players[currentPlayerIndex];
        System.out.println("Next turn: " + currentPlayer);
    }

    // region Setup Phase
    public void setUpPhase() {
        for (int i = 0; i < players.length; i++) {
            placeSettlement();
            placeRoad();
            nextTurn();
        }

        for (int i = 0; i < players.length; i++) {
            placeSettlement();
            placeRoad();
            board.produceResources(currentPlayer.getStructures().get(1));

            for (Player player: players) {
                System.out.println(player + " - " + player.getStockpile());
            }

            nextTurn();
        }

        gameStart = false;
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
    // endregion

    // region Resource Production Phase
    public void resourceProductionPhase() {
        int diceRoll = Dice.roll();
        System.out.println(currentPlayer + " rolled " + diceRoll);

        if (diceRoll == 7) {
            discardHalf();
            moveRobber();
            stealResource();
        } else {
            board.produceResources(diceRoll);
        }
    }

    public void discardHalf() {
        for (Player player: players) {
            if (player.getStockpile().getTotal() > 7) {
                final int AMOUNT_TO_DISCARD = player.getStockpile().getTotal() / 2;

                // TODO: implement way for each player to choose which resources to discard
            }
        }
    }

    public void moveRobber() {
        System.out.println("Enter location to move robber: ");
        String[] input = sc.nextLine().split(" ");
        boolean flag = board.moveRobber(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1])));

        while (!flag) {
            System.out.println("Invalid location. Enter location to move robber: ");
            flag = board.moveRobber(new Location(Integer.parseInt(input[0]), Integer.parseInt(input[1])));
        }
    }

    public void stealResource() {
        System.out.println("Available players: " + board.getAvailablePlayersToStealFrom());
        System.out.println("Enter player number to steal from:");

        int playerNum = sc.nextInt();
        Player player = null;
        boolean flag = false;

        while (!flag) {
            for (Player p: players) {
                if (p.getId() == playerNum) {
                    player = p;
                    flag = true;
                    break;
                }
            }

            System.out.println("Invalid player number. Enter player number to steal from:");
            playerNum = sc.nextInt();
        }

        board.stealRandomResource(player);
    }
    // endregion

    // region Trade Phase
    // TODO: complete this first
    public void tradePhase() {
        String input = sc.nextLine();

        while (!input.equalsIgnoreCase("stop")) {
            switch (input.toLowerCase(Locale.ROOT)) {
                case "domestic":
                    break;
                case "stockpile":
                    break;
                case "harbor":
                    // TODO: still need to implement harbors
                    break;
            }

            input = sc.nextLine();
        }
    }
    // endregion

    // region Buy Phase

    public void buyPhase() {
        String input = sc.nextLine();

        while (!input.equalsIgnoreCase("stop")) {
            switch (input.toLowerCase(Locale.ROOT)) {
                case "road":
                    break;
                case "settlement":
                    break;
                case "city":
                    break;
                case "development":
                    break;
            }
        }
    }
    // endregion

    // TODO: remember that development cards can be played at any time, including before the roll
    // TODO: only 1 development card can be played per turn; obtained development card only played on a different turn
    public void useDevelopmentCard() {

    }


    // region Place settlements/roads
    public void placeSettlement() {
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
    // endregion

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}
