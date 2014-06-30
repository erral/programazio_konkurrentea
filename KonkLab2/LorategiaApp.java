/** Programazio Konkurrentea 2002-2003
*   2003-2-27
*	Lorategiaren ariketa
*
*	Mikel Larreategi
*/


class LorategiaApp{
	public final static int MAX = 6;

	public static void main (String args[]){
		//System.out.println("LORATEGIA: sakatu return hasteko");
		//try{ int c = System.in.read();}
		//catch (Exception ex) {}

		// iritsi eta kontatzen ez dituena:
		// Kontagailua k = new Kontagailua();

		// guztiak kontatzen dituena:
		KontagailuSinkr k = new KontagailuSinkr();

		Atea aurrekoa = new Atea("aur",k);
		Atea atzekoa = new Atea ("\tatz",k);
		aurrekoa.start();
		atzekoa.start();
	}


}
class Kontagailua {
		int balioa = 0;

		Kontagailua () {
			System.out.println("\t\tguztira:" + balioa);
		}

		void gehitu (){

			int lag;
			lag = balioa;
			Simulatu.HWinterrupt();
			balioa = lag + 1;
			System.out.println("\t\tguztira:" + balioa);
			// Batuketa era honetan egin beharrean balioa = balioa + 1; eran eginez
			// arazoa konponduta daukagu, sententzia bat atomikoa delako eta Javak
			// horrela hartzen duelako.
			// Gauza da, "gaizki" egitea nahi dugula, eta batuketa egiten duen artean
			// lotara joan daitekeela HWinterrupt(); horri esker
		}
}
class KontagailuSinkr extends Kontagailua {
	KontagailuSinkr() {
		super();
	}
	// synchronized honekin elkarrekiko esklusioa egiten dugu
	// Javaz, horrela interferentziarik egon ez dadin
	synchronized void gehitu(){
		super.gehitu();
	}
}

class Atea extends Thread {
		Kontagailua jendea;
		String atearenIzena;

		public Atea (String zeinAte, Kontagailua k){
			jendea = k;
			atearenIzena = zeinAte;
		}

		public void run(){
			try{
				System.out.println(atearenIzena + ">" + jendea.balioa);
				for (int i = 1; i<=LorategiaApp.MAX; i++){
					Thread.sleep(1000);
					System.out.println(atearenIzena + ">" + i);
					jendea.gehitu();
				}
			}
			catch (InterruptedException e){}
		}
}

class Simulatu {
		public static void HWinterrupt(){
			if (Math.random()<0.5){
				try{Thread.sleep(200);}
				catch (InterruptedException e){}
			}
		}
	}