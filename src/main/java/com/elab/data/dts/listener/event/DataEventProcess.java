package com.elab.data.dts.listener.event;

import com.alibaba.fastjson.JSON;
import com.elab.data.dts.common.UserRecord;
import com.elab.data.dts.formats.avro.Operation;
import com.elab.data.dts.model.DMLData;
import com.elab.data.dts.model.TableData;
import com.elab.data.dts.utils.DataParseUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 修改事件触发
 *
 * @author ： liukx
 * @time ： 2020/9/23 - 14:15
 */
@Component
public class DataEventProcess extends AbstractEventProcess {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<Operation> subscriptionList = Arrays.asList(Operation.INSERT, Operation.UPDATE, Operation.DELETE);

    @Override
    public boolean subscription(Operation operation) {
        return subscriptionList.contains(operation);
    }

    @Override
    protected TableData parseTable(UserRecord record) {
        return DataParseUtils.parseDML(record.getRecord());
    }
    

    @Override
    protected boolean process0(TableData tableData) throws Exception {
        logger.debug("数据来源时间:" + DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(tableData.getSourceTimestamp() * 1000) + " 得到的转换数据 : " + JSON.toJSONString(tableData));
        DMLData okData = (DMLData) tableData;
        //我开始根据库名表名一些订阅到信息要做业务处理了
        //注入进来一个类来做业务处理 数据转换建议用fastjson进行深拷贝 字符串数据直接JSON.parseObject() 对象数据直接JSON.parseObject(JSON.toJSONString(),xxx.class)
        return true;
    }


}
