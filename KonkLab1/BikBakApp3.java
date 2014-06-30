class Idazlea extends Thread{
	private int bikoBako;
	private int denbora, kopurua;
	public Idazlea (int x, int i, int hasi){
		super();
		bikoBako=x;
		denbora=i;
		kopurua= hasi;
	}
	public void run (){
		int i;
		for (i=kopurua;i<bikoBako;i=i+2){
			System.out.println(i);
			try {sleep(denbora);}
			catch (InterruptedException e) {}
		}
	}
}

class BaiEzApp3 {
	public static void main (String args[]){
		Idazlea biko = new Idazlea(100,1000,2);
		Idazlea bako = new Idazlea(15,400,1);
		bako.start();
		biko.start();
	}
}