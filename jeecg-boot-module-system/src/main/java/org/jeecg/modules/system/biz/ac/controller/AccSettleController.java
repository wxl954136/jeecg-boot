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
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettle;
import org.jeecg.modules.system.biz.ac.vo.AccSettlePage;
import org.jeecg.modules.system.biz.ac.service.IAccSettleService;
import org.jeecg.modules.system.biz.ac.service.IAccSettleDetailService;
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
 * @Description: 收付款结算头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Api(tags="收付款结算头表")
@RestController
@RequestMapping("/biz.ac/accSettle")
@Slf4j
public class AccSettleController {
	@Autowired
	private IAccSettleService accSettleService;
	@Autowired
	private IAccSettleDetailService accSettleDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param accSettle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "收付款结算头表-分页列表查询")
	@ApiOperation(value="收付款结算头表-分页列表查询", notes="收付款结算头表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AccSettle accSettle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AccSettle> queryWrapper = QueryGenerator.initQueryWrapper(accSettle, req.getParameterMap());
		Page<AccSettle> page = new Page<AccSettle>(pageNo, pageSize);
		IPage<AccSettle> pageList = accSettleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param accSettlePage
	 * @return
	 */
	@AutoLog(value = "收付款结算头表-添加")
	@ApiOperation(value="收付款结算头表-添加", notes="收付款结算头表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AccSettlePage accSettlePage) {
		AccSettle accSettle = new AccSettle();
		BeanUtils.copyProperties(accSettlePage, accSettle);
		accSettleService.saveMain(accSettle, accSettlePage.getAccSettleDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param accSettlePage
	 * @return
	 */
	@AutoLog(value = "收付款结算头表-编辑")
	@ApiOperation(value="收付款结算头表-编辑", notes="收付款结算头表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AccSettlePage accSettlePage) {
		AccSettle accSettle = new AccSettle();
		BeanUtils.copyProperties(accSettlePage, accSettle);
		AccSettle accSettleEntity = accSettleService.getById(accSettle.getId());
		if(accSettleEntity==null) {
			return Result.error("未找到对应数据");
		}
		accSettleService.updateMain(accSettle, accSettlePage.getAccSettleDetailList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收付款结算头表-通过id删除")
	@ApiOperation(value="收付款结算头表-通过id删除", notes="收付款结算头表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		accSettleService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "收付款结算头表-批量删除")
	@ApiOperation(value="收付款结算头表-批量删除", notes="收付款结算头表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.accSettleService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收付款结算头表-通过id查询")
	@ApiOperation(value="收付款结算头表-通过id查询", notes="收付款结算头表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AccSettle accSettle = accSettleService.getById(id);
		if(accSettle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(accSettle);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "结算明细表-通过主表ID查询")
	@ApiOperation(value="结算明细表-通过主表ID查询", notes="结算明细表-通过主表ID查询")
	@GetMapping(value = "/queryAccSettleDetailByMainId")
	public Result<?> queryAccSettleDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<AccSettleDetail> accSettleDetailList = accSettleDetailService.selectByMainId(id);
		IPage <AccSettleDetail> page = new Page<>();
		page.setRecords(accSettleDetailList);
		page.setTotal(accSettleDetailList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param accSettle
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AccSettle accSettle) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<AccSettle> queryWrapper = QueryGenerator.initQueryWrapper(accSettle, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<AccSettle> queryList = accSettleService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<AccSettle> accSettleList = new ArrayList<AccSettle>();
      if(oConvertUtils.isEmpty(selections)) {
          accSettleList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          accSettleList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<AccSettlePage> pageList = new ArrayList<AccSettlePage>();
      for (AccSettle main : accSettleList) {
          AccSettlePage vo = new AccSettlePage();
          BeanUtils.copyProperties(main, vo);
          List<AccSettleDetail> accSettleDetailList = accSettleDetailService.selectByMainId(main.getId());
          vo.setAccSettleDetailList(accSettleDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "收付款结算头表列表");
      mv.addObject(NormalExcelConstants.CLASS, AccSettlePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收付款结算头表数据", "导出人:"+sysUser.getRealname(), "收付款结算头表"));
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
              List<AccSettlePage> list = ExcelImportUtil.importExcel(file.getInputStream(), AccSettlePage.class, params);
              for (AccSettlePage page : list) {
                  AccSettle po = new AccSettle();
                  BeanUtils.copyProperties(page, po);
                  accSettleService.saveMain(po, page.getAccSettleDetailList());
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
