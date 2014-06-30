/* Programazio Konkurrentea 2002-2003
 * Zatitzaile Komun Handienaren kalkulua, agendaren eredua erabiliz
 *
 */

// Agenda inplementatzeko Javako Stack klasea erabili dut, array normalak
// erabiltzean, elementuak ezabatzearen arazoa aurkitu dut, ezin baititut
// array arruntetik kendu. -1 edo antzeko kode bereziren bat erabil nezakeen
// baina lana askoz nekezagoa litzateke. Stack klasea erabiliz push eragiketa
// erabiltzen dut agendara elementuak sartzeko eta pop eragiketa agendatik
// elementuak kentzeko. Funtzio horietaz gain size funtzioa erabiltzen dut
// agendaren tamaina zein den jakiteko.

import java.util.*;

class ZKHSemaforoApp{

	public static void main(String args[]){
		Erregistroa v = new Erregistroa();

		for (int i=0; i<6; i++){
			// Prozesuak sortu eta martxan jarri erregistro konpartitua pasatuz
			(new Prozesua(i,v)).start();
		}

	}

}

class Prozesua extends Thread{
	boolean bukaera;
	int izena;
	Erregistroa v;

	public Prozesua(int izena, Erregistroa v){
		this.izena = izena;
		this.v = v;
		bukaera = false;

	}

	public void run(){

		int[] a = new int[2];
		int b = 0;
		int c;
		try{
			while (!v.bukaera()){
				a = v.jaso();
				if (!v.bukaera()){
					c = ZKHEuclides(a[0],a[1]);
					v.utzi(c);
				}
			}
		}

		catch(InterruptedException e){}

	}

	// Zatitzaile Komun Handienaren kalkulua Euclidesen algoritmoa jarraituz
	public int ZKHEuclides(int m, int n){
		int a = m;
		int b = n;
		int t;
		while (a > 0){
			t= b % a;
			b= a;
			a= t;
		}
		return b;
	}
}

class Erregistroa{
	Stack v;
	int n;
	Semaforoa e = new Semaforoa(1);
	Semaforoa b = new Semaforoa(0);
	int b1 = 0;

	boolean bukaera = false;

	public Erregistroa(){
		// agenda inplementatzeko pila sortu
		v = new Stack();
		n = 0;
		for (int i=0; i<4; i++){
			// zenbakiak ausaz sortu eta Integer motara bihurtu pilan sartzeko
			v.push(new Integer((int)(10*Math.random())));

		}
		// agenda pantailan erakutsi
		System.out.println(v);
	}


	// funtzio honek bi elementuko array bat itzuliko du
	// balio bi itzuli behar dituenez funtzio normal batekin
	// ezin da inplementatu
	public int[] jaso() throws InterruptedException{

		int[] a = new int[2];

		if(!((v.size()>=2)||((v.size()< 2) && (n==0)))){
			++b1;
			e.V();
			b.P();
		}
		if(!(v.size()<2 && n==0)){
			++n;
			// pilatik elementu bi hartu eta itzuli beharreko
			// array-ean gorde
			a[0] = ((Integer)v.pop()).intValue();
			a[1] = ((Integer)v.pop()).intValue();
		}
		else{
			bukaera = true;
			System.out.println("ZKH="+((Integer)v.peek()).intValue());
		}
		// SEINALEZTAPENA
		if((v.size()>=2)||((v.size()< 2) && (n==0)) && b1>0){
			--b1;
			b.V();
		}
		else{
			e.V();
		}
		return a;
	}


	public boolean bukaera(){
		return bukaera;
	}

	public void utzi(int c) throws InterruptedException{
		e.P();
		v.push(new Integer(c));
		--n;
		// Seinaleztapena
		if((v.size()>=2)||((v.size()< 2) && (n==0)) && b1>0){
			--b1;
			b.V();
		}
		else{
			e.V();
		}

	}
}

class Semaforoa {
	private int balioa;
	private Object itxaron = new Object();

	Semaforoa (int hasierakoa){
		balioa = hasierakoa;
	}

	public void P() throws InterruptedException {
		await:
		synchronized(itxaron){
			while(true){
				synchronized(this){
					if(balioa>0){
						--balioa;
						break await;
					}
				}
				itxaron.wait();
			}
		}
	}

	public void V() throws InterruptedException{
		synchronized(this){
			++balioa;
		}
		synchronized(itxaron){
			itxaron.notify();
		}
	}
}

