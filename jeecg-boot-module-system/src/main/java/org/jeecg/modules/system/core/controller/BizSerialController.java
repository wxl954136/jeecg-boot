package org.jeecg.modules.system.core.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.core.entity.BizSerial;
import org.jeecg.modules.system.core.service.IBizSerialService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: biz_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
@Api(tags="biz_serial")
@RestController
@RequestMapping("/core/bizSerial")
@Slf4j
public class BizSerialController extends JeecgController<BizSerial, IBizSerialService> {
	@Autowired
	private IBizSerialService bizSerialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bizSerial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "biz_serial-分页列表查询")
	@ApiOperation(value="biz_serial-分页列表查询", notes="biz_serial-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BizSerial bizSerial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BizSerial> queryWrapper = QueryGenerator.initQueryWrapper(bizSerial, req.getParameterMap());
		Page<BizSerial> page = new Page<BizSerial>(pageNo, pageSize);
		IPage<BizSerial> pageList = bizSerialService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bizSerial
	 * @return
	 */
	@AutoLog(value = "biz_serial-添加")
	@ApiOperation(value="biz_serial-添加", notes="biz_serial-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BizSerial bizSerial) {
		bizSerialService.save(bizSerial);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bizSerial
	 * @return
	 */
	@AutoLog(value = "biz_serial-编辑")
	@ApiOperation(value="biz_serial-编辑", notes="biz_serial-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BizSerial bizSerial) {
		bizSerialService.updateById(bizSerial);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "biz_serial-通过id删除")
	@ApiOperation(value="biz_serial-通过id删除", notes="biz_serial-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bizSerialService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "biz_serial-批量删除")
	@ApiOperation(value="biz_serial-批量删除", notes="biz_serial-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bizSerialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "biz_serial-通过id查询")
	@ApiOperation(value="biz_serial-通过id查询", notes="biz_serial-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BizSerial bizSerial = bizSerialService.getById(id);
		if(bizSerial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(bizSerial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bizSerial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BizSerial bizSerial) {
        return super.exportXls(request, bizSerial, BizSerial.class, "biz_serial");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BizSerial.class);
    }

}
