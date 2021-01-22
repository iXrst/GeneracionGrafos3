import java.util.*;
import java.awt.geom.Point2D;
import java.nio.file.*;


public class Grafo{
    private HashMap<Integer, Set<Arista>> map = new HashMap<>();
    private HashMap<String, String> label_map = new HashMap<>();

    public void addNode(int nodo) {
        if(!map.containsKey(nodo)){
            map.put(nodo, new HashSet<Arista>());
            label_map.put(""+nodo, ""+nodo);
        }
    }

    public void addNodeConnections(int nodo, Set<Arista> nodos_adyacentes) {
        map.put(nodo,nodos_adyacentes);
        label_map.put(""+nodo, ""+nodo);
	}

    public void delNode(int nodo){
        map.remove(nodo);
    }

    public boolean containsNode(int nodo){
        return map.containsKey(nodo);
    }

    public Set<Integer> getNodes() {
        return map.keySet();
    }

    public boolean isLinked(int nodo_origen, int nodo_destino){
        for (Arista aristas: map.get(nodo_origen)){
            if(aristas.nodo_destino == nodo_destino){
                return true;
            }
        }
        return false;
    }

    public Set<Arista> getLinkedNodes(int nodo) {
		return map.get(nodo);
	} 

    public void getNodesCount(){ 
        System.out.println("El grafo tiene "+ map.keySet().size() + " nodos"); 
    }

    public void addEdge(int nodo_origen, int nodo_destino){
        addEdge(new Arista(nodo_origen, nodo_destino));
    }

    public void addEdge(int nodo_origen, int nodo_destino, int peso){
        addEdge(new Arista(nodo_origen, nodo_destino, peso));
    }

    public void addEdge(Arista arista) {
        if (!map.containsKey(arista.nodo_origen)) {
            addNode(arista.nodo_origen);
        }
        if (!map.containsKey(arista.nodo_destino)) {
            addNode(arista.nodo_destino);
        }
        Arista arista_aux = new Arista(arista.nodo_destino, arista.nodo_origen, arista.peso);
        if(!this.isLinked(arista.nodo_origen, arista.nodo_destino)){
            map.get(arista.nodo_origen).add(arista);
            map.get(arista.nodo_destino).add(arista_aux);
        }
    }

    public void getEdgesCount(){ 
        int count = 0; 
        for (Integer v : map.keySet()) { 
            count += map.get(v).size(); 
        } 
        System.out.println("El grafo tiene " + count + " aristas"); 
    } 

    public int getWeigthEdge(int nodo_origen, int nodo_destino) {
        for (Arista arista : map.get(nodo_origen)) {
            if (arista.nodo_destino==nodo_destino) {
                return arista.peso;
            }
        }
        return (int)Float.POSITIVE_INFINITY;
	}

    public boolean hasNode(int nodo){ 
        if (map.containsKey(nodo)) { 
            System.out.println("El grafo contiene "+ nodo + " como nodo"); 
            return true;
        } 
        else { 
            System.out.println("El grafo no contiene "+ nodo + " como nodo"); 
            return false;
        } 
    } 
  
    public String toString(){
        StringBuilder res = new StringBuilder("graph abstract {\n");
        for (int nodo : map.keySet() ) {
			res.append("  "+nodo+" [label=\""+label_map.get(""+nodo)+"\"];\n");
        }
        
        for (Map.Entry<Integer,Set<Arista>> n : map.entrySet()){
            int nodo=n.getKey();
            for (Arista arista : n.getValue()) {
                if (nodo<arista.nodo_destino) {
                    res.append("  "+ nodo +" -- "+ arista.nodo_destino + " [label=\""+arista.peso + "\"];\n");
                }
            }
        }
        res.append("}");
        return (res.toString());
    }

    public void toGVFile(String nombre, String grafo){
        Path ubicacion = Paths.get(nombre + ".gv");
        try {
            Files.writeString(ubicacion, grafo);
        } catch (Exception e) {
        }
    }

    public static Grafo genErdosRenyi(int n, int m) {
        Random r = new Random();
        Grafo g = new Grafo();
        for (int i = 0; i < n; i++) {
            g.addNode(i);
        }
        int i = 0;
        int n1 = 0;
        int n2 = 0;
        while(i<m) {
            n1 = r.nextInt(n);
            n2 = r.nextInt(n);
            while(n1 == n2) {
                n2 = r.nextInt(n);
            }
            g.addEdge(n1,n2);
            i++;
        }
        return g;
    }

    public static Grafo genGeografico(int n, double d) {
        Random r = new Random();
        Grafo g = new Grafo();
        Point2D[] xy = new Point2D[n];
        for (int i = 0; i < n; i++) {
            g.addNode(i);
            xy[i] = new Point2D.Double(r.nextDouble(), r.nextDouble());
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    if (xy[i].distance(xy[j]) < d) {
                        g.addEdge(i,j);
                    }
                }
            }
        }
        return g;
    }

    public static Grafo genGilbert(int n, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probabilidad entre 0 y 1");
        Random r = new Random();
        Grafo g = new Grafo();
        for (int i = 0; i < n; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < n - 1 ; i++) {
            for (int j = i + 1; j < n; j++) {
                if (r.nextDouble() <= p) {
                    g.addEdge(i,j);
                }
            }
        }
        return g;
    }

    public static Grafo genBarabasiAlbert(int n, double d) {
        Grafo g = new Grafo();
        Random r = new Random();
        for(int i = 0; i < n; i++) {
			g.addNode(i);
		}
        for (int i = 0; i < d; i++){
            for (int j = i; j < d; j++){
                if(i!=j){
                    g.addEdge(i,j);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j){
                    if((g.map.get(i).size()) < d && (g.map.get(j).size()) < d){
                        if(r.nextDouble() <= (1-(g.map.get(i).size()/d))){
                            if (g.map.get(i).contains(j) || g.map.get(j).contains(i)){
                            }
                            else{
                                if (Collections.frequency(g.map.values(),i) < d && Collections.frequency(g.map.values(),j) < d){
                                    g.addEdge(i,j);
                                }
                            }
                        }
                    }
                }
            }
        }
        return g;
    }

    public static Grafo Dijkstra(Grafo grafo,int n) {
		Grafo g = new Grafo();     
        int nodo_aux_1, d_nodo_aux_1, nodo_aux_2, d;
        Set<Arista> interconexiones;
        HashMap<Integer, Integer> d_aux = new HashMap<>();
        HashMap<Integer, Integer> d_final = new HashMap<>();
        HashMap<Integer,Integer> camino = new  HashMap<>();
        ArrayList<Integer> visitados = new ArrayList<>();    
		for (int nodos : grafo.map.keySet() ) {
            d_aux.put(nodos,(int)Float.POSITIVE_INFINITY);
            d_final.put(nodos, (int)Float.POSITIVE_INFINITY);
            camino.put(nodos,nodos);
        }
        d_aux.put(n, 0);
        d_final.put(n, 0);
    	camino.remove(n);
		while(d_aux.size() > 0) {
            nodo_aux_1 = nodoLiviano(d_aux);
            d_nodo_aux_1 = d_aux.get(nodo_aux_1);
            d_final.put(nodo_aux_1, d_nodo_aux_1);
            visitados.add(nodo_aux_1);
            d_aux.remove(nodo_aux_1);
            interconexiones = grafo.getLinkedNodes(nodo_aux_1);
            for(Arista aristas: interconexiones){
                nodo_aux_2 = aristas.nodo_destino;
                d = d_nodo_aux_1 + aristas.peso;
                if(!visitados.contains(nodo_aux_2)){
                    if(d < d_aux.get(nodo_aux_2)){
                        d_aux.put(nodo_aux_2, d);
                        camino.put(nodo_aux_2, nodo_aux_1);
                    }
                }
            }
        }
        camino.put(n,n);
        for (Map.Entry label_pesos: d_final.entrySet()){           
            g.addEdge((int) label_pesos.getKey(), (int) camino.get(label_pesos.getKey()), grafo.getWeigthEdge((int) label_pesos.getKey(), (int) camino.get(label_pesos.getKey())));
            g.label_map.put(""+label_pesos.getKey(), ""+label_pesos.getKey()+"_"+label_pesos.getValue());
        }       
        return g;
    }

    private static Integer nodoLiviano(HashMap<Integer, Integer> d_aux){
        Map.Entry<Integer, Integer> siguiente = d_aux.entrySet().iterator().next();
        int distancia = siguiente.getValue();
        int nodo_siguiente  =  siguiente.getKey();
        for (Map.Entry nodo : d_aux.entrySet()) {
            if ((int)nodo.getValue() < distancia) {
                distancia = (int)nodo.getValue();
                nodo_siguiente  =  (int)nodo.getKey();
            }
        }
        return nodo_siguiente;
    }

    

} 
    

