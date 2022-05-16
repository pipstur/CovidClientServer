package paket;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class ClientHandler extends Thread {
	BufferedReader clientInput = null;
	PrintStream clientOutput = null;
	Socket soketZaKomunikaciju = null;
	String username;
	String sifra;
	String ime, prezime, JMBG, pol, email, vakcina1=null, vakcina2=null, vakcina3=null;
	GregorianCalendar datum1=null, datum2=null, datum3=null;
	ClientHandler loginovan = null;
	ClientHandler(String username, String sifra, String ime, String prezime, String JMBG, String pol, String email, String vakcina1, String vakcina2, String vakcina3, GregorianCalendar datum1, GregorianCalendar datum2, GregorianCalendar datum3){
		this.username = username;
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
		this.JMBG = JMBG;
		this.pol = pol;
		this.vakcina1 = vakcina1;
		this.vakcina2 = vakcina2;
		this.vakcina3 = vakcina3;
		this.datum1=datum1;
		this.datum2 = datum2;
		this.datum3 = datum3;
	}
	String getUsername() {return username;}
	String getSifra() {return sifra;}
	String getIme() {return ime;}
	String getPrezime() {return prezime;}
	String getJMBG() {return JMBG;}
	String getPol() {return pol;}
	String getEmail() {return email;}
	String getVakcina1() {return vakcina1;}
	String getVakcina2() {return vakcina2;}
	String getVakcina3() {return vakcina3;}
	GregorianCalendar getDatum1() {return datum1;}
	GregorianCalendar getDatum2() {return datum2;}
	GregorianCalendar getDatum3() {return datum3;}
	
	void setUsername(String username) {this.username=username;}
	void setSifra(String sifra) {this.sifra=sifra;}
	void setIme(String ime) {this.ime=ime;}
	void setPrezime(String prezime) {this.prezime=prezime;}
	void setJMBG(String JMBG) {this.JMBG=JMBG;}
	void setPol(String pol) {this.pol=pol;}
	void setEmail(String email) {this.email=email;}
	void setVakcina1(String vakcina1) {this.vakcina1=vakcina1;}
	void setVakcina2(String vakcina2) {this.vakcina2=vakcina2;}
	void setVakcina3(String vakcina3) {this.vakcina3=vakcina3;}
	
	
	boolean prijavaLog = false;
	String meni1() throws IOException {
		String izbor;
		clientOutput.println(">>>1. Registracija\n>>>2. Prijava\n>>>3. Izmena podataka\n>>>4. Provera kovid propusnice\n>>>5. Adminovski meni\n>>>0. Kraj");
		izbor = clientInput.readLine();
		return izbor;
	}
	
	String meni2() throws IOException{
		String izbor;
		clientOutput.println(">>>Izaberite na koje pitanje zelite da izmenite ili dodate odgovor\n>>>1. Prva vakcina\n>>>2. Druga vakcina\n>>>3. Treca vakcina\n>>>0. Izlaz");
		izbor = clientInput.readLine();
		return izbor;
	}
	String meniAdmin() throws IOException{
		String izbor;
		clientOutput.println(">>>Izaberite funkciju, dragi admine\n>>>1. Pregled putem JMBG-a\n>>>2. Lista korisnika i status vakcinacije\n>>>3. Broj korisnika po dozama\n>>>4. Broj korisnika sa bar dve doze po vakcinama \n>>>0. Izlaz");
		izbor = clientInput.readLine();
		return izbor;
	}
	GregorianCalendar datumVakcinacije() throws Exception, IOException {
		GregorianCalendar datum = new GregorianCalendar();
		int dan;
		int mesec;
		int godina;
		GregorianCalendar falseDatum = new GregorianCalendar();
		falseDatum = null;
		clientOutput.println(">>>Unesite dan vakcinacije: ");
		dan = Integer.parseInt(clientInput.readLine());
		
		clientOutput.println(">>>Unesite mesec vakcinacije: ");
		mesec = Integer.parseInt(clientInput.readLine());
		
		clientOutput.println(">>>Unesite godinu vakcinacije: ");
		godina = Integer.parseInt(clientInput.readLine());
		
		if(dan > 31 || dan<1||mesec>12 || mesec<1 || godina!=2021) {
			clientOutput.println("Nevalidan datum");
			if(godina!=2021) {
				clientOutput.println("Godina mora biti 2021.");
			}
		}
		else {
			datum.set(GregorianCalendar.YEAR, godina);
			datum.set(GregorianCalendar.MONTH, mesec);
			datum.set(GregorianCalendar.DATE, dan);
			return datum;
		}
		return falseDatum;	
	}
	void prijava() throws IOException {
		
			String provera1, provera2;
			clientOutput.println(">>>Prijavite se: Username");
			provera1 = clientInput.readLine();
			clientOutput.println(">>>Prijavite se: Sifra");
			provera2 = clientInput.readLine();
			
	outer: 	for (ClientHandler klijent : Server.onlineUsers) {
			
			if (klijent.getUsername().equals(provera1)) {
				if(klijent.getSifra().equals(provera2)) {
					loginovan = klijent;
					prijavaLog=true;
					break outer;
				}
				else {
					clientOutput.println(">>>Juzernejm postoji, ali sifra ne"+this.getUsername()+this.getSifra());
					break outer;
				}
			}
			/*else {
				clientOutput.println(">>>nesto nije dobro"+this.getUsername()+this.getSifra());
				continue outer;
			}*/
		}
	if(loginovan.username.equals("admin")) {
		clientOutput.println(">>>Dobrodosli, admine :D");
	}
	else {
		clientOutput.println(">>>Dobrodosli, regularni korisnice :D");
	}
	clientOutput.println(">>>Uspesno ste se prijavili! ");
	clientOutput.println(">>>Korisnicko ime: "+loginovan.username+"\n>>>Sifra: "+loginovan.getSifra()+"\n>>>JBMG: "+loginovan.JMBG+"\n>>>Vakcina1:"+loginovan.vakcina1+"");
	}
	int kovidPropusnica() {
		int brojac=0;
		int kovidStatus;
		if (loginovan.getVakcina1()!=null) {
			brojac++;
			if(loginovan.getVakcina2()!=null) {
				brojac++;
				if(loginovan.getVakcina3()!=null) {
					brojac++;
				}
			}
		}
		if(brojac>=2) {
			clientOutput.println(">>>Korisnik ima validnu kovid propusnicu! ooo ale ale om"+brojac);
			kovidStatus = 1;
		}
		else {
			clientOutput.println(">>>Korisnik nema validnu kovid propusnicu :( vakcinisi se majmune"+brojac);
			kovidStatus = 0;
		}
		return kovidStatus;
	}
	public ClientHandler(Socket soket) {
		
		
		soketZaKomunikaciju = soket;
		
		
	}
	
	@Override
	public void run() {
		try {
			clientInput = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(soketZaKomunikaciju.getOutputStream());
			
			/*Server.onlineUsers.add(new ClientHandler("admin", "admin", null, null,null,null,null,null,null,null,null,null,null));
			Server.onlineUsers.add(new ClientHandler("a", "a", "a","a","a","a","a","1","1",null,null,null,null));
			Server.onlineUsers.add(new ClientHandler("b", "b", "b","b","b","b","b","1",null,null,null,null,null));
			Server.onlineUsers.add(new ClientHandler("c", "c", "a","a","a","a","a","1","1","1",null,null,null));
			Server.onlineUsers.add(new ClientHandler("d", "d", "a","a","a","a","a","2","2",null,null,null,null));*/
			String izbor = "0";
			
			
			do {
				izbor = meni1();
			if(izbor.equals("1")) {
				
					clientOutput.println(">>>Unesite korisnicko ime: ");
					username = clientInput.readLine();
					if(username.contains(" ")) {
						clientOutput.println(">>>Juzernejm ima spejs, a to ne sme");
						
					}
					clientOutput.println(">>>Unesite sifru ");
					sifra = clientInput.readLine();
					
					clientOutput.println(">>>Unesite ime ");
					ime = clientInput.readLine();
					
					clientOutput.println(">>>Unesite prezime ");
					prezime = clientInput.readLine();
					
					clientOutput.println(">>>Unesite JMBG ");
					JMBG = clientInput.readLine();
					
					clientOutput.println(">>>Unesite pol ");
					pol = clientInput.readLine();
					
					clientOutput.println(">>>Unesite imejl ");
					email = clientInput.readLine();
					
					clientOutput.println(">>>Da li ste primili prvu vakcinu? d/n ");
					if(clientInput.readLine().equals("d")) {
						clientOutput.println(">>>Koja je to vakcina?\n>>>1. Astrazeneka\n>>>2. Sinopharm\n>>>3. Pfizer\n>>>4. Sputnik V\n>>>5. Moderna");
						vakcina1 = clientInput.readLine();
						datum1 = datumVakcinacije();
						if(datum1==null) {
							clientOutput.println("Datum je neispravno unesen, ajmo opet registracija");
							continue;
						}
						clientOutput.println(">>>Da li ste primili drugu vakcinu? d/n ");
						if(clientInput.readLine().equals("d")) {
							clientOutput.println(">>>Koja je to vakcina?\n>>>1. Astrazeneka\n>>>2. Sinopharm\n>>>3. Pfizer\n>>>4. Sputnik V\n>>>5. Moderna");
							vakcina2 = clientInput.readLine();
							datum2 = datumVakcinacije();
							if(datum2==null) {
								clientOutput.println("Datum je neispravno unesen, ajmo opet registracija");
								continue;
							}
							if((datum2.getTimeInMillis()-datum1.getTimeInMillis())/(1000*60*60*24)<21) {
								clientOutput.println(">>>Druga vakcina je manje od 3 nedelje posle prve");
								continue;
							}
							if (vakcina1.equals(vakcina2)) {
							
					
							}
							else {
								clientOutput.println(">>>Prva i druga vakcina moraju biti iste, aj ponovo");
								clientOutput.println(">>>Prva vakcina: "+vakcina1+"\n>>>Druga vakcina: "+vakcina2);
								continue;
							}
							
							clientOutput.println(">>>Da li ste primili trecu vakcinu? d/n ");
							if(clientInput.readLine().equals("d")) {
								clientOutput.println(">>>Koja je to vakcina?\n>>>1. Astrazeneka\n>>>2. Sinopharm\n>>>3. Pfizer\n>>>4. Sputnik V\n>>>5. Moderna");
								vakcina3 = clientInput.readLine();
								datum3=datumVakcinacije();
								if(datum3==null) {
									clientOutput.println("Datum je neispravno unesen, ajmo opet registracija");
									continue;
								}
								if((datum3.getTimeInMillis()-datum2.getTimeInMillis())/(1000*60*60*24)<180) {
									clientOutput.println(">>>Treca vakcina je primljena manje od 6 meseci posle druge");
									continue;
								}
								
								Server.onlineUsers.add(new ClientHandler(username, sifra, ime, prezime, JMBG, pol, email, vakcina1, vakcina2, vakcina3, datum1, datum2, datum3));
								continue;
		
							}
							else {
								
								Server.onlineUsers.add(new ClientHandler(username, sifra, ime, prezime, JMBG, pol, email, vakcina1, vakcina2, vakcina3, datum1, datum2, datum3));
								continue;
							}
						}
						else {
							
							Server.onlineUsers.add(new ClientHandler(username, sifra, ime, prezime, JMBG, pol, email, vakcina1, vakcina2, vakcina3, datum1, datum2, datum3));
							continue;
						}
						
					}
					else {
						
						Server.onlineUsers.add(new ClientHandler(username, sifra, ime, prezime, JMBG, pol, email, vakcina1, vakcina2, vakcina3, datum1, datum2, datum3));
						continue;
							
					}
					
				
				
			}
			else if(izbor.equals("2")) {
				if(prijavaLog==true) {
					clientOutput.println(">>>Vec ste prijavljeni!");
					continue;
				}
				else { 
					prijava();
				}
				
			}
			else if(izbor.equals("3")) {
				String izmena = "0";
				if(loginovan.getUsername().equals("admin")) {
					clientOutput.println(">>>Admine, vama nije potrebna ova funkcija :D");
					continue;
				}
				outer: do {
					
					izmena = meni2();
				switch (izmena){
				case "1":
					if(loginovan.vakcina1==null) {
						clientOutput.println(">>>Izaberite prvu dozu vakcine.\n>>>1. Astrazeneka\n>>>2. Sinopharm\n>>>3. Pfizer\n>>>4. Sputnik V\n>>>5. Moderna");
						loginovan.vakcina1 = clientInput.readLine();
						loginovan.datum1=datumVakcinacije();
						
						clientOutput.println(">>>Uspesno ste izmenili prvu vakcinu!");
						
						continue;
					}
					else {
						clientOutput.println(">>>Ovu vakcinu ste vec uneli!");
						continue;
					}
	
				case "2":
					if(loginovan.vakcina2==null) {
						clientOutput.println(">>>Izaberite drugu dozu vakcine.\n>>>1. Astrazeneka\n>>>2. Sinopharm\n>>>3. Pfizer\n>>>4. Sputnik V\n>>>5. Moderna");
						loginovan.vakcina2 = clientInput.readLine();
						if(loginovan.getVakcina2().equals(loginovan.getVakcina1())) {
							loginovan.datum2 = datumVakcinacije();
							if((loginovan.getDatum2().getTimeInMillis()-loginovan.getDatum1().getTimeInMillis())/(1000*60*60*24)<21) {
								clientOutput.println(">>>Druga vakcina je manje od 3 nedelje posle prve");
								break;
							}
							clientOutput.println(">>>Uspesno ste izmenili drugu vakcinu!");
							continue;
						}
						else {
							clientOutput.println(">>>Prva i druga vakcina moraju biti iste, aj ponovo");
							clientOutput.println(">>>Prva vakcina: "+vakcina1+"\n>>>Druga vakcina: "+vakcina2);
							continue;
							
						}
						//this.setVakcina2(vakcina2);
						
					}
					else {
						clientOutput.println(">>>Ovu vakcinu ste vec uneli!");
						continue;
					}
					
				case "3":
					if(loginovan.vakcina3==null) {
						clientOutput.println(">>>Izaberite trecu dozu vakcine.\n>>>1. Astrazeneka\n>>>2. Sinopharm\n>>>3. Pfizer\n>>>4. Sputnik V\n>>>5. Moderna");
						loginovan.vakcina3 = clientInput.readLine();
						loginovan.datum3 = datumVakcinacije();
						if((loginovan.getDatum3().getTimeInMillis()-loginovan.getDatum2().getTimeInMillis())/(1000*60*60*24)<180) {
							clientOutput.println(">>>Treca vakcina je primljena manje od 6 meseci posle prve");
							break;
						}
						clientOutput.println(">>>Uspesno ste izmenili trecu vakcinu!");
						continue;
					}
					else {
						clientOutput.println(">>>Ovu vakcinu ste vec uneli!");
						continue;
					}
				
					
				case "0":
					break outer;
				default:
						clientOutput.println("Aj opet unesi: ");
						continue;
				}
				}while(izmena!="0");
				
				
			}
			else if(izbor.equals("4")) {
				if(loginovan.getUsername().equals("admin")) {
					clientOutput.println(">>>Admine, vama nije potrebna ova funkcija :D");
					continue;
				}
				int rezultat = kovidPropusnica();
				if(rezultat == 1) {
					clientOutput.println(">>>Zelite li da generisete kovid propusnicu? d/n");
					String prop = clientInput.readLine();
					if(prop.equals("d")) {
						FileWriter fajl = new FileWriter("propusnica.txt");
						if(loginovan.getVakcina1()!=null && loginovan.getVakcina2()!=null && loginovan.getVakcina3()!=null) {
							
							
							GregorianCalendar date1 = new GregorianCalendar();
							date1 = loginovan.getDatum1();
							int dan1 = date1.get(Calendar.DATE);
							int mesec1 = date1.get(Calendar.MONTH);
							int godina1 = date1.get(Calendar.YEAR);
							GregorianCalendar date2 = new GregorianCalendar();
							date2 = loginovan.getDatum2();
							int dan2 = date2.get(Calendar.DATE);
							int mesec2 = date2.get(Calendar.MONTH);
							int godina2 = date2.get(Calendar.YEAR);
							GregorianCalendar date3 = new GregorianCalendar();
							date3 = loginovan.getDatum3();
							int dan3 = date3.get(Calendar.DATE);
							int mesec3 = date3.get(Calendar.MONTH);
							int godina3 = date3.get(Calendar.YEAR);
							
							fajl.write("Ime: "+loginovan.getIme()+"\nPrezime: "+loginovan.getPrezime()+"\nJMBG: "+loginovan.getJMBG()+"\nPrva doza: "+loginovan.getVakcina1()+"\nDruga doza: "+loginovan.getVakcina2()+"\nTreca doza: "+loginovan.getVakcina3()+"\nDatum prve vakcine: "+dan1+"/"+mesec1+"/"+godina1+"\nDatum druge vakcine: "+dan2+"/"+mesec2+"/"+godina2+"\nDatum trece vakcine: "+dan3+"/"+mesec3+"/"+godina3);
							fajl.close();
						}
						else if(loginovan.getVakcina1()!=null && loginovan.getVakcina2()!=null) {
							GregorianCalendar date1 = new GregorianCalendar();
							date1 = loginovan.getDatum1();
							int dan1 = date1.get(Calendar.DATE);
							int mesec1 = date1.get(Calendar.MONTH);
							int godina1 = date1.get(Calendar.YEAR);
							GregorianCalendar date2 = new GregorianCalendar();
							date2 = loginovan.getDatum2();
							int dan2 = date2.get(Calendar.DATE);
							int mesec2 = date2.get(Calendar.MONTH);
							int godina2 = date2.get(Calendar.YEAR);
							
							fajl.write("Ime: "+loginovan.getIme()+"\nPrezime: "+loginovan.getPrezime()+"\nJMBG: "+loginovan.getJMBG()+"\nPrva doza: "+loginovan.getVakcina1()+"\nDruga doza: "+loginovan.getVakcina2()+"\nTreca doza: "+loginovan.getVakcina3()+"\nDatum prve vakcine: "+dan1+"/"+mesec1+"/"+godina1+"\nDatum druge vakcine: "+dan2+"/"+mesec2+"/"+godina2);
							fajl.close();
						}
						
					}
					else {
						clientOutput.println(">>>U redu. :D");
						continue;
					}
				}
				else {
					clientOutput.println(">>>Korisnik nema mogucnost da generise kovid propusnicu : (");
					continue;
				}
			} 
			else if(izbor.equals("5")) {
				
				if(!loginovan.getUsername().equals("admin")) {
					clientOutput.println(">>>Nemate pristup ovoj funkciji! ");
					
				}
				else {
				String ad = meniAdmin();
				outer: do {
					switch(ad) {
					case "1":
						clientOutput.println(">>>Unesite zeljeni JMBG: ");
						String input = clientInput.readLine();
						int brojac=0;
						for (ClientHandler klijent : Server.onlineUsers) {
							if(klijent.getJMBG().equals(input)) {
								
								if (klijent.getVakcina1()!=null) {
									brojac++;
									if(klijent.getVakcina2()!=null) {
										brojac++;
										if(klijent.getVakcina3()!=null) {
											brojac++;
										}break;
									}
									break;
								}
								break;
							
		
							}
		
						}
						if(brojac>=2) {
							clientOutput.println(">>>Korisnik sa JMBGom \""+input+"\" ima validnu kovid propusnicu! ooo ale ale om"+brojac);
						}
						else {
							clientOutput.println(">>>Korisnik sa JMBGom \""+input+"\" nema validnu kovid propusnicu :("+brojac);
						}
						break outer;
					case "2":
						for(ClientHandler klijent : Server.onlineUsers) {
							if(klijent.getUsername().equals("admin")) {
								continue;
							}
							int brojac1 = 0;
							if (klijent.getVakcina1()!=null) {
								brojac1++;
								if(klijent.getVakcina2()!=null) {
									brojac1++;
									if(klijent.getVakcina3()!=null) {
										brojac1++;
										
									}
								}
								
							}
							clientOutput.println(">>>Korisnik: "+klijent.getUsername()+" je vakcinisan "+brojac1+"puta\n\n");
							
						}
						break outer;
					case "3":
						int brojv1 = 0;
						int brojv2 = 0;
						int brojv3 = 0;
						for(ClientHandler klijent: Server.onlineUsers) {
							if(klijent.getUsername().equals("admin")) {
								continue;
							}
							if (klijent.getVakcina1()!=null) {
								brojv1++;
								if(klijent.getVakcina2()!=null) {
									brojv2++;
									if(klijent.getVakcina3()!=null) {
										brojv3++;
										
									}
								}
								
							}
						}
						clientOutput.println(">>>Broj korisnika sa jednom dozom: "+brojv1+"\n>>>Broj korisnika sa dve doze: "+brojv2+"\n>>>Broj korisnika sa 3 doze: "+brojv3);
						break outer;
					case "4":
						clientOutput.println(">>>Unesite vakcinu za koju zelite da vidite broj korisnika koji su vakcinisani 2 puta: ");
						String vakcinaInput = clientInput.readLine();
						
						int brojac3 = 0;
						for(ClientHandler klijent : Server.onlineUsers) {
							if(klijent.getUsername().equals("admin")) {
								continue;
							}
							if(klijent.getVakcina1()==null) {
								continue;
							}
								if(klijent.getVakcina1().equals(vakcinaInput)) {
									if(klijent.getVakcina2()==null) {
										continue;
									}
									if(klijent.getVakcina2().equals(vakcinaInput)) {
										brojac3++;
									}
									
									
								}
								continue;
								
							
							
						}
						clientOutput.println("Broj korisnika vakcinisanih "+vakcinaInput+"-om je"+brojac3);
						break outer;
					
						
							
					case "0":
						break outer;
					default: 
						clientOutput.println("Nevalidan izbor, ae opet");
					}
				}while(ad!="0");
				}
			}
			
			
			else if(izbor.equals("0")) {
				
				clientOutput.println(">>>Dovidjenja");
				soketZaKomunikaciju.close();
			}
			}while(true);
			
			/*String message;
			while(true) {
				message = clientInput.readLine();
				if(message.startsWith("***quit")) {
					break;
				}
				for (ClientHandler klijent : Server.onlineUsers) {
					klijent.clientOutput.println("["+username+"]: "+message);
				}
				
			}
			
			clientOutput.println(">>>Dovidjenja "+username);
			
			for (ClientHandler klijent : Server.onlineUsers) {
				if(klijent!= this) {
					klijent.clientOutput.println(">>>Korisnik "+username+" je napustio chat");
				}
			}*/
			//Server.onlineUsers.remove(this);
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
