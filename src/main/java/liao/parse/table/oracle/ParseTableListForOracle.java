package liao.parse.table.oracle;/**
 * Created by wrj on 2019/4/2.
 */

import liao.code.generator.page.enums.NullableEnum;
import liao.parse.table.model.Column;
import liao.parse.table.model.Table;
import liao.utils.CommonUtils;
import liao.utils.NameUtils;
import liao.utils.PropertyUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author wrj
 * @date 2019/4/2 16:10
 * @description 多表
 **/
public class ParseTableListForOracle {
    private static final Properties dbConf = PropertyUtils.getConfig("config");
    private String tableColumnSQL = "select s.column_name as column_name, cs.COMMENTS as column_comment, s.nullable as is_nullable, s.data_type as data_type,s.DATA_SCALE as data_scale from user_tab_columns s, user_col_comments cs where cs.COLUMN_NAME = s.COLUMN_NAME and cs.TABLE_NAME = s.TABLE_NAME and s.table_name = '#tableName#'";
    private String tableDefineSQL = "SELECT c.COMMENTS as table_comment FROM user_tab_comments c WHERE c.table_name = '#tableName#'";
    private Connection conn;
    public List<Table> parseTableName(String tableName){
        List<Table> tableList = new ArrayList<>();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(tableName)){
            String[] tableNameList = tableName.split(",");
            for(String name : tableNameList)
                tableList.add(new Table(name.toLowerCase()));
        }
        return tableList;
    }
    public List<Table> getTable(String tableName){
        List<Table> tableList = parseTableName(tableName);
        for(Table table : tableList) {
            tableColumnSQL = tableColumnSQL.replace("#tableName#", table.getTableName().toUpperCase());
            tableDefineSQL = tableDefineSQL.replace("#tableName#", table.getTableName().toUpperCase());
            try {
                conn = getConnection();
                Statement stat = getStatement(conn);
                ResultSet rs = stat.executeQuery(tableColumnSQL);
                List<Column> columnList = convertToColumnList(rs, table.getTableName());
                ResultSet rs1 = stat.executeQuery(tableDefineSQL);
                table.setComment(getTableComment(rs1));
                table.setColumnList(columnList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tableList;
    }
    public String getTableComment(ResultSet rs) throws SQLException {
        while(rs.next()){
            return rs.getString("table_comment") == null ? "" :  rs.getString("table_comment");
        }
        return "";
    }
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(dbConf.getProperty("url"),dbConf.getProperty("name"),dbConf.getProperty("password"));
    }
    public Statement getStatement(Connection conn) throws SQLException {
        return conn.createStatement();
    }
    private List<Column> convertToColumnList(ResultSet rs,String tableName) throws SQLException {
        List<Column> columnList = new ArrayList<>();
        while(rs.next()){
            String colName = rs.getString("column_name");
            String colType = rs.getString("data_type");
            String colComment = rs.getString("column_comment");
            String dataScale = rs.getString("data_scale");
            int isNullable = rs.getBoolean("is_nullable") ? NullableEnum.YES.getValue() : NullableEnum.NO.getValue();
            String camelColName = NameUtils.underline2Camel(colName.toLowerCase());//转成驼峰命名
            String colJavaType = CommonUtils.sqlTypeToJavaTypeForOracle(colType,dataScale);
            Column col = new Column();
            col.setColName(colName);
            col.setCamelColName(camelColName);
            col.setColDBType(colType);
            col.setColJavaType(colJavaType);
            col.setComment(colComment);
            col.setNullable(isNullable);
            col.setTableName(tableName);
            columnList.add(col);
        }
        return columnList;
    }
}
