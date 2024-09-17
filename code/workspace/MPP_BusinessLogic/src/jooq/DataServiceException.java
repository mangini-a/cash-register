package jooq;

import java.sql.SQLException;

@SuppressWarnings("serial")
class DataServiceException extends RuntimeException {
	public DataServiceException(String message, SQLException cause) {
        super(message, cause);
    }
}
