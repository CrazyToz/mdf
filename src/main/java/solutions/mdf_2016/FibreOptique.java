package solutions.mdf_2016;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static solutions.mdf_2016.FibreOptique.ShortCut.toInt;

public class FibreOptique {

    public static void main(String[] argv) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line;
        int nbOrdi = toInt(sc.nextLine());
        List<Prim.Vertex> positions = new ArrayList<>();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            positions.add(new Prim.Vertex(line));
            /* Lisez les données et effectuez votre traitement */
        }

//        List<Dist> dists = IntStream.range(0, positions.size()).boxed().flatMap(i -> positions.subList(i + 1, positions.size()).stream().map(pos -> new Dist(positions.get(i), pos, Math.hypot(positions.get(i).x - pos.x, positions.get(i).y - pos.y)))).sorted(Comparator.comparing(Dist::dist)).collect(toList());


        IntStream.range(0, positions.size()).boxed().forEach(i -> {
            Prim.Vertex current = positions.get(i);
            int x1 = toInt(current.label, 0), y1 = toInt(current.label, 1);
            IntStream.range(0, positions.size()).forEach(j -> {
                if (i != j) {
                    Prim.Vertex next = positions.get(j);
                    int x2 = toInt(next.label, 0), y2 = toInt(next.label, 1);
                    current.addEdge(next, new Prim.Edge(Math.hypot(x1 - x2, y1 - y2)));
                }
            });
        });

        new Prim(positions).run();
        System.out.println(positions.stream().flatMap(v -> v.edges.entrySet().stream().map(Map.Entry::getValue)).filter(Prim.Edge::isIncluded).mapToDouble(Prim.Edge::getWeight).sum());

// https://fr.wikipedia.org/wiki/Algorithme_de_Prim
        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }

    record Pos(int x, int y) {
    }

    record Dist(Pos p1, Pos p2, double dist) {
    }

    static class Regex {

        static final Pattern NO_LETTERS = Pattern.compile("[^a-zA-Z]");

    }

    static class ShortCut {

        static Integer toInt(String line) {
            return Integer.valueOf(line);
        }

        static Integer toInt(String line, int pos) {
            return toInt(line, " ", pos);
        }

        static Integer toInt(String line, String separateur, int pos) {
            return Integer.valueOf(line.split(separateur)[pos]);
        }

        static List<String> toStrList(String line, String separateur) {
            return Arrays.asList(line.split(separateur));
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

    public static class Prim {
        private List<Vertex> graph;

        public Prim(List<Vertex> graph) {
            this.graph = graph;
        }

        public void run() {
            if (graph.size() > 0) {
                graph.get(0).setVisited(true);
            }
            while (isDisconnected()) {
                Edge nextMinimum = new Edge(Integer.MAX_VALUE);
                Vertex nextVertex = graph.get(0);
                for (Vertex vertex : graph) {
                    if (vertex.isVisited()) {
                        Pair candidate = vertex.nextMinimum();
                        if (candidate.edge.getWeight() < nextMinimum.getWeight()) {
                            nextMinimum = candidate.edge;
                            nextVertex = candidate.vertex;
                        }
                    }
                }
                nextMinimum.setIncluded(true);
                nextVertex.setVisited(true);
            }
        }

        private boolean isDisconnected() {
            for (Vertex vertex : graph) {
                if (!vertex.isVisited()) {
                    return true;
                }
            }
            return false;
        }

        public String originalGraphToString() {
            StringBuilder sb = new StringBuilder();
            for (Vertex vertex : graph) {
                sb.append(vertex.originalToString());
            }
            return sb.toString();
        }

        public void resetPrintHistory() {
            for (Vertex vertex : graph) {
                Iterator<Map.Entry<Vertex, Edge>> it = vertex.getEdges().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Vertex, Edge> pair = it.next();
                    pair.getValue().setPrinted(false);
                }
            }
        }

        public String minimumSpanningTreeToString() {
            StringBuilder sb = new StringBuilder();
            for (Vertex vertex : graph) {
                sb.append(vertex.includedToString());
            }
            return sb.toString();
        }


        public static class Vertex {

            private String label = null;
            private Map<Vertex, Edge> edges = new HashMap<>();
            private boolean isVisited = false;

            public Vertex(String label) {
                this.label = label;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public Map<Vertex, Edge> getEdges() {
                return edges;
            }

            public void addEdge(Vertex vertex, Edge edge) {
                if (this.edges.containsKey(vertex)) {
                    if (edge.getWeight() < this.edges.get(vertex).getWeight()) {
                        this.edges.replace(vertex, edge);
                    }
                } else {
                    this.edges.put(vertex, edge);
                }
            }

            public boolean isVisited() {
                return isVisited;
            }

            public void setVisited(boolean visited) {
                isVisited = visited;
            }

            public Pair nextMinimum() {
                Edge nextMinimum = new Edge(Integer.MAX_VALUE);
                Vertex nextVertex = this;
                Iterator<Map.Entry<Vertex, Edge>> it = edges.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Vertex, Edge> pair = it.next();
                    if (!pair.getKey().isVisited()) {
                        if (!pair.getValue().isIncluded()) {
                            if (pair.getValue().getWeight() < nextMinimum.getWeight()) {
                                nextMinimum = pair.getValue();
                                nextVertex = pair.getKey();
                            }
                        }
                    }
                }
                return new Pair(nextVertex, nextMinimum);
            }

            public String originalToString() {
                StringBuilder sb = new StringBuilder();
                Iterator<Map.Entry<Vertex, Edge>> it = edges.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Vertex, Edge> pair = it.next();
                    if (!pair.getValue().isPrinted()) {
                        sb.append(getLabel());
                        sb.append(" --- ");
                        sb.append(pair.getValue().getWeight());
                        sb.append(" --- ");
                        sb.append(pair.getKey().getLabel());
                        sb.append("\n");
                        pair.getValue().setPrinted(true);
                    }
                }
                return sb.toString();
            }

            public String includedToString() {
                StringBuilder sb = new StringBuilder();
                if (isVisited()) {
                    Iterator<Map.Entry<Vertex, Edge>> it = edges.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Vertex, Edge> pair = it.next();
                        if (pair.getValue().isIncluded()) {
                            if (!pair.getValue().isPrinted()) {
                                sb.append(getLabel());
                                sb.append(" --- ");
                                sb.append(pair.getValue().getWeight());
                                sb.append(" --- ");
                                sb.append(pair.getKey().getLabel());
                                sb.append("\n");
                                pair.getValue().setPrinted(true);
                            }
                        }
                    }
                }
                return sb.toString();
            }

        }

        public static class Edge {
            private double weight;
            private boolean isIncluded = false;
            private boolean isPrinted = false;

            public Edge(double weight) {
                this.weight = weight;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public boolean isIncluded() {
                return isIncluded;
            }

            public void setIncluded(boolean included) {
                isIncluded = included;
            }

            public boolean isPrinted() {
                return isPrinted;
            }

            public void setPrinted(boolean printed) {
                isPrinted = printed;
            }

        }

        private record Pair(Vertex vertex, Edge edge) {
        }

    }

}
