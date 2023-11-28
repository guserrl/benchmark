import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MostrarPrimos extends Thread{
private List<Integer> l;
private CountDownLatch c;
private File f;
public MostrarPrimos(List<Integer> l,CountDownLatch c,File f) {
	super();
	this.l = l;
	this.c=c;
	this.f=f;
}

@Override
	public void run() {
		// TODO Auto-generated method stub
	try{
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		if(f.length()!=0) {
			//raf.writeBytes("\n");
			raf.seek(f.length());
		}
		
		for(int j=0;j<l.size();j++) {
			//System.out.println(l.get(j));  //funciona
			raf.writeBytes(" "+l.get(j)+"\n");
		}
		c.countDown();
		raf.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
