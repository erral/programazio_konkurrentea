/*
* Semaforoen ariketa: 6 "|" idatzi eta gero "*"ak, hauek idaztea
* ekintza kritikotzat jota dago
*
* Mikel Larreategi
* 2003-3-6
*/

public class SemademoApp {
	public static void main (String args[]){
		Semaforo sem = new Semaforo(1);
		MutexLoop m1 = new MutexLoop(sem,3,"");
		MutexLoop m2 = new MutexLoop(sem,1,"\t");
		MutexLoop m3 = new MutexLoop(sem,2,"\t\t");
		System.out.println("1\t2\t3");
		System.out.println("----------------------");
		System.out.println("----------------------MUTEX="+sem.balioa);
		m1.start();
		m2.start();
		m3.start();
	}

}

class Semaforo{
	// Atributua
	int balioa;

	//Metodoak
	public Semaforo (int hasierakoa){
		balioa = hasierakoa;
	}

	synchronized public void gora(){
		++balioa;
		System.out.println("----------------------MUTEX="+balioa);
		notify();
	}

	synchronized public void behera() throws InterruptedException{
		while (balioa==0){
			wait();
		}
		--balioa;
		System.out.println("----------------------MUTEX="+balioa);
	}
}

class MutexLoop extends Thread{
	// Atributuak
	Semaforo mutex;
	String tartea;
	int luze;

	// Metodoak
	MutexLoop(Semaforo sem, int zenbat, String tabu){
		mutex = sem;
		tartea = tabu;
		luze = zenbat;
	}

	public void run(){
		int i;
		try{
			while(true){
				for(i=1; i<=6; i++){
					bisualizatu("|");
				}
				mutex.behera();
				for(i=1; i<=luze;i++){
					bisualizatu("*");
				}
				mutex.gora();
			}
		}
		catch (InterruptedException e) {}
	}

	void bisualizatu (String ikurra){
		try{
			System.out.println(tartea+ikurra);
			sleep((int)(Math.random()*1000));
		}
		catch (InterruptedException e){}
	}
}

