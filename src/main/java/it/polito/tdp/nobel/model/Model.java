package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> partenza; //lista esami di partenza
	private Set<Esame> soluzioneMigliore; //lista esami soluzione
	private double mediaSoluzioneMigliore; //media della soluzione
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		soluzioneMigliore = new HashSet<Esame>();
		mediaSoluzioneMigliore = 0;
		
		//cerca1(parziale, 0, numeroCrediti);
		cerca2(parziale, 0, numeroCrediti);
          return soluzioneMigliore;	
	}
	
	
	/* APROCCIO1: COMPLESSITA' = DI N!*/
	private void cerca1(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		int crediti = sommaCrediti(parziale);
		
		//2) PARZIALE.sommaCrediti() >= m -> supero i crediti -> mi fermo subito!
		if(crediti > m) {
			return;
		}
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);//sovrascrivo la soluzioneMigliore e la mediaMigliore
				mediaSoluzioneMigliore = media;
			}
			return;
		}

		//sicuramente crediti < m
		//=> posso andare avanti tranne nel caso in cui ho già provato tutti gli esami
		//1) L = PARTENZA.size() (N) -> non ci sono più esami da aggiungere
		if(L == partenza.size()) {
			return;
		}
		// se sono arrivata a questo punto generare i sotto-problemi
		/*for(Esame e : partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);          //agiungo
				cerca1(parziale, L+1, m); //lancio
				parziale.remove(e);       //rimuovo
			}
		}*/
		//oppure 
		//non è ancora perfetto perchè il controllo i>=L non è ancora sufficiente ad evitare i caso duplicati
		for(int i =0; i<partenza.size(); i++) {
			if(!parziale.contains(partenza.get(i)) && 1>=L) {
				parziale.add(partenza.get(i));
				cerca1(parziale, L+1, m);
				parziale.remove(partenza.get(i));
				//per renderlo perfetto devo vedere se l'ultimo elemento inserito in PARZIALE corrisponde all'ultimo elemento presente in PARTENZA
				//se corrispondono non vado più avanti a provare altri esami
				//per fare questo però non PARZIALE non può essere un SET, devo usare un LinkedHashSet oppure una Lista
			    // così taglio via molti casi inutili
				// e diventa:
				
				/*int lastIndex = 0;
				 * if(parziale.size() > 0)
				 *   lastIndex = partenza.indexOf(parziale.get(parziale.size()-1));
				 * 
				 * for(int i = lastIndex; i< partenza.size(); i++){
				 * if(!parziale.contains(partenza.get(i)) && 1>=L) {
				 *       parziale.add(partenza.get(i));
				 *       cerca1(parziale, L+1, m);
				 *       parziale.remove(partenza.get(i));
				 * }
	*/
			}
		}

	}
	
	/*APROCCIO2: COMPLESSITA' = 2^N*/
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
				int crediti = sommaCrediti(parziale);
				
				//2) PARZIALE.sommaCrediti() >= m -> supero i crediti -> mi fermo subito!
				if(crediti > m) {
					return;
				}
				if(crediti == m) {
					double media = calcolaMedia(parziale);
					if(media > mediaSoluzioneMigliore) {
						soluzioneMigliore = new HashSet<>(parziale);//sovrascrivo la soluzioneMigliore e la mediaMigliore
						mediaSoluzioneMigliore = media;
					}
					return;
				}

				//sicuramente crediti < m
				//=> posso andare avanti tranne nel caso in cui ho già provato tutti gli esami
				//1) L = PARTENZA.size() (N) -> non ci sono più esami da aggiungere
				if(L == partenza.size()) {
					return;
				}
				
		//generazione sotto-problemi
		//partenza[L] è da aggiungere oppure no? Provo entrambe le cose
		parziale.add(partenza.get(L)); //L al primo giro è = 0 quindi sto andando a prendere l'elemento di partenza in posizione 0
		cerca2(parziale,L+1, m);
		
		//backtrack
		parziale.remove(partenza.get(L));
		//faccio andare avanti la ricorsione supponendo di non aver aggiunto l'elemento
		cerca2(parziale, L+1, m);
				
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
