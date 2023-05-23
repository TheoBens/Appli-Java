
public class equipe {
	//Class to Store individual instances of teams
	public int id;
	public int dossard;
	public int raid_id;
	public String categorie;
	participant p1;
	participant p2;
	//Constructor from all string args gotten from online database. p1 and p2 obtained by cross-checking with participant list
	public equipe(String id, String dossard, String rid, String categorie,participant p1, participant p2) {
		this.id=Integer.parseInt(id);
		this.dossard=Integer.parseInt(dossard);
		this.raid_id=Integer.parseInt(rid);
		this.categorie=categorie;
		this.p1=p1;
		this.p2=p2;

	}
}
