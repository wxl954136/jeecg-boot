package org.jeecg.modules.system.biz.ac.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.system.mapper.SysCommonMapper;
import org.jeecg.modules.utils.SysUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import org.jeecg.modules.system.biz.ac.entity.AccPayable;
import org.jeecg.modules.system.biz.ac.vo.AccPayablePage;
import org.jeecg.modules.system.biz.ac.service.IAccPayableService;
import org.jeecg.modules.system.biz.ac.service.IAccPayableDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 应付款头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Api(tags="应付款头表")
@RestController
@RequestMapping("/biz.ac/accPayable")
@Slf4j
public class AccPayableController {
	@Autowired
	private IAccPayableService accPayableService;
	@Autowired
	private IAccPayableDetailService accPayableDetailService;
	 @Autowired
	 SysCommonMapper sysCommonMapper;
	/**
	 * 分页列表查询
	 *
	 * @param accPayable
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "应付款头表-分页列表查询")
	@ApiOperation(value="应付款头表-分页列表查询", notes="应付款头表-分页列表查询")
	@GetMapping(value = "/list/{bizType}")
	public Result<?> queryPageList(AccPayable accPayable,
								   @PathVariable("bizType") String bizType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		if (bizPurchaseInPage.getBizPurchaseInDetailList().size()<=0)
//		{
//			return Result.error("请添加明细");
//		}
		QueryWrapper<AccPayable> queryWrapper = QueryGenerator.initQueryWrapper(accPayable, req.getParameterMap());
		Page<AccPayable> page = new Page<AccPayable>(pageNo, pageSize);
		IPage<AccPayable> pageList = accPayableService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param accPayablePage
	 * @return
	 */
	@AutoLog(value = "应付款头表-添加")
	@ApiOperation(value="应付款头表-添加", notes="应付款头表-添加")
	@PostMapping(value = "/add/{bizType}")
	public Result<?> add(@PathVariable("bizType") String bizType,@RequestBody AccPayablePage accPayablePage) {

		if (accPayablePage.getAccPayableDetailList().size()<=0) 	return Result.error("请添加明细");

		AccPayable accPayable = new AccPayable();
		BeanUtils.copyProperties(accPayablePage, accPayable);

		if (SysUtils.izNewNote(accPayablePage.getBizNo()))
		{
			String newBizNo = SysUtils.getNewNoteNo(sysCommonMapper,"acc_payable",bizType.trim().toUpperCase());
			accPayable.setBizNo(newBizNo);
		}
		accPayable.setBizType(bizType.toUpperCase().trim());
		accPayableService.saveMain(accPayable, accPayablePage.getAccPayableDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param accPayablePage
	 * @return
	 */
	@AutoLog(value = "应付款头表-编辑")
	@ApiOperation(value="应付款头表-编辑", notes="应付款头表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AccPayablePage accPayablePage) {

		if (accPayablePage.getAccPayableDetailList().size()<=0) 	return Result.error("请添加明细");

		AccPayable accPayable = new AccPayable();
		BeanUtils.copyProperties(accPayablePage, accPayable);
		AccPayable accPayableEntity = accPayableService.getById(accPayable.getId());
		if(accPayableEntity==null) {
			return Result.error("未找到对应数据");
		}
		accPayableService.updateMain(accPayable, accPayablePage.getAccPayableDetailList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "应付款头表-通过id删除")
	@ApiOperation(value="应付款头表-通过id删除", notes="应付款头表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		accPayableService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "应付款头表-批量删除")
	@ApiOperation(value="应付款头表-批量删除", notes="应付款头表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.accPayableService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "应付款头表-通过id查询")
	@ApiOperation(value="应付款头表-通过id查询", notes="应付款头表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AccPayable accPayable = accPayableService.getById(id);
		if(accPayable==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(accPayable);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "应付款明细表-通过主表ID查询")
	@ApiOperation(value="应付款明细表-通过主表ID查询", notes="应付款明细表-通过主表ID查询")
	@GetMapping(value = "/queryAccPayableDetailByMainId")
	public Result<?> queryAccPayableDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<AccPayableDetail> accPayableDetailList = accPayableDetailService.selectByMainId(id);
		IPage <AccPayableDetail> page = new Page<>();
		page.setRecords(accPayableDetailList);
		page.setTotal(accPayableDetailList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param accPayable
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AccPayable accPayable) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<AccPayable> queryWrapper = QueryGenerator.initQueryWrapper(accPayable, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<AccPayable> queryList = accPayableService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<AccPayable> accPayableList = new ArrayList<AccPayable>();
      if(oConvertUtils.isEmpty(selections)) {
          accPayableList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          accPayableList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<AccPayablePage> pageList = new ArrayList<AccPayablePage>();
      for (AccPayable main : accPayableList) {
          AccPayablePage vo = new AccPayablePage();
          BeanUtils.copyProperties(main, vo);
          List<AccPayableDetail> accPayableDetailList = accPayableDetailService.selectByMainId(main.getId());
          vo.setAccPayableDetailList(accPayableDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "应付款头表列表");
      mv.addObject(NormalExcelConstants.CLASS, AccPayablePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("应付款头表数据", "导出人:"+sysUser.getRealname(), "应付款头表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<AccPayablePage> list = ExcelImportUtil.importExcel(file.getInputStream(), AccPayablePage.class, params);
              for (AccPayablePage page : list) {
                  AccPayable po = new AccPayable();
                  BeanUtils.copyProperties(page, po);
                  accPayableService.saveMain(po, page.getAccPayableDetailList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
