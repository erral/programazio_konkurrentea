/*
* Aparkalekuaren ariketa
*
* Mikel Larreategi
* 2003-3-6
*/

class AparkalekuaApp {
	final static int Plazak = 4;
	public static void main (String args[]){
		Kontrolatzailea k = new Kontrolatzailea(Plazak);
		Sarrerak sar = new Sarrerak(k);
		Irteerak irt = new Irteerak(k);
		sar.start();
		irt.start();
	}
}

class Sarrerak extends Thread{
	// Atributuak
	Kontrolatzailea aparkalekua;

	// Metodoak

	Sarrerak (Kontrolatzailea k){
		aparkalekua = k;
	}

	public void run(){
		try{
			while (true){
				Simulatu.HWInterrupt();
				aparkalekua.sartu();
				System.out.println("Kotxe bat sartu da: " + aparkalekua.plazak() + " plaza libre");
			}
		}
		catch(InterruptedException e){}
	}
}

class Irteerak extends Thread{
	// Atributuak
	Kontrolatzailea aparkalekua;

	// Metodoak

	Irteerak (Kontrolatzailea k){
		aparkalekua = k;
	}

	public void run(){
		try{
			while (true){
				Simulatu.HWInterrupt();
				aparkalekua.irten();
				System.out.println("Kotxe bat irten da: " + aparkalekua.plazak() + " plaza libre");
			}
		}
		catch(InterruptedException e){}
	}
}

class Kontrolatzailea {
	// Atributuak
	private int lekuak;
	private int edukiera;

	// Metodoak

	Kontrolatzailea (int k){
		lekuak = edukiera = k;
	}

	public synchronized int plazak(){
		return lekuak;
	}

	synchronized void sartu() throws InterruptedException{
		while (lekuak==0) {
			wait();
		}
		lekuak = lekuak - 1;
		notifyAll();
	}

	synchronized void irten() throws InterruptedException{
		while(lekuak==edukiera){
			wait();
		}
		lekuak = lekuak +1;
		notifyAll();
	}
}

class Simulatu {
	public static void HWInterrupt(){
		if (Math.random()<0.5){
			try{
				Thread.sleep(200);
			}
			catch (InterruptedException e){}
		}
	}
}
