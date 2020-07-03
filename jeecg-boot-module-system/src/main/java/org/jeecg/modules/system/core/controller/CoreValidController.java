package org.jeecg.modules.system.core.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.api.vo.Status;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.core.entity.CoreStockSku;
import org.jeecg.modules.system.core.service.IBizFlowSerialService;
import org.jeecg.modules.system.core.service.IBizSerialService;
import org.jeecg.modules.system.core.service.ICoreStockSkuService;
import org.jeecg.modules.system.mapper.SysCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 核心库存表
 * @Author: jeecg-boot
 * @Date: 2020-06-10
 * @Version: V1.0
 */
@Api(tags = "公共的供前端调用的交互判断")
@RestController
@RequestMapping("/core/valid")
@Slf4j
public class CoreValidController extends JeecgController<CoreStockSku, ICoreStockSkuService> {
    @Autowired
    private IBizFlowSerialService bizFlowSerialService;
    @Autowired
    private IBizSerialService bizSerialService;
    @Autowired
    SysCommonMapper sysCommonMapper;

    /**
     * 验证录入串号是否重复，不经过数据库
     * @param serials
     * @return
     */
    @RequestMapping(value = "/duplicateSerial", method = RequestMethod.GET)
    public Result<?> validDuplicateSerial(@RequestParam(name = "serials", required = true) String serials) {
        Result<?> result = new Result<>();
        String allSerials[] = serials.split(",");
        List<String> listSerials = Arrays.asList(allSerials);
        Status status = bizFlowSerialService.izDuplicateSerial(listSerials);
        if (!status.getSuccess())
        {
            result.error500(status.getMessage());
        }
        result.setSuccess(true);
        result.setResult(null); //存放结果集,没有就不管它
        result.setMessage("串号验证完毕");
        return result;
    }

    /**
     * 验证串号是否在库，如采购入库时，如果串号在库，不允许再入库
     * @param serials
     * @return
     */
    @RequestMapping(value = "/inStockSerial", method = RequestMethod.GET)
    public Result<?> validInStockBySerials(@RequestParam(name = "serials", required = true) String serials) {
        Result<List<Object>> result = new Result<>();
        String allSerials[] = serials.split(",");
        List<String> listSerials = Arrays.asList(allSerials);
        Status status = bizSerialService.izStockBySerials(listSerials);
        if (!status.getSuccess())
        {
            result.error500(status.getMessage());
        }
        result.setSuccess(true);
        result.setResult(status.getListObject()); //存放结果集,没有就不管它
        result.setMessage("串号验证完毕");
        return result;
    }
}
