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
import org.jeecg.modules.system.core.entity.CoreCostSku;
import org.jeecg.modules.system.core.service.ICoreCostSkuService;

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
 * @Description: core_cost_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
@Api(tags="core_cost_sku")
@RestController
@RequestMapping("/core/coreCostSku")
@Slf4j
public class CoreCostSkuController extends JeecgController<CoreCostSku, ICoreCostSkuService> {
	@Autowired
	private ICoreCostSkuService coreCostSkuService;
	
	/**
	 * 分页列表查询
	 *
	 * @param coreCostSku
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "core_cost_sku-分页列表查询")
	@ApiOperation(value="core_cost_sku-分页列表查询", notes="core_cost_sku-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CoreCostSku coreCostSku,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CoreCostSku> queryWrapper = QueryGenerator.initQueryWrapper(coreCostSku, req.getParameterMap());
		Page<CoreCostSku> page = new Page<CoreCostSku>(pageNo, pageSize);
		IPage<CoreCostSku> pageList = coreCostSkuService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param coreCostSku
	 * @return
	 */
	@AutoLog(value = "core_cost_sku-添加")
	@ApiOperation(value="core_cost_sku-添加", notes="core_cost_sku-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CoreCostSku coreCostSku) {
		coreCostSkuService.save(coreCostSku);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param coreCostSku
	 * @return
	 */
	@AutoLog(value = "core_cost_sku-编辑")
	@ApiOperation(value="core_cost_sku-编辑", notes="core_cost_sku-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CoreCostSku coreCostSku) {
		coreCostSkuService.updateById(coreCostSku);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "core_cost_sku-通过id删除")
	@ApiOperation(value="core_cost_sku-通过id删除", notes="core_cost_sku-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		coreCostSkuService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "core_cost_sku-批量删除")
	@ApiOperation(value="core_cost_sku-批量删除", notes="core_cost_sku-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.coreCostSkuService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "core_cost_sku-通过id查询")
	@ApiOperation(value="core_cost_sku-通过id查询", notes="core_cost_sku-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CoreCostSku coreCostSku = coreCostSkuService.getById(id);
		if(coreCostSku==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(coreCostSku);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param coreCostSku
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CoreCostSku coreCostSku) {
        return super.exportXls(request, coreCostSku, CoreCostSku.class, "core_cost_sku");
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
        return super.importExcel(request, response, CoreCostSku.class);
    }

}
