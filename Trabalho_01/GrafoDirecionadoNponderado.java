/*
| Materia: Teoria dos Grafos e Computabilidade
| Data de Entrega: 01/09/2021
| Integrantes: Ricardo Portilho de Andrade / Hugo Souza Almeida
| Matriculas: 705069 / 396702
*/

import java.util.*;

// Classe Menor parte do Grafo Direcionado Nao-Ponderado
class AtomGraph {
	// Variaveis
	public AtomGraph prox; // Vertice apontado
	public int element; // Valor do vertice

	// Construtores
	public AtomGraph(){
		this(0);
	}

	public AtomGraph(int e){
		element = e;
		prox = null;
	}
}

// Classe Grafo Direcionado Nao-Ponderado Completo
class Graph{
	// Variaveis
	public AtomGraph[] objectGraph;
	public int vert;
	public int arest;

	// Construtores
	public Graph(int v){
		this.objectGraph = new AtomGraph[v];
		for(int i = 0; i < v; i++){
			objectGraph[i] = new AtomGraph(i+1);
		}
		this.vert = v;
		this.arest = 0;
	}

	// Verificar se um vertice aponta para outro
	public boolean pesquisarVert(AtomGraph a, int valor){
		boolean result = false;
		for(AtomGraph j = a.prox; j != null && !result; j = j.prox)
			if(j.element == valor)
				result = true;
		return(result);
	}

	// Colocar uma conexao entre dois vertices
	public void inserirArests(AtomGraph a, int valor){
		AtomGraph tmp = new AtomGraph(valor);
		boolean inserido = true;
		for(AtomGraph i = a; inserido; i = i.prox){
			if(i.prox == null){
				i.prox = tmp;
				arest++;
				inserido = false;
			}
		}
	}

	// Verificar para quantos vertices um vertice aponta
	public int numConexoes(AtomGraph a){
		int result = 0;
		for(AtomGraph i = a.prox; i != null; i = i.prox)
			++result;
		return(result);
	}

	// Mostrar um vertice e suas conexoes
	public void printVert(AtomGraph a){
		System.out.print(a.element + " -> ");
		if(a.prox != null){
			for(AtomGraph j = a.prox; j != null; j = j.prox){
				System.out.print(j.element);
				if(j.prox != null)
					System.out.print("/");
				else
					System.out.println(";");
			}
		} else{
			System.out.println("null;");
		}
	}

	// Mostrar um grafo por meio das conexoes de cada vertice
	public void printGraph(){
		for(int i = 0; i < this.vert; i++){
			System.out.print("\t" + objectGraph[i].element + " -> ");
			if(objectGraph[i].prox != null){
				for(AtomGraph j = objectGraph[i].prox; j != null; j = j.prox){
					System.out.print(j.element);
					if(j.prox != null)
						System.out.print("/");
					else
						System.out.println(";");
				}
			} else{
				System.out.println("null;");
			}
		}
	}

	// Mostrar uma matrix que representa as ligacoes
	public void printGraphMatrix(){
		int[][] matrix = new int[this.vert][this.vert];
		for(int i = 0; i < this.vert; i++)
			for(int j = 0; j < this.vert; j++)
				matrix[i][j] = 0;

		for(int i = 0; i < this.vert; i++){
			for(AtomGraph j = objectGraph[i].prox; j != null; j = j.prox){
				matrix[objectGraph[i].element-1][j.element-1] = 1;
			}
		}

		System.out.println("");
		for(int i = 0; i < this.vert; i++){
			System.out.print("\t| ");
			for(int j = 0; j < this.vert; j++){
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("|");
		}
		System.out.println("");
	}

	// Mostrar uma matrix de incidencia
	public void printGraphMatrixInc(){
		AtomGraph a = new AtomGraph(0);
		int[][] matrix = new int[this.vert][this.arest];
		int pos = 0;
		for(int i = 0; i < this.vert; i++)
			for(int j = 0; j < this.arest; j++)
				matrix[i][j] = 0;

		for(int i = 0; i < this.arest; i++){
			if(a == null){
				++pos;
				a = objectGraph[pos].prox;
			}else if(a.element == 0)
				a = objectGraph[pos].prox;

			if(a != null && pos < this.vert && objectGraph[pos] != null){
				if(objectGraph[pos].element-1 != a.element-1){
					matrix[objectGraph[pos].element-1][i] = 1;
					matrix[a.element-1][i] = -1;
				} else
					matrix[a.element-1][i] = -2;
				a = a.prox;
			}
		}

		System.out.println("");
		for(int i = 0; i < this.vert; i++){
			System.out.print("\t| ");
			for(int j = 0; j < this.arest; j++){
				if(matrix[i][j] == -1)
					System.out.print(matrix[i][j] + " ");
				else if(matrix[i][j] == -2)
					System.out.print(" * ");
				else
					System.out.print(" " + matrix[i][j] + " ");
			}
			System.out.println("|");
		}
		System.out.println("");
	}
}

// Classe Principal
class GrafoDirecionadoNponderado {
	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in); // Leitor de entrada
		int valor = -1;
		System.out.print("\n\tConstruir um Grafo\n" +
		                 "\nColoque a quatidade de vertices: ");
		Graph g = new Graph(leitor.nextInt());
		for(int i = 0; i < g.vert; i++){
			System.out.println("\n\tColoque as conexoes do vertice ("
			                   + g.objectGraph[i].element + ")\n" +
								 "OBS.: Para finalizar a insercao coloque o valor 0 (zero)");
			System.out.print("---------------------------------------------------\n" +
			                 "\tEstado inicial do vertice: ");
			g.printVert(g.objectGraph[i]);
			System.out.print("---------------------------------------------------\n");
			while(valor != 0){
				System.out.print("Coloque o valor do vertice: ");
				valor = leitor.nextInt();
				if(valor > 0 && valor <= g.vert)
					g.inserirArests(g.objectGraph[i], valor);
			}
			valor = -1;
		}
		System.out.println("---------------------------------------------------\n" +
		                   "Ponteiros:\n");
		g.printGraph();
		System.out.println("\nMatriz (1):");
		g.printGraphMatrix();
		System.out.println("\nMatriz (2):");
		g.printGraphMatrixInc();
		System.out.print("---------------------------------------------------\n");
	}
}
