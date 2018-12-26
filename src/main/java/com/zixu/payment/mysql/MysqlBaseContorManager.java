package com.zixu.payment.mysql;

import com.zixu.payment.mysql.JsonUtils;
import com.zixu.payment.mysql.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author: Zixu Liao
 * @description:mysql操作方法
 */
public class MysqlBaseContorManager {
    private static final Config config = new Config();

    public static List<Map<String, String>> executeQuery(String sql) {
        return executeQuery(sql, (Object) null);
    }

    /**
     * mysql查询
     *
     * @param sql  sql语句
     * @param args 注入参数
     * @return 查询结果
     */

    public static List<Map<String, String>> executeQuery(String sql, Object... args) {
        System.out.println(sql);

        Connection conn = MysqlConnPoolManager.getConn();
        if (conn == null) {
            return null;
        }

        sql = sql.replace("tablepre_", config.get("pre"));


        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    Object val = args[i];
                    if(val != null) {
                        ps.setObject(i + 1, val);
                    }
                }
            }
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            List<Map<String, String>> query = new ArrayList<Map<String, String>>();

            while (rs.next()) {
                HashMap<String, String> list = new HashMap<String, String>();
                for (int i = 1; i <= col; i++) {
                    list.put(rsmd.getColumnName(i), String.valueOf(rs.getObject(i)));
                }
                query.add(list);
            }
            rs.close();
            ps.close();
            return query;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            MysqlConnPoolManager.closeConn(conn);
        }
    }

    public static boolean executeUpdate(String sql) {
        return executeUpdate(sql, (Object) null);
    }

    /***
     * mysql执行语句
     * @param sql sql语句
     * @param args 注入参数
     * @return true or false
     */

    public static boolean executeUpdate(String sql, Object... args) {
        System.out.println(sql);
        Connection conn = MysqlConnPoolManager.getConn();
        if (conn == null) {
            return false;
        }

        sql = sql.replace("tablepre_", config.get("pre"));

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (args.length >= 1) {
                if (args.length == 1 && args[0] == null) {
                    return false;
                }
                for (int i = 0; i < args.length; i++) {
                    Object val = args[i];
                    ps.setObject(i + 1, val);
                }
            }
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            MysqlConnPoolManager.closeConn(conn);
        }
    }

    /***
     * 新增条目方法
     * @param entity 实体类
     * @return 结果
     */

    public static boolean add(Object entity) {
        String[] entityClassName = entity.getClass().toString().split("\\.");
        String tableName = config.get("pre") + CommonUtils.camelToUnderline(entityClassName[entityClassName.length - 1]);
        StringBuilder sql = new StringBuilder().append("insert into `").append(tableName).append("` ");
        StringBuilder insertKey = new StringBuilder();
        StringBuilder insertVal = new StringBuilder();
        Map<String, Object> entityToMap = JsonUtils.jsonObject(JsonUtils.toJson(entity));
        for (Object key : entityToMap.keySet()) {
            insertKey.append(" `").append(key).append("` ,");
            insertVal.append(" ? ,");
        }
        insertKey.setLength(insertKey.length() - 1);
        insertVal.setLength(insertVal.length() - 1);
        sql.append("(").append(insertKey).append(")").append(" values ").append("(").append(insertVal).append(")");
        int i = 0;
        Object[] sendArgs = new Object[entityToMap.size()];
        for (Object key : entityToMap.keySet()) {
            if (entityToMap.get(key) instanceof Boolean) {
                boolean bool = (boolean) entityToMap.get(key);
                sendArgs[i] = bool ? 1 : 0;
            } else if(entityToMap.get(key).getClass().equals(Date.class)){
                sendArgs[i] = DateUtil.dateToStrLong((Date) entityToMap.get(key));
            }else{
                sendArgs[i] = entityToMap.get(key);
            }
            i++;
        }
        return executeUpdate(sql.toString(), sendArgs);
    }
    public static boolean add(String tableName,Object entity) {
    	
    	StringBuilder sql = new StringBuilder().append("insert into `").append(tableName).append("` ");
    	StringBuilder insertKey = new StringBuilder();
    	StringBuilder insertVal = new StringBuilder();
    	Map<String, Object> entityToMap = JsonUtils.jsonObject(JsonUtils.toJson(entity));
    	for (Object key : entityToMap.keySet()) {
    		insertKey.append(" `").append(key).append("` ,");
    		insertVal.append(" ? ,");
    	}
    	insertKey.setLength(insertKey.length() - 1);
    	insertVal.setLength(insertVal.length() - 1);
    	sql.append("(").append(insertKey).append(")").append(" values ").append("(").append(insertVal).append(")");
    	int i = 0;
    	Object[] sendArgs = new Object[entityToMap.size()];
    	for (Object key : entityToMap.keySet()) {
    		if (entityToMap.get(key) instanceof Boolean) {
    			boolean bool = (boolean) entityToMap.get(key);
    			sendArgs[i] = bool ? 1 : 0;
    		} else if(entityToMap.get(key).getClass().equals(Date.class)){
    			sendArgs[i] = DateUtil.dateToStrLong((Date) entityToMap.get(key));
    		}else{
    			sendArgs[i] = entityToMap.get(key);
    		}
    		i++;
    	}
    	return executeUpdate(sql.toString(), sendArgs);
    }

    /***
     * 删除方法
     * @param tableName 表名
     * @param args {  id,=,1,  name,=,aname  } < 数组类型
     * @return 查询结果
     */
    public static boolean delete(String tableName, Object... args) {
        if (args == null || args.length <= 0) {
            return false;
        }
        tableName = config.get("pre") + tableName;
        StringBuilder sql = new StringBuilder(MessageFormat.format("delete from `{0}`", tableName));
        int loop = 0;
        if (args.length % 3 != 0) {
            return false;
        } else {
            loop = args.length / 3;
        }
        Object[] sendArgs = new Object[loop];
        for (int i = 0; i < loop; i++) {
            if (i == 0) {
                sql.append("where ");
            } else {
                sql.append("and ");
            }
            sql.append(args[(i * 3)]).append(" ").append(args[(i * 3) + 1]).append(" ? ");
            sendArgs[i] = args[(i * 3) + 2];
        }
        return executeUpdate(sql.toString(), sendArgs);
    }

    /***
     * 删除方法
     * @param entity 可传实体 实体class名即为数据库表名 必须带有id 驼峰会转下横线写法
     * @return 删除结果
     */

    public static boolean delete(Object entity) {
        String[] entityClassName = entity.getClass().toString().split("\\.");
        String tableName = CommonUtils.camelToUnderline(entityClassName[entityClassName.length - 1]);
        Map<String, Object> entityToMap = JsonUtils.jsonObject(JsonUtils.toJson(entity));
        if (entityToMap == null || entityToMap.get("id") == null) {
            return false;
        }
        return delete(tableName, "id", "=", entityToMap.get("id"));
    }

    public static boolean update(Object entity) {
        return update(entity, "id");
    }

    /***
     * 更新方法
     * @param entity 更新后实体
     * @param principaKey 更新条件key
     * @return true or false
     */

    public static boolean update(Object entity, String principaKey) {
        String[] entityClassName = entity.getClass().toString().split("\\.");
        String tableName = config.get("pre") + CommonUtils.camelToUnderline(entityClassName[entityClassName.length - 1]);
        Map<String, Object> entityToMap = JsonUtils.jsonObject(JsonUtils.toJson(entity));
        if (entityToMap == null || entityToMap.get(principaKey) == null) {
            return false;
        }
        StringBuilder set = new StringBuilder();
        Object principaValue = entityToMap.get(principaKey);
        entityToMap.remove(principaKey);
        for (Object key : entityToMap.keySet()) {
            set.append(" `").append(key).append("` = ? ,");
        }
        set.setLength(set.length() - 1);
        StringBuilder sql = new StringBuilder(MessageFormat.format("update `{0}`", tableName)).append(" set").append(set);
//        where
        sql.append("where ").append(principaKey).append(" = ?");
//        注入参数
        Object[] sendArgs = new Object[entityToMap.size() + 1];
        int i = 0;
        for (Object key : entityToMap.keySet()) {
            sendArgs[i] = entityToMap.get(key);
            i++;
        }

        sendArgs[entityToMap.size()] = principaValue;

        return executeUpdate(sql.toString(), sendArgs);
    }

    /***
     * 统计方法
     * @param sql sql语句
     * @param args 注入参数
     * @return 数量
     */

    public static int count(String sql, Object... args) {
        List<Map<String, String>> list = executeQuery(sql, args);
        if (list == null) {
            return 0;
        }
        Map<String, String> res = list.get(0);
        for (Map.Entry<String, String> entry : res.entrySet()) {
            String value = entry.getValue();
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /***
     * 统计方法
     * @param tableName 表名
     * @return 表下所有的条目数量
     */

    public static int count(String tableName) {
        tableName = config.get("pre") + tableName;
        String sql = "select count(*) from `" + tableName + "`";
        return count(sql, (Object) null);
    }

    /***
     * 查询方法
     * @param tableName 表名
     * @param args {  id,=,1,  name,=,aname  } < 数组类型
     * @return 查询结果
     */

    public static List<Map<String, String>> find(String tableName, Object[] args) {
        tableName = config.get("pre") + tableName;
        int loop = 0;
        if (args != null) {
            if (args.length % 3 != 0) {
                return null;
            } else {
                loop = args.length / 3;
            }
        }
        StringBuilder sql = new StringBuilder(MessageFormat.format("select * from `{0}` ", tableName));
        Object[] sendArgs = new Object[loop];
        for (int i = 0; i < loop; i++) {
            if (i == 0) {
                sql.append("where ");
            } else {
                sql.append("and ");
            }
            sql.append(args[(i * 3)]).append(" ").append(args[(i * 3) + 1]).append(" ? ");
            sendArgs[i] = args[(i * 3) + 2];
        }
        sql.append(" order by id desc ");
        return executeQuery(sql.toString(), sendArgs);
    }

    /***
     * 查询方法（只返回一条）
     * @param tableName 表名
     * @param args {  id,=,1,  name,=,aname  } < 数组类型
     * @return 查询结果
     */

    public static Map<String, String> findOne(String tableName, Object[] args) {
        List<Map<String, String>> list = find(tableName, args);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /***
     * 查询方法（只返回第一条）
     * @param sql 语句
     * @param args 参数
     * @return map
     */

    public static Map<String, String> executeQueryGet(String sql, Object args) {
        List<Map<String, String>> queryRes = executeQuery(sql, args);
        if (queryRes == null || queryRes.size() == 0) {
            return null;
        }
        return queryRes.get(0);
    }
}
