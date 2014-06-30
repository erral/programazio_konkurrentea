/* Programazio Konkurrentea 2002-2003
*
*  Pilaren arazoa
*  Momentuz ez du kontrolatzen behekoa inoiz irten ahal ez izatea
*
*  Bertsio hau, Javaren Stack klasea erabiliz inplementatuta dago
*  2002-3-22
*/

import java.util.*;

class StackKontrolatuaApp {
	public static final int max = 9;

	public static void main (String args[]){

		Pila pila = new Pila();
		Prozesu proz[] = new Prozesu[max];

		for (int i=0; i<max; i++){
			proz[i] = new Prozesu(i,pila);
		}

		for (int i=0; i<max;i++){
			proz[i].start();
		}
	}
}

class Pila extends Stack{

	private Object a;
	private int sarreraKop;
	private boolean husten;
	private static final denbora = 10;


	Pila(){
		super();
	}

	public synchronized Object peek(){
		return super.peek();
	}

	public synchronized Object push(Integer i){
		while (sarreraKop == denbora || husten){
			try{
				System.out.println(i + " ezin da sartu pila hustu arte");
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.println(i+" prozesua pilara sartu da");
		sarreraKop = sarreraKop +1;
		super.push(i);
		if (sarreraKop == denbora){
			husten = true;
		}
		show();
		return a;
	}

	public synchronized Object pop(Integer zenbakia){
		while (! peek().equals(zenbakia)){
			try {
				wait();
				System.out.println("\t\t\t"+zenbakia+" -k irten nahi du");
			}
			catch (InterruptedException e) {}
		}
		if (sarreraKop == denbora || husten){
					sarreraKop = 0;
		}
		super.pop();
		if (super.empty()){
			husten = false;
		}
		show();
		notifyAll();
		return a;
	}

	public synchronized void show(){
		Stack lag = new Stack();
		lag = (Stack)(super.clone());
		while(! lag.empty()){
			System.out.println("\t\t| "+lag.peek()+" |");
			lag.pop();
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
					pila.push(new Integer(zenbakia));
					sleep((int)(10000 * Math.random()));
					System.out.println("\t\t\t"+zenbakia+" -k irten nahi du");
					pila.pop(new Integer(zenbakia));
					System.out.println("\t\t\t"+zenbakia+ " irten da");
					//sleep((int)(1000* Math.random()));


			}
			catch (InterruptedException e) {}
		}
	}
}