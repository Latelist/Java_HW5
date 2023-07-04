import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;

public class Task4onemore {
    Stack<List<Integer>> moves = new Stack<>();
    HashMap<List<Integer>, ArrayList<List<Integer>>> potentialMoves = new HashMap<>();
    HashMap<List<Integer>, ArrayList<List<Integer>>> forbiddenMoves = new HashMap<>();
    List<Integer> lastMove = new ArrayList<Integer>();
    List<Integer> previousMove = new ArrayList<Integer>();
    List<Integer> cell = new ArrayList<>(Arrays.asList(0, 0));
    
    
    
    Task4onemore() {
        int[][] deck = new int[8][8];
            deckPrinter(deck, cell);
        
        horsePlacer(cell, deck);
            deckPrinter(deck, cell);
        moves.add(cell);
        potentialMovesFinder(cell, deck, potentialMoves, forbiddenMoves);
        
        while (isHasNulls(deck)) {
            while (!potentialMoves.get(cell).isEmpty()) {
                previousMove = moves.peek();
                cell = placeFinder(previousMove, potentialMoves);
                deckPrinter(deck, cell);
                horsePlacer(cell, deck);
                    deckPrinter(deck, cell);
                moves.add(cell);
                potentialMovesFinder(cell, deck, potentialMoves, forbiddenMoves);
                    // System.out.println(potentialToString(forbiddenMoves));
            } 
            lastMove = moves.pop();
            previousMove = moves.peek();
            cancelStep(deck, lastMove, previousMove, forbiddenMoves);
            potentialMovesFinder(previousMove, deck, potentialMoves, forbiddenMoves);
            cell = previousMove;
            // deckPrinter(deck, cell);
        }
    }

    public static void horsePlacer(List<Integer> cell, int[][] deck) {
        deck[cell.get(0)][cell.get(1)] = 1;
    }

    public static void potentialMovesFinder(List<Integer> move, int[][] deck,
                                             HashMap<List<Integer>, ArrayList<List<Integer>>> potentialMoves,
                                             HashMap<List<Integer>, ArrayList<List<Integer>>> forbiddenMoves) {
        ArrayList<List<Integer>> potential = new ArrayList<>();
        
        if (move.get(0) - 2 >= 0) {
            if (move.get(1) - 1 >= 0 && deck[move.get(0) - 2][move.get(1) - 1] == 0) {
                List<Integer> potential1 = new ArrayList<>(Arrays.asList(move.get(0) - 2, move.get(1) - 1));
                if (!isForbidden(move, potential1, forbiddenMoves)) {
                    potential.add(potential1);
                }
            }
            if (move.get(1) + 1 <= 7 && deck[move.get(0) - 2][move.get(1) + 1] == 0) {
                List<Integer> potential2 = new ArrayList<>(Arrays.asList(move.get(0) - 2, move.get(1) + 1));
                if (!isForbidden(move, potential2, forbiddenMoves)) {
                    potential.add(potential2);
                }
            }
        }

        if (move.get(0) - 1 >= 0) {
            if (move.get(1) - 2 >= 0 && deck[move.get(0) - 1][move.get(1) - 2] == 0) {
                List<Integer> potential3 = new ArrayList<>(Arrays.asList(move.get(0) - 1, move.get(1) - 2));
                if (!isForbidden(move, potential3, forbiddenMoves)) {
                    potential.add(potential3);
                }
            }
            if (move.get(1) + 2 <= 7 && deck[move.get(0) - 1][move.get(1) + 2] == 0) {
                List<Integer> potential4 = new ArrayList<>(Arrays.asList(move.get(0) - 1, move.get(1) + 2));
                if (!isForbidden(move, potential4, forbiddenMoves)) {
                    potential.add(potential4);
                }
            }
        }

        if (move.get(0) + 1 <= 7) {
            if (move.get(1) - 2 >= 0 && deck[move.get(0) + 1][move.get(1) - 2] == 0) {
                List<Integer> potential5 = new ArrayList<>(Arrays.asList(move.get(0) + 1, move.get(1) - 2));
                if (!isForbidden(move, potential5, forbiddenMoves)) {
                    potential.add(potential5);
                }
                
            }
            if (move.get(1) + 2 <= 7 && deck[move.get(0) + 1][move.get(1) + 2] == 0) {
                List<Integer> potential6 = new ArrayList<>(Arrays.asList(move.get(0) + 1, move.get(1) + 2));
                if (!isForbidden(move, potential6, forbiddenMoves)) {
                    potential.add(potential6);
                } 
            }
        } 

        if (move.get(0) + 2 <= 7) {
            if (move.get(1) -1 >= 0 && deck[move.get(0) + 2][move.get(1) - 1] == 0) {
                List<Integer> potential7 = new ArrayList<>(Arrays.asList(move.get(0) + 2, move.get(1) - 1));
                if (!isForbidden(move, potential7, forbiddenMoves)) {
                    potential.add(potential7);
                }
                
            }
            if (move.get(1) +1 <= 7 && deck[move.get(0) + 2][move.get(1) + 1] == 0) {
                List<Integer> potential8 = new ArrayList<>(Arrays.asList(move.get(0) + 2, move.get(1) + 1));
                if (!isForbidden(move, potential8, forbiddenMoves)) {
                    potential.add(potential8);
                }
                
            }
        }
            potentialMoves.put(move, potential);

    }

    public static List<Integer> placeFinder(List<Integer> move, HashMap<List<Integer>, ArrayList<List<Integer>>> potentialMoves) {
        if (potentialMoves.containsKey(move)) {
            for (List<Integer> place : potentialMoves.get(move)) {
                return place;
            }
        }
        List<Integer> noPlace = new ArrayList<>(Arrays.asList(-1, -1));
        return noPlace;
    }

    
    public static void deckPrinter(int[][] deck, List<Integer> cell) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                // List<Integer> place = new List<Integer> {i, j};
                // if (isContains(badPlace, place)){
                //     System.out.print("|__|");
                // } else 
                if (i == cell.get(0) && j == cell.get(1) && deck[i][j] != 1) {
                    System.out.print("||||");
                } else if (deck[i][j] == 0) {
                    System.out.print("|__|");
                } else if (deck[i][j] == 1) {
                    System.out.print("| H|");
                } else if (deck[i][j] == -1) {
                    System.out.print("| .|");
                } 
            }
            System.out.println();
        }
        System.out.println();
    }
    
    
    public static void arrayPrinter(List<Integer> array) {
        for (Integer integer : array) {
            System.out.print(integer + " ");
        }
        System.out.println();
    }

    public static String potentialToString(HashMap<List<Integer>, ArrayList<List<Integer>>> potentialMoves) {
        StringBuilder strb = new StringBuilder();
        
        for (List<Integer> key : potentialMoves.keySet()) {
            strb.append(key.get(0) + " " + key.get(1) + ": ");
            for (List<Integer> cell : potentialMoves.get(key)) {
                strb.append(cell.get(0) + " " + cell.get(1) + ", ");
            }
            strb.append("\n");            
        }
        String allPotential = strb.toString();
        return allPotential;
    }

    public static boolean isForbidden(List<Integer> move, List<Integer> potential, HashMap<List<Integer>, ArrayList<List<Integer>>> forbiddenMoves) {
        for (List<Integer> key : forbiddenMoves.keySet()) {
            System.out.println();
            if (key.get(0).equals(move.get(0)) && key.get(1).equals(move.get(1))) {
                ArrayList<List<Integer>> forbidden = forbiddenMoves.get(move);
                if (forbidden != null) {
                    for (List<Integer> place : forbidden) {
                        if (place.get(0).equals(potential.get(0)) && place.get(1).equals(potential.get(1))) {
                            return true;
                        }
                    }
                }
            }           
        }
        return false;
    }

    public static int cancelStep(int[][] deck, List<Integer> lastMove, List<Integer> previousMove, HashMap<List<Integer>, ArrayList<List<Integer>>> forbiddenMoves) {
        deck[lastMove.get(0)][lastMove.get(1)] = 0;
        for (List<Integer> key : forbiddenMoves.keySet()) {
            if (key.get(0).equals(previousMove.get(0)) && key.get(1).equals(previousMove.get(1))) {
                forbiddenMoves.get(key).add(lastMove);
                return 0;
            }
        }
            forbiddenMoves.put(previousMove, new ArrayList<List<Integer>>());
            forbiddenMoves.get(previousMove).add(lastMove);
            return 0;
    }

    // public static void checkAgain(List<Integer> move, HashMap<List<Integer>, ArrayList<Integer[]>> potentialMoves, HashMap<List<Integer>, ArrayList<Integer[]>> forbiddenMoves) {
    //     if (forbiddenMoves.containsKey(move)) {
    //         for (Integer[] place : potentialMoves.get(move)) {
    //             for (Integer[] badPlace : forbiddenMoves.get(move)) {
    //                 if (place.equals(badPlace)) {
    //                     potentialMoves.get(move).remove(place);
    //                 }
    //             }                
    //         }
    //     }
    // }

    public static boolean isHasNulls(int[][] deck) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                if (deck[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
