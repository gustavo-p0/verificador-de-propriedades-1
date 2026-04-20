import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

class Graph {
    private final int vertices;
    private final int[][] matrix;

    public Graph(int n, Scanner sc){
        this.vertices = n;
        this.matrix = new int [vertices][vertices];
        fullFillMatrix(sc);    }

    private void fullFillMatrix(Scanner sc){
        for (int n = 0; n <= vertices - 1; n++) {
            matrix[n][n] = -1;
            for(int m = n + 1; m <= vertices - 1; m++){
                int weight = sc.nextInt();
                matrix[m][n] = matrix[n][m] = weight;
            }
        }
    }

    private boolean checkUpperTriangle(BiPredicate<Integer, Integer> condition){
        for (int i = 0; i < vertices; i++) {
            for (int j = i + 1; j < vertices; j++) {
                if(condition.test(i, j)) return false;
            }
        }
        return true;
    }

    private boolean checkRows(Predicate<Integer> conditionFailure){
        for (int n = 0; n < vertices; n++){
            if(conditionFailure.test(n)) return false;
        }   
        return true;
    }

    private int degree(int n){
        int count = 0;
        for(int i = 0; i < vertices; i++){
            if(i == n) continue;
            if(matrix[n][i] != -1) count++;
        }

        return count;
    }

    private int countVerticesWithDegree(int value){
        int count = 0;
        for(int i = 0; i < vertices; i++){
            if(degree(i) == value) count++;
        }
        return count;
    }

    public boolean isSimple(){
        return checkRows((n) ->  matrix[n][n] != -1);
    }

    public boolean isRegular(){
        return checkRows(n -> degree(n) != degree(0));
    }

    public boolean isComplete(){
        return isSimple() && checkUpperTriangle((n,m) -> matrix[n][m] == -1);
    }
    public boolean isNullish(){
        return checkUpperTriangle((n, m) -> matrix[n][m] != -1);
    }
    public boolean isLinear() {
        if (vertices == 1) return true;

        if (countVerticesWithDegree(1) != 2 || countVerticesWithDegree(2) != vertices - 2) {
            return false;
        }

        int startNode = -1;
        for (int i = 0; i < vertices; i++) {
            if (degree(i) == 1) {
                startNode = i;
                break; 
            }
        }

        if (startNode == -1) return false;

        boolean[] visited = new boolean[vertices];
        int current = startNode;
        int visitedCount = 0;

        while (current != -1) {
            visited[current] = true;
            visitedCount++;
            int next = -1;

            for (int i = 0; i < vertices; i++) {
                if (matrix[current][i] != -1 && !visited[i]) {
                    next = i;
                    break; 
                }
            }
            current = next;
        }

        return visitedCount == vertices;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            while(n != 0){
                Graph g = new Graph(n, sc);
                StringBuilder sb = new StringBuilder();
                sb.append(g.isSimple() ? 1 : 0).append(" ");
                sb.append(g.isRegular() ? 1 : 0).append(" ");
                sb.append(g.isComplete() ? 1 : 0).append(" ");
                sb.append(g.isNullish() ? 1 : 0).append(" ");
                sb.append(g.isLinear() ? 1 : 0);
                System.out.println(sb);
                n = sc.nextInt();
            }
        }
    }
}
