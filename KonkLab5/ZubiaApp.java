/*
*  Programazio Konkurrentea 2002-2003
*  2003-3-20
*  Bide bakarreko zubiaren problema
*
*/

class ZubiaApp {
	public final static int max = 2;
	public static void main (String args[]){
		KotxeGorria gorria[] = new KotxeGorria[max];
		KotxeUrdina urdina[] = new KotxeUrdina[max];
		Pantaila p = new Pantaila(max);
		Zubia b = new ZubiSegurua();
		//Zubia b = new Zubia();

		for (int i = 0; i< max; i++){
			gorria[i] = new KotxeGorria(b,p,i);
			urdina[i] = new KotxeUrdina(b,p,i);
		}
		for (int i = 0; i< max; i++){
			gorria[i].start();
			urdina[i].start();
		}
	}
}

class KotxeGorria extends Thread {
	Zubia kontrola;
	Pantaila erakutsi;
	int zenb;

	KotxeGorria(Zubia b, Pantaila erak, int zenb){
		kontrola = b;
		erakutsi = erak;
		this.zenb = zenb;
	}

	public void run(){
		try{
			while(true){
				while(!erakutsi.mugituGorria(zenb)){
					;
				}
				kontrola.sartuGorria();
				while(erakutsi.mugituGorria(zenb)){
					;
				}
				kontrola.irtenGorria();
			}
		}
		catch (InterruptedException e) {}
	}
}

class KotxeUrdina extends Thread {
	Zubia kontrola;
	Pantaila erakutsi;
	int zenb;

	KotxeUrdina(Zubia b, Pantaila erak, int zenb){
		kontrola = b;
		erakutsi = erak;
		this.zenb = zenb;
	}

	public void run(){
		try{
			while(true){
				while(!erakutsi.mugituUrdina(zenb)){
					;
				}
				kontrola.sartuGorria();
				while(erakutsi.mugituUrdina(zenb)){
					;
				}
				kontrola.irtenUrdina();
			}
		}
		catch (InterruptedException e) {}
	}
}

class Zubia {
	synchronized void sartuGorria() throws InterruptedException{}
	synchronized void irtenGorria(){}
	synchronized void sartuUrdina() throws InterruptedException{}
	synchronized void irtenUrdina(){}
}

class ZubiSegurua extends Zubia{
	private int kGorria = 0; // Zubian dagoen kotxe gorri kopurua
	private int kUrdina = 0; // Zubian dagoen kotxe urdin kopurua

	synchronized void sartuGorria() throws InterruptedException{
		while (kUrdina>0){
			wait();
		}
		++kGorria;
	}
	synchronized void irtenGorria(){
		--kGorria;
		if (kGorria == 0){
			notifyAll();
		}
	}
	synchronized void sartuUrdina() throws InterruptedException{
		while (kGorria>0){
			wait();
		}
		++kUrdina;
	}
	synchronized void irtenUrdina(){
		--kUrdina;
		if (kUrdina == 0){
			notifyAll();
		}
	}
}

class Pantaila{
	    int pos=11;
		int zubezk=pos/2;
		int zubesk=(pos/2)+1;
		int max;
		int[] gorriaX,urdinaX;
		String[] tabul;

		Pantaila(int n) {
	 		max = n;
			gorriaX  = new int[max];
			urdinaX = new int[max];
			for (int i = 0; i<max ; i++) {
				gorriaX[i] = i;
			    urdinaX[i] = pos-i;
			}

	   		tabul = new String[pos+1];
		  	for (int i=0; i<pos+1; ++i){
				tabul[i]="";
				for (int j=0; j<i; ++j){
					tabul[i]=tabul[i]+"\t";
				}
		  	}
			pantailaratu();
		}

		public void pantailaratu (){
			for (int i = 0; i<max ; i++) 							//gorriak
				if (gorriaX[i]>=max)
					System.out.println(tabul[gorriaX[i]]+"gorria"+i+">>");
			for (int i = 0; i<max ; i++)  							//urdinak
				if (urdinaX[i]<=pos-max)
					System.out.println(tabul[urdinaX[i]]+"<<urdina"+i);
			for (int i = 0; i<zubezk ; i++) System.out.print("\t"); //zubia
			for (int i = zubezk; i<zubesk ; i++) System.out.println("*******\t*******");
		}

	    boolean mugituGorria(int i) throws InterruptedException{
			int X = gorriaX[i];
			synchronized (this) {
				if      (X==pos-max && gorriaX[(i+1)%max]!=max) X=max;
				else if (X!=pos-max && gorriaX[(i+1)%max]!=X+1) X=X+1;
				if (gorriaX[i]!=X) {
					gorriaX[i]=X; pantailaratu();}
				//System.out.println(i+"gorri "+gorriaX[i]);
			}
			try{Thread.sleep(2000);} catch(InterruptedException e) {}
			return (X>=zubezk-1 && X<=zubesk);
		}

		boolean mugituUrdina(int i) throws InterruptedException{
			int X = urdinaX[i];
			synchronized (this) {
				if      (X==max && urdinaX[(i+1)%max]!=pos-max) X=pos-max;
				else if (X!=max && urdinaX[(i+1)%max]!=X-1 )    X=X-1;
				if (urdinaX[i]!=X) {
					urdinaX[i]=X; pantailaratu();}
			}
			try{Thread.sleep(2000);} catch(InterruptedException e) {}
			return (X>=zubezk && X<=zubesk+1);
	}
}
