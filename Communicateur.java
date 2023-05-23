import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Communicateur {
	
	//-----------------------------------------------------------------------
	public String get(String uri) throws Exception {
		// Création d'un client HTTP (une sorte de "navigateur web")
	    HttpClient client = HttpClient.newHttpClient();
	    
	    // Création d'une requete HTTP (le message qui sera envoyé)
	    HttpRequest request = HttpRequest.newBuilder()
	          .uri(URI.create(uri))
	          .build();

	    // Envoi de la requete, recupération de la réponse (blocage en attendant la réponse)
	    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

	    return response.body();
	}
	
	
	//-----------------------------------------------------------------------	
	public CompletableFuture<String> getAsync(String uri) {
		// Création d'un client HTTP (une sorte de "navigateur web")
	    HttpClient client = HttpClient.newHttpClient();
	    
	    // Création d'une requete HTTP (le message qui sera envoyé)	    
	    HttpRequest request = HttpRequest.newBuilder()
	          .uri(URI.create(uri))
	          .build();

	    // Envoi de la requete, retour d'un "CompletableFutur"	    
	    return client.sendAsync(request, BodyHandlers.ofString())
	          .thenApply(HttpResponse::body);
	}
	
	
}
