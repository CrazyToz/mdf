/*******
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package solutions.testblanc;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Macaron {
    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        Integer l1 = Integer.valueOf(line.split(" ")[0]);
        Integer l2 = Integer.valueOf(line.split(" ")[1]);
        Integer l3 = Integer.valueOf(line.split(" ")[2]);
        line = sc.nextLine();
        Integer c1 = Integer.valueOf(line.split(" ")[0]);
        Integer c2 = Integer.valueOf(line.split(" ")[1]);
        Integer c3 = Integer.valueOf(line.split(" ")[2]);


        Result res = new Result();

        Itertools.permute(9, IntStream.rangeClosed(1, 9).mapToObj(Integer::valueOf).toArray(Integer[]::new), elements -> {
            if (Arrays.stream(elements).distinct().count() == 9
                    && c1 == (elements[0] + elements[3] + elements[6])
                    && c2 == (elements[1] + elements[4] + elements[7])
                    && c3 == (elements[2] + elements[5] + elements[8])
                    && l1 == (elements[0] + elements[1] + elements[2])
                    && l2 == (elements[3] + elements[4] + elements[5])
                    && l3 == (elements[6] + elements[7] + elements[8])) {
                res.set(elements);
            }
        });

        res.print();

    }

    static class Result {

        Integer[] result = null;

        void set(Integer[] result) {
            if (this.result == null) {
                this.result = Arrays.copyOf(result, 9);
            }
        }

        void print() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                builder.append(result[i]);
                if ((i + 1) % 3 == 0) {
                    System.out.println(builder);
                    builder = new StringBuilder();
                }
            }
        }

    }

    static class Itertools {

//        public static <T> List<List<T>> product(List<T>... list) {
//            class CustomCollector {
//                private List<List<T>> accumulator = new ArrayList<>();
//
//                void accumulate(List<T> list) {
//                    if (accumulator.isEmpty()) {
//                        list.stream().forEach(t -> {
//                            ArrayList<T> accList = new ArrayList<>();
//                            accList.add(t);
//                            accumulator.add(accList);
//                        });
//                    } else {
//                        List<List<T>> tmp = new ArrayList<>();
//                        while (!accumulator.isEmpty()) {
//                            List<T> subList = accumulator.remove(0);
//                            tmp.addAll(list.stream().map(newItem -> {
//                                List<T> newList = new ArrayList<>(subList);
//                                newList.add(newItem);
//                                return newList;
//                            }).collect(Collectors.toList()));
//                        }
//                        accumulator.addAll(tmp);
//                    }
//                }
//
//                CustomCollector combine(CustomCollector other) {
//                    return this;
//                }
//
//                List<List<T>> finish() {
//                    return accumulator;
//                }
//            }
//            if (list == null || list.length < 2) {
//                return Collections.emptyList();
//            }
//            return Stream.of(list).collect(Collector.of(CustomCollector::new, CustomCollector::accumulate, CustomCollector::combine, CustomCollector::finish));
//        }

        public static <T> List<List<T>> zip(List<List<T>> lists) {
            if (lists == null || lists.size() < 2) {
                return Collections.emptyList();
            }
            int maxItems = lists.stream().mapToInt(List::size).min().orElse(0);
            return IntStream.of(0, maxItems).mapToObj(i -> lists.stream().map(list -> list.get(i)).collect(Collectors.toList())).collect(Collectors.toList());
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