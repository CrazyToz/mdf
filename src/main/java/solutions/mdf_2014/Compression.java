package solutions.mdf_2014;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class Compression {

    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        System.out.println(line.chars().mapToObj(Character::toString).collect(Collector.of(Custom::new, Custom::accumulate, Custom::combine, Custom::finish)));

        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }


    static class Custom {

        StringBuilder result = new StringBuilder();
        StringBuilder buffer = new StringBuilder();

        void accumulate(String other) {
            if (buffer.length() > 2) {
                if (buffer.substring(1, buffer.length()).chars().mapToObj(Character::toString).allMatch(s -> s.equals(buffer.substring(0, 1))) && !other.equals(buffer.substring(buffer.length() - 1, buffer.length()))) {
                    result.append(buffer.length()).append(buffer.charAt(0));
                    buffer = new StringBuilder(other);
                } else if (!other.equals(buffer.substring(buffer.length() - 1, buffer.length()))) {
                    result.append(buffer);
                    buffer = new StringBuilder(other);
                } else {
                    buffer.append(other);
                }
            } else {
                buffer.append(other);
            }
        }

        Custom combine(Custom other) {
            return this;
        }

        String finish() {
            System.err.println(result);
            System.err.println(buffer);
            if (!buffer.isEmpty()) {
                if (buffer.length() > 2 && buffer.substring(1, buffer.length()).chars().mapToObj(Character::toString).allMatch(s -> s.equals(buffer.substring(0, 1)))) {
                    result.append(buffer.length()).append(buffer.charAt(0));
                } else {
                    result.append(buffer);
                }
            }
            return result.toString();
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

}
