import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class principal {
public static void main(String[] args) {
	int numCPU=Runtime.getRuntime().availableProcessors();
	System.out.println(numCPU);
	//N=numero de hilos
	int N=10;
//	Carrera c = new Carrera(N);
//	c.start();
	for(int i=0;i<numCPU;i++) {
		Timer t = new Timer();
		t.schedule(new Carrera(N), 10000);
	}

}
}
