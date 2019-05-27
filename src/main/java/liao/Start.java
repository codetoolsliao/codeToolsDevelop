package liao;

import liao.code.generator.AbstractCodeGenerator;
import liao.code.generator.back.javacode.AbstractClassGenerator;
import liao.code.generator.back.factory.RegistrationFactory;
import liao.code.generator.back.sql.SqlGenerator;
import liao.parse.table.model.Table;
import liao.parse.table.mysql.ParseTableForMySQL;
import liao.parse.table.oracle.ParseTableForOracle;
import liao.parse.table.oracle.ParseTableListForOracle;

import java.util.List;
import java.util.Scanner;

/**
 * Created by ao on 2017/10/16.
 */
public class Start {
    public static void  main(String[] args){
        System.out.println("请输入表名称：");
        Scanner sc = new Scanner(System.in);
        String tableName = sc.nextLine().trim();
       // Table table = new ParseTableForMySQL(tableName).getTable();
        Table table = new ParseTableForOracle(tableName).getTable();
       // List<Table> tableList = new ParseTableListForOracle().getTable(tableName);
        //tb_com_city,tb_com_county
        List<AbstractCodeGenerator> generatorList = RegistrationFactory.getGeneratorList();
        //for(Table table : tableList){
            for(AbstractCodeGenerator classGenerator : generatorList){
                classGenerator.generatorCode(table);
            }
      //  }

    }
}
