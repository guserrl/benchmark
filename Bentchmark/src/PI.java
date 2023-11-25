import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class PI implements Callable<Double>{
	private int inicio;
    private int fin;
    private double resultadoParcial=0.0;
    private CountDownLatch starter;
	public PI(int inicio, int fin,CountDownLatch starter) {
		super();
		this.inicio = inicio;
		this.fin = fin;
		this.starter=starter;
	}
	@Override
	public Double call() throws Exception {
		// TODO Auto-generated method stub
		for (int i = inicio; i <= fin; i++) {
            resultadoParcial += Math.pow(-1, i) / (2.0 * i + 1);
        }
		this.starter.countDown();
		return resultadoParcial;
	}
    
}
