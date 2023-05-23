
public class participant {
	//Class to Store individual instances people participating in a race.
	public int id;
	public String code_puce;
	public int equipe_id;
	public Boolean abandon;
	public String categorie;
	public String nom;
	public String prenom;
	public String sexe;
	
	//Constructor from all string args gotten from online database. nom, prenom, sexe obtained from local Database
	public participant(String id, String code_puce, String eid, String abandon, String nom, String prenom, String sexe) {
		this.id=Integer.parseInt(id);
		this.code_puce=code_puce;
		this.equipe_id=Integer.parseInt(eid);
		this.abandon=(abandon!="0");
		this.nom=nom;
		this.prenom=prenom;
		this.sexe=sexe;
	}
	
	
	
}
