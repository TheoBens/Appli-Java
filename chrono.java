import java.time.LocalTime;



public class chrono {
	//Class to Store individual instances of times checked in a checkpoint
	public int id;
	public String code_puce;
	public int epreuve_id;
	public LocalTime time;
	public int raid_id;
	
	//Constructor from all string args gotten from online database. raid_id obtained by cross-checking with raid list
	public chrono(String id, String code_puce, String epreuve_id, String time,int raid_id) {
		this.id=Integer.parseInt(id);
		this.code_puce=code_puce;
		this.epreuve_id=Integer.parseInt(epreuve_id);
		this.time=LocalTime.parse(time);
		this.raid_id=raid_id;
	}
	
	//Calculates seconds since start of the race
	public static int score(raid raid, chrono chrono) {
		LocalTime heure_init = raid.dateDebut;
		LocalTime heure_arrivee = chrono.time;
	 	return heure_arrivee.toSecondOfDay() - heure_init.toSecondOfDay();
	}
}
