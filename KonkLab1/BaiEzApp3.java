class BaiEzApp3{
	public static void main(String args[]){
		Idazlea bai = new Idazlea("bai",1000,4);
		Idazlea ez = new Idazlea("\tez",400,4);

		bai.start();
		ez.start();
	}
}


class Idazlea extends Thread{
	private String baiEz; // Idatziko dena
	private int denbora; // Zenbat denboran egongo den lo
	private int zenbat; // Zenbat aldiz idatziko den

	public Idazlea(String s, int i, int j){
		// Thread klasearen eraikitzaileari deitu
		super();
		baiEz = s;
		denbora = i;
		zenbat = j;
	}

	public void run(){
		int i;

		for (i=0; i<zenbat; i++){
			System.out.println(baiEz);
			try{
				sleep(denbora);
			}
			catch (InterruptedException e){}
		}
	}
}