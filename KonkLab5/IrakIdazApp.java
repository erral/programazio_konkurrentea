// Berea
class IrakIdazApp{

	public static void main (String args[]) {
		System.out.println("1.irak\t2.irak\t1.idaz\t2.idaz\n=======|=======|=======|==========|");
	    IrakurriIdatziLehentasuna blokeoa = new IrakurriIdatziLehentasuna();
	    Irakurlea irak1 = new Irakurlea(blokeoa,"",1,2);
		Irakurlea irak2 = new Irakurlea(blokeoa,"\t",1,3);
		Idazlea idaz1   = new Idazlea  (blokeoa,"\t\t",2,2);
		Idazlea idaz2   = new Idazlea  (blokeoa,"\t\t\t",1,5);
        irak1.start();
        irak2.start();
        idaz1.start();
        idaz2.start();
	}
}


class Idazlea extends Thread {
    IrakurriIdatzi blok;
    String tab;
    int lo, lan, x;

    Idazlea(IrakurriIdatzi blokeoa, String tabul, int loegiten, int lanegiten) {
        blok=blokeoa; tab=tabul; lan=lanegiten; lo=loegiten; x=1;
    }
    public void run() {
      try {
        while(true)  {
            while(!jarduera("lo"));
            // sekzio kritikoan sartu
            blok.eskuratuIdatzi();
            while(jarduera("aldatu"));
            blok.askatuIdatzi();
        }
      } catch (InterruptedException e){}
    }
    boolean jarduera(String s){
		System.out.println(tab+s);
		try{Thread.sleep(1000);} catch(InterruptedException e) {}
		if (x<lo) {x=x+1; return false;}
		else if (x<lo+lan) {x=x+1; return true;}
		else {x=1;return false;}
	}
}


class Irakurlea extends Thread {
    IrakurriIdatzi blok;
    String tab;
    int lo, lan, x;

    Irakurlea(IrakurriIdatzi blokeoa, String tabul, int loegiten, int lanegiten) {
        blok=blokeoa; tab=tabul; lan=lanegiten; lo=loegiten; x=1;
    }

    public void run() {
      try {
        while(true)  {
            while(!jarduera("lo"));
            // sekzio kritikoan sartu
            blok.eskuratuIrakurri();
            while(jarduera("aztertu"));
            blok.askatuIrakurri();
        }
      } catch (InterruptedException e){}
    }
    boolean jarduera(String s){
		System.out.println(tab+s);
		try{Thread.sleep(1000);} catch(InterruptedException e) {}
		if (x<lo) {x=x+1; return false;}
		else if (x<lo+lan) {x=x+1; return true;}
		else {x=1;return false;}
	}
}


interface IrakurriIdatzi {
     public void eskuratuIrakurri()
         throws InterruptedException;
     public void askatuIrakurri();
     public void eskuratuIdatzi()
         throws InterruptedException;
     public void askatuIdatzi();
}

class IrakurriIdatziSegurua implements IrakurriIdatzi {
  private int irakurleak = 0;
  private boolean idazten = false;

  public synchronized void eskuratuIrakurri()
             throws InterruptedException {
    while (idazten) wait();
    ++irakurleak;
  }

  public synchronized void askatuIrakurri() {
    --irakurleak;
    if(irakurleak==0) notify();
  }
 public synchronized void eskuratuIdatzi()
              throws InterruptedException {
    while (irakurleak>0 || idazten) wait();
    idazten = true;
  }

  public synchronized void askatuIdatzi() {
    idazten = false;
    notifyAll();
  }
}

class IrakurriIdatziLehentasuna implements IrakurriIdatzi{
  private int irakurleak =0;
  private boolean idazten = false;
  private int zaiW = 0; // zai dauden Idazleen kopurua.

  public synchronized void eskuratuIrakurri()
             throws InterruptedException {
    while (idazten || zaiW>0) wait();
     ++irakurleak;
  }

  public synchronized void askatuIrakurri() {
    --irakurleak;
    if (irakurleak==0) notify();
  }
  synchronized public void eskuratuIdatzi() {
    ++zaiW;
    while (irakurleak>0 || idazten) try{ wait();}
          catch(InterruptedException e){}
    --zaiW;
    idazten = true;
  }

  synchronized public void askatuIdatzi() {
    idazten = false;
    notifyAll();
  }
}
