class geltokiaApp{

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

	public Object autobusera = new Object();
	public Object kokatuta = new Object();


	Geltokia (int autobusKop, int bidaiariKop, int plazaKop){
		bidaiariAutobusean = new int[bidaiariKop];
		kokatua = new boolean[autobusKop];
		autobusBidaiariKop = new int[autobusKop];
		plazaLibreak = new int[autobusKop];
		this.autobusKop = autobusKop;
		this.bidaiariKop = bidaiariKop;
		this.plazaKop = plazaKop;

	}

	public void kokatu(int autobusa){

		synchronized(this){

			kokatua[autobusa] = true;
			plazaLibreak[autobusa] = plazaKop;
			autobusBidaiariKop[autobusa] = 0;
			synchronized (autobusera){
				autobusera.notifyAll();
			}
		}
	}

	public void atera (int autobusa) throws InterruptedException{
		await:
		synchronized(kokatuta){
			while(true){
				synchronized(this){
					if(autobusBidaiariKop[autobusa] == plazaKop){
						kokatua[autobusa] = false;
						break await;
					}

				}
				kokatuta.wait();

			}
		}

	}

	public void igo (int bidaiaria){
		boolean betea = false;
		int autobusa =0;

		await:
		synchronized(autobusera){

			while(true){
				synchronized(this){
					if (libreEtaPlazak()){
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
						break await;
					}
					try{
						autobusera.wait();
					}
					catch (InterruptedException e){}
				}
			}

		}
		if(betea){
			synchronized (kokatuta){
				kokatuta.notifyAll();
				System.out.println(autobusa +" autobusa atera da geltokitik.");
			}
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
		//while(true){
			geltoki.kokatu(izena);
			try{
			geltoki.atera(izena);
			//Thread.sleep((int)(100*Math.random()));
			}
			catch (InterruptedException e){}

		//}
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
		//while(true){
			geltoki.igo(izena);
			/*try{
				sleep((int)(1000*Math.random()));
			}
			catch (InterruptedException e){}*/
		//}
	}
}