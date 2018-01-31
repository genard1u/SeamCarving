package modelisation;

@SuppressWarnings("serial")
public class ReductionException extends RuntimeException {

	public ReductionException(int i, int r) {
		super("ERREUR REDUCTION\n\t" + "L'image ne peut pas être réduite davantage (" + i + " / " + r + ")");
	}

}
