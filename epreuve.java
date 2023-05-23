
public class epreuve {
	//Class to Store individual instances of a checkpoint in a raid
	public int id;
	public int raid_id;
	public String nom;
	public int checkpoint;
	public float longueur;

	//Constructor from all string args gotten from online database.
	public epreuve(String id, String raid_id, String nom, String checkpoint, String longueur) {
		this.id = Integer.parseInt(id);
		this.raid_id = Integer.parseInt(raid_id);
		this.nom = nom;
		if (checkpoint != "")
			this.checkpoint = Integer.parseInt(checkpoint);
		this.longueur = Float.parseFloat(longueur);
	}
}
