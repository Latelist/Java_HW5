// АЛГОРИТМ НАХОДИТ 49 ИЗ 92 ВОЗМОЖНЫХ РЕШЕНИЙ. 
    
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Task3 {
    private HashMap<Integer, ArrayList<Integer[]>> badPlace = new HashMap<>(); // словарь мест, которые нельзя занимать: ключ - ряд, значение — список клеток
    private HashMap<Integer[], ArrayList<Integer[]>> lastMove = new HashMap<>(); // клетки, которые были заняты последним ходом
    private Stack<HashMap<Integer[], ArrayList<Integer[]>>> moves = new Stack<>(); // стэк из занятых клеток, чтобы отменять ходы
    private HashSet<String> combinations = new HashSet<>(); // множество решений
    
    Task3() {
        int[][] deck = new int[8][8];
        int r = 0;  // ряд, с которого начинаем искать свободное место.
        int count = 0;
        Integer[] firstCell = new Integer[] {0, 0}; // назначаем клетку, на которую ставим первого ферзя

        while (count != 65) {
            lastMove = queenPlacer(deck, firstCell[0], firstCell[1]);
            r = lastQueenFinder(lastMove);
            ++r;
            if (r > 7) r = 0;

            Integer[] freePlace = freePlaceFinder(deck, badPlace, r); // массив, содержащий координату первого найденного свободного места

            while (moves.size() != 7) { // Цикл продолжается, пока ферзями не занято 8 клеток
                    if (freePlace[0] != -1 && freePlace[1] != -1) { // Если есть свободное место
                        lastMove = queenPlacer(deck, freePlace[0], freePlace[1]); // Ставим королеву и записываем все перебитые поля
                        moves.add(lastMove); // Добавляем ход в стэк
                        r++;
                        if (r > 7) r = 0; // Переходим на следующий ряд
                        if (moves.size() == 7) {
                            break;
                        }
                        // deckPrinter(deck, badPlace);
                        // System.out.println(r);
                        freePlace = freePlaceFinder(deck, badPlace, r); // Ищем новое свободное поле
                    } else { // Если свободного места нет
                        r = lastQueenFinder(moves.peek()); // Находим ряд, где стоит последняя королева
                        cancelStep(deck, moves.pop(), badPlace, r); // Отменяем ее ход
                        // deckPrinter(deck, badPlace);
                        // System.out.println(r);
                        freePlace = freePlaceFinder(deck, badPlace, r); // Ищем в этом же ряду свободное место
                        if (freePlace[0] != -1 && freePlace[1] != -1) { // Если находим — ставим королеву
                            lastMove = queenPlacer(deck, freePlace[0], freePlace[1]); 
                            moves.add(lastMove);
                            // deckPrinter(deck, badPlace);
                            // System.out.println(r);
                            if (moves.size() == 7) {
                                break;
                            }
                            ++r;
                            if (r > 7) r = 0;
                            freePlace = freePlaceFinder(deck, badPlace, r);
                        } else { // Если не находим — убираем из нежелательных мест все места в этом ряду и переходим на предыдущий
                                badPlace.remove(r);
                                --r;
                                if (r < 0) r = 7;
                        }
                    }
                } 
                String combination = deckToString(deck); // Переводим матрицу расположений в строку и добавляем во множество
                if (!combinations.contains(combination)) {
                    combinations.add(combination);
                    deckPrinter(deck, badPlace);
                }
                deckClearer(deck); // зачищаем все используемые переменные, кроме общего счётчика
                r = 0;
                badPlace.clear();
                moves.clear();
                lastMove.clear();
                ++count;
                if (firstCell[1] < 7) { // переназначаем клетку для первой королевы
                    ++firstCell[1];
                } else {
                    ++firstCell[0];
                    firstCell[1] = 0;
                } 
            }            
            System.out.println(combinations.size()); 
        }      

    /**
     * Печать доски
     * @param deck
     * @param badPlace
     */
    public static void deckPrinter(int[][] deck,  HashMap<Integer, ArrayList<Integer[]>> badPlace) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                Integer[] place = new Integer[] {i, j};
                if (isContains(badPlace, place)){
                    System.out.print("|__|");
                } else if (deck[i][j] == 0 && deck[i][j] != -1) {
                    System.out.print("|__|");
                } else if (deck[i][j] == 1) {
                    System.out.print("| Q|");
                } else if (deck[i][j] == -1) {
                    System.out.print("|__|");
                } 
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void deckClearer(int[][] deck) {
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                deck[i][j] = 0;
            }
        }
    }
    
        /**
     * Ищем свободное место в нужном ряду
     * @param deck
     * @param badPLace запрещенные места
     * @param i ряд
     * @return координаты места
     */
    public static Integer[] freePlaceFinder(int[][] deck, HashMap<Integer, ArrayList<Integer[]>> badPLace, int i) {
        Integer[] free = new Integer[] {-1, -1};
            for (int j = 0; j < deck[i].length; ++j) {
                Integer[] place = new Integer[] {i, j};
                if (!isContains(badPLace, place)) {
                    if (deck[i][j] == 0) {
                        free[0] = i;
                        free[1] = j;
                        return free;
                    }
                }      
            } 
        return free;
    }

        /**
     * Проверяем, запрещено ли ставить королеву на клетку
     * @param badPLace
     * @param place
     * @return
     */
    public static boolean isContains(HashMap<Integer, ArrayList<Integer[]>> badPLace, Integer[] place) {
        for (Integer key : badPLace.keySet()) {
            for (Integer[] cell : badPLace.get(key)) {
                if (Arrays.equals(cell, place)) {
                    return true;
                }
            }
        }
        return false;
    }


        /**
     * Ставим королеву на доску
     * @param deck доска
     * @param r координаты клетки, куда нужно поставить королеву
     * @param c
     * @return Словарь, в котором ключ — это координаты клетки, где стоит королева, а значение — все перебитые клетки
     */
    public static HashMap<Integer[], ArrayList<Integer[]>> queenPlacer(int[][] deck, int r, int c) {
        ArrayList<Integer[]> occupied = new ArrayList<>();
        if (deck[r][c] == 0) {
            for (int i = 0; i < deck.length; ++i) {
                for (int j = 0; j < deck[i].length; ++j) {
                    if ((i == r || j == c) && (deck[i][j] != 1 && deck[i][j] != -1)) {
                        deck[i][j] = -1;
                        Integer[] occ = new Integer[] {i,j};
                        occupied.add(occ);
                    } else if ((i + j == r + c ) && (deck[i][j] != 1 && deck[i][j] != -1)) {
                        deck[i][j] = -1;
                        Integer[] occ = new Integer[] {i,j};
                        occupied.add(occ);
                    } else {
                        for (int s = -8; s < 8; ++s ) {
                            if ((i == r + s && j == c + s) && (deck[i][j] != 1 && deck[i][j] != -1)) {
                                deck[i][j] = -1;
                                Integer[] occ = new Integer[] {i,j};
                                occupied.add(occ);
                            }
                        }
                        }
                    }
                }
        } deck[r][c] = 1;
        Integer[] place = new Integer[] {r, c};
        HashMap<Integer[], ArrayList<Integer[]>> move = new HashMap<>();
        move.put(place, occupied);
        return move;
    }

        /**
     * Ищем ряд, где стоит последняя королева
     * @param deck
     * @return
     */
    public static int lastQueenFinder(HashMap<Integer[], ArrayList<Integer[]>> lastMove) {
        for (Integer[] item : lastMove.keySet()) {
            int r = item[0];
            return r;
        }
        return -1;
    }


        /**
     * Отменяем ход и вносим клетку, где стояла королева, в список запретных полей
     * @param deck
     * @param lastMove последний ход
     * @param badPlace запретные места
     * @param r ряд
     */
    public static void cancelStep(int[][] deck, HashMap<Integer[], ArrayList<Integer[]>> lastMove, HashMap<Integer, ArrayList<Integer[]>> badPlace, int r) {
        ArrayList<Integer[]> occupied = new ArrayList<>();
        for (ArrayList<Integer[]> item: lastMove.values()) {
            occupied = item;
        }
        for (Integer[] integers : occupied) {
            deck[integers[0]][integers[1]] = 0;
        }
        addToBadPlace(badPlace, lastMove, r);
    }


            /**
     * Записываем нежелательные места в словарь: ряд — список нежелательных мест
     * @param badPlace
     * @param lastMove
     * @param r
     */
    public static void addToBadPlace(HashMap<Integer, ArrayList<Integer[]>> badPlace, HashMap<Integer[], ArrayList<Integer[]>> lastMove, int r) {
        
        Integer[] place = new Integer[2];
        for (Integer[] integers : lastMove.keySet()) {
            place = integers;
        }
        if (badPlace.containsKey(r)) {
            badPlace.get(r).add(place);
        } else {
            badPlace.put(r, new ArrayList<Integer[]>());
            badPlace.get(r).add(place);
        }

    }

    public static boolean queenFinder(int[][] deck, int r) {
        for (int j = 0; j < deck[r].length; ++j) {
            if (deck[r][j] == 1) return true;
        }
        return false;
    }

    public static String deckToString(int[][] deck) {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < deck.length; ++i) {
            for (int j = 0; j < deck[i].length; ++j) {
                strb.append(deck[i][j] + " ");
            }
            strb.append("\n");
        }
        String combination = strb.toString();
        return combination;
    }









}

