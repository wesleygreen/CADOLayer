package ca.joshtaylor.CADO;
import org.apache.log4j.Logger;

public class TestLogger {
	
	final static Logger logger = Logger.getLogger(TestLogger.class);

	public static void main(String[] args) {
		
		logger.info("\nHello World");	//Standard output
		logger.error("\nError");			//Exceptions
		logger.debug("\nDebug");			//DEBUG Info
		
		
		
	}
	
}
