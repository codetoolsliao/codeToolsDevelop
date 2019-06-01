package liao;

import liao.code.generator.AbstractCodeGenerator;
import liao.code.generator.back.factory.RegistrationFactory;
import liao.parse.table.model.Table;
import liao.parse.table.oracle.ParseTableForOracle;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Scanner;

/**
 * Created by ao on 2017/10/16.
 */
public class Start {
    public static void  main(String[] args){
        Start.multiTable();
    }
    //单表
    private static void singleTable(){
        System.out.println("请输入单个表名称：");
        Scanner sc = new Scanner(System.in);
        String tableName = sc.nextLine().trim();
        // Table table = new ParseTableForMySQL(tableName).getTable();
        Table table = new ParseTableForOracle(tableName).getTable();
        List<AbstractCodeGenerator> generatorList = RegistrationFactory.getGeneratorList();
        for(AbstractCodeGenerator classGenerator : generatorList){
            classGenerator.generatorCode(table);
        }
    }
    //多表
    private static void multiTable(){
        System.out.println("请输入多个表名称，以英文逗号隔开：");
        Scanner sc = new Scanner(System.in);
        String tableName = sc.nextLine().trim();
        if(StringUtils.isBlank(tableName)){
            return;
        }
        List<Table> tableList = new ParseTableForOracle(tableName).getTableList();
        List<AbstractCodeGenerator> generatorList = RegistrationFactory.getGeneratorList();
        for(Table table : tableList){
        for(AbstractCodeGenerator classGenerator : generatorList){
            classGenerator.generatorCode(table);
        }
        }
    }
}
