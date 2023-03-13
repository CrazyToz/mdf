package solutions.meilleurdevlaposte2021;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class DecodageTemporel {

    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String code1 = line.split(" ")[0];
        String chaine1 = line.split(" ")[1];
        line = sc.nextLine();
        String code2 = line.split(" ")[0];
        String chaine2 = line.split(" ")[1];
        String codeRes = sc.nextLine();

        Map<String, String> mappingsRes = null;

        for (List<Integer> item : Itertools.cartesianProduct(Stream.generate(() -> Arrays.asList(3, 4)).limit(10).collect(toList()))
        ) {
            if (item.stream().mapToInt(Integer::intValue).sum() == chaine1.length()) {
                Map<String, String> mappings = new HashMap<>();
                List<String> code1S = code1.chars().mapToObj(Character::toString).collect(toList());
                int currentIndex = 0;
                for (int i = 0; i < 10; i++) {
                    mappings.put(code1S.get(i), chaine1.substring(currentIndex, currentIndex + item.get(i)));
                    currentIndex+=item.get(i);
                }
                String calcChaine2 = code2.chars().mapToObj(Character::toString).map(key -> mappings.get(key)).collect(Collectors.joining());
                if (chaine2.equals(calcChaine2)) {
                    mappingsRes = Map.copyOf(mappings);
                    break;
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (String key :
                codeRes.chars().mapToObj(Character::toString).collect(toList())) {
            result.append(mappingsRes.get(key));
        }
        System.out.println(result);
    }

    static class Itertools {

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
                    .reduce(startProduct, Itertools::joinToCartesianProduct, noOp) //
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

        public static <T> List<List<T>> zip(List<List<T>> lists) {
            if (lists == null || lists.size() < 2) {
                return Collections.emptyList();
            }
            int maxItems = lists.stream().mapToInt(List::size).min().orElse(0);
            return IntStream.of(0, maxItems).mapToObj(i -> lists.stream().map(list -> list.get(i)).collect(toList())).collect(toList());
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

}
