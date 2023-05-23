
import java.util.ArrayList;

public class MainJava {

	public static void main(String[] args) throws Exception {
		
		//Extract Data from databases (both local and online)
		DataBase Data = new DataBase();
		raid raid=Data.raids.get(0);
		//for each raid, creating 4 rankings : for teams and solo, each of them either Male or Female, then exporting them as .csv
		for (raid r : Data.raids) {
			//raid = r;
			//raid.idLastEpreuve = 1;
			ArrayList<String[]> SoloHommes = Classement.classement_solo(raid, Data.participants, Data.chronos, "Homme");
			ArrayList<String[]> SoloFemmes = Classement.classement_solo(raid, Data.participants, Data.chronos, "Femme");
			ArrayList<String[]> TeamHommes = Classement.classement_teams(raid, Data.participants, Data.chronos,Data.equipes, "Homme");
			ArrayList<String[]> TeamFemmes = Classement.classement_teams(raid, Data.participants, Data.chronos,Data.equipes, "Femme");
			
			Classement.toFile(SoloHommes, "C:\\Users\\theob\\Eclipse_Workspace\\AppliJava\\src\\raid"+r.id+"SoloHommes.csv");
			Classement.toFile(SoloFemmes, "C:\\Users\\theob\\Eclipse_Workspace\\AppliJava\\src\\raid"+r.id+"SoloFemmes.csv");
			Classement.toFile(TeamHommes, "C:\\Users\\theob\\Eclipse_Workspace\\AppliJava\\src\\raid"+r.id+"TeamHommes.csv");
			Classement.toFile(TeamFemmes, "C:\\Users\\theob\\Eclipse_Workspace\\AppliJava\\src\\raid"+r.id+"TeamFemmes.csv");
		}

	}

}
