import java.util.*;

// Agendan koloreen sekuentziak egongo dira. Multzo bezala inplementatu
// behar da agenda. Sekuentziak herrialdeak koloreztatzeko konbinazio
// posibleak dira: DESKRIBATZAILEAK
// Deskribatzailea klasea egiteko dio
class herrialdeakKoloreztatuApp {

	public final int N = 5;


	public static void main (String args[]){

		Agenda agenda = new Agenda();
		for (int i=0;i<10;i++){
			(new Koloreztatzailea(i,agenda)).start();
		}

	}
}


class Koloreztatzailea extends Thread{
	public final int N = 5;
	int izena;
	Agenda agenda;

	public Koloreztatzailea(int i, Agenda agenda){
		izena = i;
		this.agenda = agenda;
	}

	public void run(){
		while(!agenda.bukaera()){
			agenda.jaso();

			if (!agenda.bukaera()){
				int herrialdea = agenda.luzera();
				for (int herri = 0; herri<agenda.luzera(); herri++){
					if(agenda.mugakoak(herrialdea,herri)){
						agenda.koloreDebekatuakGehitu(agenda.lortuKolorea(herri));
					}
					else{
						agenda.koloreErabilgarriakGehitu(agenda.lortuKolorea(herri));
					}
				}
				for (int kolore=1; kolore<=4; kolore++){
					agenda.utzi(kolore);
				}
				agenda.kenduN();
			}
		}
	}
}

class Agenda{

	public final int N = 5;
	boolean[][] grafoa;
	Stack agenda;
	boolean bukaera;
	int KoloreKopHobezina = 5;
	Vector koloreDebekatuak;
	Vector koloreErabilgarriak;
	Vector sekuentzia;
	Vector soluzioa;
	int n;

	public Agenda(){
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


		bukaera = false;
		agenda = new Stack();
		koloreDebekatuak = new Vector();
		koloreErabilgarriak = new Vector();
		sekuentzia = new Vector();
		agenda.push(sekuentzia);
		soluzioa = new Vector();
		n=0;

		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				System.out.print(grafoa[i][j]+ " ");
			}
			System.out.println("");
		}
	}

	public void koloreDebekatuakGehitu(int kolore){
		koloreDebekatuak.addElement(new Integer(kolore));
		System.out.println("DEB: "+koloreDebekatuak);
	}

	public void koloreErabilgarriakGehitu(int kolore){
		koloreErabilgarriak.addElement(new Integer(kolore));
		System.out.println("ERA: "+koloreErabilgarriak);
	}

	public int luzera(){
		return sekuentzia.size();
	}

	public int lortuKolorea(int herri){
		return ((Integer)sekuentzia.elementAt(herri)).intValue();
	}

	public boolean mugakoak(int i, int j){
		return grafoa[i][j];
	}

	public boolean bukaera(){
		return bukaera;
	}

	public void jaso(){
		synchronized(this){
			Vector a = new Vector();
			while(!((!agenda.isEmpty())||(agenda.isEmpty() && n==0))){
				try{
					wait();
				}
				catch (InterruptedException e){}
			}
			System.out.println("AGE: " + agenda);
			if(!(agenda.isEmpty() && n==0)){
				//sekuentzia.removeAllElements();
				sekuentzia = (Vector)agenda.pop();
				System.out.println("SEK: "+sekuentzia);
				++n;
			}
			else{
				bukaera = true;
				System.out.println("SOL: "+soluzioa);
			}
			notifyAll();

		}
	}

	public void utzi(int kolor){
		synchronized(this){
			Integer kolore = new Integer(kolor);
			if(!(koloreDebekatuak.contains(kolore)) &&
			((koloreErabilgarriak.contains(kolore)) &&
			koloreKopurua(sekuentzia)<KoloreKopHobezina) ||
			(!(koloreErabilgarriak.contains(kolore)) &&
			koloreKopurua(sekuentzia) + 1 < KoloreKopHobezina)){
				Vector sekLag = new Vector();
				sekLag = sekuentzia;
				System.out.println("sekLag-era: "+kolore);
				sekLag.addElement(kolore);
				System.out.println("SEKLAG :" + sekLag);

				if(sekLag.size()< N){
					System.out.println("Seklag agendaratu");
					agenda.push(sekLag);
					System.out.println("AGE: "+agenda);
				}
				else{
					KoloreKopHobezina = koloreKopurua(sekLag);
					soluzioa.removeAllElements();
					soluzioa.addAll(sekLag);


				}
			}
		notifyAll();
		}

	}

	public void kenduN(){
		synchronized(this){
			--n;
			notifyAll();
		}
	}

	public int koloreKopurua(Vector v){
		int[] koloreak = new int[5];
		int zenbat =0;
		for (int i=0;i<v.size();i++){
			koloreak[((Integer)v.elementAt(i)).intValue()]++;
		}
		for (int j=0;j<5;j++){
			if (koloreak[j]!=0){
				zenbat++;
			}
		}
		return zenbat;
	}
}


