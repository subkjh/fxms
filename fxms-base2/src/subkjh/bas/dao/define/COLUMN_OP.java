package subkjh.bas.dao.define;

public enum COLUMN_OP {

	insert {
		@Override
		public boolean isAddable() {
			return true;
		}

		@Override
		public boolean isUpdatable() {
			return false;
		}

	}

	,
	update {
		@Override
		public boolean isAddable() {
			return false;
		}

		@Override
		public boolean isUpdatable() {
			return true;
		}

	}

	,
	all {
		@Override
		public boolean isAddable() {
			return true;
		}

		@Override
		public boolean isUpdatable() {
			return true;
		}

	};

	private COLUMN_OP() {

	}

	public abstract boolean isAddable();

	public abstract boolean isUpdatable();

}
