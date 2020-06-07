package org.jeecg.modules.system.biz.po.controller;

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

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.utils.SysStatusEnum;
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
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import org.jeecg.modules.system.biz.po.vo.BizPurchaseInPage;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInService;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInDetailService;
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
 * @Description: 采购信息主表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="采购信息主表")
@RestController
@RequestMapping("/biz.po/bizPurchaseIn")
@Slf4j
public class BizPurchaseInController {
	@Autowired
	private IBizPurchaseInService bizPurchaseInService;
	@Autowired
	private IBizPurchaseInDetailService bizPurchaseInDetailService;

	 @Autowired
	 HttpServletRequest request;
	 @Autowired
	 private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param bizPurchaseIn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购信息主表-分页列表查询")
	@ApiOperation(value="采购信息主表-分页列表查询", notes="采购信息主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BizPurchaseIn bizPurchaseIn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
		SysUser loginUser = (SysUser)redisUtil.get(token);
		System.out.println(loginUser.getUsername() + "====" + loginUser.getGsdm());

		QueryWrapper<BizPurchaseIn> queryWrapper = QueryGenerator.initQueryWrapper(bizPurchaseIn, req.getParameterMap());
		Page<BizPurchaseIn> page = new Page<BizPurchaseIn>(pageNo, pageSize);
		IPage<BizPurchaseIn> pageList = bizPurchaseInService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bizPurchaseInPage
	 * @return
	 */
	@AutoLog(value = "采购信息主表-添加")
	@ApiOperation(value="采购信息主表-添加", notes="采购信息主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BizPurchaseInPage bizPurchaseInPage) {

		if (SysUtils.izNewNote(bizPurchaseInPage.getBizNo()))
		{

		}
		BizPurchaseIn bizPurchaseIn = new BizPurchaseIn();
		BeanUtils.copyProperties(bizPurchaseInPage, bizPurchaseIn);
		bizPurchaseInService.saveMain(bizPurchaseIn, bizPurchaseInPage.getBizPurchaseInDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bizPurchaseInPage
	 * @return
	 */
	@AutoLog(value = "采购信息主表-编辑")
	@ApiOperation(value="采购信息主表-编辑", notes="采购信息主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BizPurchaseInPage bizPurchaseInPage) {
		BizPurchaseIn bizPurchaseIn = new BizPurchaseIn();
		BeanUtils.copyProperties(bizPurchaseInPage, bizPurchaseIn);
		BizPurchaseIn bizPurchaseInEntity = bizPurchaseInService.getById(bizPurchaseIn.getId());
		if(bizPurchaseInEntity==null) {
			return Result.error("未找到对应数据");
		}
		bizPurchaseInService.updateMain(bizPurchaseIn, bizPurchaseInPage.getBizPurchaseInDetailList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购信息主表-通过id删除")
	@ApiOperation(value="采购信息主表-通过id删除", notes="采购信息主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bizPurchaseInService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购信息主表-批量删除")
	@ApiOperation(value="采购信息主表-批量删除", notes="采购信息主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bizPurchaseInService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购信息主表-通过id查询")
	@ApiOperation(value="采购信息主表-通过id查询", notes="采购信息主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BizPurchaseIn bizPurchaseIn = bizPurchaseInService.getById(id);
		if(bizPurchaseIn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(bizPurchaseIn);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购入库明细表-通过主表ID查询")
	@ApiOperation(value="采购入库明细表-通过主表ID查询", notes="采购入库明细表-通过主表ID查询")
	@GetMapping(value = "/queryBizPurchaseInDetailByMainId")
	public Result<?> queryBizPurchaseInDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BizPurchaseInDetail> bizPurchaseInDetailList = bizPurchaseInDetailService.selectByMainId(id);
		IPage <BizPurchaseInDetail> page = new Page<>();
		page.setRecords(bizPurchaseInDetailList);
		page.setTotal(bizPurchaseInDetailList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bizPurchaseIn
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BizPurchaseIn bizPurchaseIn) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BizPurchaseIn> queryWrapper = QueryGenerator.initQueryWrapper(bizPurchaseIn, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<BizPurchaseIn> queryList = bizPurchaseInService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<BizPurchaseIn> bizPurchaseInList = new ArrayList<BizPurchaseIn>();
      if(oConvertUtils.isEmpty(selections)) {
          bizPurchaseInList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          bizPurchaseInList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<BizPurchaseInPage> pageList = new ArrayList<BizPurchaseInPage>();
      for (BizPurchaseIn main : bizPurchaseInList) {
          BizPurchaseInPage vo = new BizPurchaseInPage();
          BeanUtils.copyProperties(main, vo);
          List<BizPurchaseInDetail> bizPurchaseInDetailList = bizPurchaseInDetailService.selectByMainId(main.getId());
          vo.setBizPurchaseInDetailList(bizPurchaseInDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购信息主表列表");
      mv.addObject(NormalExcelConstants.CLASS, BizPurchaseInPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购信息主表数据", "导出人:"+sysUser.getRealname(), "采购信息主表"));
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
              List<BizPurchaseInPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BizPurchaseInPage.class, params);
              for (BizPurchaseInPage page : list) {
                  BizPurchaseIn po = new BizPurchaseIn();
                  BeanUtils.copyProperties(page, po);
                  bizPurchaseInService.saveMain(po, page.getBizPurchaseInDetailList());
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
