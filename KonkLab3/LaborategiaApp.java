/* Programazio Konkurrentea 2002-2003
*
*  Muga izeneko monitorearen inplementazioa, "sink" metodo batekin
*  ziurtatzen duena N hari guztiek deitu behar dutela "sink" martxan jartzeko
*
*/

class LaborategiaApp{
	public static final int N = 5;

	public static void main (String args[]){
		// Prozesuen array-a
		Prozesu prozesuak[] = new Prozesu[N];
		// Monitorea
		Monitore muga = new Monitore(N);

		for(int i =0; i<N; i++){
			prozesuak[i] = new Prozesu(muga, i);
		}

		for(int i=0; i<N; i++){
			prozesuak[i].start();
		}
	}
}

class Prozesu extends Thread{
	private Monitore monitorea;
	private int izena;

	Prozesu (Monitore monitorea, int izena){
		this.monitorea = monitorea;
		this.izena = izena;
	}

	public void run(){
		Simulatu.HWInterrupt();
		System.out.println(izena+" prozesuak exekutatu nahi du");
		monitorea.sink(izena);
		System.out.println(izena+" prozesua aurrera!!!");

	}
}

class Monitore{

	private boolean prozesuak[];
	private int luzera;

	Monitore(int i){
		luzera = i;
		prozesuak = new boolean[i];
		for (int j=0; j<i;j++){
			prozesuak[j] = false;
		}
	}

	public synchronized void sink(int i){
		prozesuak[i] = true;
		notifyAll();
		while(!guztiak()){
			try{
				wait();
			}
			catch(InterruptedException e) {}
		}
	}

	public synchronized boolean guztiak(){
		boolean emaitza = true;
		for(int i=0;i<luzera;i++){
			emaitza = emaitza && prozesuak[i];
		}
		return emaitza;
	}
}

 class Simulatu {
	 public static void HWInterrupt(){
 		if (Math.random()<0.5){
 			try{Thread.sleep(200);}
 			catch (InterruptedException e){}
 		}
 	}
}