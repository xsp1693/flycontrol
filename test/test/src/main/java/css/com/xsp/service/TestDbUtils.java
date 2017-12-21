/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>TestDbUtils.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2017年4月10日 上午10:56:16<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestDbUtils{
    //database parameters
    private static String url       = "jdbc:mysql://127.0.0.1:3306/flycontrol";  
    private static String driver    = "com.mysql.jdbc.Driver";  
    private static String account   = "root";  
    private static String password  = "123456";  
    //pool manage parameters
    private static ComboPooledDataSource poolDataSource;
    private static boolean loaded    = false;
    private static boolean inited   = false;    //global parameter,only initialize one time

    private static int initPoolSize = 10;
    private static int minPoolSize  = 5;
    private static int maxPoolSize  = 10;
    private static int maxStatement = 100;
    private static int maxIdleTime  = 60;
    
    //current instance manage
    private Connection conn          = null;        //current connection object
    private boolean connected       = false;    //current connection if established

    /**to initialize pool data source
     * ComboPooledDataSource only will be initialize one time even if you call it for many times
     */
    private static void initParam(){
        if (inited) return;

        try{
            poolDataSource = new ComboPooledDataSource();
            poolDataSource.setUser(account);  
            poolDataSource.setPassword(password);  
            poolDataSource.setJdbcUrl(url);  
            poolDataSource.setDriverClass(driver);  
            poolDataSource.setInitialPoolSize(initPoolSize);  
            poolDataSource.setMinPoolSize(minPoolSize);  
            poolDataSource.setMaxPoolSize(maxPoolSize);  
            poolDataSource.setMaxStatements(maxStatement);  
            poolDataSource.setMaxIdleTime(maxIdleTime);
        }catch(Exception e){
            e.printStackTrace();
        }
        inited = true;
    }
    /**to load database configuration file and get database parameter
     * this function only can be called one time
     * @param configFile file of database configuration
     */
    private static void loadParam(String configFile) {
        if (loaded) return;

/*        if (CStrUtils.isBlankStr(configFile))
            configFile = "/config/db.properties";

        CPropUtils cfg = new CPropUtils(configFile);
        //load database parameters
        url       = cfg.getValue("url");
        driver    = cfg.getValue("driver");
        account   = cfg.getValue("account");
        password  = cfg.getValue("password");
        //load pool parameters
        initPoolSize = CStrUtils.strToIntDef(cfg.getValue("initPoolSize"),10);
        minPoolSize  = CStrUtils.strToIntDef(cfg.getValue("minPoolSize"),5);
        maxPoolSize  = CStrUtils.strToIntDef(cfg.getValue("maxPoolSize"),10);
        maxStatement = CStrUtils.strToIntDef(cfg.getValue("maxStatement"),100);
        maxIdleTime  = CStrUtils.strToIntDef(cfg.getValue("maxIdleTime"),60);
        
        loaded = true;
        cfg    = null;*/
    }
    
    public TestDbUtils(String configFile){
        loadParam(configFile);
        initParam();
        openConnection();
    }

   
    public void init(String configFile) {
        loadParam(configFile);
        initParam();
    }
        
    /**to override CObject.dispose function
     */
    public void dispose(){
        closeConnection();
    }
 
    /**to establish connection to database
     * @return true if connected,false due to failure connection
     */
    public boolean openConnection() {
        if (connected) return true;

        initParam();
        try{
            this.conn = poolDataSource.getConnection();
            this.connected = true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return this.connected;
    }
    
    public Connection getConnection(){
    	if (!connected){
    		openConnection();
    	}
    	return this.conn;
    }

    /**to close database connection,caller need to close database after using
     */
    public void closeConnection() {
        try{
            if ((conn!=null) && (!conn.isClosed()))
                DbUtils.closeQuietly(conn);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        conn = null;
    }

    /**execute sql script for effect rows
     * @param strSql the sql text to be executed
     * @return =-1 means system error or >=0 means rows effected
     */
   
    public int executeSql(String sql) {
        int ret = -1;
        QueryRunner qr = null;
        try{
            qr = new QueryRunner();
            ret = qr.update(conn,sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**execute sql with parameter
     * @param strSql sql to be executed
     * @param params parameters of sql
     * @return =-1 means system error or >=0 means rows effected
     */
   
    public int executeSql(String sql, Object[] params) {
        int ret = -1;
        QueryRunner qr = null;
        try{
            qr = new QueryRunner();
            ret = qr.update(conn,sql,params);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**to get auto increment field value after insert
     * @param tbName table to query
     * @param fieldName the table's field name
     * @return -1 means system error or return the field max value
     */
    public int getAutoIncId(String table, String field) {
        int ret = -1;
        QueryRunner qr = null;
        try{
            String strSql = String.format("select max(%s) from %s",field,table);
            qr = new QueryRunner(); 
            Long t = (Long)qr.query(conn,strSql,new ScalarHandler<Long>(1));    //to get column 1 value
            ret = t.intValue();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**to query table record count
     * @param tbName table to query
     * @return -1 means system error or return the table record count
     */
   
    public int getTableRows(String table) {
        int ret = -1;
        QueryRunner qr = null;
        try{
            String strSql = String.format("select count(*) from %s",table);
            qr = new QueryRunner();             
            Long t = (Long)qr.query(conn,strSql,new ScalarHandler<Long>(1));    //to get column 1 value
            ret = t.intValue();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**to judge table whether exists
     * @param tableName name of the table to judge
     * @return true if exists or return false 
     */
   
    public boolean tableExists(String table) {
        boolean ret = false;
        QueryRunner qr = null;
        try{
            String strSql = String.format("select count(*) from %s where 1<0",table);
            qr = new QueryRunner();           
            Long t = (Long)qr.query(conn,strSql,new ScalarHandler<Long>(1));    //to get column 1 value
            ret = t.intValue()>=0;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**execute sql for first row data and convert to bean
     * @param sql query sql script
     * @param bh, such as User.class
     * eg: User u=queryForBean("select * from user where id=10",User.Class);
     * @return bean or null
     */
    public <T>T queryForBean(String sql, Class<T> cls){
        QueryRunner qr = null;
        T ret = null;
        try{
            qr = new QueryRunner();
            ret = qr.query(conn,sql,new BeanHandler<T>(cls));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**execute sql for first row data and convert to bean with sql parameters
     * @param sql query sql script
     * @param bhd, such as User.class
     * @param params parameters of sql
     * eg: User u=queryForBean("select * from user where id=? and name=?",User.class,new Object[1,"kitty"]);
     * @return bean or null
     */
    public <T>T queryForBean(String sql, Class<T> cls, Object[] params){
        QueryRunner qr = null;
        T ret = null;
        try{
            qr = new QueryRunner();
            ret = qr.query(conn,sql,new BeanHandler<T>(cls),params);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }
    
    /**execute sql for Java bean
     * @param the sql for data
     * @param cls bean class
     * eg: List<User> us=queryForBeans("select * from user where id<10",User.class);
     * @return bean object list if success, otherwise return null
     */
   
    public <T>List<T> queryForBeans(String sql, Class<T> cls){
        QueryRunner qr = null;
        List<T> ret = null;
        try{
            qr = new QueryRunner();
            ret = qr.query(conn,sql,new BeanListHandler<T>(cls));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }

    /**execute sql for java bean with sql parameters
     * @param sql the sql text
     * @param cls bean class,such as new User.class
     * @param params sql parameters
     * @return bean object list if success, otherwise return null
     * eg: List<User> u=queryForBeans("select * from user where id=? and name=?",User.class,new Object[]{1,"test"});
     */
    public <T>List<T> queryForBeans(String sql, Class<T> cls, Object[] params){
        QueryRunner qr = null;
        List<T> ret = null;
        try{
            qr = new QueryRunner();
            ret = qr.query(conn, sql, new BeanListHandler<T>(cls), params);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        qr = null;
        return ret;
    }
    public List< Map<String, Object>> queryForMapList(String sql,Object[] params){
    	QueryRunner qRunner=new QueryRunner();
    	List< Map<String, Object>> list=null;
        try {
			list= qRunner.query(conn, sql, new MapListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return list;
    }
    
    ////////////////////////////////////////// for test case //////////////////////////////////////////
    public static void main(String[] args){
    	
        //for (int i=0;i<20;i++){
            TestDbUtils drv = new TestDbUtils("/config/db.properties");
            /*SDictItem b = drv.queryForBean("select item_id uuid,dict_code code,item_name itemname,item_code itemcode,ITEM_ORDER itemorder from S_DICT_ITEM where dict_code=?", SDictItem.class,new Object[]{"ywfl"});
            System.out.println("query for bean:"+b.getItemName());

            List<SDictItem> bs = drv.queryForBeans("select item_id uuid,dict_code code,item_name itemname,item_code itemcode,ITEM_ORDER itemorder from S_DICT_ITEM", SDictItem.class);
            System.out.println("query for list bean:"+bs.size());*/
            
            String sql="select * from S_DICT_ITEM where dict_code='ywfl'";
            List< Map<String, Object>> list= drv.queryForMapList(sql, null);
            String s=JSON.toJSONString(list);
            System.out.println(s);
            JSONArray arr=JSONArray.parseArray(s);
            System.out.println(arr.toJSONString());
            drv.dispose();
            drv = null;
//        }
        System.out.println("finished");
    }
}
