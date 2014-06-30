class geltokiaSemaApp{

	public static final int autobusKop = 5;
	public static final int bidaiariKop = 50;
	public static final int plazaKop = 10;

	public static void main (String args[]){

		Geltokia gel = new Geltokia(autobusKop,bidaiariKop,plazaKop);
		// Autobus prozesuen arraya
		Autobus[] autobusak = new Autobus[autobusKop];

		for (int i=0; i<autobusKop; i++){
			autobusak[i] = new Autobus(i,gel);
			autobusak[i].start();
		}

		// Bidaiariak sortu
		Bidaiari[] bidaiariak = new Bidaiari[bidaiariKop];

		for (int i=0; i<bidaiariKop; i++){
			bidaiariak[i] = new Bidaiari(i,gel);
		}

		for (int i=0; i<bidaiariKop; i++){
			bidaiariak[i].start();
		}



	}
}


class Geltokia{
	// Bidaiari bakoitza zein autobusetan dagoen jakiteko
	int[] bidaiariAutobusean;

	// Autobusa geltokian kokatuta dagoen ala ez jakiteko
	boolean[] kokatua;

	// Autobus bakoitzaren bidaiari kopurua jakiteko
	int[] autobusBidaiariKop;

	// Autobus bakoitzaren plaza libre kopurua jakiteko
	int[] plazaLibreak;

	private int autobusKop;
	private int bidaiariKop;
	private int plazaKop;

	// bukaera aldagai boolear bat jarri bukaera kontrolatzeko:
	// bidaiariak agortzean => bukaera = true

	Semaforoa e = new Semaforoa(1);
	Semaforoa b1 = new Semaforoa(0);
	Semaforoa b2 = new Semaforoa(0);
	int d1 = 0;
	int d2 = 0;


	Geltokia (int autobusKop, int bidaiariKop, int plazaKop){
		bidaiariAutobusean = new int[bidaiariKop];
		kokatua = new boolean[autobusKop];
		autobusBidaiariKop = new int[autobusKop];
		plazaLibreak = new int[autobusKop];
		this.autobusKop = autobusKop;
		this.bidaiariKop = bidaiariKop;
		this.plazaKop = plazaKop;

	}

	public void kokatu(int autobusa)throws InterruptedException{

		e.P();
		kokatua[autobusa] = true;
		plazaLibreak[autobusa] = plazaKop;
		autobusBidaiariKop[autobusa] = 0;

		// SEINALEZTAPENA
		if((autobusBidaiariKop[autobusa] == plazaKop) && d1>0){
			--d1;
			b1.V();
		}
		else if(libreEtaPlazak() && d2>0){
			--d2;
			b2.V();
		}
		else{
			e.V();
		}

	}

	public void atera (int autobusa) throws InterruptedException{
		e.P();
		if(!(autobusBidaiariKop[autobusa] == plazaKop)){
			++d1;
			e.V();
			b1.P();
		}
		kokatua[autobusa] = false;

		// SEINALEZTAPENA
		if((autobusBidaiariKop[autobusa] == plazaKop) && d1>0){
			--d1;
			b1.V();
		}
		else if(libreEtaPlazak() && d2>0){
			--d2;
			b2.V();
		}
		else{
			e.V();
		}

	}

	public void igo (int bidaiaria)throws InterruptedException {
		boolean betea = false;
		int autobusa =0;

		e.P();
		if (!libreEtaPlazak()){
			++d2;
			e.V();
			b2.P();
		}
		for(int i =0; i<autobusKop;i++){
			if (kokatua[i] && plazaLibreak[i]>0){
				bidaiariAutobusean[bidaiaria] = i;
				++autobusBidaiariKop[i];
				--plazaLibreak[i];
				System.out.println(bidaiaria + " bidaiaria, "+i+" autobusera igo da ("+plazaLibreak[i]+" plaza libre)");
				betea = (plazaLibreak[i] == 0);
				autobusa = i;
				i = autobusKop;
			}
		}
		if(betea){
			System.out.println(autobusa +" autobusa atera da geltokitik.");

		}
		//SEINALEZTAPENA
		if((autobusBidaiariKop[autobusa] == plazaKop) && d1>0){
			--d1;
			b1.V();
		}
		else if(libreEtaPlazak() && d2>0){
			--d2;
			b2.V();
		}
		else{
			e.V();
		}

	}

	public synchronized boolean libreEtaPlazak(){
		for (int i = 0; i<autobusKop;i++){
			if(kokatua[i] && plazaLibreak[i]>0){
				return true;
			}
		}
		return false;
	}
}


class Autobus extends Thread{
	Geltokia geltoki;
	int izena;

	Autobus(int izena, Geltokia geltoki){
		this.izena = izena;
		this.geltoki = geltoki;
	}

	public void run(){
		try{
			geltoki.kokatu(izena);
			geltoki.atera(izena);

		}
		catch (InterruptedException e){}
	}
}

class Bidaiari extends Thread{
	Geltokia geltoki;
	int izena;

	Bidaiari(int izena, Geltokia geltoki){
		this.izena = izena;
		this.geltoki = geltoki;
	}

	public void run(){
		try{
			geltoki.igo(izena);
		}
		catch (InterruptedException e){}
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