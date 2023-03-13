package solutions.mdf_2018_15_30;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static solutions.mdf_2018_15_30.BouquetJour.ShortCut.toInt;

public class BouquetJour {

    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int nbV = toInt(line, 0), nbR = toInt(line, 1), nbJ = toInt(line, 2), nbO = toInt(line, 3);
        int res = 0;
        List<Arrivage> stock = new ArrayList<>();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            int aV = (int) (toInt(line, 0) + stock.stream().filter(a -> a.type == Type.V).count());
            int aR = (int) (toInt(line, 1) + stock.stream().filter(a -> a.type == Type.R).count());
            int aJ = (int) (toInt(line, 2) + stock.stream().filter(a -> a.type == Type.J).count());
            int aO = (int) (toInt(line, 3) + stock.stream().filter(a -> a.type == Type.O).count());

            boolean tmp = true;
            while (tmp) {
                if (aV - nbV >= 0 && aR - nbR >= 0 && aJ - nbJ >= 0 && aO - nbO >= 0) {
                    res++;
                    aV -= nbV;
                    aJ -= nbJ;
                    aR -= nbR;
                    aO -= nbO;
                } else {
                    tmp = false;
                }
            }

            stock = new ArrayList<>();

            int vResteV = Math.min(toInt(line, 0), aV);
            if (vResteV > 0) {
                for (int i = 0; i < vResteV; i++) {
                    stock.add(new Arrivage(Type.V));
                }
            }
            int vResteR = Math.min(toInt(line, 1), aR);
            if (vResteR > 0) {
                for (int i = 0; i < vResteR; i++) {
                    stock.add(new Arrivage(Type.R));
                }
            }
            int vResteJ = Math.min(toInt(line, 2), aJ);
            if (vResteJ > 0) {
                for (int i = 0; i < vResteJ; i++) {
                    stock.add(new Arrivage(Type.J));
                }
            }
            int vResteO = Math.min(toInt(line, 3), aO);
            if (vResteO > 0) {
                for (int i = 0; i < vResteO; i++) {
                    stock.add(new Arrivage(Type.O));
                }
            }
            /* Lisez les données et effectuez votre traitement */

        }

        System.out.println(res);

        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }

    record Arrivage(Type type, int valide) {
        Arrivage(Type type) {
            this(type, 1);
        }
    }

    static enum Type {V, R, J, O}

    static class Regex {

        static final Pattern NO_LETTERS = Pattern.compile("[^a-zA-Z]");

    }

    static class ShortCut {

        static Integer toInt(String line) {
            return Integer.valueOf(line);
        }

        static Integer toInt(String line, int pos) {
            return toInt(line, pos, " ");
        }

        static Integer toInt(String line, int pos, String separateur) {
            return Integer.valueOf(line.split(separateur)[pos]);
        }

        static List<String> toStrList(String line, String separateur) {
            return Arrays.asList(line.split(separateur));
        }

        static String toStr(String line, int pos, String separateur) {
            return line.split(separateur)[pos];
        }

        static String toStr(String line, int pos) {
            return line.split(" ")[pos];
        }

        static Double toDouble(String line) {
            return Double.valueOf(line);
        }

        static Double toDouble(String line, int pos) {
            return toDouble(line, pos, " ");
        }

        static Double toDouble(String line, int pos, String separateur) {
            return Double.valueOf(line.split(separateur)[pos]);
        }

        static Boolean toBool(String line, String trueRep) {
            return line.equals(trueRep);
        }

        static Boolean toBool(String line, String trueRep, int pos) {
            return toBool(line, trueRep, pos, " ");
        }

        static Boolean toBool(String line, String trueRep, int pos, String separateur) {
            return line.split(separateur)[pos].equalsIgnoreCase(trueRep);
        }
    }

    static class Utils {

        /**
         * Compute the cartesian product for n lists.
         * The algorithm employs that A x B x C = (A x B) x C
         *
         * @param listsToJoin [a, b], [x, y], [1, 2]
         * @return [a, x, 1], [a, x, 2], [a, y, 1], [a, y, 2], [b, x, 1], [b, x, 2], [b, y, 1], [b, y, 2]
         */
        public static <T> List<List<T>> cartesianProduct(List<List<T>> listsToJoin) {
            if (listsToJoin.isEmpty()) {
                return new ArrayList<>();
            }

            listsToJoin = new ArrayList<>(listsToJoin);
            List<T> firstListToJoin = listsToJoin.remove(0);
            Stream<List<T>> startProduct = joinLists(new ArrayList<T>(), firstListToJoin);

            BinaryOperator<Stream<List<T>>> noOp = (a, b) -> null;

            return listsToJoin.stream() //
                    .filter(Objects::nonNull) //
                    .filter(list -> !list.isEmpty()) //
                    .reduce(startProduct, Utils::joinToCartesianProduct, noOp) //
                    .collect(toList());
        }

        /**
         * @param products [a, b], [x, y]
         * @param toJoin   [1, 2]
         * @return [a, b, 1], [a, b, 2], [x, y, 1], [x, y, 2]
         */
        private static <T> Stream<List<T>> joinToCartesianProduct(Stream<List<T>> products, List<T> toJoin) {
            return products.flatMap(product -> joinLists(product, toJoin));
        }

        /**
         * @param list   [a, b]
         * @param toJoin [1, 2]
         * @return [a, b, 1], [a, b, 2]
         */
        private static <T> Stream<List<T>> joinLists(List<T> list, List<T> toJoin) {
            return toJoin.stream().map(element -> appendElementToList(list, element));
        }

        /**
         * @param list    [a, b]
         * @param element 1
         * @return [a, b, 1]
         */
        private static <T> List<T> appendElementToList(List<T> list, T element) {
            int capacity = list.size() + 1;
            ArrayList<T> newList = new ArrayList<>(capacity);
            newList.addAll(list);
            newList.add(element);
            return unmodifiableList(newList);
        }

        public static <T> void permute(
                int n, T[] elements, Consumer<T[]> consumer) {

            if (n == 1) {
                consumer.accept(elements);
            } else {
                for (int i = 0; i < n - 1; i++) {
                    permute(n - 1, elements, consumer);
                    if (n % 2 == 0) {
                        swap(elements, i, n - 1);
                    } else {
                        swap(elements, 0, n - 1);
                    }
                }
                permute(n - 1, elements, consumer);
            }
        }

        private static <T> void swap(T[] elements, int a, int b) {
            T tmp = elements[a];
            elements[a] = elements[b];
            elements[b] = tmp;
        }

    }

    static class Algo {

        /**
         * The Damerau-Levenshtein Algorithm is an extension to the Levenshtein
         * Algorithm which solves the edit distance problem between a source string and
         * a target string with the following operations:
         *
         * <ul>
         * <li>Character Insertion</li>
         * <li>Character Deletion</li>
         * <li>Character Replacement</li>
         * <li>Adjacent Character Swap</li>
         * </ul>
         * <p>
         * Note that the adjacent character swap operation is an edit that may be
         * applied when two adjacent characters in the source string match two adjacent
         * characters in the target string, but in reverse order, rather than a general
         * allowance for adjacent character swaps.
         * <p>
         * <p>
         * This implementation allows the client to specify the costs of the various
         * edit operations with the restriction that the cost of two swap operations
         * must not be less than the cost of a delete operation followed by an insert
         * operation. This restriction is required to preclude two swaps involving the
         * same character being required for optimality which, in turn, enables a fast
         * dynamic programming solution.
         * <p>
         * <p>
         * The running time of the Damerau-Levenshtein algorithm is O(n*m) where n is
         * the length of the source string and m is the length of the target string.
         * This implementation consumes O(n*m) space.
         *
         * @author Kevin L. Stern
         */
        public static class DamerauLevenshteinAlgorithm {
            private final int deleteCost, insertCost, replaceCost, swapCost;

            /**
             * Constructor.
             *
             * @param deleteCost  the cost of deleting a character.
             * @param insertCost  the cost of inserting a character.
             * @param replaceCost the cost of replacing a character.
             * @param swapCost    the cost of swapping two adjacent characters.
             */
            public DamerauLevenshteinAlgorithm(int deleteCost, int insertCost,
                                               int replaceCost, int swapCost) {
                /*
                 * Required to facilitate the premise to the algorithm that two swaps of the
                 * same character are never required for optimality.
                 */
                if (2 * swapCost < insertCost + deleteCost) {
                    throw new IllegalArgumentException("Unsupported cost assignment");
                }
                this.deleteCost = deleteCost;
                this.insertCost = insertCost;
                this.replaceCost = replaceCost;
                this.swapCost = swapCost;
            }

            /**
             * Compute the Damerau-Levenshtein distance between the specified source
             * string and the specified target string.
             */
            public int execute(String source, String target) {
                if (source.length() == 0) {
                    return target.length() * insertCost;
                }
                if (target.length() == 0) {
                    return source.length() * deleteCost;
                }
                int[][] table = new int[source.length()][target.length()];
                Map<Character, Integer> sourceIndexByCharacter = new HashMap<Character, Integer>();
                if (source.charAt(0) != target.charAt(0)) {
                    table[0][0] = Math.min(replaceCost, deleteCost + insertCost);
                }
                sourceIndexByCharacter.put(source.charAt(0), 0);
                for (int i = 1; i < source.length(); i++) {
                    int deleteDistance = table[i - 1][0] + deleteCost;
                    int insertDistance = (i + 1) * deleteCost + insertCost;
                    int matchDistance = i * deleteCost
                            + (source.charAt(i) == target.charAt(0) ? 0 : replaceCost);
                    table[i][0] = Math.min(Math.min(deleteDistance, insertDistance),
                            matchDistance);
                }
                for (int j = 1; j < target.length(); j++) {
                    int deleteDistance = (j + 1) * insertCost + deleteCost;
                    int insertDistance = table[0][j - 1] + insertCost;
                    int matchDistance = j * insertCost
                            + (source.charAt(0) == target.charAt(j) ? 0 : replaceCost);
                    table[0][j] = Math.min(Math.min(deleteDistance, insertDistance),
                            matchDistance);
                }
                for (int i = 1; i < source.length(); i++) {
                    int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0
                            : -1;
                    for (int j = 1; j < target.length(); j++) {
                        Integer candidateSwapIndex = sourceIndexByCharacter.get(target
                                .charAt(j));
                        int jSwap = maxSourceLetterMatchIndex;
                        int deleteDistance = table[i - 1][j] + deleteCost;
                        int insertDistance = table[i][j - 1] + insertCost;
                        int matchDistance = table[i - 1][j - 1];
                        if (source.charAt(i) != target.charAt(j)) {
                            matchDistance += replaceCost;
                        } else {
                            maxSourceLetterMatchIndex = j;
                        }
                        int swapDistance;
                        if (candidateSwapIndex != null && jSwap != -1) {
                            int iSwap = candidateSwapIndex;
                            int preSwapCost;
                            if (iSwap == 0 && jSwap == 0) {
                                preSwapCost = 0;
                            } else {
                                preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
                            }
                            swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost
                                    + (j - jSwap - 1) * insertCost + swapCost;
                        } else {
                            swapDistance = Integer.MAX_VALUE;
                        }
                        table[i][j] = Math.min(Math.min(Math
                                .min(deleteDistance, insertDistance), matchDistance), swapDistance);
                    }
                    sourceIndexByCharacter.put(source.charAt(i), i);
                }
                return table[source.length() - 1][target.length() - 1];
            }
        }
    }
}
