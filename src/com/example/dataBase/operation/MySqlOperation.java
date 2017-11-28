package com.example.dataBase.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.annotation.MyAnnotation;
import com.example.bean.baseBean.BaseBean;
import com.example.dataBase.Impl.MySqlDataBaseManager;
import com.example.dataBase.util.PropertiesUtil;
import com.example.exception.IdIsNullException;
import com.example.exception.IdNotExistException;
/**
 * 本类是MySqlDataBaseManager类的代理类的，用户进行数据库的增、删、改、查，都是通过本类
 * 来进行操作的。本类连接用户和数据库。使用本类操作数据库时，需要注意的是，本类中每个操作，不会自动close连接，
 * 而在本类中提供了close，调用此close方法可以关闭数据库的连接。
 * @author pby
 */
public class MySqlOperation<T extends BaseBean> {

	private MySqlDataBaseManager mMySqlDataBaseManager = null;
	
	/**
	 * 构造方法，在这个方法里面初始化MySqlDataBaseManager对象。在这里，会发现每次创建MySqlOperation对象时，
	 * 都会初始化一个MySqlDataBaseManager一个对象，因此在每次数据库操作完成之后
	 */
	public MySqlOperation() {
		this.mMySqlDataBaseManager = new MySqlDataBaseManager();
	}
	
	/**
	 * 本方法判断一张表在数据库中是否存在，如果存在返回true，反之返回false。本方法在查询一张表是否存在时，
	 * 从dataBase.properties文件读取了key为dataBase的值。
	 * @param tableName 查询的表名
	 * @return boolean值，如果该表存在的话，返回值为true，反之为false
	 */
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
	
	/**
	 * 将一个对象保存数据库中，表名就为该对象的所在类的类名。这里的T表示的是泛型类型，具体类型是用户传递进来的对象类型。
	 * 在保存对象时，先判断该对象所在的表是否存在，如果不存在的话，就先进行建表操作，因此用户不需要自己来建表。如果建表
	 * 失败了的话，返回为false，并且不会往下执行。在进行真正的插入数据操作时，首先调用了T对象的setId方法设置了该对象的id属性，
	 * 通过源码可以看出id为UUID类型。
	 * 
	 * @param t 需要保存到数据库中的对象
	 * @return boolean值，如果插入数据成功的话，返回为true，反之为false
	 */
	public boolean save(T t) {
		//判断表是否存在，如果不存在，进行建表操作
		//如果建表失败的话，直接返回false
		if (!isTableExist(t.getClassName())
				&& !mMySqlDataBaseManager.ceareTable(t.getClassName(), t.getFieldNames(), t.getFieldTypes())) {
			return false;
		}
		//设置id值，通过UUID来生成
		t.setId(UUID.randomUUID().toString());
		return mMySqlDataBaseManager.insert(t.getClassName(), t.getFieldNames(), t.getFieldValues());
	}
	/**
	 * 本方法进行对数据库中的数据更新操作。使用该方法需要注意的是：t对象必须设置id属性值
	 * 因为更新是根据id来操作的，如果id为null，会抛出IdIsNullException异常（自定义的异常），如果
	 * 不能获取IDName值的话，会抛出IdNotExistException异常，因此Id属性必须增加@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)
	 * 注解来标记。
	 * 更新字段是非空的字段，不包括id字段。
	 * 
	 * @param t 需要的更新对象，必须设置id属性，其他设置的属性作为更新的字段
	 * @return boolean值，更新成功返回true，更新失败返回false
	 */
	public boolean update(T t) {
		//如果idName为空的话，表示没有id属性，或者id没有设置@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)注解
		if (t.getIdName() == null) {
			throw new IdNotExistException();
		}
		//如果id属性为空的话，抛出IdIsNullException异常
		if (t.getId() == null) {
			throw new IdIsNullException();
		}
		return mMySqlDataBaseManager.update(t.getClassName(), t.getSignificativeFieldNames(),
				t.getSignificativeFieldValues(), new String[] { t.getIdName() }, new String[] { t.getId() });
	}
	
	/**
	 * 本方法用来对数据进行删除操作，t对象中只需要设置id属性，设置其他属性是没有意义的
	 * 因为在进行删除操作的时候，是依靠Id属性。
	 * 
	 * @param t 需要删除的对象，只需要设置id属性即可
	 * @return boolean值，删除成功返回true，删除失败返回false
	 */
	public boolean delete(T t) {
		//如果idName为空的话，表示没有id属性，或者id没有设置@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)注解
		if (t.getIdName() == null) {
			throw new IdNotExistException();
		}
		//如果id属性为空的话，抛出IdIsNullException异常
		if (t.getId() == null) {
			throw new IdIsNullException();
		}
		return mMySqlDataBaseManager.delete(t.getClassName(), new String[] { t.getIdName() },
				new String[] { t.getId() });
	}
	
	/**
	 * 本方法进行数据的查询操作。方法是根据对象id值来查询，由于对象的id在数据库是唯一的，
	 * 所以本方法查询的结果集最多只有一个。
	 * @param t 需要查询的对象，必须设置id属性，也只需要设置id即可
	 * @return  返回的结果集
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryById(T t){
		String idName = t.getIdName();
		//如果idName为空的话，抛出IsNotExistException异常
		if(idName == null) {
			throw new IdNotExistException();
		}	
		//如果id属性为空的话，抛出IdIsNullException异常
		if (t.getId() == null) {
			throw new IdIsNullException();
		}
		List<T> list = new ArrayList<>();
		try (ResultSet resultSet = mMySqlDataBaseManager.query(t.getClassName(), new String[] {idName}, new String[] {t.getId()});){
		
			String[] fieldNames = t.getFieldNames();
			String[] fieldValues = new String[fieldNames.length];
			while(resultSet.next()) {
				//通过属性名来遍历每条记录的值
				for(int i = 0; i < fieldNames.length; i++) {
					fieldValues[i] = resultSet.getString(fieldNames[i]);
				}
				//调用makeObject方法来生成一个T对象
				T t1 = (T) t.makeObject(fieldNames, fieldValues);
				list.add(t1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 本方法进行数据的查询操作。本方法通过除id之外的其他属性来进行查询，将非空的属性作为查询条件。
	 * 由于条件不是唯一的，因此在有可能会返回多个值。在方法中设置id属性是没有意义的
	 * @param t 需要查询的对象，里面封装了查询的条件
	 * @return 返回的结果集
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryByValuedField(T t){
		List<T> list = new ArrayList<>();
		try (ResultSet resultSet = mMySqlDataBaseManager.query(t.getClassName(), t.getSignificativeFieldNames(), t.getSignificativeFieldValues());){
			String[] fieldNames = t.getFieldNames();
			String[] fieldValues = new String[fieldNames.length];
			while(resultSet.next()) {
				//通过属性名来遍历每条记录的值
				for(int i = 0; i < fieldNames.length; i++) {
					fieldValues[i] = resultSet.getString(fieldNames[i]);
				}
				//调用makeObject方法来生成一个T对象
				T t1 = (T) t.makeObject(fieldNames, fieldValues);
				list.add(t1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 本方法用于关闭数据库连接，在进行数据库操作之后，记得调用此方法来关闭数据连接
	 */
	public void close() {
		mMySqlDataBaseManager.close();
	}
}
