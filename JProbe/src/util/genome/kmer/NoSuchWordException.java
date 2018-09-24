package util.genome.kmer;

public class NoSuchWordException extends RuntimeException{
	public NoSuchWordException() {
		super();
	}

	protected NoSuchWordException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoSuchWordException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchWordException(String message) {
		super(message);
	}

	public NoSuchWordException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}
