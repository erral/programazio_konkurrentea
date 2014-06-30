/* Programazio Konkurrentea 2002-2003
*
*  Pilaren arazoa
*  Prozesu bat sartu eta ezin ateratzearen arazoa konponduta
*
*  2002-3-22
*/

class PilaKontrolatuaApp {
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
	private int sarreraKop;
	private boolean husten;
	private static final int denbora = 10;

	Pila(int i){
		lista = new int[i];
		gailurra = 0;
	}

	public synchronized int peek(){
		return lista[gailurra];
	}

	public synchronized void push(int i){
		while(sarreraKop == denbora || husten){
			try{
				System.out.println(i + " ezin da sartu pila hustu arte");
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.println(i+" prozesua pilara sartu da");
		sarreraKop = sarreraKop + 1;
		++gailurra;
		lista[gailurra] = i;
		if (sarreraKop == denbora){
			husten = true;
		}
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
		if (sarreraKop == denbora || husten){
			sarreraKop = 0;
		}
		--gailurra;
		if (gailurra == 0){
			husten = false;
		}
		show();
		notifyAll();

	}

	public synchronized void show(){
		for (int i=gailurra;i>0;i--){
			System.out.println("\t\t| "+lista[i]+" |");
		}
		System.out.println("\t\t ~~~ ");
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


					System.out.println(zenbakia+" prozesuak pilara sartu nahi du");
					pila.push(zenbakia);
					sleep((int)(1000 * Math.random()));
					System.out.println("\t\t\t"+zenbakia+"-k irten nahi du");
					pila.pop(zenbakia);
					System.out.println("\t\t\t"+zenbakia+ " irten da");
					//sleep((int)(1000* Math.random()));


			}
			catch (InterruptedException e) {}
		}
	}
}