import java.util.*;

class herriakKoloreztatuApp{

	public static void main(String args[]){
		Erregistroa e = new Erregistroa();
		for(int i=0; i<4; i++){
			(new Koloreztatzailea(e,i)).start();
		}
	}

}

class Koloreztatzailea extends Thread{

	private Erregistroa e;
	private int izena;

	Koloreztatzailea(Erregistroa e, int izena){
		this.e = e;
		this.izena = izena;
	}

	public void run(){
		int herrialdea;
		Vector sek = new Vector();
		Vector hassek = new Vector();
		while(!e.bukaera()){
				sek.removeAllElements();
				try{
					sek = e.jaso();
				}
				catch (InterruptedException e){}
				hassek = (Vector)sek.clone();
				if(!e.bukaera()){
					e.koloreDebekatuak.removeAllElements();
					e.koloreErabilgarriak.removeAllElements();
					herrialdea = sek.size();
					for(int herri=0; herri<sek.size(); herri++){
						System.out.println("herrialdea: "+herrialdea + ", herri "+herri);
						if(e.mugakoak(herrialdea,herri)){
							System.out.println("Debekatua gehitu" + sek + " " + herri);
							e.koloreDebekatuakGehitu(sek,herri);
							/*System.out.print("Debekatuak :");
							e.printDebekatuak();*/
						}
						else{
							System.out.println("Erabilgarria gehitu" + sek + " " + herri);
							e.koloreErabilgarriakGehitu(sek,herri);
							/*System.out.print("Erabilgarriak :");
							e.printErabilgarriak();*/

						}
					}
					for(int kolore=1; kolore<=4; kolore++){
						sek.removeAllElements();
						sek = new Vector();
						sek = (Vector)hassek.clone();
						e.utzi(sek,kolore);
					}
					e.kenduN();

				}

		}
	}
}

class Erregistroa{
	Stack agenda;
	public Vector koloreErabilgarriak;
	public Vector koloreDebekatuak;
	boolean bukaera = false;
	int n = 0;
	int koloreKopuruHobezina = 5;
	public final int N = 5;
	boolean[][] grafoa;
	Vector soluzioa = new Vector();


	Object baldintza = new Object();

	Erregistroa(){
		agenda = new Stack();
		koloreErabilgarriak = new Vector();
		koloreDebekatuak = new Vector();
		agenda.addElement(new Vector());
		grafoa = new boolean[N][N];
		for(int i=0;i<N;i++){
			for(int j=i;j<N;j++){
				grafoa[i][j]=false;
			}
		}
		grafoa[1][2] = true;
		grafoa[2][1] = true;
		grafoa[3][4] = true;
		grafoa[4][3] = true;
		grafoa[1][3] = true;
		grafoa[3][1] = true;
		grafoa[2][0] = true;
		grafoa[0][2] = true;
		grafoa[0][3] = true;
		grafoa[3][0] = true;
		grafoa[4][0] = true;
		grafoa[0][4] = true;
		grafoa[1][4] = true;
		grafoa[4][1] = true;
		grafoa[2][4] = true;
		grafoa[4][2] = true;
		grafoa[3][4] = true;
		grafoa[4][3] = true;
	}
	public Vector jaso() throws InterruptedException{
		Vector p = new Vector();
		await:
			synchronized(baldintza){
				while(true){
					synchronized(this){
						if ((!agenda.empty()) || (agenda.empty() && n==0)){
							if(!(agenda.empty() && n==0)){
								p = (Vector)agenda.pop();
								++n;
							}
							else{
								bukaera = true;
								System.out.println("SOL: "+soluzioa);
							}
					    break await;
						}
					}
					baldintza.wait();
				}
			}
			return (Vector)p.clone();
	}
	public void utzi(Vector sek, int kolore){

		synchronized(this){
			Vector seklag = new Vector();
			seklag.removeAllElements();
			if (!(koloreDebekatuak.contains(new Integer(kolore))) &&
			     ((koloreErabilgarriak.contains(new Integer(kolore)) &&
			     (koloreKopurua(sek)<koloreKopuruHobezina)) ||
			   (!(koloreErabilgarriak.contains(new Integer(kolore))) &&
			      koloreKopurua(sek)+1 <koloreKopuruHobezina))){

				seklag = (Vector)sek.clone();
				seklag.addElement(new Integer(kolore));
				if (seklag.size() < N){
					agenda.push(seklag);
				}
				else{
					koloreKopuruHobezina = koloreKopurua(seklag);
					soluzioa.removeAllElements();
					soluzioa = (Vector)seklag.clone();
				}
			}
			synchronized(baldintza){
				baldintza.notifyAll();
			}
		}
	}
	public int koloreKopurua(Vector bektorea){
		int[] koloreak = new int[5];
		for (int i=0; i<bektorea.size();i++){
			koloreak[((Integer)bektorea.elementAt(i)).intValue()]++;
		}
		int zenbat = 0;
		for (int i=0;i<5;i++){
			if (koloreak[i] != 0){
				zenbat++;
			}
		}
		return zenbat;
	}

	public void kenduN(){

		synchronized(this){
			--n;
			synchronized(baldintza){
				baldintza.notifyAll();
			}
		}
	}

	public boolean bukaera(){
		return bukaera;
	}

	public void koloreDebekatuakGehitu(Vector sek,int herri){
		koloreDebekatuak.addElement(sek.elementAt(herri));
	}

	public void koloreErabilgarriakGehitu(Vector sek, int herri){
		koloreErabilgarriak.addElement(sek.elementAt(herri));
	}

	public boolean mugakoak(int herrialdea, int herri){
		return grafoa[herrialdea][herri];
	}

	public void printDebekatuak(){
		System.out.println(koloreDebekatuak);
	}

	public void printErabilgarriak(){
		System.out.println(koloreErabilgarriak);
	}

}