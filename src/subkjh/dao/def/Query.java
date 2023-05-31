package subkjh.dao.def;

public class Query {

	public enum SQL_TYPE {

		INSERT

		, DELETE

		, UPDATE

		, CREATE

		, DROP;

	}

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
