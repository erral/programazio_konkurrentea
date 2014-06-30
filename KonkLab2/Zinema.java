/*	Programazio Konkurrentea 2002-2003
*	2003-2-27
*	Zinemaren ariketa
*
*	Mikel Larreategi
*/

class Zinema {

	public static void main (String args[]){

		Eserlekua es = new Eserlekua();
		Eroslea e1 = new Eroslea("Mikel","",es);
		Eroslea e2 = new Eroslea("Aitor","\t\t\t",es);

		e1.start();
		e2.start();
	}
}

class Eserlekua {
	boolean jarlekuak[];

	Eserlekua(){
		int i;
		jarlekuak = new boolean[15];
		for (i=0; i<15; i++){
			jarlekuak[i] = false;
		}
	}

	void erreserbatu(int i,String tartea){
		int j;
		// i. eserlekua erreserbatu
		if (jarlekuak[i]== false){
			jarlekuak[i]= true;
			System.out.println(tartea+"Jarleku hori erreserbatu duzu");
		}
		else{
			System.out.println(tartea+"Jarleku hori erreserbatuta dago");
		}
		Simulatu.HWinterrupt();
	}
}

class Eroslea extends Thread {

	String erosIzena;
	String tartea;
	Eserlekua eser;

	public Eroslea (String izena, String tarte,Eserlekua es){
		erosIzena = izena;
		tartea = tarte;
		eser = es;
	}

	public void run(){
		int i;
		try{
			for (i=0; i<15; i++){
				Thread.sleep((int)(1000*Math.random()));
				System.out.println(tartea+erosIzena+"-ek ,"+i+". eserlekua erreserbatu nahi du");
				eser.erreserbatu(i,tartea);
				Thread.sleep((int)(500*Math.random()));
			}
		}
		catch (InterruptedException e){
		};
	}
}

class Simulatu{
	public static void HWinterrupt(){
		if (Math.random() < 0.5){
			try{
				Thread.sleep(200);
			}
			catch(InterruptedException e){
			};
		}
	}
}