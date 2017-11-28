package com.example.dataBase.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.bean.baseBean.BaseBean;
import com.example.dataBase.Impl.MySqlDataBaseManager;
import com.example.dataBase.util.PropertiesUtil;
import com.example.exception.IdIsNullException;
import com.example.exception.IdNotExistException;

public class MySqlOperation<T extends BaseBean> {

	private MySqlDataBaseManager mMySqlDataBaseManager = null;

	public MySqlOperation() {
		this.mMySqlDataBaseManager = new MySqlDataBaseManager();
	}

	public boolean isTableExist(String tableName) {
		String[] selections = new String[] { "table_name" , "TABLE_SCHEMA"};
		String[] selectionValues = new String[] { tableName, PropertiesUtil.getValue("dataBase.properties", "dataBase")};
		ResultSet query = mMySqlDataBaseManager.query("INFORMATION_SCHEMA.TABLES", selections,
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

	public boolean save(T t) {
		if (!isTableExist(t.getClassName())
				&& !mMySqlDataBaseManager.ceareTable(t.getClassName(), t.getFieldNames(), t.getFieldTypes())) {
			return false;
		}
		t.setId(UUID.randomUUID().toString());
		return mMySqlDataBaseManager.insert(t.getClassName(), t.getFieldNames(), t.getFieldValues());
	}
	public boolean update(T t) {
		if (t.getId() == null) {
			throw new IdIsNullException();
		}
		if (t.getIdName() == null) {
			throw new IdNotExistException();
		}
		return mMySqlDataBaseManager.update(t.getClassName(), t.getSignificativeFieldNames(),
				t.getSignificativeFieldValues(), new String[] { t.getIdName() }, new String[] { t.getId() });
	}

	public boolean delete(T t) {
		if (t.getId() == null) {
			throw new IdIsNullException();
		}
		if (t.getIdName() == null) {
			throw new IdNotExistException();
		}
		return mMySqlDataBaseManager.delete(t.getClassName(), new String[] { t.getIdName() },
				new String[] { t.getId() });
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryById(T t){
		String idName = t.getIdName();
		if(idName == null) {
			throw new IdNotExistException();
		}	
		List<T> list = new ArrayList<>();
		try (ResultSet resultSet = mMySqlDataBaseManager.query(t.getClassName(), new String[] {idName}, new String[] {t.getId()});){
			String[] fieldNames = t.getFieldNames();
			String[] fieldValues = new String[fieldNames.length];
			while(resultSet.next()) {
				for(int i = 0; i < fieldNames.length; i++) {
					fieldValues[i] = resultSet.getString(fieldNames[i]);
				}
				T t1 = (T) t.makeObject(fieldNames, fieldValues);
				list.add(t1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryByValuedField(T t){
		List<T> list = new ArrayList<>();
		try (ResultSet resultSet = mMySqlDataBaseManager.query(t.getClassName(), t.getSignificativeFieldNames(), t.getSignificativeFieldValues());){
			String[] fieldNames = t.getFieldNames();
			String[] fieldValues = new String[fieldNames.length];
			while(resultSet.next()) {
				for(int i = 0; i < fieldNames.length; i++) {
					fieldValues[i] = resultSet.getString(fieldNames[i]);
				}
				T t1 = (T) t.makeObject(fieldNames, fieldValues);
				list.add(t1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public void close() {
		mMySqlDataBaseManager.close();
	}
}
