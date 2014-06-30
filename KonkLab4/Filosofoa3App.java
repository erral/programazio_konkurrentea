/* Programazio Konkurrentea 2002-2003
*
*  Filosofoen ariketa, semaforo bat erabiliz, mahaian 4 filosofo
*  baino gehiago eser ez daitezen
*
*  Mikel Larreategi Arana
*  2003-3-13
*
*/


class Filosofoa3App {

	public static int N = 5;
	static String tabul[] = new String[5];


	public static void main (String args[]){
		Sardeska sardeska[] = new Sardeska[N];
		Filosofoa fil[] = new Filosofoa[N];
		Semaforo maiordomoa = new Semaforo (4);

		System.out.println("Sardeak |                              Filosofoak                              ");
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
			fil[i] = new Filosofoa(i,sardeska[(i-1+N)%N],sardeska[i],tabul[i],maiordomoa);
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

	synchronized void get() throws InterruptedException {
		while(hartua) {
			wait();
		}
		hartua = true;
		System.out.println(zenbakia + " hartua|");
	}
}

class Filosofoa extends Thread {

	public String tartea;
	public Sardeska ezkerrekoa;
	public Sardeska eskubikoa;
	public int zenbakia;
	public Semaforo maiordomoa;

	Filosofoa(int i, Sardeska ezkerra, Sardeska eskuina, String tarte, Semaforo maior){
		tartea = tarte;
		ezkerrekoa = ezkerra;
		eskubikoa = eskuina;
		zenbakia = i;
		maiordomoa = maior;
	}

	public void run(){
		try{
			while (true){
				System.out.println(tartea + " pentsatzen");
				sleep((int)(1000*Math.random()));
				System.out.println(tartea + " gose");
				// maiordomoari esan esertzera doala. Lekurik ez badago blokeatu
				maiordomoa.eseri();
				eskubikoa.get();
				System.out.println(tartea + " eskub.hartu");
				sleep(500);
				ezkerrekoa.get();
				System.out.println(tartea + " ezker.hartu");
				System.out.println(tartea + " jaten");
				sleep((int)(500*Math.random()));
				eskubikoa.put();
				ezkerrekoa.put();
				// maiordomoari esan altxatzera doala.
				maiordomoa.altxa();
			}
		}
		catch (InterruptedException e) {}
	}
}


class Semaforo{
	// Atributua
	int balioa;

	//Metodoak
	public Semaforo (int hasierakoa){
		balioa = hasierakoa;
	}

	synchronized public void altxa(){
		++balioa;
		System.out.println(balioa + " libre |");
		notify();
	}

	synchronized public void eseri() throws InterruptedException{
		while (balioa==0){
			wait();
		}
		--balioa;
		System.out.println(balioa + " libre |");
	}
}

