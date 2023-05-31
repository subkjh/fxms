package subkjh.dao.def;

/**
 * Oracle Table Space
 * 
 * @author subkjh
 * @since 2013.10.02
 * 
 */
public class TableSpace {

	private String name;
	private String filename;

	public String getFilename() {
		return filename;
	}

	public String getName() {
		return name;
	}

	public String getSqlCreate() {
		return "create tablespace " + name + " '" + filename + "' SIZE 5000M AUTOEXTEND ON NEXT 1024M MAXSIZE UNLIMITED" //
				+ " LOGGING ONLINE  PERMANENT " //
				+ " EXTENT MANAGEMENT LOCAL AUTOALLOCATE "//
				+ " BLOCKSIZE 8K  SEGMENT SPACE MANAGEMENT AUTO "//
				+ " FLASHBACK ON";
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setName(String name) {
		this.name = name;
	}
}
