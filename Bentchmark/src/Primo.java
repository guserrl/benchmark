import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Primo implements Callable<List<Integer>>{
	private int inicio;
    private int fin;
    private List<Integer> l=new ArrayList<>();
    private CyclicBarrier barrera;
	public Primo(int inicio, int fin,CyclicBarrier barrera) {
		super();
		this.inicio = inicio;
		this.fin = fin;
		this.barrera=barrera;
	}
	@Override
	public List<Integer> call() throws Exception {
		// TODO Auto-generated method stub
		for (int i = inicio; i <= fin; i++) {
            if (esPrimo(i)) {
                l.add(i);
            }
        }
		this.barrera.await();
		return l;
	}
	
	private boolean esPrimo(int numero) {
        if (numero <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }
}
