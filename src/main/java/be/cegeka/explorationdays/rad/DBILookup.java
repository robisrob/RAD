package be.cegeka.explorationdays.rad;

import org.skife.jdbi.v2.DBI;

public class DBILookup {

	private static DBILookup instance  = new DBILookup();
	private DBI jdbi;
	
	private DBILookup() {
	}
	
	public static DBILookup getInstance() {
		return instance;
	}
	
	public void setDBI(DBI jdbi) {
		this.jdbi = jdbi;
	}
	
	public DBI getJdbi() {
		return jdbi;
	}
	
}
