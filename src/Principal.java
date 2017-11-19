/*
 * Universidade Federal de Santa Catarina - UFSC
 * Departamento de Informática e Estatística - INE
 * Programa de Pós-Graduação em Ciências da Computação - PROPG
 * Disciplinas: Projeto e Análise de Algoritmos
 * Prof Alexandre Gonçalves da Silva 
 *
 * Página 474 Thomas H. Cormen 3a Ed 
 *
 * Caminho mínimos de fonte única, Algoritmo de Bellman-Ford
 */

/**
 * @author Osmar de Oliveira Braz Junior
 */
import java.util.LinkedList;
import java.util.List;

public class Principal {

    //Vetor dos pais de um vértice
    static int[] pi;
    //Vetor das distâncias
    static int[] d;

    /**
     * Troca um número que representa a posição pela vértice do grafo.
     *
     * @param i Posição da letra
     * @return Uma String com a letra da posição i
     */
    public static String trocar(int i) {
        String letras = "stxyz";
        return letras.charAt(i) + "";
    }

    /**
     * Troca a letra pela posição na matriz de adjacência
     *
     * @param v Letra a ser troca pela posição
     * @return Um inteiro com a posição da letra no grafo
     */
    public static int destrocar(char v) {
        String letras = "stxyz";
        int pos = -1;
        for (int i = 0; i < letras.length(); i++) {
            if (letras.charAt(i) == v) {
                pos = i;
            }
        }
        return pos;
    }

    /**
     * Gera um vetor de arestas e pesos.
     *
     * @param G Matriz de adjacência do grafo
     * @return Um vetor de areastas e pesos.
     */
    public static List getMatrizVertices(int[][] G) {
        int n = G.length;
        List vertices = new LinkedList();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //Somente para o triângulo superior
                if (G[i][j] != 0) {
                    vertices.add(new int[]{i, j, G[i][j]});
                }
            }
        }
        return vertices;
    }

    /**
     * Inicializa as estimativas de caminhos mínimos e predecessores.
     *
     * @param G Grafo a ser inicializado
     * @param s Vértice inicial
     */
    public static void inicializaFonteUnica(int[][] G, int s) {
        for (int v = 0; v < G.length; v++) {
            d[v] = Integer.MAX_VALUE;
            pi[v] = -1;
            //h[v] = false;
        }
        d[s] = 0;
        pi[s] = 0;
    }

    /**
     * Teste se pode ser melhorado o caminho mínimo de u até v.
     *
     * @param u Vértice de origem.
     * @param v Vértice de destico
     * @param w Peso do caminho u até v.
     */
    private static void relaxamento(int u, int v, int w) {
        //if (!h[v] || d[v] > d[u] + w) {
        if (d[v] > d[u] + w) {
            d[v] = d[u] + w;
            pi[v] = u;
        }
    }

    /**
     * Mostra o caminho de s até v no grafo G
     *
     * @param G Matriz do grafo
     * @param s Origem no grafo
     * @param v Destino no grafo
     */
    public static void mostraCaminho(int[][] G, int s, int v) {
        if (v == s) {
            System.out.println("Partindo de:" + trocar(v));
        } else {
            if (pi[v] == -1) {
                System.out.println("Não existe caminho de " + trocar(s) + " a " + trocar(v));
            } else {
                mostraCaminho(G, s, pi[v]);
                System.out.println("Visitando:" + trocar(v) + " distância até " + trocar(s) + " = " + d[v]);
            }
        }
    }
    
    /**
     * Executa o algoritmo de Belmman-Ford para Caminhos Mínimos de fonte única.
     *
     * Encontra a distância mais curta de s para todos os outros vértices.
     * Retorna se existe ciclo negativo no grafo.
     *
     * Complexidade do algoritmo é O(E lg E)
     *
     * @param G Matriz de indicência da árvore
     * @return Vetor com a lista das arestas de menor custo
     */
    public static boolean algoritmoBellmanFord(int[][] G, int s) {

        //Quantidade de vértices do grafo G
        int V = G.length;

        //Instancia os vetores
        d = new int[V];
        pi = new int[V];

        //Converte a matriz em uma lista de arestas
        List arestas = getMatrizVertices(G);

        //Quantidade de arestas do grafo
        int E = arestas.size();

        //Realiza a inicialização das estimativas
        inicializaFonteUnica(G, s);

        //Percorre todos os vértice do grafo
        for (int i = 1; i <= V - 1; i++) {
            //Percorre todas as arestas do grafo
            for (int j = 0; j < E; j++) {
                int[] vertice = (int[]) arestas.get(j);
                int u = vertice[0];
                int v = vertice[1];
                int w = vertice[2];
                relaxamento(u, v, w);
            }
        }

        //Verifica se existe ciclo negativo no grafo
        for (int j = 0; j < E; j++) {
            int[] vertice = (int[]) arestas.get(j);
            int u = vertice[0];
            int v = vertice[1];
            int w = vertice[2];
            if (v > u + w) {
                return false;
            }
        }
        return true;
    }

    public static void main(String args[]) {

        //Grafo da página 465 Thomas H. Cormen 3 ed
        int G[][]
                = //s   t   x  y   z    
                {{0, 6, 0, 7, 0}, //s
                {0, 0, 5, 8, -4}, //t
                {0, -2, 0, 0, 0}, //x
                {0, 0, -3, 0, 9}, //y
                {2, 0, 7, 0, 0}};//z

        System.out.println("Caminho mínimos de fonte única, Algoritmo de Bellman-Ford");

        //Executa o algoritmo
        int s = destrocar('s');
        boolean retorno = algoritmoBellmanFord(G, s);

        if (retorno == false) {
            int v = destrocar('z');
            System.out.println("Caminho de " + trocar(s) + " até " + trocar(v) + ":");
            mostraCaminho(G, s, 4);

        } else {
            System.out.println("Existe ciclo negativo no grafo ");
        }
    }
}
