package com.domain.service;

import com.application.service.PackToDto;
import com.application.service.PackToEntity;
import com.domain.Entity.CreateTempEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.event.DDLOperate;
import com.domain.event.DMLOperate;
import com.domain.event.sqlParser.SelectParser;
import com.interfaces.dto.ResultDto;
import net.sf.jsqlparser.JSQLParserException;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:将sql的解析结果映射到domain的具体操作
 */
public class SqlToEntity {

    SelectParser selectParser = new SelectParser();

    DDLOperate ddlOperate = new DDLOperate();

    public ResultDto sqlMapToDML(String sql) throws JSQLParserException {
        List<String> tableLists = selectParser.getSelectTables(sql);
        CreateTempEntity createTempEntity = PackToEntity.packFromToWhereToCreateTempEntity(tableLists);
        OperateResult operateResult = ddlOperate.createTempTable(createTempEntity);

        return PackToDto.ResultToDto(operateResult);
    }
}
