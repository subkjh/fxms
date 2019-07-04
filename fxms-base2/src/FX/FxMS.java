package FX;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FxMS extends Remote {

	public String addService(String serviceName, String javaClass) throws RemoteException, Exception;

	public String getVersion() throws RemoteException, Exception;

	public String removeService(String serviceName) throws RemoteException, Exception;

	public String startService(String serviceName) throws RemoteException, Exception;

	public String stopService(String serviceName) throws RemoteException, Exception;

}
