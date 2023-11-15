import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class Corredor implements Callable<Float>{
private int distancia;
private int velocidad;
CyclicBarrier starter;
private int id;
private int tiempo=0;

public Corredor(int distancia, int velocidad, CyclicBarrier starter,int id) {
	this.distancia = distancia;
	this.velocidad = velocidad;
	this.starter = starter;
	this.id=id;
}
@Override
public Float call() throws Exception {
	starter.await();
	long inicio = System.currentTimeMillis();
	// TODO Auto-generated method stub
	for(int i=0;i<this.distancia;i=i+velocidad) {
		//System.out.println(i);
	}
	long fin = System.currentTimeMillis()-inicio;
	//return "Corredor:"+this.id+" ha tardado "+fin/ 1000.0+" segundos";
	return  (float) (fin/ 1000.0);
}


}
