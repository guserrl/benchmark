import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MostrarPrimos extends Thread{
private List<Integer> l;
private CountDownLatch c;
public MostrarPrimos(List<Integer> l,CountDownLatch c) {
	super();
	this.l = l;
	this.c=c;
}

@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		for(int j=0;j<l.size();j++) {
			System.out.println(l.get(j));  //funciona
		}
		c.countDown();
	}
}
