package FX;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FxMS extends Remote {

	public String getVersion() throws RemoteException, Exception;

}
