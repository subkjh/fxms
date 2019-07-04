package subkjh.bas.dao.data;

public class Query extends SoDo {

	private String name;

	private String query;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {

		if (query != null && query.trim().length() > 0) {
			query = query.replaceAll("‘", "'");
			query = query.replaceAll("’", "'");
		}

		this.query = query;
	}

}
