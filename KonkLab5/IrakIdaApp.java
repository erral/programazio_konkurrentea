/*
*  Programazio Konkurrentea 2002-2003
*  2003-3-20
*  Irakurle eta Idazleen problema
*
*/

class IrakIdaApp{


}

interface IrakurriIdatzi{
	public void eskuratuIrakurri() throws InterruptedException;
	public void askatuIrakurri();
	public void eskuratuIdatzi() throws InterruptedException;
	public void askatuIdatzi();
}


class IrakurriIdatziSegurua implements IrakurriIdatzi{
	private int irakurleak = 0;
	private boolean idazten = false;

	public synchronized void eskuratuIrakurri() throws InterruptedException{
		while (idazten){
			wait();
		}
		++irakurleak;
	}

	public synchronized void askatuIrakurri(){
		--irakurleak;
		if(irakurleak==0){
			notify(); // Irakurle gehiago ez badago, idazleren bat egon daiteke blokeatuta
					  // eta bat bakarrik esnatuko dugu. Bi baldin badaude zain eta notifyAll();
					  // egiten badugu biak esnatuko dira, baina segituan bestea lotara bidaliko
					  // dugu eskatuIdatzi synchronized delako
		}
	}

	public synchronized void eskuratuIdatzi() throws InterruptedException{
		while (irakurleak>0 || idazten){
			wait();
		}
		idazten = true;
	}

	public synchronized void askatuIdatzi(){
		idazten = false;
		notifyAll(); // Irakurle guztiak desblokeatuz
	}
}

class IrakurriIdatziLehentasuna implements IrakurriIdatzi{
	private int irakurleak = 0;
	private boolean idazten = false;
	private int zaiW = 0; // Idazteko zain dagoen prozesu kopurua

	public synchronized void askatuIrakurri(){
		--irakurleak;
		if(irakurleak==0){
			notify();
		}
	}

	public synchronized void eskuratuIrakurri(){
		++zaiW;
		while(irakurleak>0 || idazten){
			wait();
		}
		--zaiW;
		idazten = true;
	}

	public synchronized void askatuIdatzi(){
		idazten = false;
		notifyAll();
	}
}

