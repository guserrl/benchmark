import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
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
					System.out.println("Esta prueba calcula decimales de pi \n"
							+ "con un algoritmo de GAUSS  (Tener en cuenta que la precision y valores a mostrar estaran limitados) \n");
					System.out.println("Cuantas numeros?");
					int piNum = sc.nextInt();
					pi(piNum);
				}break;
				case 3:{
					System.out.println("Esta prueba calculara todos los numero primos asta el valor que proporciones \n"
							+ " y luego indicara cuanto tarda en escribir esos valores en el log");
					System.out.println("Valor? maximo numoro a leer = 2,147,483,647");
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
					
					System.out.println("Esta prueba mide cuanto tardarias en realizar peticiones get y descargar su contenido");
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
		Timer t = new Timer();
		for (int i = 0; i < n; i++) {
			t.schedule(new Carrera(N, i), 10000);
		}
		System.out.println("Empiezan las carreras, puede continuar on otra cosa");
	}

	public static void pi(int n) {
		ExecutorService pool = Executors.newCachedThreadPool();
		//final CountDownLatch starter = new CountDownLatch(numCPU);
		Future<BigDecimal> l = null;
		int numerosPI = n;// puedo poner esto como variable
		long t1 = System.currentTimeMillis();
		l = pool.submit(new PI(numerosPI));
		/*for (int i = 1; i < numCPU; i++) {
			int inicio = i * (numerosPI / numCPU);
			int fin = (i + 1) * (numerosPI / numCPU) - 1;
			l.add(pool.submit(new PI(inicio, fin, starter)));
			inicio = fin + 1;
			fin += fin;
		}*/
		//starter.countDown();
		pool.shutdown();
		long t2 = System.currentTimeMillis() - t1;
		System.out.println("Ha tardado en calcular :" + t2 / 1000.0 + "s");
		/*double resultadoFinal = 0.0;
		// Si no uso threads para leer tarda mas en hacer el bucle que en calcular
		for (int i = 0; i < l.size(); i++) {
			try {
				//System.out.println("resltdofinal antes: "+resultadoFinal.toString());
				resultadoFinal += l.get(i).get(); //+= l.get(i).get();
				//System.out.println("resltdofinal despues: "+resultadoFinal.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultadoFinal *= 4;*/
		try {
			System.out.println("PI:" + l.get());
			//System.out.println("double:"+l.get().doubleValue());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void primo(int numCPU,int n) {
		
		ExecutorService pool = Executors.newFixedThreadPool(numCPU);
		final CyclicBarrier barrera = new CyclicBarrier(numCPU+1);

		List<Future<List<Integer>>> l = new ArrayList<>();
		int rangoInicio = 1;
        int rangoFin = n;//200000000
        int rangoPorThread = (rangoFin - rangoInicio ) / numCPU;
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
		//lLResultados ya calculados
		//Logs
		File f = new File("logs_primo");
		if(!f.exists() || !f.isDirectory()) {
			f.mkdir();
		};
		File f2 = new File(f,"logs_numeros_primos.txt");
		if(f2.exists()) {
			PrintWriter writer;
			//Poner a cero
			try {
				writer = new PrintWriter(f2);
				writer.print("");
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final CountDownLatch c = new CountDownLatch(numCPU);
		ExecutorService pool2 = Executors.newFixedThreadPool(numCPU);
		t1 = System.currentTimeMillis();
		for(int i=0;i<l.size();i++) {	
			try {
				List<Integer> l2 = l.get(i).get();
				MostrarPrimos m = new MostrarPrimos(l2,c,f2);
				synchronized (m) {
					pool2.execute(m);
				}
				//pool2.execute(new MostrarPrimos(l2,c,f2));
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
			System.out.println("Ejemplo REST: https://www.fruityvice.com/api/fruit/all");
			System.out.println("Repositorio con apis: https://github.com/public-apis/public-apis#index");
			System.out.println("Ejemplo zip: https://www.bis.org/statistics/full_tc_csv.zip");
			System.out.println("Repositorio con datasets: https://github.com/awesomedata/awesome-public-datasets#finance");
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
