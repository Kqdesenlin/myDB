package com.domain.event;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.ExpressionVisitorWithRtn;
import com.domain.Entity.AlterEntity;
import com.domain.Entity.InsertEntity;
import com.domain.Entity.bTree.Entry;
import com.domain.Entity.common.ColumnInfoEntity;
import com.domain.Entity.common.LimitPart;
import com.domain.Entity.enums.AlterColumnEnums;
import com.domain.Entity.enums.ColumnSpecsEnums;
import com.domain.Entity.enums.ColumnTypeEnums;
import com.domain.Entity.enums.ColumnTypeLengthEnums;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.result.ResultCode;
import com.domain.repository.TableConstant;
import net.sf.jsqlparser.expression.Expression;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 校验操作
 */
public class CheckOperate {
    /**
     * 是否存在对应名称的表
     * @param tableName
     * @return
     */

    public boolean ifTableExists(String tableName){
        return TableConstant.tableMap.containsKey(tableName);
    }
    /**
     * 判断建表规则是否合法
     * @param
     * @return
     */
    public OperateResult ifColumnLegal(List<ColumnInfoEntity> columnInfoEntities){
        //校验的同时，把对象重新装配，在返回的时候添加进去，避免重新装配
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        for (ColumnInfoEntity columnInfoEntity : columnInfoEntities) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(columnInfoEntity.getColumnName());
            //关键词的类型校验
            String dataType = columnInfoEntity.getDataType();
            if (!ColumnTypeEnums.ifContains(dataType)) {
                return OperateResult.error("关键词校验未通过," + dataType + "不存在");
            }
            columnInfo.setColumnType(dataType.toUpperCase(Locale.ROOT));
            //关键词的参数校验，如varchar，char等
            if (null != columnInfoEntity.getColumnArguments()) {
                int arguments = Integer.parseInt(columnInfoEntity.getColumnArguments());
                if (ColumnTypeLengthEnums.ifContains(dataType) && ColumnTypeLengthEnums.findType(dataType).getLength() < arguments) {
                    return OperateResult.error("关键词校验未通过," + dataType + "超过最大长度");
                }
                columnInfo.setColumnArgument(arguments);
            }
            //特殊条件，如not null,unique校验
            List<String> dataSpecies = columnInfoEntity.getColumnSpecs();
            if (null != dataSpecies) {
                for (int var1 = 0; var1<dataSpecies.size();) {
                    String first = dataSpecies.get(var1).toUpperCase(Locale.ROOT);
                    ColumnSpecsEnums columnSpecsEnums = ColumnSpecsEnums.findTypeByFirst(first.toUpperCase(Locale.ROOT));
                    if (null == columnSpecsEnums) {
                        return OperateResult.error("关键词校验未通过," + first + "未知条件");
                    }
                    int length = columnSpecsEnums.getLength();
                    if ((var1 + length)>dataSpecies.size()) {
                        return OperateResult.error("关键词校验未通过," +first + "未知条件" );
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int var2 = 0;var2<length;var2++) {
                        sb.append(dataSpecies.get(var1+var2));
                    }
                    if (!columnSpecsEnums.getTotal().equals(sb.toString())) {
                        return OperateResult.error("关键词校验未通过," + sb.toString() + "未知条件");
                    }
                    var1 += length;
                    columnInfo.addSpecs(columnSpecsEnums);
                }
            }

            columnInfoList.add(columnInfo);
        }
        return OperateResult.ok("关键词校验通过",columnInfoList);
    }

    /**
     * 在对column第一次语法校验的基础上，
     * 对column进行二次校验，判断是否在原表的基础上修改合法
     * 包括add的column是否已存在
     *  todo not null 需求default
     *  alter的column在修改之后，原来的值是否能够转换成新的值
     *  todo drop的column，是否是别的表的index
     */

    public OperateResult ifAlterColumnTypeLegal(TableInfo tableInfo, List<ColumnInfo> columnInfoList, AlterColumnEnums enums) {
        List<ColumnInfo> oldColumnList = tableInfo.getColumnInfoList();
        List<String> oldColumnListName = oldColumnList.stream().map(ColumnInfo::getColumnName).collect(Collectors.toList());
        switch (enums){
            case ADD:
                for (ColumnInfo columnInfo:columnInfoList){
                    if (oldColumnListName.contains(columnInfo.getColumnName())) {
                        return OperateResult.error("关键词校验未通过,add" + columnInfo.getColumnName() + "已存在");
                    }
                }
                break;
            case ALTER:
                for (ColumnInfo columnInfo:columnInfoList){
                    Optional<ColumnInfo> oldColumnOption = oldColumnList.stream()
                            .filter(x->x.getColumnName().equals(columnInfo.getColumnName())).findFirst();
                    //判断是否存在原来同名的列
                    if (oldColumnOption.isPresent()) {
                        ColumnInfo oldColumn = oldColumnOption.get();
                        if (!ifTypeConverLegal(oldColumn,columnInfo)){
                            return OperateResult.error("关键词校验未通过,alter类型转换失败" + oldColumn.getColumnName() + columnInfo.getColumnName());
                        }
                    } else {
                        return OperateResult.error("关键词校验未通过,alter" + columnInfo.getColumnName() + "不存在");
                    }
                }
                break;
            case DROP:
                for (ColumnInfo columnInfo:columnInfoList) {
                    if (!oldColumnList.contains(columnInfo.getColumnName())) {
                        return OperateResult.error("关键词校验未通过,drop" + columnInfo.getColumnName() + "不存在");
                    }
                }
                break;
            case NULL:
                return OperateResult.error("未知错误");
        }
        return OperateResult.ok("关键词校验通过");
    }

    public OperateResult ifAlterColumnLegal(TableInfo tableInfo, AlterEntity alterEntity) {
        List<List<ColumnInfo>> addAlterDropList = new ArrayList<>();
        OperateResult rtn = OperateResult.ok("修改成功");
        List<ColumnInfo> oldColumnList = tableInfo.getColumnInfoList();
        List<ColumnInfoEntity> newAddColumnList = alterEntity.getAddColumnList();
        rtn = ifColumnLegal(newAddColumnList);
        if (ResultCode.ok != rtn.getCode()) {
            return rtn;
        } else {
            List<ColumnInfo> tempColumnInfoList = (List<ColumnInfo>)rtn.getRtn();
            addAlterDropList.add(tempColumnInfoList);
            rtn = ifAlterColumnTypeLegal(tableInfo,tempColumnInfoList, AlterColumnEnums.ADD);
            if (ResultCode.ok !=rtn.getCode()) {
                return rtn;
            }
        }
        List<ColumnInfoEntity> newAlterColumnList = alterEntity.getAlterColumnList();
        rtn = ifColumnLegal(newAlterColumnList);
        if (ResultCode.ok != rtn.getCode()) {
            return rtn;
        } else {
            List<ColumnInfo> tempColumnInfoList = (List<ColumnInfo>)rtn.getRtn();
            addAlterDropList.add(tempColumnInfoList);
            rtn = ifAlterColumnTypeLegal(tableInfo,tempColumnInfoList,AlterColumnEnums.ALTER);
            if (ResultCode.ok != rtn.getCode()) {
                return rtn;
            }
        }
        List<ColumnInfoEntity> newDropColumnList = alterEntity.getDropColumnList();
        rtn = ifColumnLegal(newDropColumnList);
        if (ResultCode.ok != rtn.getCode()) {
            return rtn;
        } else {
            List<ColumnInfo> tempColumnInfoList = (List<ColumnInfo>)rtn.getRtn();
            addAlterDropList.add(tempColumnInfoList);
            rtn = ifAlterColumnTypeLegal(tableInfo,tempColumnInfoList,AlterColumnEnums.DROP);
            if (ResultCode.ok != rtn.getCode()) {
                return rtn;
            }
        }
        rtn.setRtn(addAlterDropList);
        return rtn;
    }
    /**
     * 判断插入规则是否合法
     * @param insertEntity
     * @return
     */
    /**
     * 一个值具有三个属性，这一列(column)的对应的名字(name)
     * ，这一列对应的类型(type)如string,int,double等
     * ，这一列对应的值(value)具体的值如12345，abcd等
     * 当一个数据插入的时候，插入的是name:value，首先是value必须包含在内，同时不能为key，应为value会重复
     * 那么下一个key的选择只能在name和type之间，而一行多个列可以有重复的type，所以只能选择name作为key
     * 则，插入的map为，name:value
     * 而对其进行类型的校验时，首先需要吧name转换成type，之后才能对value进行校验
     * 当name转换成type之后，会出现，type:value的情况，是多对多的情况，所以这里不能使用map进行存储
     * @param insertEntity
     * @return
     */
    public OperateResult ifInsertItemsLegal(InsertEntity insertEntity){
//        Map<String,String> insertItems = insertEntity.getItems();
//        String tableName = insertEntity.getTableName();
//        Map<String,String> tableRules = TableConstant.getTableByName(tableName).
//        for (Map.Entry<String,String> entry : insertItems.entrySet()){
//            String rule = tableRules.get(entry.getKey());
//            ColumnTypeEnums columnTypeEnums = ColumnTypeEnums.findType(rule);
//            if (ColumnTypeEnums.Known.equals(columnTypeEnums)){
//                return OperateResult.error("参数校验未通过," + "非法参数类型:" + rule);
//            }
//            if (!checkColumnType(columnTypeEnums,entry.getValue())){
//                return OperateResult.error("参数校验未通过," + "未匹配参数:" + entry.getValue());
//            }
//        }
        return OperateResult.ok("参数校验通过");
    }

    public OperateResult checkDeleteResultLegal(Entry<Integer, List<String>> deleteEntity){
        if (null == deleteEntity.getKey()){
            return OperateResult.error("删除校验未通过," + "无匹配删除值");
        }
        return OperateResult.ok("删除校验通过");
    }



    public boolean checkColumnType(ColumnTypeEnums columnTypeEnums,String insertItem){
        boolean flag = true;
        switch (columnTypeEnums){
            case Int:
                flag = ifInt(insertItem);
                break;
            case Char:
                flag = ifChar(insertItem);
                break;
            case Double:
                flag = ifDouble(insertItem);
                break;
            case VarChar:
                flag = ifString(insertItem);
                break;
        }
        return flag;
    }

    public boolean ifInt(String IntType){
        String pattern = "[+-]?[0-9]+";
        return Pattern.matches(pattern,IntType);
    }

    public boolean ifDouble(String DoubleType){
        String pattern = "[+-]?[0-9]+(\\.[0-9]+)";
        return Pattern.matches(pattern,DoubleType);
    }

    public boolean ifChar(String CharType){
        String pattern = "[a-z|A-Z]";
        return Pattern.matches(pattern,CharType);
    }

    public boolean ifString(String StringType){
        String pattern = "[a-z|A-Z]+";
        return Pattern.matches(pattern,StringType);
    }

    public OperateResult ifLimitLegal(LimitPart limitPart) {
        Expression offset = limitPart.getOffset();
        Integer off = 0;
        if (null != offset) {
            ExpressionVisitorWithRtn visitor = new ExpressionVisitorWithRtn();
            offset.accept(visitor);
            off = Integer.valueOf(visitor.getRtn());
        }
        Expression rowcount = limitPart.getRowcount();
        Integer count = 0;
        if (null != rowcount) {
            ExpressionVisitorWithRtn visitor = new ExpressionVisitorWithRtn();
            rowcount.accept(visitor);
            count = Integer.valueOf(visitor.getRtn());
        } else {
            return OperateResult.error("limit缺少关键参数rowcount");
        }
        return OperateResult.ok("limit校验通过", Arrays.asList(off,count));
    }

    /**
     * column类型转换合法判断
     * @param oldColumn
     * @param newColumn
     * @return
     */
    public boolean ifTypeConverLegal(ColumnInfo oldColumn,ColumnInfo newColumn) {
        ColumnTypeEnums oldType = ColumnTypeEnums.findType(oldColumn.getColumnType());
        ColumnTypeEnums newType = ColumnTypeEnums.findType(newColumn.getColumnType());
        if (oldType == ColumnTypeEnums.Int) {
            return newType == ColumnTypeEnums.Int || newType == ColumnTypeEnums.Double;
        }
        if (oldType == ColumnTypeEnums.Double) {
            return newType == ColumnTypeEnums.Double;
        }
        if (oldType == ColumnTypeEnums.Char) {
            if (newType == ColumnTypeEnums.Char && (newColumn.getColumnArgument()>= oldColumn.getColumnArgument())) {
                return true;
            }
            if (newType == ColumnTypeEnums.VarChar) {
                return true;
            }
            return false;
        }
        if (oldType == ColumnTypeEnums.VarChar) {
            return newType == ColumnTypeEnums.Char || newType == ColumnTypeEnums.VarChar;
        }
        return false;
    }

}
