import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;


//leer de una peticion get el archivo  mirar cuanto tarda
public class Peticiones extends Thread{
private URL url;
private String key,value,accept;
private int id;

public Peticiones(URL url, String key, String value, String accept,int id) {
	super();
	this.url = url;
	this.key = key;
	this.value = value;
	this.accept = accept;
	this.id=id;
}

@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		HttpURLConnection con;
		File dir = new File("logs");
		if(!dir.exists() || !dir.isDirectory()) dir.mkdir();
		String a[] = url.getFile().split("/");
		String nombre = a[a.length-1];
		File f = new File(dir,nombre );//"log_id_"+id+".txt"
		try {
			FileOutputStream fo = new FileOutputStream(f);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			if(key!=null && value!=null) {
				con.addRequestProperty(key, value);
			}
			if(accept!=null) {
				con.addRequestProperty("Accept", accept);
			}
			
			long t1 = System.currentTimeMillis();
			InputStream i = con.getInputStream();
			byte[] b = new byte[1024];
			int leido=i.read(b);
			while(leido!=-1) {
				fo.write(b,0,leido);
				leido=i.read(b);
			}
			long t2 = System.currentTimeMillis()-t1;
			System.out.println("Peticion "+this.id+" tardo "+t2/1000.0+" s");
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
