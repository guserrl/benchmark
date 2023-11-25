import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.sun.net.httpserver.HttpServer;
public class principal {
	public static void main(String[] args) {
		int numCPU = Runtime.getRuntime().availableProcessors();
		System.out.println("Tu cpu:"+numCPU);
		int i=0;
		do{
			System.out.println("1.Carreras");
			System.out.println("2.PI");
			System.out.println("3.Primos");
			System.out.println("4.Peticiones");
			System.out.println("5.Salir");
			System.out.println("Intoduce opcion:");
			Scanner sc = new Scanner(System.in);
			i = sc.nextInt();
			
			
			switch(i) {
				case 1:{
					System.out.println("Esta prueba crea carreras de hilos que se esqueludizan cada 10 segundos");
					System.out.println("Cuantas carreras?");
					int carreras = sc.nextInt();
					System.out.println("Cuantos corredores?");
					int corredores = sc.nextInt();
					Thread t = new Thread(new Runnable() {	
						@Override
						public void run() {
							// TODO Auto-generated method stub
							carreras(carreras, corredores);
						}
					});t.start();
				}break;
				case 2:{
					System.out.println("Esta prueba calcula los posibles numeros de pi que permite java \n"
							+ "con un algoritmo simple  (Tener en cuenta que la precision y valores a mostrar estaran limitados) \n");
					System.out.println("Cuantas numeros?maximo numoro a leer = 2,147,483,647");
					int piNum = sc.nextInt();
					primo(numCPU, piNum);
				}break;
				case 3:{
					System.out.println("Esta prueba calculara todos los numero primos asta el valor que proporciones \n"
							+ " y luego indicara cuanto tardaria en mostrar esos valoresmaximo numoro a leer = 2,147,483,647");
					System.out.println("Valor techo?");
					int primos = sc.nextInt();
					Thread t = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							primo(numCPU,primos);
						}
					});t.start();
				}break;
				case 4:{
					
					System.out.println("Esta prueba mide cuanto tardarias en realizar peticiones get a una res api");
					peticiones(numCPU);
				}
			}
		}while(i!=5);
		
		System.out.println("Fin");
		System.exit(1);
	}

	public static void carreras(int n, int N) {
		System.out.println("Se crearan carreras que iniciaran cada 10 segundos");
		// System.out.println("Se va a usar el maximo numero posible en java");
		for (int i = 0; i < n; i++) {
			Timer t = new Timer();
			t.schedule(new Carrera(N, i), 10000);
		}
		System.out.println("Empiezan las carreras, puede continuar on otra cosa");
	}

	public static void pi(int numCPU,int n) {
		ExecutorService pool = Executors.newFixedThreadPool(numCPU);
		final CountDownLatch starter = new CountDownLatch(numCPU);
		List<Future<Double>> l = new ArrayList<>();
		int numerosPI = n;// puedo poner esto como variable
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < numCPU; i++) {
			int inicio = i * (numerosPI / numCPU);
			int fin = (i + 1) * (numerosPI / numCPU) - 1;
			l.add(pool.submit(new PI(inicio, fin, starter)));
			inicio = fin + 1;
			fin += fin;
		}
		starter.countDown();
		pool.shutdown();
		long t2 = System.currentTimeMillis() - t1;
		System.out.println("Ha tardado en calcular :" + t2 / 1000.0 + "s");
		double resultadoFinal = 0.0;
		// Si no uso threads para leer tarda mas en hacer el bucle que en calcular
		for (int i = 0; i < l.size(); i++) {
			try {
				resultadoFinal += l.get(i).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultadoFinal *= 4;
		System.out.println("PI:" + resultadoFinal);
	}
	
	public static void primo(int numCPU,int n) {
		
		ExecutorService pool = Executors.newFixedThreadPool(numCPU);
		final CyclicBarrier barrera = new CyclicBarrier(numCPU+1);

		List<Future<List<Integer>>> l = new ArrayList<>();
		int rangoInicio = 1;
        int rangoFin = n;//200000000
        int rangoPorThread = (rangoFin - rangoInicio ) / numCPU;
        //int inicio = rangoInicio;
        //int fin = inicio + rangoPorThread;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < numCPU; i++) {
        	int inicio = rangoInicio + i * rangoPorThread;;
        	int fin = Math.min(inicio + rangoPorThread - 1, rangoFin);
            l.add(pool.submit(new Primo(inicio,fin,barrera)));
        }
        try {
			barrera.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pool.shutdown();
		long t2 = System.currentTimeMillis() - t1;
		System.out.println("Ha tardado en calcular:" + t2 / 1000.0 + "s");
		//Deberia usar hilos para esto pero que pereza
		
		final CountDownLatch c = new CountDownLatch(numCPU);
		ExecutorService pool2 = Executors.newFixedThreadPool(numCPU);
		t1 = System.currentTimeMillis();
		for(int i=0;i<l.size();i++) {
			try {
				List<Integer> l2 = l.get(i).get();
				pool2.execute(new MostrarPrimos(l2,c));
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			c.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool2.shutdown();
		t2 = System.currentTimeMillis() - t1;
		System.out.println("Ha tardado en mostrar:" + t2 / 1000.0 + "s");
	}
	
	public static void peticiones(int numCPU) {
		try {
			System.out.println("Introudce url");
			System.out.println("Ejemplo: https://www.fruityvice.com/api/fruit/all");
			System.out.println("Repositorio con apis: https://github.com/public-apis/public-apis#index");
			Scanner sc = new Scanner(System.in);
			String url = sc.nextLine();
			
			
			//Tengo que cambiarlo
			URL u = new URL(url);

			System.out.println("Numero de peticiones"); //200
			int pet = sc.nextInt();
			
			System.out.println("Requiere autorizacion?");
			System.out.println("1.Si");
			System.out.println("2.No");
			int sino = sc.nextInt();
			String key = null;
			String value = null;
			if(sino==1) {
				System.out.println("Key");
				key = sc.nextLine();
				System.out.println("Value");
				value = sc.nextLine();
			}
			
			System.out.println("Cabecera Accept");
			System.out.println("1.XML");
			System.out.println("2.JSON");
			System.out.println("3.Html");
			System.out.println("4.Otro:");
			System.out.println("5.No es necesaria:");
			int accept = sc.nextInt();
			String acc = null;
			if(accept==1) {
				acc = "application/xml";
			}else if(accept==2) {
				acc = "application/json";
			}else if(accept==3) {
				acc = "text/html";
			}else if(accept==4) {
				acc = sc.nextLine();
			}		

			for(int i=0;i<pet;i++) {
				//Peticiones p = new Peticiones(u, key, value, acc, i);
				Peticiones p = new Peticiones(u, key, value, acc, i+1);
				p.start();
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
