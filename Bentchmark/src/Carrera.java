import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Carrera extends TimerTask{
List<Corredor> participantes = new ArrayList<>();
int corredores;
final CyclicBarrier starter;
ExecutorService pool;
int ganador;
static final boolean b = false;
Timer t;
public Carrera(int corredores) {
	this.corredores = corredores;
	starter = new CyclicBarrier(corredores+1);
	pool=Executors.newFixedThreadPool(corredores);
	this.t=t;
}

public void run() {
		// TODO Auto-generated method stub
	Random rand = new Random();
	List<Future<Float>> f = new ArrayList<>();
	try {
		for(int i=0;i<corredores;i++) {
			//System.out.println(i);
			//2147483647
			f.add(pool.submit(new Corredor(999999999, rand.nextInt(1,10), starter, i)));
		}
		starter.await();
		pool.shutdown();
		float min = f.get(0).get();
		int id=0;
		for(int j=0;j<f.size();j++) {
			if(f.get(j).get()<min) {
				min = f.get(j).get();
				id=j;
			}
			System.out.println("Corredor:"+j+" ha tardado "+f.get(j).get()+" segundos");
		}
		System.out.println("Ganador:"+id+" con "+min+" segundos");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch (BrokenBarrierException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
}
