import java.time.LocalTime;
public class raid {
	//Class to Store individual instances of races
	public int id;
	public String nom;
	public LocalTime dateDebut;
	public int idLastEpreuve;
	
	
	//Constructor from all string args gotten from online database. Leid obtained by cross-checking with epreuve list
	public raid(String id, String nom, String dateDebut,int Leid) {
		this.id=Integer.parseInt(id);
		this.nom=nom;
		this.dateDebut=LocalTime.parse(dateDebut);
		this.idLastEpreuve=Leid;
	}
}
