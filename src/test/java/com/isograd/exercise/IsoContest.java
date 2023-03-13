package com.isograd.exercise;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.isograd.exercise.IsoContest.ShortCut.*;
import static com.isograd.exercise.IsoContest.ShortCut.toBool;
import static com.isograd.exercise.IsoContest.Utils.*;
import static com.isograd.exercise.IsoContest.Regex.*;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class IsoContest {

    public static void main(String[] argv) throws Exception {
        debug(sc -> {
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

                if (aV > 0) {
                    for (int i = 0; i < aV; i++) {
                        stock.add(new Arrivage(Type.V));
                    }
                }
                if (aR > 0) {
                    for (int i = 0; i < aR; i++) {
                        stock.add(new Arrivage(Type.R));
                    }
                }
                if (aJ > 0) {
                    for (int i = 0; i < aJ; i++) {
                        stock.add(new Arrivage(Type.J));
                    }
                }
                if (aO > 0) {
                    for (int i = 0; i < aO; i++) {
                        stock.add(new Arrivage(Type.O));
                    }
                }
                /* Lisez les données et effectuez votre traitement */

            }

            System.out.println(res);

        });
    }

    record Arrivage(Type type, int valide) {
        Arrivage(Type type) {
            this(type, 1);
        }
    }

    static enum Type {V, R, J, O}

    private static void debug(Consumer<Scanner> consumer) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream("src/test/resources/sample.zip"));
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (!entry.isDirectory() && entry.getName().contains("/input")) {
                String nomFichier = entry.getName().substring(entry.getName().indexOf("/input"), entry.getName().length());
                System.out.println("### Début exécution du jdd " + nomFichier);
                Scanner scanner = new Scanner(zipInputStream);
                consumer.accept(scanner);
                System.out.println("### Fin exécution du jdd " + nomFichier);
            } else if (!entry.isDirectory() && entry.getName().contains("/output")) {
                String nomFichier = entry.getName().substring(entry.getName().indexOf("/output"), entry.getName().length());
                System.out.println("### Résultat du fichier " + nomFichier);
                Scanner scanner = new Scanner(zipInputStream);
                StringBuilder resultat = new StringBuilder();
                while (scanner.hasNextLine()) {
                    resultat.append(scanner.nextLine()).append(System.lineSeparator());
                }
                System.out.println(resultat);
            }
        }
    }

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
