/*
 * Programazio Konkurrentea 2002-2003
 * Mikel Larreategi
 *
 * Basatiaren problema
 *
 */

 class BasatiaApp {

	 public static final int Max = 4;

	 public static void main (String args[]){
		 Lapikoa lapikoa = new Lapikoa(10);
		 Sukaldaria sukal = new Sukaldaria(lapikoa);
		 Basatia b[] = new Basatia[4];

		 for(int i=0; i<Max;i++){
			 b[i] = new Basatia(i,lapikoa);
		 }

		 for (int i=0; i<Max;i++){
			 b[i].start();
		 }

		 sukal.start();
	 }
 }

 class Basatia extends Thread{
	 int izena;
	 Lapikoa lapiko;

	 Basatia(int izen, Lapikoa lapi){
		 izena = izen;
		 lapiko = lapi;
	 }

	 public void run(){

		 while(true){
			Simulatu.HWInterrupt();
		 	lapiko.jan(izena);

		}
	 }
 }

 class Sukaldaria extends Thread{
	 Lapikoa lapiko;

	 Sukaldaria (Lapikoa lapiko){
		 this.lapiko = lapiko;
	 }

	 public void run(){
		while(true){
			Simulatu.HWInterrupt();
			Simulatu.HWInterrupt();
		 	Simulatu.HWInterrupt();
		 	int z = (int)(10*Math.random());
		 	lapiko.bota(z);
		 }
	 }
 }

 class Lapikoa{
	 private int janaria;

	 Lapikoa (int zenbat){
		 janaria = zenbat;
	 }

	 public synchronized void jan(int nork) {
		 while(janaria==0){
			 try{
				 wait();
			 }
			 catch (InterruptedException e){}
		 }
		 --janaria;
		 System.out.print(nork + " basatiak puska bat jan du = ");
		 System.out.println(nolaDagoLapikoa());
	 }

	 public synchronized void bota(int zenbat){
		 janaria = janaria + zenbat;
		 System.out.print("Sukaldariak "+zenbat+" puska bota ditu = ");
		 System.out.println(nolaDagoLapikoa());
		 notifyAll();
	 }

	 public synchronized int nolaDagoLapikoa(){
		 return janaria;
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





