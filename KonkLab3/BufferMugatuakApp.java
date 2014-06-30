/*
* Buffer mugatuak
*
* Mikel Larreategi
* 2003-3-6
*/


class BufferMugatuakApp {

	public static void main (String args[]){
		Buffer buf = new Buffer(6);
		Idazlea ida = new Idazlea(buf);
		Irakurlea ira = new Irakurlea(buf);
		ida.start();
		ira.start();
	}


}

class Buffer{

	private int tam;
	private int in;
	private int out;
	private char[] buf;
	private int kont;

	Buffer (int tamaina){
		int i;

		tam = tamaina;
		buf = new char[tam];
		in = out = 0;
		kont = 0;
		for (i=0;i<tam;i++){
			buf[i]=' ';
		}
	}

	public void erakutsi(){
		int i;
		System.out.print("\t| ");
		for (i=0;i<tam;i++){
			System.out.print(buf[i]+" | ");
		}
		System.out.println("");
	}
	public synchronized void put(char c) throws InterruptedException{
		while(kont==tam){
			wait();
		}
		buf[in] = c;
		++kont;
		in=(in+1)%tam;
		erakutsi();
		notify();
	}

	public synchronized char get() throws InterruptedException{
		while (kont==0){
			wait();
		}
		char c = buf[out];
		buf[out]=' ';
		--kont;
		out=(out+1)%tam;
		erakutsi();
		notify();
		return (c);
	}
}

class Idazlea extends Thread{
	Buffer buf;
	String alphabet ="abcdefghijklmnopqrstuvwxyz";

	Idazlea (Buffer b) {
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
	Buffer buf;
	String alphabet ="abcdefghijklmnopqrstuvwxyz";

	Irakurlea (Buffer b) {
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