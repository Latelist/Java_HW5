import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

// 4) Шахматную доску размером NxN обойти конём так, чтобы фигура в каждой клетке была строго один раз



public class Task4 {
    private ArrayList<Integer[]> badPlace = new ArrayList<>(); // словарь мест, которые нельзя занимать: ключ - ряд, значение — список клеток
    
    private Stack<Integer[]> moves = new Stack<>(); // стэк ходов
    private HashSet<String> combinations = new HashSet<>();
    private HashMap<Integer[], ArrayList<Integer[]>> potentialMoves = new HashMap<>();
    private HashMap<Integer[], ArrayList<Integer[]>> forbiddenMoves = new HashMap<>();
    private Integer[] lastMove = new Integer[2];

    Task4() {
        int[][] deck = new int[8][8];
        Integer[] cell = new Integer[] {0, 0};
        while(isContains(deck)) {
            if (cell[0] != -1 && cell[1] != -1) {
                horsePlacer(deck, cell, potentialMoves);
                deckPrinter(deck, cell);
                lastMove = cell;
                moves.add(cell);
                cell = placeFinder(deck, potentialMoves, lastMove, forbiddenMoves);
            } else {
                while (cell[0] == -1 || cell[1] == -1) {
                    lastMove = cancelStep(deck, moves.pop(), moves.peek(), forbiddenMoves);
                    // for (Integer[] move : moves) {
                    //     System.out.print("[" + move[0] + " " + move[1] + "] ");
                    // }
                    cell = placeFinder(deck, potentialMoves, lastMove, forbiddenMoves);
                    System.out.println();
                    // for (Integer[] move : moves) {
                    //     System.out.print("[" + move[0] + " " + move[1] + "] ");
                    // }
                    System.out.println();
                    System.out.println(cell[0] + " " + cell[1]);
                    deckPrinter(deck, cell);
                } 
            }

        }
    }

    public static void horsePlacer(int[][] deck, Integer[] cell, HashMap<Integer[], ArrayList<Integer[]>> potentialMoves) {
        if (deck[cell[0]][cell[1]] == 0) {
            deck[cell[0]][cell[1]] = 1;
            ArrayList<Integer[]> potMoves = new ArrayList<>();
            if (cell[0] - 2 >= 0) {
                if (cell[1] - 1 >= 0) {
                    Integer[] potMove1 = new Integer[] {cell[0] - 2, cell[1] - 1};
                    potMoves.add(potMove1);
                }
                if (cell[1] + 1 <= 7) {
                    Integer[] potMove2 = new Integer[] {cell[0] - 2, cell[1] + 1};
                    potMoves.add(potMove2);
                }
            }

            if (cell[0] - 1 >= 0) {
                if (cell[1] - 2 >= 0) {
                    Integer[] potMove3 = new Integer[] {cell[0] - 1, cell[1] - 2};
                    potMoves.add(potMove3);
                }
                if (cell[1] + 2 <= 7) {
                    Integer[] potMove4 = new Integer[] {cell[0] - 1, cell[1] + 2};
                    potMoves.add(potMove4);
                }
            }

            if (cell[0] + 1 <= 7) {
                if (cell[1] - 2 >= 0) {
                    Integer[] potMove5 = new Integer[] {cell[0] + 1, cell[1] - 2};
                    potMoves.add(potMove5);
                }
                if (cell[1] + 2 <= 7) {
                    Integer[] potMove6 = new Integer[] {cell[0] + 1, cell[1] + 2};
                    potMoves.add(potMove6);
                }
            } 

            if (cell[0] + 2 <= 7) {
                if (cell[1] -1 >= 0) {
                    Integer[] potMove7 = new Integer[] {cell[0] + 2, cell[1] - 1};
                    potMoves.add(potMove7);
                }
                if (cell[1] +1 <= 7) {
                    Integer[] potMove8 = new Integer[] {cell[0] + 2, cell[1] + 1};
                    potMoves.add(potMove8);
                }
            }

            potentialMoves.put(cell, potMoves);
        }
    }


    public static Integer[] placeFinder(int[][] deck, HashMap<Integer[], ArrayList<Integer[]>> potentialMoves, Integer[] lastMove, HashMap<Integer[], ArrayList<Integer[]>> forbiddenMoves) {
        Integer[] key = lastMove;
        for (Integer[] cell : potentialMoves.get(key)) {
            if (deck[cell[0]][cell[1]] == 0 && !isForbidden(cell, forbiddenMoves)) {
                return cell;
            }            
        } 
        Integer[] noCell = new Integer[] {-1, -1};
        return noCell;
    }


    public static Integer[] cancelStep(int[][] deck, Integer[] lastMove, Integer[] previousMove, 
                                    HashMap<Integer[], ArrayList<Integer[]>> forbiddenMoves) {
        deck[lastMove[0]][lastMove[1]] = 0;
        if (forbiddenMoves.containsKey(previousMove)) {
            forbiddenMoves.get(previousMove).add(lastMove);
        } else {
            forbiddenMoves.put(previousMove, new ArrayList<>());
            forbiddenMoves.get(previousMove).add(lastMove);
        }
        for (Integer[] key : forbiddenMoves.keySet()) {
            arrayPrinter(key); 
            for (Integer[] integer : forbiddenMoves.get(key)) {
                arrayPrinter(integer);
            }           
        }
       return previousMove;
    }


    public static void purifier(int[][] deck) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                if (deck[i][j] == -1) {
                    deck[i][j] = 0;
                }
            }
        }
    }


    public static boolean isContains(int[][] deck) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                if (deck[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

        /**
     * Печать доски
     * @param deck
     * @param badPlace
     */
    public static void deckPrinter(int[][] deck, Integer[] cell) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                // Integer[] place = new Integer[] {i, j};
                // if (isContains(badPlace, place)){
                //     System.out.print("|__|");
                // } else 
                if (i == cell[0] && j == cell[1]) {
                    System.out.print("||||");
                } else if (deck[i][j] == 0 && deck[i][j] != -1) {
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


    public static boolean isForbidden(Integer[] cell, HashMap<Integer[], ArrayList<Integer[]>> forbiddenMoves) {
        if (forbiddenMoves.isEmpty()) {
            return false;
        } else {
            for (Integer[] key : forbiddenMoves.keySet()) {
                arrayPrinter(key);
                arrayPrinter(cell);
                if (key.equals(cell)) {
                        ArrayList<Integer[]> forbidden = forbiddenMoves.get(key);
                        for (Integer[] integers : forbidden) {
                            arrayPrinter(integers);
                        }
                        if (forbidden.isEmpty()) {
                            return false;
                        }
                        for (Integer[] place : forbidden) {
                            if (place.equals(cell)) {
                                return true;
                            }
                        }
                }
                
            }
        }
        return false;
    }


    public static void arrayPrinter(Integer[] array) {
        for (Integer integer : array) {
            System.out.print(integer + " ");
        }
        System.out.println();
    }
}
