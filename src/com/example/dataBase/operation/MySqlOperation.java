package com.example.dataBase.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.example.bean.baseBean.BaseBean;
import com.example.dataBase.Impl.MySqlDataBaseManager;
import com.example.dataBase.util.PropertiesUtil;

public class MySqlOperation<T extends BaseBean> {

	private MySqlDataBaseManager mMySqlDataBaseManager = null;

	public MySqlOperation() {
		this.mMySqlDataBaseManager = new MySqlDataBaseManager();
	}

	public boolean isTableExist(String tableName) {
		String[] columns = new String[0];
		String[] selections = new String[] { "table_name" , "TABLE_SCHEMA"};
		String[] selectionValues = new String[] { tableName, PropertiesUtil.getValue("dataBase.properties", "dataBase")};
		ResultSet query = mMySqlDataBaseManager.query("INFORMATION_SCHEMA.TABLES", columns, selections,
				selectionValues);
		boolean isExist = true;
		try {
			if (query == null || !query.next()) {
				isExist = false;
			} else {
				query.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public boolean insert(T t) {
		if (!isTableExist(t.getClassName())
				&& !mMySqlDataBaseManager.ceareTable(t.getClassName(), t.getFieldNames(), t.getFieldTypes())) {
			return false;
		}
		System.out.println(Arrays.toString(t.getFieldNames()));
		System.out.println(Arrays.toString(t.getFieldTypes()));
		return mMySqlDataBaseManager.insert(t.getClassName(), t.getFieldNames(), t.getFieldValues());
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
