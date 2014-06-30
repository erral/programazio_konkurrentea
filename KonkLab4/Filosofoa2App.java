/* Programazio Konkurrentea 2002-2003
*
* Filosofoen ariketa, 19. gardenkian esaten den aldaketarekin
* get() metodoa boolean bat itzultzeko aldatuta
*
*/



class Filosofoa2App {

	public static int N = 5;
	static String tabul[] = new String[5];


	public static void main (String args[]){
		Sardeska sardeska[] = new Sardeska[N];
		Filosofoa fil[] = new Filosofoa[N];

		System.out.println("Sardeak |                                Filosofoak                              ");
		System.out.println("        |      0\t\t1\t\t2\t\t3\t\t4");
		System.out.println("========|==================================================================================");

		tabul[0] = "        |";
		for (int i=1; i<N; i++){
			tabul[i] = tabul[i-1] + "\t\t";
		}

		for (int i=0; i<N; i++){
			sardeska[i] = new Sardeska(i);
		}
		for (int i=0; i<N; i++){
			fil[i] = new Filosofoa(i,sardeska[(i-1+N)%N],sardeska[i],tabul[i]);
			fil[i].start();
		}
	}

}

class Sardeska{
	private boolean hartua = false;
	private int zenbakia;

	Sardeska(int zenb){
		zenbakia = zenb;
	}

	synchronized void put(){
		hartua = false;
		System.out.println(zenbakia + " utzia |");
		notify();
	}

	synchronized boolean get() throws InterruptedException {
		while(hartua) {
			// ALDAKETA : Segundu bat baino gehiago egiten badu zain, false itzultzen du
			wait(1000);
			return false;
		}
		hartua = true;
		System.out.println(zenbakia + " hartua|");
		return true;
	}
}

class Filosofoa extends Thread {

	public String tartea;
	public Sardeska ezkerrekoa;
	public Sardeska eskubikoa;
	public int zenbakia;

	Filosofoa(int i, Sardeska ezkerra, Sardeska eskuina, String tarte){
		tartea = tarte;
		ezkerrekoa = ezkerra;
		eskubikoa = eskuina;
		zenbakia = i;
	}

	public void run(){
		try{
			while (true){
				System.out.println(tartea + " pentsatzen");
				sleep((int)(1000*Math.random()));
				System.out.println(tartea + " gose");
				if (eskubikoa.get()){

					System.out.println(tartea + " eskub.hartu");
					sleep(500);
					if (ezkerrekoa.get()){

						System.out.println(tartea + " ezker.hartu");
						System.out.println(tartea + " jaten");
						sleep((int)(500*Math.random()));
						eskubikoa.put();
						System.out.println(tartea + " eskub.utzi");
						ezkerrekoa.put();
						System.out.println(tartea + " ezker.utzi");
					}
					else{ // Segundu bat baino gehiago zain egiten badu hartutakoa uzten du
						ezkerrekoa.put();
						System.out.println(tartea + " ezker.utzi");
					}
				}
			}
		}
		catch (InterruptedException e) {}
	}
}
