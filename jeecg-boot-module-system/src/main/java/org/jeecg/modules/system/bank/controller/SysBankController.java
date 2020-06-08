package org.jeecg.modules.system.bank.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.bank.entity.SysBank;
import org.jeecg.modules.system.bank.service.ISysBankService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 银行信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Api(tags="银行信息")
@RestController
@RequestMapping("/bank/sysBank")
@Slf4j
public class SysBankController extends JeecgController<SysBank, ISysBankService> {
	@Autowired
	private ISysBankService sysBankService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysBank
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "银行信息-分页列表查询")
	@ApiOperation(value="银行信息-分页列表查询", notes="银行信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysBank sysBank,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<?> queryWrapper = QueryGenerator.initQueryWrapper(?, req.getParameterMap());
		QueryWrapper<SysBank> queryWrapper = QueryGenerator.initQueryWrapper(sysBank, req.getParameterMap());
		Page<SysBank> page = new Page<SysBank>(pageNo, pageSize);
		IPage<SysBank> pageList = sysBankService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysBank
	 * @return
	 */
	@AutoLog(value = "银行信息-添加")
	@ApiOperation(value="银行信息-添加", notes="银行信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysBank sysBank) {


		sysBank.setGsdm(SysUtils.getLoginUser().getGsdm());
		sysBankService.save(sysBank);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysBank
	 * @return
	 */
	@AutoLog(value = "银行信息-编辑")
	@ApiOperation(value="银行信息-编辑", notes="银行信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysBank sysBank) {


		//sysBankService.updateById(sysBank);
		sysBankService.updateSysBank(sysBank);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "银行信息-通过id删除")
	@ApiOperation(value="银行信息-通过id删除", notes="银行信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysBankService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "银行信息-批量删除")
	@ApiOperation(value="银行信息-批量删除", notes="银行信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysBankService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "银行信息-通过id查询")
	@ApiOperation(value="银行信息-通过id查询", notes="银行信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysBank sysBank = sysBankService.getById(id);
		if(sysBank==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysBank);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysBank
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysBank sysBank) {
        return super.exportXls(request, sysBank, SysBank.class, "银行信息");
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
        return super.importExcel(request, response, SysBank.class);
    }

}
