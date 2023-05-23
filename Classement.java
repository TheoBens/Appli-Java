import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;

public class Classement {

	public static chrono[] sort(ArrayList<chrono> liste_chronos) {
		//Sorts an arraylist of chronos
		chrono[] sorted = new chrono[liste_chronos.size()];
		chrono max;
		int size = liste_chronos.size();
		for (int i = 0; i < size; i++) {
			max = liste_chronos.get(0);
			for (int j = 0; j < liste_chronos.size(); j++) {
				if (max.time.isAfter(liste_chronos.get(j).time)) {
					max = liste_chronos.get(j);
				}
			}
			sorted[i] = max;
			liste_chronos.remove(max);
		}

		return sorted;
	}

	public static chrono[] sort(chrono[] liste) {
		//Sorts an array of chronos
		ArrayList<chrono> liste_chronos = new ArrayList<chrono>();
		for (chrono c : liste)
			liste_chronos.add(c);
		chrono[] sorted = new chrono[liste_chronos.size()];
		chrono max;
		int size = liste_chronos.size();
		for (int i = 0; i < size; i++) {
			max = liste_chronos.get(0);
			for (int j = 0; j < liste_chronos.size(); j++) {
				if (max.time.isAfter(liste_chronos.get(j).time)) {
					max = liste_chronos.get(j);
				}
			}
			sorted[i] = max;
			liste_chronos.remove(max);
		}

		return sorted;
	}

	public static ArrayList<String[]> classement_teams(raid raid, ArrayList<participant> liste_inscrits,
			ArrayList<chrono> liste_chronos, ArrayList<equipe> equipes, String categorie) {
		//Creates A ranking of teams based on the worst performing member
		
		
		//select teams that are participating in the race and category inputed
		ArrayList<equipe> listeEquipes = new ArrayList<equipe>();
		for (equipe i : equipes) {
			if (i.raid_id == raid.id && i.categorie.equals(categorie)) {
				listeEquipes.add(i);
			}
		}
		
		//selecting chronos that are from the race inputed
		ArrayList<chrono> chronos = new ArrayList<chrono>();
		for (chrono i : liste_chronos) {
			if (i.raid_id == raid.id) {
				chronos.add(i);
			}
		}
		
		//Sorting them
		chrono SortedChronos[] = Classement.sort(chronos);
		
		//creating an array to know if each chrono sorted is the last from it's participant : useful if there are multiple checkpoints, as we only consider finishing time
		Boolean isLastFromPlayer[] = new Boolean[SortedChronos.length];
		for (int i = 0; i < SortedChronos.length; i++) {
			isLastFromPlayer[i] = true;
			for (int j = i + 1; j < SortedChronos.length; j++) {
				if (chrono.score(raid, SortedChronos[i]) <= chrono.score(raid, SortedChronos[j])
						&& SortedChronos[i].code_puce == SortedChronos[j].code_puce) {
					isLastFromPlayer[i] = false;
				}
			}
		}
		
		//creating an array only composed of chronos that are the last of their player
		ArrayList<chrono> LastChronoFromPlayers = new ArrayList<chrono>();
		for (int i = 0; i < SortedChronos.length; i++) {
			if (isLastFromPlayer[i]) {
				if (SortedChronos[i].epreuve_id != raid.idLastEpreuve)
					SortedChronos[i].time = LocalTime.MAX;
				LastChronoFromPlayers.add(SortedChronos[i]);
			}
		}
		
		
		//creating an array of the worst chronos of teams : worst of its player, and worse than the worst of second player
		chrono[] lastChronoFromTeams = new chrono[listeEquipes.size()];
		int j;
		chrono[] chronosEquipes = new chrono[2];

		for (int i = 0; (i < listeEquipes.size()); i++) {
			j = 0;
			for (chrono c : LastChronoFromPlayers) {
				if ((c.code_puce.equals(listeEquipes.get(i).p1.code_puce))
						|| (c.code_puce.equals(listeEquipes.get(i).p2.code_puce))) {
					chronosEquipes[j] = c;
					j++;
				}
			}
			if (chrono.score(raid, chronosEquipes[0]) <= chrono.score(raid, chronosEquipes[1]))
				lastChronoFromTeams[i] = chronosEquipes[1];
			else
				lastChronoFromTeams[i] = chronosEquipes[0];
		}
		
		//sorting this
		lastChronoFromTeams = Classement.sort(lastChronoFromTeams);
		
		
		//formatting output to contain every info that a ranking needs
		String[] ligne = new String[7];
		equipe team = null;
		ArrayList<String[]> rendu = new ArrayList<String[]>();
		for (int i = 0; i < lastChronoFromTeams.length; i++) {
			ligne = new String[7];
			team = null;
			
			//for each chrono, finding to which team it belongs
			for (equipe e : listeEquipes) {
				if (e.p1.code_puce.equals(lastChronoFromTeams[i].code_puce)
						|| e.p2.code_puce.equals(lastChronoFromTeams[i].code_puce)) {
					team = e;
				}
			}

			ligne[0] = (i + 1) + "";
			ligne[1] = team.dossard + "";
			ligne[2] = team.p1.prenom;
			ligne[3] = team.p1.nom;
			ligne[4] = team.p2.prenom;
			ligne[5] = team.p2.nom;

			if (lastChronoFromTeams[i].time.equals(LocalTime.MAX))
				ligne[6] = "ABANDON";
			else
				ligne[6] = lastChronoFromTeams[i].time.toString();
			rendu.add(ligne);

		}
		return rendu;

	}

	public static ArrayList<String[]> classement_solo(raid raid, ArrayList<participant> liste_inscrits,
			ArrayList<chrono> liste_chronos, String categorie) {
		//Creates A ranking of racers based on their last chrono
		
		//selecting chronos that are from the race inputed
		ArrayList<chrono> chronos = new ArrayList<chrono>();
		for (chrono i : liste_chronos) {
			if (i.raid_id == raid.id) {
				chronos.add(i);
			}
		}
		
		//sorting them
		chrono SortedChronos[] = Classement.sort(chronos);
		
		//creating an array to know if each chrono sorted is the last from it's participant : useful if there are multiple checkpoints, as we only consider finishing time
		Boolean isLastFromPlayer[] = new Boolean[SortedChronos.length];
		for (int i = 0; i < SortedChronos.length; i++) {
			isLastFromPlayer[i] = true;
			for (int j = i + 1; j < SortedChronos.length; j++) {
				if (chrono.score(raid, SortedChronos[i]) <= chrono.score(raid, SortedChronos[j])
						&& SortedChronos[i].code_puce == SortedChronos[j].code_puce) {
					isLastFromPlayer[i] = false;
				}
			}
		}
		
		//creating an array only composed of chronos that are the last of their player
		ArrayList<chrono> LastChronoFromPlayers = new ArrayList<chrono>();
		for (int i = 0; i < SortedChronos.length; i++) {
			if (isLastFromPlayer[i]) {
				if (SortedChronos[i].epreuve_id != raid.idLastEpreuve)
					SortedChronos[i].time = LocalTime.MAX;
				LastChronoFromPlayers.add(SortedChronos[i]);
			}
		}
		
		//sorting it
		chrono[] SortedLastChronoFromPlayers = Classement.sort(LastChronoFromPlayers);
		
		//formatting output to contain every info that a ranking needs
		ArrayList<String[]> rendu = new ArrayList<String[]>();
		String[] ligne = new String[4];
		participant joueur;
		int o = 0;
		for (int i = 0; i < SortedLastChronoFromPlayers.length; i++) {
			joueur = null;
			ligne = new String[4];
			//Checking to which racer the time belongs
			for (participant p : liste_inscrits) {
				if (p.code_puce.equals(SortedLastChronoFromPlayers[i].code_puce)) {
					joueur = p;
				}
			}
			//checking if the player is in the category inputed. otherwise, expluding them from ranking
			if (joueur.sexe.equals(categorie)) {
				ligne[0] = (i + 1 - o) + "";
				ligne[1] = joueur.nom;
				ligne[2] = joueur.prenom;
				if (SortedLastChronoFromPlayers[i].time.equals(LocalTime.MAX))
					ligne[3] = "ABANDON";
				else
					ligne[3] = SortedLastChronoFromPlayers[i].time.toString();

				rendu.add(ligne);
			} else {
				o++;
			}

		}

		return rendu;

	}
	
	
	//turns an arrayList of arrays of string to a csv file, with its path given
	public static void toFile(ArrayList<String[]> matrix, String FilePath) throws FileNotFoundException {
		File csvFile = new File(FilePath);
		PrintWriter out = new PrintWriter(csvFile);
		
		String line;
		for (String[] i : matrix) {
			line = "\"";
			for (int j = 0; j < i.length; j++) {
				if (j + 1 < i.length) {
					line += i[j] + "\",\"";
				} else {
					line += i[j] + "\"";
				}
			}
			out.println(line);
		}

		out.close();
	}

}
