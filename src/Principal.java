/*
 * Universidade Federal de Santa Catarina - UFSC
 * Departamento de Informática e Estatística - INE
 * Programa de Pós-Graduação em Ciências da Computação - PROPG
 * Disciplina: Projeto e Análise de Algoritmos
 * Prof Alexandre Gonçalves da Silva 
 *
 * Baseado nos slides 85 da aula do dia 27/10/2017 
 *
 * Página 474 Thomas H. Cormen 3a Ed 
 *
 * Caminho mínimos de fonte única, Algoritmo de Bellman-Ford
 *
 * O algoritmo de Bellman-Ford recebe um grafo orientado ponderado(G,w) 
 * (possivelmente com arestas de peso negativo) e um v vértice origem s de G.
 * Tempo maior de execução que Dijkstra. Menor caminho de um vértice para
 * todos os outros, pesos negativos são permitidos.
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
        if ((i >=0) && (i<=letras.length())) {
            return letras.charAt(i) + "";
        } else {
            return "-";
        }
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
     * @return Um vetor de arestas e pesos.
     */
    public static List getMatrizVertices(int[][] G) {
        int n = G.length;
        List vertices = new LinkedList();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {                
                if (G[i][j] != 0) {
                    //Cria um vetor de 3 elementos para conter                     
                    //[0]=u(origem), [1]=v(destino), [2]=w(peso)
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
        //Quantidade de vértices do grafo G
        int V = G.length;
        //Instancia os vetores
        d = new int[V];
        pi = new int[V];
        for (int v = 0; v < G.length; v++) {
            d[v] = Integer.MAX_VALUE;
            pi[v] = -1;            
        }
        d[s] = 0;
        pi[s] = 0;
    }

    /**
     * Teste se pode ser melhorado o caminho mínimo de u até v.
     *
     * @param u Vértice de origem.
     * @param v Vértice de destino
     * @param w Peso do caminho u até v.
     */
    private static void relaxamento(int u, int v, int w) {        
        if (d[v] > d[u] + w) {
            d[v] = d[u] + w;
            pi[v] = u;
        }
    }

    /**
     * Mostra o caminho de s até v no grafo G
     *
     * @param G Matriz do grafo
     * @param s Vértice de origem no grafo
     * @param v Vértice de destino no grafo
     */
    public static void mostrarCaminho(int[][] G, int s, int v) {
        if (v != s) {            
            if (pi[v] == -1) {
                System.out.println("Não existe caminho de " + trocar(s) + " a " + trocar(v));
            } else {                
                mostrarCaminho(G, s, pi[v]);
                System.out.println(trocar(pi[v]) + " -> " + trocar(v) + " custo: " + d[v]);                
            }
        }
    }          
    
    /**
     * Executa o algoritmo de Belmman-Ford para Caminhos Mínimos de fonte única.
     *
     * Encontra a distância mais curta de s para todos os outros vértices.
     * Retorna se existe ciclo negativo no grafo.
     *
     * Complexidade do algoritmo é O(VE)
     *
     * @param G Matriz de indicência da árvore
     * @return Vetor com a lista das arestas de menor custo
     */
    public static boolean algoritmoBellmanFord(int[][] G, int s) {

        //Quantidade de vértices do grafo G
        int V = G.length;

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
             = //s  t  x  y  z    
               {{0, 6, 0, 7, 0}, //s
                {0, 0, 5, 8,-4}, //t
                {0,-2, 0, 0, 0}, //x
                {0, 0,-3, 0, 9}, //y
                {2, 0, 7, 0, 0}};//z

        System.out.println(">>> Caminho mínimos de fonte única, Algoritmo de Bellman-Ford <<<");

        //Executa o algoritmo
        int s = destrocar('s');
        boolean retorno = algoritmoBellmanFord(G, s);

        if (retorno == false) {
            int v = destrocar('z');
            System.out.println("Caminho mínimo de " + trocar(s) + " -> " + trocar(v) + ":");
            mostrarCaminho(G, s, 4); 
        } else {
            System.out.println("Existe ciclo negativo no grafo ");
        }
    }
}
