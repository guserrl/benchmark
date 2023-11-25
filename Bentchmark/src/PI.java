import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class PI implements Callable<BigDecimal>{
	private static int n;
    private double resultadoParcial=0.0;
    
	public PI(int n) {
		super();
		this.n=n;
	}
	@Override
	public BigDecimal call() throws Exception {
		// TODO Auto-generated method stub
		/*for (int i = inicio; i <= fin; i++) {
            resultadoParcial += Math.pow(-1, i) / (2.0 * i + 1);
        }
		this.starter.countDown();*/
		//System.out.println(resultadoParcial.toString()+" soy del metodo");
		return calculoPiGauss();
	}
    
	public static BigDecimal calculoPiGauss(){
		  BigDecimal pi = serieArcotangente(1.00/18.00, 60,n)
		  .multiply(new BigDecimal(12.00))
		     .add(serieArcotangente(1.00/57.00, 60,n)
		  .multiply(new BigDecimal(8.00))
		     .subtract(serieArcotangente(1.00/239.00, 60,n)
		  .multiply(new BigDecimal(5.00)))
		 );
		 return pi.multiply(new BigDecimal(4.00));
		}
	
	private static BigDecimal serieArcotangente(double x, int iteraciones,int n) {
		MathContext mc = new MathContext(n);
		BigDecimal result = new BigDecimal(0.00, mc);
		BigDecimal uno = new BigDecimal(1.00);
		for (int i = 0; i < iteraciones; i++) {
			double div = (2.00 * i + 1.00) * (Math.pow(1.00 / x, 2.00 * i + 1.00));
			BigDecimal divisor = new BigDecimal(div);
			if (i % 2 == 0) {
				result = result.add(uno.divide(divisor, mc));
			} else {
				result = result.subtract(uno.divide(divisor, mc));
			}
		}
		return result;
	}
}
