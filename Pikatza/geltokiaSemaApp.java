class geltokiaSemaApp{

	public static final int autobusKop = 5;
	public static final int bidaiariKop = 50;
	public static final int plazaKop = 10;

	public static Semaforo e;
	public static Semaforo igo;
	public static Semaforo atera;
	public static int igodj;
	public static int ateradj;

	public static int[] bidaiariAutobusean;
	public static boolean[] kokatua;
	public static int[] autobusBidaiariKop;
	public static int[] plazaLibreak;

	public static void main (String args[]){

		bidaiariAutobusean = new int[bidaiariKop];
		kokatua = new boolean[autobusKop];
		autobusBidaiariKop = new int[autobusKop];
		plazaLibreak = new int[autobusKop];


		e = new Semaforo(1);
		igo = new Semaforo(0);
		atera = new Semaforo (0);
		igodj = 0;
		ateradj = 0;

		// Autobus prozesuen arraya
		Autobus[] autobusak = new Autobus[autobusKop];

		for (int i=0; i<autobusKop; i++){
			autobusak[i] = new Autobus(i);
			autobusak[i].start();
		}

		// Bidaiariak sortu
		Bidaiari[] bidaiariak = new Bidaiari[bidaiariKop];

		for (int i=0; i<bidaiariKop; i++){
			bidaiariak[i] = new Bidaiari(i);
		}

		for (int i=0; i<bidaiariKop; i++){
			bidaiariak[i].start();
		}


	}
	public static boolean libreEtaPlazak(){
			for (int i = 0; i<autobusKop;i++){
				if(kokatua[i] && plazaLibreak[i]>0){
					return true;
				}
			}
			return false;
	}
}





class Autobus extends Thread{

	int izena;

	Autobus(int izena){
		this.izena = izena;

	}

	public void run(){
		try{
			geltokiaSemaApp.e.P();
		}
		catch (InterruptedException e) {}
		geltokiaSemaApp.kokatua[izena] = true;
		geltokiaSemaApp.plazaLibreak[izena] = geltokiaSemaApp.plazaKop;
		geltokiaSemaApp.autobusBidaiariKop[izena] = 0;

		// SEINALEZTAPENA
		if ((geltokiaSemaApp.autobusBidaiariKop[izena] == geltokiaSemaApp.plazaKop) && geltokiaSemaApp.ateradj >0){
			geltokiaSemaApp.ateradj--;
			geltokiaSemaApp.atera.V();
		}
		else{
			if (geltokiaSemaApp.libreEtaPlazak() && geltokiaSemaApp.igodj>0){
				geltokiaSemaApp.igodj--;
				geltokiaSemaApp.igo.V();
			}
			else{
				geltokiaSemaApp.e.V();
			}
		}




			if(! (geltokiaSemaApp.autobusBidaiariKop[izena] == geltokiaSemaApp.plazaKop)){
				geltokiaSemaApp.ateradj++;
				geltokiaSemaApp.e.V();
				try{
					geltokiaSemaApp.atera.P();
				}
				catch (InterruptedException e){}
			}
			geltokiaSemaApp.kokatua[izena] = false;
			System.out.println(izena + " geltokitik atera da.");
			// SEINALEZTAPENA
			if ((geltokiaSemaApp.autobusBidaiariKop[izena] == geltokiaSemaApp.plazaKop) && geltokiaSemaApp.ateradj >0){
				geltokiaSemaApp.ateradj--;
				geltokiaSemaApp.atera.V();
			}
			else{
				if (geltokiaSemaApp.libreEtaPlazak() && geltokiaSemaApp.igodj>0){
					geltokiaSemaApp.igodj--;
					geltokiaSemaApp.igo.V();
				}
				else{
					geltokiaSemaApp.e.V();
				}
			}

	}
}

class Bidaiari extends Thread{

	int izena;

	Bidaiari(int izena){
		this.izena = izena;

	}

	public void run(){
		int autobusa =0;

		if (!geltokiaSemaApp.libreEtaPlazak()){
			geltokiaSemaApp.igodj++;
			geltokiaSemaApp.e.V();
			try{
				geltokiaSemaApp.igo.P();
			}
			catch (InterruptedException e){}
		}

		for(int i =0; i<geltokiaSemaApp.autobusKop;i++){
			if (geltokiaSemaApp.kokatua[i] && geltokiaSemaApp.plazaLibreak[i]>0){
				geltokiaSemaApp.bidaiariAutobusean[izena] = i;
				++geltokiaSemaApp.autobusBidaiariKop[i];
				autobusa = i;
				i = geltokiaSemaApp.autobusKop;
			}
		}
		// SEINALEZTAPENA
		if ((geltokiaSemaApp.autobusBidaiariKop[autobusa] == geltokiaSemaApp.plazaKop) && geltokiaSemaApp.ateradj >0){
			geltokiaSemaApp.ateradj--;
			geltokiaSemaApp.atera.V();
		}
		else{
			if (geltokiaSemaApp.libreEtaPlazak() && geltokiaSemaApp.igodj>0){
				geltokiaSemaApp.igodj--;
				geltokiaSemaApp.igo.V();
			}
			else{
				geltokiaSemaApp.e.V();
			}
		}
		try{
			geltokiaSemaApp.e.P();
		}
		catch (InterruptedException e){}
		--geltokiaSemaApp.plazaLibreak[autobusa];
		System.out.println(izena + " bidaiaria, "+autobusa+" autobusera igo da ("+geltokiaSemaApp.plazaLibreak[autobusa]+" plaza libre)");
		// SEINALEZTAPENA
		if ((geltokiaSemaApp.autobusBidaiariKop[autobusa] == geltokiaSemaApp.plazaKop) && geltokiaSemaApp.ateradj >0){
			geltokiaSemaApp.ateradj--;
			geltokiaSemaApp.atera.V();
		}
		else{
			if (geltokiaSemaApp.libreEtaPlazak() && geltokiaSemaApp.igodj>0){
				geltokiaSemaApp.igodj--;
				geltokiaSemaApp.igo.V();
			}
			else{
				geltokiaSemaApp.e.V();
			}
		}

	}
}

class Semaforo{
	// Atributua
	int balioa;

	// Metodoak
	public Semaforo (int hasierakoa){
		balioa = hasierakoa;
	}

	synchronized public void V(){
		++balioa;
	//	System.out.println(balioa);
		notify();
	}

	synchronized public void P() throws InterruptedException{
		while (balioa==0){
			wait();
		}
		--balioa;
	//	System.out.println(balioa);
	}
}