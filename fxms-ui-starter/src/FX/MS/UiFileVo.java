package FX.MS;

public class UiFileVo {

	private String name;
	private long size;
	private long lastModified;
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append(" : size=");
		sb.append(size);
		sb.append(" : time=");
		sb.append(lastModified);
		return sb.toString();
	}
	
	public UiFileVo()
	{
		
	}
	
	public String getValue()
	{
		return size + "," + lastModified;
	}
	
	public void setValue(String s)
	{
		String ss[] = s.split(",");
		if ( ss.length == 2) {
			size = Long.valueOf(ss[0]);
			lastModified = Long.valueOf(ss[1]);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
