class BikBakApp {

  public static void main (String args[]){

	BikIdazlea bik = new BikIdazlea("\t");
	BakIdazlea bak = new BakIdazlea("");
	bak.start();
	bik.start();
  }
}

class BikIdazlea extends Thread {

	String tartea;

	BikIdazlea (String tarte){
		tartea = tarte;
	}

public void run() {
	int i=2;
	while(true){
		if (i%2==0){
			try{ sleep(1450);}
			catch(InterruptedException e){}
			System.out.println(tartea + i);
		}
		i++;
		}
	}
}

class BakIdazlea extends Thread {

	String tartea;

	BakIdazlea(String tarte){
		tartea = tarte;
	}

public void run() {
	int i=1;
	while(true){
		if (i%2!=0){
			System.out.println(tartea + i);
			try{ sleep(1000);}
			catch(InterruptedException e){}
		}
		i++;
		}
	}
}

