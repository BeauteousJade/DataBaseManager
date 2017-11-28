
# DataBaseManager
##### 电脑环境要求：jdk版本在1.7及其以上
# 1.介绍
&emsp;&emsp;本项目使用使用的是mysql的5.1.44的版本,通过配置dataBase.properties文件可以连接不同的mysql数据库。如果需要更改dataBase.properties的读入地址，或者文件名有所变换，可以改写com.example.dataBase.util.PropertiesUtil类中的代码，但是必须保证读入的内容是正确的。  
&emsp;&emsp;使用本项目连接数据库中需要注意几个类的使用：
##### &emsp;&emsp;1.BaseBean类。这个类是对应着数据库每个表，用户想要将数据保存在数据库中，所以使用的Bean必须继承于BaseBean类。在BaseBean类中，已经定义了id为主键，所以用户不需要指定主键。BaseBean类中还有很多的方法，这里不解释，如果用户感兴趣的话， 可以查看看BaseBean的源代码。
##### &emsp;&emsp;2. MySqlOperation类。这个类有很多关于数据库操作的方式，例如查询，删除，修改等等。
&emsp;&emsp;使用本项目只需要了解这两个类就行了。
# 2. 使用方式
##### &emsp;&emsp;1. 配置dataBase.properties文件，其中参数需要跟你的mysql数据库配置一致。
##### &emsp;&emsp;2. 如果想要将一个对象保存在数据库，首先这个对象所属的类必须继承于BaseBean，还同时可以在定义很多属性，但是属性必须含有set和get方法，同时必须是String类型。
##### &emsp;&emsp;3. 创建MySqlOperation类的对象，并且指定泛型类型为自定义的Bean
```
		MySqlOperation<User> operation = new MySqlOperation<>();
		
		User user = new User();
		
		//保存一条记录，对应的表名是类名，如果表不存在的话，会自己创建
		operation.save(user);
		
		//删除一条记录，删除记录时必须调用setId方法来设置
		user.setId("id");
		operation.delete(user);
		
		//更新一条记录，设置你需要改变的属性的值，必须设置Id
		user.setId("id");
		user.setPassword("password");
		user.setDisplayName("displayName");
		operation.update(user);
		
		//查询记录，两种方式
		//通过Id，只要找到一条记录
		user.setId("id");
		//找到返回一个User的List，其中的每个对象属性都是与数据库一致
		List<User> userList1 = operation.queryById(user);
		
		//通过有值的属性，可能会找到很多的记录
		user.setUserName("pby");
		List<User> userList2 = operation.queryById(user);
		
		//关闭连接
		operation.close();
```
# 3.注意事项
##### &emsp;&emsp;1. 所定义的Bean类中属性，属性名必须符合驼峰命名法，例如：userName, password, displayName之类。
##### &emsp;&emsp;2. 属性必须同时含有set和get方式。因为内部使用反射来调用这两个方法的。
##### &emsp;&emsp;3. 所有的属性必须是String类型的。考虑到有些字符串是大字符串，可以通过@MyAnnotation(fieldType = MyAnnotation.FieldType.LONG_STRING)注解来设置属性是大字符串类型。
##### &emsp;&emsp;4. 不得给属性设置@MyAnnotation(fieldMark = MyAnnotation.FieldMark.ID)注解，这个注解是标注当前属性是主键。

