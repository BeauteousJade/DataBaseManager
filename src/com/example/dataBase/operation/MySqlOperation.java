package com.example.dataBase.operation;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.bean.baseBean.BaseBean;
import com.example.dataBase.Impl.MySqlDataBaseManager;

public class MySqlOperation<T extends BaseBean> {

	private MySqlDataBaseManager mMySqlDataBaseManager = null;

	public MySqlOperation() {
		this.mMySqlDataBaseManager = new MySqlDataBaseManager();
	}

	public boolean isTableExist(String tableName) {
		String[] columns = new String[0];
		String[] selections = new String[] { "table_name" };
		String[] selectionValues = new String[] { tableName };
		ResultSet query = mMySqlDataBaseManager.query("INFORMATION_SCHEMA.TABLES", columns, selections,
				selectionValues);
		boolean isExist = false;
		try {
			if (query.next()) {
				isExist = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public boolean insert(T t) {
		if (!isTableExist(t.getClassName())) {
			return false;
		}
		return mMySqlDataBaseManager.insert(t.getClassName(), t.getFieldNames(), t.getFieldTypes());
	}

	public boolean update(T t) {

		String[] selectArgNames = new String[] { t.getIdName() };
		String[] selectArgValues = new String[] { t.getId() };
		String[] updateArgNames = t.getFieldNames();
		String[] updateArgValues = t.getFieldValues();

		return mMySqlDataBaseManager.update(t.getClassName(), selectArgNames, selectArgValues, updateArgNames,
				updateArgValues);
	}

	public boolean delete(T t) {

		return mMySqlDataBaseManager.delete(t.getClassName(), new String[] { t.getIdName() },
				new String[] { t.getId() });
	}

	public void close() {
		mMySqlDataBaseManager.close();
	}
}
