/* Programazio Konkurrentea 2002-2003
*
*  Pilaren arazoa
*  Momentuz ez du kontrolatzen behekoa inoiz irten ahal ez izatea
*
*  2002-3-22
*/

class PilaApp {
	public static final int max = 9;

	public static void main (String args[]){

		Pila pila = new Pila(100);
		Prozesu proz[] = new Prozesu[max];

		for (int i=0; i<max; i++){
			proz[i] = new Prozesu(i,pila);
		}

		for (int i=0; i<max;i++){
			proz[i].start();
		}
	}
}

class Pila {
	private int lista[];
	private int gailurra;
	Pila(int i){
		lista = new int[i];
		gailurra = 0;
	}

	public synchronized int peek(){
		return lista[gailurra];
	}

	public synchronized void push(int i){
		++gailurra;
		lista[gailurra] = i;
		show();
	}

	public synchronized void pop(int zenbakia){
		while (lista[gailurra] != zenbakia){
			try{
				wait();
				System.out.println("\t\t\t"+zenbakia+"-k irten nahi du");
			}
			catch (InterruptedException e) {}
		}
		--gailurra;
		show();
		notifyAll();

	}

	public synchronized void show(){
		for (int i=gailurra;i>0;i--){
			System.out.println("\t\t| "+lista[i]+" |");
		}
		System.out.println("\t\t ___ ");
	}
}

class Prozesu extends Thread{
	private int zenbakia;
	private Pila pila;

	Prozesu(int zenbakia, Pila pila){
		this.zenbakia = zenbakia;
		this.pila = pila;
	}

	public void run(){
		while(true){
			try{


					System.out.println(zenbakia+" prozesua pilara");
					pila.push(zenbakia);
					sleep((int)(10000 * Math.random()));
					System.out.println("\t\t\t"+zenbakia+"-k irten nahi du");
					pila.pop(zenbakia);
					System.out.println("\t\t\t"+zenbakia+ " irten da");
					//sleep((int)(1000* Math.random()));


			}
			catch (InterruptedException e) {}
		}
	}
}