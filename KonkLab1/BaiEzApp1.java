class BaiEzApp1{
	public static void main(String args[]){
		BaiIdazlea bai = new BaiIdazlea();
		EzIdazlea ez = new EzIdazlea();

		bai.start();
		ez.start();
	}
}


class BaiIdazlea extends Thread {
public void run() {
	while(true){
		System.out.println("bai");
		try{ sleep(1000);}
		catch(InterruptedException e){}
		}
	}
}

class EzIdazlea extends Thread {
public void run() {
	while(true){
		System.out.println("ez");
		try{ sleep(400);}
		catch(InterruptedException e){}
		}
	}
}