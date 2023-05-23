import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase {
	
	public ArrayList<epreuve> epreuves;
	public ArrayList<equipe> equipes;
	public ArrayList<participant> participants;
	public ArrayList<raid> raids;
	public ArrayList<chrono> chronos;
	
	
	public DataBase() {
		this.epreuves=extractEpreuves();
		this.equipes=extractEquipes();
		this.participants=extractParticipants();
		this.raids=extractRaids();
		this.chronos=extractChronos();
	}
	
	public static ArrayList<chrono> extractChronos() {
		Communicateur c = new Communicateur();
		String rawString = "";
		ArrayList<chrono> chronos = new ArrayList<chrono>();
		ArrayList<epreuve> listeEpreuves = extractEpreuves();
		int idEpreuve;
		int idRaid = 0;
		System.out.println("Appel sync chronos...");
		try {
			rawString = c.get("https://fafa.kroko.xyz/~groupe2/Java/reqChronos.php");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin appel sync.");
		String dataString[] = rawString.split("~");
		String[][] dataMatrix = new String[dataString.length][4];
		for (int i = 0; i < dataString.length; i++) {

			dataMatrix[i] = dataString[i].split("/");
			idEpreuve = Integer.parseInt(dataMatrix[i][2]);
			for (epreuve e : listeEpreuves) {
				if (e.id == idEpreuve) {
					idRaid = e.raid_id;
				}
			}
			chronos.add(new chrono(dataMatrix[i][0], dataMatrix[i][1], dataMatrix[i][2], dataMatrix[i][3], idRaid));
		}
		return chronos;
	}
	
	public static ArrayList<epreuve> extractEpreuves() {
		Communicateur c = new Communicateur();
		String rawString = "";
		ArrayList<epreuve> epreuves = new ArrayList<epreuve>();
		System.out.println("Appel sync epreuves...");
		try {
			rawString = c.get("https://fafa.kroko.xyz/~groupe2/Java/reqEpreuves.php");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin appel sync.");
		String dataString[] = rawString.split("~");
		String[][] dataMatrix = new String[dataString.length][5];
		for (int i = 0; i < dataString.length; i++) {
			dataMatrix[i] = dataString[i].split("/");

			epreuves.add(new epreuve(dataMatrix[i][0], dataMatrix[i][1], dataMatrix[i][2], dataMatrix[i][3],
					dataMatrix[i][4]));
		}
		return epreuves;
	}

	public static ArrayList<equipe> extractEquipes() {
		Communicateur c = new Communicateur();
		String rawString = "";
		int j = 0;
		ArrayList<equipe> epreuves = new ArrayList<equipe>();
		ArrayList<participant> participants = extractParticipants();
		participant[] equipiers = new participant[2];
		System.out.println("Appel sync equipes...");
		try {
			rawString = c.get("https://fafa.kroko.xyz/~groupe2/Java/reqEquipes.php");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin appel sync.");
		String dataString[] = rawString.split("~");
		String[][] dataMatrix = new String[dataString.length][5];
		for (int i = 0; i < dataString.length; i++) {
			j = 0;
			dataMatrix[i] = dataString[i].split("/");
			for (participant p : participants) {
				if (p.equipe_id == Integer.parseInt(dataMatrix[i][0])) {
					equipiers[j] = p;
					j++;
				}
			}
			epreuves.add(new equipe(dataMatrix[i][0], dataMatrix[i][1], dataMatrix[i][2], dataMatrix[i][3],
					equipiers[0], equipiers[1]));
		}
		return epreuves;
	}

	public static ArrayList<participant> extractParticipants() {
		Communicateur c = new Communicateur();
		Connection conn = ConnexionSQLite.connect();
		String nom = "";
		String prenom = "";
		String sexe = "";
		String rawString = "";
		ArrayList<participant> participants = new ArrayList<participant>();
		System.out.println("Appel sync participants...");
		try {
			rawString = c.get("https://fafa.kroko.xyz/~groupe2/Java/reqParticipants.php");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin appel sync.");
		String dataString[] = rawString.split("~");
		String[][] dataMatrix = new String[dataString.length][4];
		for (int i = 0; i < dataString.length; i++) {
			dataMatrix[i] = dataString[i].split("/");
			String texteReq = "SELECT nom,prenom,sexe,num_puce FROM participant";

			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(texteReq);

				// loop through the result set
				while (rs.next()) {
					if (rs.getString("num_puce").equals(dataMatrix[i][1])) {
						nom = rs.getString("nom");
						prenom = rs.getString("prenom");
						sexe = rs.getString("sexe");
					}

				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			participants.add(new participant(dataMatrix[i][0], dataMatrix[i][1], dataMatrix[i][2], dataMatrix[i][3],
					nom, prenom, sexe));
		}
		return participants;
	}

	public static ArrayList<raid> extractRaids() {
		Communicateur c = new Communicateur();
		ArrayList<epreuve> epreuves = extractEpreuves();
		int idLastEpreuve = 0;
		String rawString = "";
		ArrayList<raid> raids = new ArrayList<raid>();
		System.out.println("Appel sync raids...");
		try {
			rawString = c.get("https://fafa.kroko.xyz/~groupe2/Java/reqRaids.php");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin appel sync.");
		String dataString[] = rawString.split("~");
		String[][] dataMatrix = new String[dataString.length][4];
		for (int i = 0; i < dataString.length; i++) {
			idLastEpreuve = 0;
			dataMatrix[i] = dataString[i].split("/");
			for (epreuve e : epreuves) {
				if (Integer.parseInt(dataMatrix[i][0]) == e.raid_id) {
					idLastEpreuve = e.id;
				}
			}
			raids.add(new raid(dataMatrix[i][0], dataMatrix[i][1], dataMatrix[i][2], idLastEpreuve));
		}
		return raids;
	}
}
