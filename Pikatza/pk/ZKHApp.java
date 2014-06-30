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

class ZKHApp{

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

		while (!v.bukaera()){
			try{
				a = v.jaso();
			}
			catch (InterruptedException e) {}
			if (!v.bukaera()){
				c = ZKHEuclides(a[0],a[1]);
				v.utzi(c);
			}
		}
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

	boolean bukaera = false;

	Object baldintza = new Object();

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

		/*synchronized(this){
			// itzuli beharreko arraya sortu
			int[] a = new int[2];
			while(!(
					(v.size()>=2)
					||
					((v.size()< 2) && (n==0))
					)
					)
					{
				try{
					wait();
				}
				catch(InterruptedException e){}
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
				// bukaera heldu da. Idatzi emaitza pantailan
				System.out.println("ZKH="+((Integer)v.peek()).intValue());
			}
			notifyAll();
			return a;
		}*/
		int[] a = new int[2];
		await:
			synchronized(baldintza){
				while(true){
					synchronized(this){
						if((v.size()>=2)||((v.size()< 2) && (n==0))){
							if(!(v.size()<2 && n==0)){
								//System.out.println("PILA jaso: " + v);
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
							break await;
						}
						baldintza.wait();
					}
				}
			}
		return a;

	}


	public boolean bukaera(){
		return bukaera;
	}

	public void utzi(int c){
		synchronized(this){
			v.push(new Integer(c));
			//System.out.println("PILA-utzi "+v);
			--n;
			synchronized(baldintza){
				notifyAll();
			}
		}
	}
}