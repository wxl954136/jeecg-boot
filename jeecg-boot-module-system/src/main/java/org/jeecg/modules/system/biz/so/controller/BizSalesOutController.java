package org.jeecg.modules.system.biz.so.controller;

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
import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import org.jeecg.modules.system.biz.so.entity.BizSalesOut;
import org.jeecg.modules.system.biz.so.vo.BizSalesOutPage;
import org.jeecg.modules.system.biz.so.service.IBizSalesOutService;
import org.jeecg.modules.system.biz.so.service.IBizSalesOutDetailService;
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
 * @Description: 销售主表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Api(tags="销售主表")
@RestController
@RequestMapping("/biz.so/bizSalesOut")
@Slf4j
public class BizSalesOutController {
	@Autowired
	private IBizSalesOutService bizSalesOutService;
	@Autowired
	private IBizSalesOutDetailService bizSalesOutDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bizSalesOut
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "销售主表-分页列表查询")
	@ApiOperation(value="销售主表-分页列表查询", notes="销售主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BizSalesOut bizSalesOut,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BizSalesOut> queryWrapper = QueryGenerator.initQueryWrapper(bizSalesOut, req.getParameterMap());
		Page<BizSalesOut> page = new Page<BizSalesOut>(pageNo, pageSize);
		IPage<BizSalesOut> pageList = bizSalesOutService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bizSalesOutPage
	 * @return
	 */
	@AutoLog(value = "销售主表-添加")
	@ApiOperation(value="销售主表-添加", notes="销售主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BizSalesOutPage bizSalesOutPage) {
		BizSalesOut bizSalesOut = new BizSalesOut();
		BeanUtils.copyProperties(bizSalesOutPage, bizSalesOut);
		bizSalesOutService.saveMain(bizSalesOut, bizSalesOutPage.getBizSalesOutDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bizSalesOutPage
	 * @return
	 */
	@AutoLog(value = "销售主表-编辑")
	@ApiOperation(value="销售主表-编辑", notes="销售主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BizSalesOutPage bizSalesOutPage) {
		BizSalesOut bizSalesOut = new BizSalesOut();
		BeanUtils.copyProperties(bizSalesOutPage, bizSalesOut);
		BizSalesOut bizSalesOutEntity = bizSalesOutService.getById(bizSalesOut.getId());
		if(bizSalesOutEntity==null) {
			return Result.error("未找到对应数据");
		}
		bizSalesOutService.updateMain(bizSalesOut, bizSalesOutPage.getBizSalesOutDetailList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售主表-通过id删除")
	@ApiOperation(value="销售主表-通过id删除", notes="销售主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bizSalesOutService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售主表-批量删除")
	@ApiOperation(value="销售主表-批量删除", notes="销售主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bizSalesOutService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售主表-通过id查询")
	@ApiOperation(value="销售主表-通过id查询", notes="销售主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BizSalesOut bizSalesOut = bizSalesOutService.getById(id);
		if(bizSalesOut==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(bizSalesOut);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售明细表-通过主表ID查询")
	@ApiOperation(value="销售明细表-通过主表ID查询", notes="销售明细表-通过主表ID查询")
	@GetMapping(value = "/queryBizSalesOutDetailByMainId")
	public Result<?> queryBizSalesOutDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BizSalesOutDetail> bizSalesOutDetailList = bizSalesOutDetailService.selectByMainId(id);
		IPage <BizSalesOutDetail> page = new Page<>();
		page.setRecords(bizSalesOutDetailList);
		page.setTotal(bizSalesOutDetailList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bizSalesOut
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BizSalesOut bizSalesOut) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BizSalesOut> queryWrapper = QueryGenerator.initQueryWrapper(bizSalesOut, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<BizSalesOut> queryList = bizSalesOutService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<BizSalesOut> bizSalesOutList = new ArrayList<BizSalesOut>();
      if(oConvertUtils.isEmpty(selections)) {
          bizSalesOutList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          bizSalesOutList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<BizSalesOutPage> pageList = new ArrayList<BizSalesOutPage>();
      for (BizSalesOut main : bizSalesOutList) {
          BizSalesOutPage vo = new BizSalesOutPage();
          BeanUtils.copyProperties(main, vo);
          List<BizSalesOutDetail> bizSalesOutDetailList = bizSalesOutDetailService.selectByMainId(main.getId());
          vo.setBizSalesOutDetailList(bizSalesOutDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售主表列表");
      mv.addObject(NormalExcelConstants.CLASS, BizSalesOutPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售主表数据", "导出人:"+sysUser.getRealname(), "销售主表"));
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
              List<BizSalesOutPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BizSalesOutPage.class, params);
              for (BizSalesOutPage page : list) {
                  BizSalesOut po = new BizSalesOut();
                  BeanUtils.copyProperties(page, po);
                  bizSalesOutService.saveMain(po, page.getBizSalesOutDetailList());
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
