/*
* Monitore habiratuen problema ikusteko
*
* Mikel Larreategi
* 2003-3-6
*/


class MonitoreHabiratuakApp {
	public static void main (String args[]){
		SemaBuffer semabuf = new SemaBuffer(6);
		Irakurlea ira = new Irakurlea(semabuf);
		Idazlea ida = new Idazlea(semabuf);
		ida.start();
		ira.start();
	}
}




class SemaBuffer {
	private int tam;
	private int in;
	private int out;
	Semaforo beteta;
	Semaforo hutsik;
	private char[] buf;
	private int kont;

	SemaBuffer (int tam){
		int i;

		this.tam = tam;
		in = out= 0;
		kont = 0;
		buf = new char[tam];
		for (i=0;i<this.tam;i++){
			buf[i]=' ';
		}
		beteta = new Semaforo(0);
		hutsik = new Semaforo(tam);
	}

	// Hau ere aldatu
    // public synchronized void put(char c) throws InterruptedException{
	public void put(char c) throws InterruptedException{
		hutsik.behera();
		synchronized (this){ // Hau jarri konpontzeko
			buf[in] = c;
			++kont;
			in=(in+1)%tam;
			erakutsi();
		}
		beteta.gora();
	}

	// Hau ere aldatu
	// public synchronized char get() throws InterruptedException{
	public char get() throws InterruptedException{
		char c;

		beteta.behera();

		synchronized (this){ // Hau jarri konpontzeko
			c = buf[out];
			buf[out]=' ';
			--kont;
			out=(out+1)%tam;
			erakutsi();
			hutsik.gora();
		}
		return (c);
	}
	public void erakutsi(){
			int i;
			System.out.print("\t| ");
			for (i=0;i<tam;i++){
				System.out.print(buf[i]+" | ");
			}
			System.out.println("");
	}

}

class Semaforo {
	private int balioa;

	public Semaforo (int hasi){
		balioa = hasi;
	}

	synchronized public void gora(){
		++balioa;
		notify();
	}

	synchronized public void behera() throws InterruptedException{
		while (balioa==0){
			wait();
		}
		--balioa;
		notify();
	}
}

class Idazlea extends Thread{
	SemaBuffer buf;
	String alphabet ="abcdefghijklmnopqrstuvwxyz";

	Idazlea (SemaBuffer b) {
		buf = b;
	}

	public void run(){
		try{
			int ai=0;
			while (true){
				if (Math.random()<0.3){
					sleep(1000);
				}
				System.out.println(alphabet.charAt(ai)+">");
				buf.put(alphabet.charAt(ai));
				ai=(ai+1)%alphabet.length();
			}
		}
		catch (InterruptedException e){}
	}
}

class Irakurlea extends Thread{
	SemaBuffer buf;
	String alphabet ="abcdefghijklmnopqrstuvwxyz";

	Irakurlea (SemaBuffer b) {
		buf = b;
	}

	public void run(){
		try{
			int ai=0;
			while (true){
				if (Math.random()<0.3){
					sleep(1000);
				}
				System.out.println("\t\t\t\t\t"+">"+buf.get());
				ai=(ai+1)%alphabet.length();
			}
		}
			catch (InterruptedException e){}
	}
}

