import java.util.Random;

public class Arista{
	private static Random r = new Random();
	int nodo_origen;
	int nodo_destino;
	int peso;
	

	public Arista(int nodo_origen,int nodo_destino,int peso) {
		this.nodo_origen=nodo_origen;
		this.nodo_destino=nodo_destino;
		this.peso=peso;
	}
	
	public Arista(int nodo_origen,int nodo_destino) {
		this.nodo_origen=nodo_origen;
		this.nodo_destino=nodo_destino;
		this.peso=r.nextInt(100)+1;
	}
	
}