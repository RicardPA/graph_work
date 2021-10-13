/*
| Materia: Teoria dos Grafos e Computabilidade 
| Data de Entrega: 15/10/2021
| Integrantes: Ricardo Portilho de Andrade / Hugo Souza Almeida
| Matriculas: 705069 / 396702
*/

import java.util.*;

// Classe Principal
class Grafo {
	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in); // Leitor de entrada
		int valor = -1;
		int quantVert = 0;
		long inicio;
		System.out.print("\n\tConstruir um Grafo\n\n   Coloque a quatidade de vertices\n" + 
			               "(Obs.: A quantidade nao pode ser maior que 9)\n");
		while(quantVert > 9 || quantVert <= 1) {
			System.out.print("Quantidade: ");
			quantVert = leitor.nextInt();
		}
		Graph g = new Graph(quantVert);
		for(int i = 0; i < g.vert; i++) {
			System.out.println("\n\tColoque as conexoes do vertice (" + g.objectGraph[i].element + ")\n" +
				"OBS.: Para finalizar a insercao coloque o valor 0 (zero)");
			System.out.print("---------------------------------------------------\n\tEstado inicial do vertice: ");
			g.printVert(g.objectGraph[i]);
			System.out.print("---------------------------------------------------\n");
			while(valor != 0) {
				System.out.print("Coloque o valor do vertice: ");
				valor = leitor.nextInt();
				if(valor > 0 && valor <= g.vert)
					g.inserirArests(g.objectGraph[i], valor);
			}
			valor = -1;
		}
		System.out.print("\n\t------------------------ /Grafo criado/ ------------------------\n");
		g.printGraph();
		System.out.println("\t--------------- /Ciclos existentes (Permutacao)/ ---------------");
		inicio = System.currentTimeMillis();
		g.testPermutacaoCiclos();
		System.out.println("\tTempo de resposta(milissegundos): " + (System.currentTimeMillis() - inicio));
		System.out.println("\t-------------- /Ciclos existentes (Caminhamento)/ --------------");
		inicio = System.currentTimeMillis();
		System.out.println("\tTempo de resposta(milissegundos): " + (System.currentTimeMillis() - inicio));
	}
}

// Classe Menor parte do Grafo Nao-direcionado Nao-Ponderado
class AtomGraph {
	// Variaveis 
	public AtomGraph prox; // Vertice apontado
	public AtomGraph posInVertor; // Posição do vertice no vetor
	public boolean acessado; // verificar se vertice foi acessado
	public int element; // Valor do vertice

	// Construtores
	public AtomGraph() {
		this(0);
	}

	public AtomGraph(int e) {
		this.element = e;
		this.acessado = false;
		this.prox = null;
		this.posInVertor = null;
	}
}

// Classe Grafo Nao-direcionado Nao-Ponderado Completo
class Graph {
	// Variaveis
	public AtomGraph[] objectGraph;
	public int vert;
	public int arest;

	// Construtores
	public Graph(int v) {
		this.objectGraph = new AtomGraph[v];
		for(int i = 0; i < v; i++) {
			this.objectGraph[i] = new AtomGraph(i+1);
		}
		this.vert = v;
		this.arest = 0;
	}

	// Resetar acesso
	public void resetVertsAcess() {
		for(int i = 0; i < this.vert; i++)
			for(AtomGraph j = this.objectGraph[i].prox; j != null; j = j.prox)
				this.objectGraph[i].acessado = false;
	}

	// Verificar se um vertice aponta para outro 
	public boolean pesquisarVert(AtomGraph a, int valor) {
		boolean result = false;
		for(AtomGraph j = a.prox; j != null && !result; j = j.prox)
			if(j.element == valor)
				result = true;
		return(result);
	}

	// Inserir conexao entre vertices
	private void inserirArestsAux(AtomGraph a, int valor) {
		AtomGraph tmp = new AtomGraph(valor);
		boolean inserido = true;
		for(AtomGraph i = a; inserido; i = i.prox) {
			if(i.prox == null) {
				tmp.posInVertor = this.objectGraph[valor-1];
				i.prox = tmp;
				inserido = false;
			}
		}
	}

	public void inserirArests(AtomGraph a, int valor) {
		AtomGraph tmp = new AtomGraph(valor);
		boolean inserido = true;
		if(a.element != valor && !pesquisarVert(a, valor)) {
			for(AtomGraph i = a; inserido; i = i.prox) {
				if(i.prox == null) {
					tmp.posInVertor = this.objectGraph[valor-1];
					i.prox = tmp;
					this.arest++;
					inserido = false;
					if(a.element != valor)
						inserirArestsAux(this.objectGraph[valor-1], a.element);
				}
			}
		}
	}

	// Verificar para quantos vertices um vertice aponta
	public int numConexoes(AtomGraph a) {
		int result = 0;
		for(AtomGraph i = a.prox; i != null; i = i.prox)
			++result;
		return(result);
	}

	// Mostrar um vertice e suas conexoes
	public void printVert(AtomGraph a) {
		System.out.print(a.element + " -> ");
		if(a.prox != null) {
			for(AtomGraph j = a.prox; j != null; j = j.prox) {
				System.out.print(j.element);
				if(j.prox != null)
					System.out.print("/");
				else
					System.out.println(";");
			}		
		} else
			System.out.println("null;");
	}

	// Mostrar um grafo por meio das conexoes de cada vertice
	public void printGraph() {
		for(int i = 0; i < this.vert; i++) {
			System.out.print("\t" + this.objectGraph[i].element + " -> ");
			if(this.objectGraph[i].prox != null) {
				for(AtomGraph j = this.objectGraph[i].prox; j != null; j = j.prox) {
					System.out.print(j.element);
					if(j.prox != null)
						System.out.print("/");
					else
						System.out.println(";");
				}		
			} else
				System.out.println("null;");
		}
	}

	// Mostrar uma matrix que representa as ligacoes
	public void printGraphMatrix() {
		int[][] matrix = new int[this.vert][this.vert];
		for(int i = 0; i < this.vert; i++)
			for(int j = 0; j < this.vert; j++)
				matrix[i][j] = 0;

		for(int i = 0; i < this.vert; i++)
			for(AtomGraph j = this.objectGraph[i].prox; j != null; j = j.prox)
				matrix[this.objectGraph[i].element-1][j.element-1] = 1;

		System.out.println("");
		for(int i = 0; i < this.vert; i++) {
			System.out.print("\t| ");
			for(int j = 0; j < this.vert; j++) 
				System.out.print(matrix[i][j] + " ");
			System.out.println("|");
		}
		System.out.println("");
	}

	// Encontrar ciclos por meio de permutacao (Linha 195 - 380)
	// Permutar e armasenar permutacoes
  private static void permutacao(List<Integer> arr, int k, List<List<Integer>> result) {
    for(int i = k; i < arr.size(); i++) {
      Collections.swap(arr, i, k);
      permutacao(arr, k+1, result);
      Collections.swap(arr, k, i);
    }
    if (k == arr.size() - 1) {
      List<Integer> c = new ArrayList<Integer>(arr);
      result.add(c);
    }
  }

  // filtrar valores permutados e testar ciclos
  private static List<List<Integer>> permutacao(int v) {
    // variaveis
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> graf = new ArrayList<Integer>();
    List<Integer> auxgraf = new ArrayList<Integer>();
    boolean test = false;
    int sizeInicial = 0;
    int quantAdd = 0;
    // criar grafo
    for(int i = 0; i < v; i++)
      graf.add(i+1);
    // permutar
    permutacao(graf, 0, result);
    // pegar sub conexoes
    sizeInicial = result.size();
    for(int i = 0; i < result.size(); i++) {
      auxgraf = new ArrayList<Integer>();
      for(int j = 1; j < result.get(i).size(); j++)
        auxgraf.add(result.get(i).get(j));
      while(auxgraf.size() >= 3) {
        for(int k = sizeInicial; k < result.size(); k++) {
          if(auxgraf.size() == result.get(k).size() &&
            (auxgraf.equals(result.get(k)) || 
              comparaList(auxgraf, result.get(k)))) {
              test = true;
          }
        }
        if(!test || sizeInicial == result.size()) {
          result.add(auxgraf);
          ++quantAdd;
        }
        test = false;
        auxgraf = new ArrayList<Integer>(auxgraf);
        auxgraf.remove(auxgraf.get(0));
      }
      auxgraf = null;
    }
    // limpar os resultados obtidos
    for (int i = 0; i < result.size() - quantAdd; i++) {
      List<Integer> s = new ArrayList<Integer>(result.get(i));
      for(int j = (i+1); j < result.size(); j++) {
        if(s.size() == result.get(j).size() &&
           (s.equals(result.get(j)) || 
            comparaList(s, result.get(j)))) {
          result.remove(j);
          j--;
        }
      }
    }
    return(result);
  }

  private static boolean comparaList(List<Integer> s1, List<Integer> s2) {
    boolean resp = true;
    boolean resp2 = true;
    int test1 = 0;
    int test2 = 0;
    int pos = -1;
    if(s1.size() == s2.size()) {
      for(int i = 0; i < s2.size(); i++)
        if(s1.get(0) == s2.get(i))
          pos = i;
      if(pos != -1) {
        test1 = pos;
        test2 = pos;
        for(int i = 0; i < s1.size() && resp; i++) {
          if(s1.get(i) != s2.get(test1))
            resp = false;
          ++test1;
          if(test1 >= s2.size())
            test1 = 0;
        }
        for(int i = 0; i < s1.size() && resp2; i++) {
          if(s1.get(i) != s2.get(test2))
            resp2 = false;
          --test2;
          if(test2 < 0)
            test2 = s2.size()-1;
        }
      } else{
        resp = false;
        resp2 = false;
      }
    } else{
      resp = false;
      resp2 = false;
    }
    resp = resp || resp2;
    return(resp);
  }

	// Encontrar ciclos e testar
	public List<List<Integer>> testPermutacaoCiclos() {
		// variaveis 
		List<List<Integer>> possibilidades = permutacao(this.vert);
		List<Integer> conexao = new ArrayList<Integer>();
		boolean test = false; 
		int pos = 0;
		// fazer
		while (pos < possibilidades.size()) {
			for(int j = 1; !test && j <= possibilidades.get(pos).size(); j++) {
				if(j == possibilidades.get(pos).size() && 
					!pesquisarVert(objectGraph[
					possibilidades.get(pos).get(j-1)-1], 
					possibilidades.get(pos).get(0))) {
					test = true;
					conexao.add(possibilidades.get(pos).get(j-1));
					conexao.add(possibilidades.get(pos).get(0));
					possibilidades.remove(possibilidades.get(pos));
					--pos;
				}	if(j < possibilidades.get(pos).size() && 
					!pesquisarVert(objectGraph[
					possibilidades.get(pos).get(j-1)-1], 
					possibilidades.get(pos).get(j))) {
					test = true;
					conexao.add(possibilidades.get(pos).get(j-1)); 
					conexao.add(possibilidades.get(pos).get(j));
					possibilidades.remove(possibilidades.get(pos));
					--pos;
			  }
			}
			for(int j = 0; test && j < possibilidades.size() && conexao.size() == 2; j++) {
				if(ConteConexao(possibilidades.get(j), conexao)) {
					possibilidades.remove(j);
					--pos;
					j = (j != 0) ? --j : 0;
				}
			}
			pos = (pos < 0) ? 0 : ++pos;
			conexao = new ArrayList<Integer>();
			test = false;
		}
		ordenarList(possibilidades);
		for(int i = 0; i < possibilidades.size(); i++) 
			System.out.println("\t" + (i+1) + ".\t" + possibilidades.get(i));
		System.out.println("\tQuantidade de ciclos: " + possibilidades.size());
		return(possibilidades);
	}

	// ordenar o resultado
	private static void ordenarList(List<List<Integer>> result) {
    for (int fixo = 0; fixo < result.size() - 1; fixo++) {
	    int menor = fixo;

	    for (int i = menor + 1; i < result.size(); i++) {
	       if (result.get(i).size() < result.get(menor).size()) {
	          menor = i;
	       }
	    }

	    if (menor != fixo) {
	      List<Integer> valor = new ArrayList<Integer>(result.get(fixo));
	      result.set(fixo, result.get(menor));
	      result.set(menor, valor);
	    }
  	}
	}

	// verificar existencia de conexao
	private boolean ConteConexao(List<Integer> l1, List<Integer> l2) {
  	// variaveis
  	boolean test = false;
  	// fazer
  	for(int i = 0; i < l1.size() && l2.size() == 2 && !test; i++) {
  		if(l1.get(i) == l2.get(0) && 
  			(((i+1 < l1.size() && l1.get(i+1) == l2.get(1)) || (i+1 >= l1.size() && l1.get(0) == l2.get(1))) ||
  			((i-1 >= 0 && l1.get(i-1) == l2.get(1)) || (i-1 < 0 && l1.get(l1.size()-1) == l2.get(1))))) 
  			test = true;
  	}
  	return(test);
  }

  // Encontrar ciclos por meio de caminhamento (Linha 382 - ???)
  // Caminhar
  private void caminharVertices(List<List<Integer>> result) {
  	List<Integer> valor = new ArrayList<Integer>();
  	for(int i = 0; i < this.vert; i++) {
  		caminharVertices(valor, ObjectGraph[i]);
  		if(valor.size() > 0)
				result.add(valor);
			valor = new ArrayList<Integer>();
  	}
  }

  private void caminharVertices(List<Integer> result) {
  	for(AtomGraph j = this.objectGraph[i].prox; j != null; j = j.prox) {}
  }

  // Armazenar e tratar os valores
  public List<List<Integer>> testCaminhamentoCiclos() {
  	// Variaveis
  	List<List<Integer>> result = new ArrayList<>();
  	// Fazer
  	buscaProfundidade(result);
  }
}